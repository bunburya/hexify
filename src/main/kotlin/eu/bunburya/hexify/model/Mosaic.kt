package eu.bunburya.hexify.model

import eu.bunburya.hexify.model.brick.BrickConfig
import eu.bunburya.hexify.model.brick.BrickMosaic
import eu.bunburya.hexify.model.hex.HexConfig
import eu.bunburya.hexify.model.hex.HexMosaic
import eu.bunburya.hexify.model.square.SquareConfig
import eu.bunburya.hexify.model.square.SquareMosaic
import kotlin.reflect.full.primaryConstructor

/**
 * A Mosaic is a collection of Tiles.  It needs to implement a means of generating the set of all Tiles, as well
 * as a means of mapping (x, y) coordinates to Tiles.
 */
interface Mosaic {

    companion object {

        /**
         * availableTypes maps a String identifying each Mosaic type to Pair consisting of the KClass representing the
         * relevant Mosaic subclass and a function that takes no arguments and returns an instance of the relevant
         * MosaicConfig subclass (with default values).
         *
         * This lets easily add new Mosaic subclasses.
         *
         * NOTE: We also have to add each new Mosaic subclass to the factory function in MosaicConfigView.
         */
        val availableTypes = mapOf(
            "hex" to Pair(HexMosaic::class, { HexConfig() }),
            "square" to Pair(SquareMosaic::class, { SquareConfig() }),
            "brick" to Pair(BrickMosaic::class, { BrickConfig() })
        )

        fun factory(type: String, imageWidth: Int, imageHeight: Int, existingConfig: MosaicConfig): Mosaic {
            val mosaicType = availableTypes[type]?.first ?:
                throw IllegalArgumentException("$type not a valid mosaic type.")
            val config: MosaicConfig
            val newConfig = MosaicConfig.factory(type)
            config = if (existingConfig::class == newConfig::class) existingConfig else newConfig
            return mosaicType.primaryConstructor!!.call(config, imageWidth, imageHeight)
        }
    }

    val tiles: MutableSet<out Tile>
    fun getTile(x: Int, y: Int): Tile?

}