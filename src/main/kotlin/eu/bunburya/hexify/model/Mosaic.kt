package eu.bunburya.hexify.model

import eu.bunburya.hexify.model.hex.HexConfig
import eu.bunburya.hexify.model.hex.HexMosaic

/**
 * A Mosaic is a collection of Tiles.  It needs to implement a means of generating the set of all Tiles, as well
 * as a means of mapping (x, y) coordinates to Tiles.
 */
interface Mosaic {

    companion object {

        val availableTypes = arrayListOf(
            "hex"
        )

        fun factory(type: String, imageWidth: Int, imageHeight: Int, config: MosaicConfig): Mosaic {
            when (type) {
                "hex" -> return HexMosaic(config as HexConfig, imageWidth, imageHeight)
                else -> throw IllegalArgumentException("$type not a valid mosaic type.")
            }
        }
    }

    val tiles: MutableSet<out Tile>
    fun getTile(x: Int, y: Int): Tile?

}