package eu.bunburya.hexify.view

import eu.bunburya.hexify.controller.ImageController
import eu.bunburya.hexify.model.PolygonTile
import eu.bunburya.hexify.model.Tile
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import tornadofx.*

class TileTypeNotImplementedError(msg: String): Exception(msg)

abstract class SingleImageView(title: String): View(title){

    protected val imageController: ImageController by inject()
    protected lateinit var imageNode: Node

    abstract fun showImage()

    fun clearImage() {
        println("Clearing")
        imageNode.getChildList()?.clear() // TODO: Get to work
    }

    private fun drawTileOutline(group: Group, polygonTile: PolygonTile) {
        group.polygon(*polygonTile.points.toTypedArray()) {
            stroke = Color.WHITE
            strokeWidth = 0.5
            fill = null
            //fill = controller.getCellColor(range, valueFunc(cell))
        }
    }

    private fun drawTileOutline(group: Group, tile: Tile) {
        throw TileTypeNotImplementedError(
            "No method implemented to draw tiles of type \"${tile::class.simpleName}\"."
        )
    }

    protected fun showOverlay(group: Group) {
        for (t in imageController.mosaifier.mosaic.tiles) {
            drawTileOutline(group, t)
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
