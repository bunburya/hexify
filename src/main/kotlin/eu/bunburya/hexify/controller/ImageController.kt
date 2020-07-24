package eu.bunburya.hexify.controller

import eu.bunburya.hexify.model.Hexifier
import eu.bunburya.hexify.view.ImageContainerView
import eu.bunburya.hexify.view.InputImageView
import eu.bunburya.hexify.view.OutputImageView
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import tornadofx.Controller
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

enum class ViewMode {
    INPUT,
    OUTPUT,
    DUAL
}

class ImageController: Controller() {

    private val mainController: MainController by inject()
    private val inputImageView: InputImageView by inject()
    private val outputImageView: OutputImageView by inject()
    private val imageContainerView: ImageContainerView by inject()

    val initialViewMode = ViewMode.INPUT
    lateinit var hexifier: Hexifier

    var imageWidth = 0
    var imageHeight = 0
    var showHexOverlay = false

    var outputImage: Image? = null
        set(new) {
            println("Setting outputImage to $new")
            field = new
            refreshAll()
        }
    var inputImage: Image? = null
        set(new) {
            println("Setting inputImage to $new")
            field = new
            if (new == null) {
                imageWidth = 0
                imageHeight = 0
            } else {
                imageWidth = new.width.toInt()
                imageHeight = new.height.toInt()
                hexifier = Hexifier(new, mainController.userConfig)
            }
            outputImage = null
        }

    init {
        println("Initialising ImageController.")
        imageContainerView.viewMode = initialViewMode
    }

    fun loadImage(file: String? = mainController.currentFile) {
        if (file == null) {
            return
        }
        println("Loading image $file.")
        inputImage = Image(file)
        mainController.currentFile = file
    }

    fun hexify() {
        outputImage = hexifier.hexify()
    }

    fun writeImage(fileName: String? = mainController.currentFile) {
        if (fileName == null) {
            return
        }
        var bufferedImage = SwingFXUtils.fromFXImage(outputImage, null)
        val fileType = mainController.getFileType(fileName)
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

    fun refreshAll() {
        inputImageView.showImage()
        outputImageView.showImage()
    }

    fun clearAll() {
        inputImage = null
        outputImage = null
    }
}