package eu.bunburya.hexify.view

import eu.bunburya.hexify.controller.ImageController
import tornadofx.*

class ImageContainerView : View("Image Container") {

    private val imageController: ImageController by inject()
    private val inputImageView: InputImageView by inject()
    private val outputImageView: OutputImageView by inject()
    private val dualImageView: DualImageView by inject()
    private val initialView = inputImageView

    override val root = borderpane {
        top {
            hbox {
                button("Input Image") {
                    action {
                        showInputImage()
                    }
                }
                button("Output Image") {
                    action {
                        showOutputImage()
                    }
                }
                button("Dual Image") {
                    action {
                        showDualImage()
                    }
                }
            }
        }

        center = initialView.root
    }

    fun showInputImage() {
        root.center = inputImageView.root
    }

    fun showOutputImage() {
        root.center = outputImageView.root
    }

    fun showDualImage() {
        root.center = hbox {
            add(inputImageView)
            add(outputImageView)
        }
    }

}
