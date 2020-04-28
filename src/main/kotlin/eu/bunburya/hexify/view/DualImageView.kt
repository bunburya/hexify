package eu.bunburya.hexify.view

import eu.bunburya.hexify.controller.MainController
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.ScrollPane
import javafx.scene.image.WritableImage
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import tornadofx.*

class DualImageView : View("My View") {

    private val mainController: MainController by inject()
    lateinit var inputImageNode: Node
    lateinit var outputImageNode: Node
    lateinit var inputScrollPane: ScrollPane
    lateinit var outputScrollPane: ScrollPane

    override val root = hbox {
        inputScrollPane = scrollpane {
            isFitToWidth = true
            isFitToHeight = true
            inputImageNode = stackpane ()
            hboxConstraints {
                hGrow = Priority.ALWAYS
            }
        }
        outputScrollPane = scrollpane {
            isFitToWidth = true
            isFitToHeight = true
            outputImageNode = group()
            hboxConstraints {
                hGrow = Priority.ALWAYS
            }
        }
    }

    fun showInputImage(showHexOverlay: Boolean = false) {
        val image = mainController.inputImage
        if (image != null) {
            inputImageNode.group {
                imageview(image)
                if (showHexOverlay) {
                    showOverlay(this)
                }
            }
            inputScrollPane.apply{
                hboxConstraints {
                    prefWidth = mainController.imageWidth.toDouble()
                    prefHeight = mainController.imageHeight.toDouble()
                }
            }
        }
    }

    fun showOutputImage() {
        outputImageNode.imageview(mainController.outputImage).apply {
            mainController.hexifier.hexify()
        }
        outputScrollPane.apply {
            hboxConstraints {
                prefWidth = mainController.imageWidth.toDouble()
                prefHeight = mainController.imageHeight.toDouble()
            }
        }
    }

    fun clearInputImage() {
        inputImageNode.getChildList()?.clear()
    }

    fun clearOutputImage() {
        outputImageNode.getChildList()?.clear()
    }

    private fun showOverlay(group: Group) {
        var points: Array<Double>
        for (h in mainController.hexifier.hexOverlay.hexes) {
            points = Array<Double>(12) { i -> mainController.hexifier.hexOverlay.hexLayout.polygonCorners(h).flatten()[i] }
            group.polygon(*points) {
                stroke = Color.WHITE
                strokeWidth = 0.5
                fill = null
                //fill = controller.getCellColor(range, valueFunc(cell))
            }
        }
    }

    private fun getImageView(): Group = group {
        val image = mainController.inputImage
        if (image != null) {
            imageview(image)
        }
    }
}
