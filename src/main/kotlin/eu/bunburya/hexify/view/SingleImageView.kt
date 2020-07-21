package eu.bunburya.hexify.view

import eu.bunburya.hexify.controller.ImageController
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import tornadofx.*

abstract class SingleImageView(title: String): View(title){

    protected val imageController: ImageController by inject()
    protected lateinit var imageNode: Node

    abstract fun showImage()

    fun clearImage() {
        println("Clearing")
        getChildList()?.clear() // TODO: Get to work
    }

    protected fun showOverlay(group: Group) {
        var points: Array<Double>
        for (h in imageController.hexifier.hexOverlay.hexes) {
            points = Array<Double>(12) { i -> imageController.hexifier.hexOverlay.hexLayout.polygonCorners(h).flatten()[i] }
            group.polygon(*points) {
                stroke = Color.WHITE
                strokeWidth = 0.5
                fill = null
                //fill = controller.getCellColor(range, valueFunc(cell))
            }
        }
    }

}

class InputImageView(): SingleImageView("Input Image") {

    override val root = scrollpane {
        isFitToWidth = true
        isFitToHeight = true
        imageNode = stackpane {
            group()
        }
        hboxConstraints {
            hGrow = Priority.ALWAYS
        }
    }

    override fun showImage() {
        val image = imageController.inputImage
        if (image != null) {
            imageNode.group {
                imageview(image)
                if (imageController.showHexOverlay) {
                    showOverlay(this)
                }
            }
            root.apply{
                hboxConstraints {
                    prefWidth = imageController.imageWidth.toDouble()
                    prefHeight = imageController.imageHeight.toDouble()
                }
            }
        } else {
            println("inputImage is null")
            clearImage()
        }
    }
}

class OutputImageView(): SingleImageView("Output Image") {

    override val root = scrollpane {
        isFitToWidth = true
        isFitToHeight = true
        imageNode = group()
        hboxConstraints {
            hGrow = Priority.ALWAYS
        }
    }

    override fun showImage() {
        // TODO: Separate out hexify call here.
        val image = imageController.outputImage
        if (image != null) {
            imageNode.imageview(image)
            root.apply {
                hboxConstraints {
                    prefWidth = imageController.imageWidth.toDouble()
                    prefHeight = imageController.imageHeight.toDouble()
                }
            }
        } else {
            println("outputImage is null")
            clearImage()
        }
    }

}
