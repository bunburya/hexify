package eu.bunburya.hexify.view

import eu.bunburya.hexify.controller.ImageController
import javafx.scene.Group
import tornadofx.*

class DualImageView : View("Dual Image View") {

    private val imageController: ImageController by inject()
    private val inputImageView: InputImageView by inject()
    private val outputImageView: OutputImageView by inject()

    override var root = hbox {
        add(inputImageView)
        add(outputImageView)
    }

}
