package eu.bunburya.hexify.controller

import eu.bunburya.hexify.model.*
import eu.bunburya.hexify.view.MainConfigView
import eu.bunburya.hexify.view.MainView
import eu.bunburya.hexify.view.StatusBarView
import javafx.stage.FileChooser
import tornadofx.Controller
import tornadofx.FileChooserMode
import tornadofx.chooseFile

class MainController: Controller() {

    private val imageController: ImageController by inject()

    private val mainView: MainView by inject()
    private val statusBarView: StatusBarView by inject()
    private val mainConfigView: MainConfigView by inject()

    val userConfig = MainConfig()
    var mosaicConfig = MosaicConfig.factory(userConfig.mosaicType)
        set(new) {
            field = new
            imageController.mosaifier.mosaicConfig = new
        }
    val availableMosaicTypes = Mosaic.availableTypes.keys
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
            val fromFile = fileList[0].toURI().toString()
            imageController.loadImage(fromFile)
            status("Loaded image at $fromFile.")
        }
    }

    fun saveFile() {
        val fileList = chooseFile(
            "Choose save location.", filters = validFileTypes,
            mode = FileChooserMode.Save
        )
        if (fileList.isNotEmpty()) {
            val toFile = fileList[0].toURI().toString()
            imageController.writeImage(toFile)
            status("Saved image to $toFile.")
        }
    }

    fun mosaify() {
        try {
            imageController.mosaify()
        } catch (e: UninitializedPropertyAccessException) {
            status("Must provide input image to mosaify.")
        }
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

    fun status(text: String = "") {
        statusBarView.update(text)
    }
}