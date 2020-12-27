package eu.bunburya.hexify.model

class MainConfig {

    var mosaicType = "hex"
    var aggregatorMethod = "mode"
    var filters: MutableList<String> = mutableListOf()

}

interface MosaicConfig {
    companion object {
        fun factory(type: String): MosaicConfig {
            val configFunc = Mosaic.availableTypes[type]?.second
            if (configFunc == null) {
                throw IllegalArgumentException("$type not a valid mosaic type.")
            } else {
                return configFunc()
            }
            /*return when (type) {
                "hex" -> HexConfig()
                "square" -> SquareConfig()
                else ->
            }*/
        }
    }
}