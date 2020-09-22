package eu.bunburya.hexify.model

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

/**
 * The class that "mosaifies" an image.
 */
class Mosaifier(inputImage: Image, private val config: MainConfig, mosaicConfig: MosaicConfig) {

    private val imageWidth = inputImage.width.toInt()
    private val imageHeight = inputImage.height.toInt()
    private val writableImage = WritableImage(inputImage.pixelReader, imageWidth, imageHeight)
    val mosaic = Mosaic.factory(config.mosaicType, imageWidth, imageHeight, mosaicConfig)

    fun mosaify(): Image {
        val reader = writableImage.pixelReader
        val writer = writableImage.pixelWriter
        val emptyColor = Color(0.0, 0.0, 0.0, 0.0)
        var tile: Tile?
        var color: Color
        val aggregators: MutableMap<Tile, ColorAggregator> = mutableMapOf()
        val aggregates: MutableMap<Tile, Color> = mutableMapOf()
        var tileAgg: ColorAggregator?
        var filterFunc: ((Color) -> Color)?
        for (i in 0 until imageWidth) {
            // first pass to get colors
            for (j in 0 until imageHeight) {
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

        for (entry in aggregators) {
            // go through list of collected colors and get aggregate
            tile = entry.key
            tileAgg = entry.value
            aggregates[tile] = tileAgg.aggregateColor
        }
        for (i in 0 until imageWidth) {
            // second pass to set colors to aggregates
            for (j in 0 until imageHeight) {
                tile = mosaic.getTile(i, j)
                if (tile == null) {
                    //println("No hex found for ($i, $j).")
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