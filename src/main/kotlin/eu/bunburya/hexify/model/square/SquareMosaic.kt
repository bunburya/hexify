package eu.bunburya.hexify.model.square

import eu.bunburya.hexify.model.Mosaic
import eu.bunburya.hexify.model.PolygonTile
import eu.bunburya.hexify.model.Tile

class SquareMosaic(
    config: SquareConfig,
    private val imageWidth: Int,
    private val imageHeight: Int
): Mosaic {

    private val squareSize = config.squareSize
    private val startX = (imageWidth % config.squareSize) / 2
    private val startY = (imageHeight % config.squareSize) / 2
    private val numSquaresX = imageWidth / config.squareSize
    private val numSquaresY = imageHeight / config.squareSize
    private val endX = startX + (numSquaresX * config.squareSize)
    private val endY = startY + (numSquaresY * config.squareSize)

    /**
     * Returns a square PolygonTile with (x, y) as the top-left corner.
     */
    private fun topLeftToSquare(x: Int, y: Int): PolygonTile {
        return PolygonTile(listOf(
            x, y,
            x + squareSize, y,
            x + squareSize, y + squareSize,
            x, y + squareSize
        ))
    }

    override val tiles = mutableSetOf<PolygonTile>().apply {
        for (i in startX..endX step squareSize) {
            for (j in startY..endY step squareSize) {
                this.add(topLeftToSquare(i, j))
            }
        }

    }

    override fun getTile(x: Int, y: Int): Tile? {
        return if (x <= imageWidth && y <= imageHeight)
            topLeftToSquare(
                x - ((x - startX) % squareSize),
                y - ((y - startY) % squareSize)
            )
        else null
    }

}