package eu.bunburya.hexify.controller

import eu.bunburya.hexify.model.Config
import eu.bunburya.hexify.model.Filter
import eu.bunburya.hexify.model.HexAggregator
import eu.bunburya.hexify.model.Hexifier
import eu.bunburya.hexify.view.ConfigView
import eu.bunburya.hexify.view.DualImageView
import eu.bunburya.hexify.view.MainView
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.stage.FileChooser
import tornadofx.Controller
import tornadofx.FileChooserMode
import tornadofx.chooseFile
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


class MainController: Controller() {

    private val mainView: MainView by inject()
    private val dualImageView: DualImageView by inject()
    private val configView: ConfigView by inject()

    val userConfig = Config()
    val availableAggregators = HexAggregator.availableAggregators
    val availableFilters = Filter.availableFilters

    var currentFile: String? = null

    var imageWidth = 0
    var imageHeight = 0
    lateinit var outputImage: WritableImage
    lateinit var hexifier: Hexifier
    var inputImage: Image? = null
        set(new: Image?) {
            field = new
            if (new == null) {
                imageWidth = 0
                imageHeight = 0
                outputImage = WritableImage(0, 0)
            } else {
                imageWidth = new.width.toInt()
                imageHeight = new.height.toInt()
                outputImage = WritableImage(new.pixelReader, imageWidth, imageHeight)
            }
            hexifier = Hexifier(outputImage, userConfig)
        }

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

    fun chooseImage() {
        val fileList = chooseFile("Choose an image to load.", filters=validFileTypes)
        if (fileList.isNotEmpty()) {
            loadImage(fileList[0].toURI().toString())
        }
    }

    fun loadImage(file: String? = currentFile) {
        if (file == null) {
            return
        }
        inputImage = Image(file)
        clearAll()
        dualImageView.showInputImage()
        dualImageView.showOutputImage()
        currentFile = file
    }

    fun saveImage() {
        val fileList = chooseFile("Choose save location.", filters=validFileTypes, mode = FileChooserMode.Save)
        if (fileList.isNotEmpty()) {
            writeImage(fileList[0].toURI().toString())
        }
    }

    fun writeImage(fileName: String? = currentFile) {
        if (fileName == null) {
            return
        }
        var bufferedImage = SwingFXUtils.fromFXImage(outputImage, null)
        val fileType = getFileType(fileName)
        if (fileType == "jpg" || fileType == "jpeg") {
            bufferedImage = removeTransparency(bufferedImage)
        }
        val file = File(fileName.removePrefix("file:"))
        file.createNewFile()
        ImageIO.write(bufferedImage, fileType, file)
    }

    fun removeTransparency(input: BufferedImage): BufferedImage {
        val width = input.width
        val height = input.height
        val pixels = IntArray(width * height)
        input.getRGB(0, 0, width, height, pixels, 0, width)
        val output = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        output.setRGB(0, 0, width, height, pixels, 0, width)
        return output
    }

    fun clearAll() {
        dualImageView.clearInputImage()
        dualImageView.clearOutputImage()
        currentFile = null
    }

    fun launchConfig() {
        configView.openWindow()
    }

    fun quit() {
        System.exit(0)
    }
}