package eu.bunburya.hexify.model

import eu.bunburya.hexagons.*
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.properties.Delegates

class HexOverlay(val imageWidth: Int, val imageHeight:Int, val hexSize: Int = 10, val hexOrientation: LayoutOrientation = LAYOUT_FLAT, hexOffset: Offset = Offset.EVEN) {

    var hexPixelWidth by Delegates.notNull<Int>()
    var hexPixelHeight by Delegates.notNull<Int>()
    var gridWidth by Delegates.notNull<Int>()
    var gridHeight by Delegates.notNull<Int>()
    var horizontalMargin by Delegates.notNull<Int>()
    var verticalMargin by Delegates.notNull<Int>()
    lateinit var origin: Point

    init {
        println("Hex size $hexSize")
        if (hexOrientation == LAYOUT_FLAT) {
            hexPixelWidth = hexSize * 2
            hexPixelHeight = (kotlin.math.sqrt(3.0) * hexSize).toInt()
        } else if (hexOrientation == LAYOUT_POINTY) {
            hexPixelWidth = (kotlin.math.sqrt(3.0) * hexSize).toInt()
            hexPixelHeight = hexSize * 2
        } else {
            throw IllegalArgumentException("Invalid layout orientation: $hexOrientation.")
        }
        setMeasurements()

        println("hexPixelWidth $hexPixelWidth")
        println("hexPixelHeight $hexPixelHeight")
        println("gridWidth $gridWidth")
        println("gridHeight $gridHeight")
    }

    /**
     * Calculates the largest number of hexagons that will fit inside maxPixels, along the "long" axis (for a flat
     * layout, this means the x axis; for a pointy layout, it means the y axis).
     * Returns a Pair where the first element is the number of hexagons and the second is the amount of pixel space they
     * would take up.
     */
    private fun getLongGridLength(maxPixels: Int): Pair<Int, Int> {
        val numHexes = ((maxPixels - (0.5 * hexSize)) / (1.5 * hexSize)).toInt()
        val gridPixelLength = ((numHexes * 1.5 * hexSize) + (0.5 * hexSize)).roundToInt()
        return Pair(numHexes, gridPixelLength)
    }

    /**
     * Calculates the largest number of hexagons that will fit inside maxPixels, along the "short" axis (for a flat
     * layout, this means the y axis; for a pointy layout, it means the x axis).  See also getLongGridLength.
     */
    private fun getShortGridLength(maxPixels: Int, longGridLength: Int): Pair<Int, Int> {
        var numHexes: Int
        var gridPixelLength: Int
        numHexes = ((maxPixels - (0.5 * hexSize * sqrt(3.0))) / (hexSize * sqrt(3.0))).toInt()
        gridPixelLength = ((numHexes * hexSize * sqrt(3.0)) + (0.5 * hexSize * sqrt(3.0))).toInt()

        /*if (longGridLength % 2 == 0) {
            numHexes = (maxPixels / (hexSize * sqrt(3.0))).toInt()
            gridPixelLength = (numHexes * hexSize * sqrt(3.0)).toInt()
        } else {
            numHexes = ((maxPixels - (0.5 * hexSize * sqrt(3.0))) / (hexSize * sqrt(3.0))).toInt()
            gridPixelLength = ((numHexes * hexSize * sqrt(3.0)) + (0.5 * hexSize * sqrt(3.0))).toInt()
        }*/
        return Pair(numHexes, gridPixelLength)
    }

    fun setMeasurements() {
        var widthMeasurements: Pair<Int, Int>
        var heightMeasurements: Pair<Int, Int>
        if (hexOrientation == LAYOUT_FLAT) {
            // Hexes are longer width-ways
            widthMeasurements = getLongGridLength(imageWidth)
            gridWidth = widthMeasurements.first
            heightMeasurements = getShortGridLength(imageHeight, gridWidth)
            gridHeight = heightMeasurements.first
        } else {
            heightMeasurements = getLongGridLength(imageHeight)
            gridHeight = heightMeasurements.first
            widthMeasurements = getShortGridLength(imageWidth, gridHeight)
            gridWidth = widthMeasurements.first
        }
        horizontalMargin = imageWidth - widthMeasurements.second
        verticalMargin = imageHeight - heightMeasurements.second
        origin = Point((horizontalMargin + hexPixelWidth) / 2.0, (verticalMargin + hexPixelHeight) / 2.0)
        //origin = Point(50.0, 50.0)
        println("Origin $origin")
    }
    val hexLayout = Layout(hexOrientation, Point(hexSize.toDouble(), hexSize.toDouble()), origin)

    val hexes = MapBuilder.rectangle(gridWidth, gridHeight, "qr")

    /**
     * Return the Hex which contains the point (x, y).
     */
    fun getHex(x: Int, y: Int): Hex? {
        val hex = hexLayout.pixelToHex(Point(x.toDouble(), y.toDouble())).hexRound()
        return if (hex in hexes) hex else null
    }

}