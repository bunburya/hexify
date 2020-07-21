package eu.bunburya.hexify.model

import eu.bunburya.hexagons.Hex
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

class Hexifier(inputImage: Image, val config: Config) {

    private val imageWidth = inputImage.width.toInt()
    private val imageHeight = inputImage.height.toInt()
    private val writableImage = WritableImage(inputImage.pixelReader, imageWidth, imageHeight)
    val hexOverlay = HexOverlay(imageWidth, imageHeight, config.hexSize)

    fun hexify(): Image {
        val reader = writableImage.pixelReader
        val writer = writableImage.pixelWriter
        val emptyColor = Color(0.0, 0.0, 0.0, 0.0)
        var hex: Hex?
        var color: Color
        val rgbValues: MutableMap<Hex, HexAggregator> = mutableMapOf()
        val aggregates: MutableMap<Hex, Color> = mutableMapOf()
        var hexAgg: HexAggregator?
        var filterFunc: ((Color) -> Color)?
        for (i in 0 until imageWidth) {
            // first pass to get colors
            for (j in 0 until imageHeight) {
                hex = hexOverlay.getHex(i, j)
                if (hex == null) continue
                color = reader.getColor(i, j)
                hexAgg = rgbValues[hex]
                if (hexAgg == null) {
                    hexAgg = HexAggregator.factory(config.hexAggregator)
                    rgbValues[hex] = hexAgg
                }
                hexAgg.add(color)
            }
        }

        for (entry in rgbValues) {
            // go through list of collected colors and get aggregate
            hex = entry.key
            hexAgg = entry.value
            aggregates[hex] = hexAgg.aggregateColor
        }
        for (i in 0 until imageWidth) {
            // second pass to set colors to averages
            for (j in 0 until imageHeight) {
                hex = hexOverlay.getHex(i, j)
                if (hex == null) {
                    //println("No hex found for ($i, $j).")
                    writer.setColor(i, j, emptyColor)
                } else {
                    color = aggregates[hex] ?: emptyColor
                    for (filterName in config.filters) {
                        filterFunc = Filter.availableFilters[filterName]
                        color = if (filterFunc != null) filterFunc(color) else color
                    }
                    writer.setColor(i, j, color)
                }
            }
        }
        return writableImage
    }
}