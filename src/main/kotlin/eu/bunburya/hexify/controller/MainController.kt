package eu.bunburya.hexify.controller

import eu.bunburya.hexify.model.*
import eu.bunburya.hexify.view.MainConfigView
import eu.bunburya.hexify.view.MainView
import javafx.stage.FileChooser
import tornadofx.Controller
import tornadofx.FileChooserMode
import tornadofx.chooseFile

class MainController: Controller() {

    private val imageController: ImageController by inject()

    private val mainView: MainView by inject()
    private val mainConfigView: MainConfigView by inject()

    val userConfig = MainConfig()
    var mosaicConfig = MosaicConfig.factory(userConfig.mosaicType)
    val availableMosaicTypes = Mosaic.availableTypes
    val availableAggregators = ColorAggregator.availableAggregators
    val availableFilters = Filter.availableFilters

    var currentFile: String? = null

    val validFileTypes = arrayOf(
        FileChooser.ExtensionFilter("All", "*.bmp", "*.gif", "*.jpeg", "*.jpg", "*.png"),
        FileChooser.ExtensionFilter("Bitmap", "*.bmp"),
        FileChooser.ExtensionFilter("Gif", "*.gif"),
        FileChooser.ExtensionFilter("Jpeg", "*.jpeg", "*.jpg"),
        FileChooser.ExtensionFilter("PNG", "*.png")
    )

    fun getFileType(name: String): String? {
        var suffix: String
        for (filter in validFileTypes) {
            for (extension in filter.extensions) {
                suffix = extension.removePrefix("*")
                if (name.endsWith(suffix)) {
                    return suffix.removePrefix(".")
                }
            }
        }
        return null
    }

    fun openFile() {
        val fileList = chooseFile("Choose a image to load.", filters=validFileTypes)
        if (fileList.isNotEmpty()) {
            imageController.loadImage(fileList[0].toURI().toString())
        }
    }

    fun saveFile() {
        val fileList = chooseFile("Choose save location.", filters=validFileTypes,
            mode = FileChooserMode.Save)
        if (fileList.isNotEmpty()) {
            imageController.writeImage(fileList[0].toURI().toString())
        }
    }

    fun rerun() {
        imageController.loadImage()
    }

    fun hexify() {
        imageController.mosaify()
    }

    fun launchConfig() {
        mainConfigView.openWindow()
    }

    fun clearAll() {
        imageController.clearAll()
        currentFile = null
    }

    fun quit() {
        System.exit(0)
    }
}