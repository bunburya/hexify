package eu.bunburya.hexify.model

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

/**
 * The class that "mosaifies" an image.
 */
class Mosaifier(private val config: MainConfig, initialMosaicConfig: MosaicConfig) {

    // Initialise with placeholder values until real inputImage is set
    private var imageWidth = 1
    private var imageHeight = 1
    private var writableImage = WritableImage(imageWidth, imageHeight)
    lateinit var mosaic: Mosaic

    var mosaicConfig = initialMosaicConfig
        set(new) {
            field = new
            mosaic = Mosaic.factory(config.mosaicType, imageWidth, imageHeight, new)
        }

    var inputImage: Image? = null
        set(newImage: Image?) {
            field = newImage
            if (newImage != null) {
                imageWidth = newImage.width.toInt()
                imageHeight = newImage.height.toInt()
                writableImage = WritableImage(newImage.pixelReader, imageWidth, imageHeight)
                mosaic = Mosaic.factory(config.mosaicType, imageWidth, imageHeight, mosaicConfig)
            }
        }

    fun mosaify(): Image {
        if (inputImage == null) {
            throw UninitializedPropertyAccessException("No inputImage has been specified.")
        }
        val reader = inputImage!!.pixelReader
        val writer = writableImage.pixelWriter
        val emptyColor = Color(0.0, 0.0, 0.0, 0.0)
        var tile: Tile?
        var color: Color
        val aggregators: MutableMap<Tile, ColorAggregator> = mutableMapOf()
        val aggregates: MutableMap<Tile, Color> = mutableMapOf()
        var tileAgg: ColorAggregator?
        var filterFunc: ((Color) -> Color)?
        println("Mosaify first pass")
        for (i in 0 until imageWidth) {
            // first pass to get colors
            for (j in 0 until imageHeight) {
                //println("$i, $j")
                tile = mosaic.getTile(i, j)
                if (tile == null) continue
                color = reader.getColor(i, j)
                tileAgg = aggregators[tile]
                if (tileAgg == null) {
                    tileAgg = ColorAggregator.factory(config.aggregatorMethod)
                    aggregators[tile] = tileAgg
                }
                tileAgg.add(color)
            }
        }

        println("Mosaify getting aggregates")
        for (entry in aggregators) {
            // go through list of collected colors and get aggregate
            tile = entry.key
            tileAgg = entry.value
            aggregates[tile] = tileAgg.aggregateColor
        }

        println("Mosaify second pass")
        for (i in 0 until imageWidth) {
            // second pass to set colors to aggregates
            for (j in 0 until imageHeight) {
                tile = mosaic.getTile(i, j)
                if (tile == null) {
                    //println("No hex found for ($i, $j).")
                    // TODO: Should probably just not set anything here.
                    writer.setColor(i, j, emptyColor)
                } else {
                    color = aggregates[tile] ?: emptyColor
                    for (filterName in config.filters) {
                        filterFunc = Filter.availableFilters[filterName]
                        color = if (filterFunc != null) filterFunc(color) else color
                    }
                    //println("Setting $i, $j to $color.")
                    writer.setColor(i, j, color)
                }
            }
        }
        return writableImage
    }
}