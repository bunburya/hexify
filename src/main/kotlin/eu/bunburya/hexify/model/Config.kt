package eu.bunburya.hexify.model

import eu.bunburya.hexify.model.hex.HexConfig
import eu.bunburya.hexify.model.square.SquareConfig

class MainConfig {

    var mosaicType = "hex"
    var aggregatorMethod = "mode"
    var filters: MutableList<String> = mutableListOf()

}

interface MosaicConfig {
    companion object {
        fun factory(type: String): MosaicConfig {
            return when (type) {
                "hex" -> HexConfig()
                "square" -> SquareConfig()
                else -> throw IllegalArgumentException("$type not a valid mosaic type.")
            }
        }
    }
}