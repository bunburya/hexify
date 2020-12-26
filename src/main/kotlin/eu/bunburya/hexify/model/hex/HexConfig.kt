package eu.bunburya.hexify.model.hex

import eu.bunburya.hexify.model.MosaicConfig

data class HexConfig(
    var hexSize: Int = 15,
    var orientation: String = "flat" /* NOTE: pointy not yet implemented, as we will need to adjust the
                                        layout to work with it. */
): MosaicConfig {
    companion object {
        val availableOrientations = arrayListOf(
            "pointy",
            "flat"
        )
    }
}