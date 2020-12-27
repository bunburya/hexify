package eu.bunburya.hexify.model.brick

import eu.bunburya.hexify.model.Mosaic
import eu.bunburya.hexify.model.PolygonTile
import eu.bunburya.hexify.model.Tile

class BrickMosaic(
    config: BrickConfig,
    private val imageWidth: Int,
    private val imageHeight: Int
): Mosaic {

    private val brickHeight = config.brickHeight
    private val brickWidth = brickHeight * 2
    private val startXEven = (imageWidth % brickWidth) / 2
    private val startXOdd = startXEven + (brickWidth / 2)
    private val startY = (imageHeight % brickHeight) / 2
    private val numBricksXEven = imageWidth / brickWidth
    private val numBricksXOdd = numBricksXEven - 1
    private val numBricksY = imageHeight / brickHeight
    private val endXEven = startXEven + (numBricksXEven * brickWidth)
    private val endXOdd = startXOdd + (numBricksXOdd * brickWidth)
    private val endY = startY + (numBricksY * brickHeight)

    private fun topLeftToBrick(x: Int, y: Int): PolygonTile {
        return PolygonTile(listOf(
            x, y,
            x + brickWidth, y,
            x + brickWidth, y + brickHeight,
            x, y + brickHeight
        ))
    }

    override val tiles = mutableSetOf<PolygonTile>().apply {
        for (row in 0..numBricksY) {
            val topLeftY = startY + (row * brickHeight)
            var topLeftX: Int
            val numBricksX: Int

            if (row % 2 == 0) {
                // Even row
                numBricksX = numBricksXEven
            } else {
                // Odd row
                numBricksX = numBricksXOdd
            }

            for (brick in 0..numBricksX) {
                topLeftX = startXOdd + (brick * brickWidth)
                this.add(topLeftToBrick(topLeftX, topLeftY))
            }
        }
    }

    override fun getTile(x: Int, y: Int): Tile? {
        if (y < startY || y > endY) return null
        val row = (y - startY) / brickHeight
        if (row > numBricksY) return null
        val topLeftY = startY + (row * brickHeight)
        val startX: Int
        val endX: Int
        val numBricksX: Int
        if (row % 2 == 0) {
            startX = startXEven
            numBricksX = numBricksXEven
            endX = endXEven
        } else {
            startX = startXOdd
            numBricksX = numBricksXOdd
            endX = endXOdd
        }
        if (x < startX || x > endX) return null
        val brick = (x - startX) / brickWidth
        if (brick > numBricksX) return null
        val topLeftX = startX + (brick * brickWidth)
        return topLeftToBrick(topLeftX, topLeftY)
    }

}