package eu.bunburya.hexify.model.hex

import eu.bunburya.hexagons.*
import eu.bunburya.hexify.model.Mosaic
import eu.bunburya.hexify.model.PolygonTile
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.properties.Delegates

class BadOrientationError(msg: String): Exception(msg)

class HexMosaic(
    config: HexConfig,
    private val imageWidth: Int,
    private val imageHeight:Int
): Mosaic {


    var hexOrientation: LayoutOrientation
    var hexSize by Delegates.notNull<Int>()
    var hexPixelWidth by Delegates.notNull<Int>()
    var hexPixelHeight by Delegates.notNull<Int>()
    var gridWidth by Delegates.notNull<Int>()
    var gridHeight by Delegates.notNull<Int>()
    var horizontalMargin by Delegates.notNull<Int>()
    var verticalMargin by Delegates.notNull<Int>()
    lateinit var origin: Point

    init {
        hexSize = config.hexSize
        if (config.orientation == "pointy") {
            hexOrientation = LAYOUT_POINTY
        } else if (config.orientation == "flat") {
            hexOrientation = LAYOUT_FLAT
        } else {
            throw BadOrientationError("Orientation must be \"pointy\" or \"flat\", not ${config.orientation}.")
        }
        if (hexOrientation == LAYOUT_FLAT) {
            hexPixelWidth = hexSize * 2
            hexPixelHeight = (sqrt(3.0) * hexSize).toInt()
        } else if (hexOrientation == LAYOUT_POINTY) {
            hexPixelWidth = (sqrt(3.0) * hexSize).toInt()
            hexPixelHeight = hexSize * 2
        } else {
            throw IllegalArgumentException("Invalid layout orientation: $hexOrientation.")
        }
        setMeasurements()
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
        val numHexes = ((maxPixels - (0.5 * hexSize * sqrt(3.0))) / (hexSize * sqrt(3.0))).toInt()
        val gridPixelLength = ((numHexes * hexSize * sqrt(3.0)) + (0.5 * hexSize * sqrt(3.0))).toInt()
        return Pair(numHexes, gridPixelLength)
    }

    private fun setMeasurements() {
        val widthMeasurements: Pair<Int, Int>
        val heightMeasurements: Pair<Int, Int>
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
    }
    private val hexLayout = Layout(hexOrientation, Point(hexSize.toDouble(), hexSize.toDouble()), origin)

    private fun hexToTile(hex: Hex): PolygonTile
            = PolygonTile(hexLayout.polygonCorners(hex).flatten())

    override val tiles
            = MapBuilder.rectangle(gridWidth, gridHeight, "qr")
                .map(this::hexToTile)
                .toMutableSet()

    override fun getTile(x: Int, y: Int): PolygonTile? {
        val hex = hexLayout.pixelToHex(Point(x.toDouble(), y.toDouble())).hexRound()
        val hexTile = hexToTile(hex)
        return if (hexTile in tiles) hexTile else null
    }

}