package eu.bunburya.hexify.view

import eu.bunburya.hexify.controller.MainController
import eu.bunburya.hexify.model.MosaicConfig
import eu.bunburya.hexify.model.hex.HexConfig
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.Spinner
import tornadofx.*
import tornadofx.Stylesheet.Companion.form


abstract class MosaicConfigView(title: String? = null): View(title) {

    companion object {
        fun factory(type: String, existingConfig: MosaicConfig): MosaicConfigView {
            when (type) {
                "hex" -> return HexMosaicConfigView(existingConfig as HexConfig)
                else -> throw IllegalArgumentException("$type not a valid mosaic type.")
            }
        }
    }

    abstract fun onSubmit()
}

class HexMosaicConfigView(private var existingConfig: HexConfig? = null): MosaicConfigView("Configure HexMosaic") {

    private val mainController: MainController by inject()

    var hexSizeSpinner: Spinner<Int> by singleAssign()
    //var orientationDropdown: ComboBox<String> by singleAssign()

    override val root = fieldset("Configure hexagon mosaic") {
        field("Hexagon size (pixels from centre to each corner)") {
            hexSizeSpinner = spinner(1, 40, existingConfig?.hexSize)
        }
        /*field("Orientation dropdown") {
            orientationDropdown = combobox {
                items = FXCollections.observableArrayList(HexConfig.availableOrientations)
                selectionModel.select(existingConfig?.orientation)
            }
        } */
    }

    override fun onSubmit() {
        mainController.mosaicConfig = HexConfig(
            hexSize = hexSizeSpinner.value
            //orientation = orientationDropdown.value
        )
    }

}