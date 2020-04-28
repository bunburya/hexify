package eu.bunburya.hexify.view

import eu.bunburya.hexify.controller.MainController
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import tornadofx.*

class MainView : View("Hexify") {

    private val mainController: MainController by inject()

    override val root = borderpane {
        top {
            hbox {
                button("Open file") {
                    action {
                        mainController.chooseImage()
                    }
                }
                button("Clear") {
                    action {
                        mainController.clearAll()
                    }
                }
                button("Configure") {
                    action {
                        mainController.launchConfig()
                    }
                }
                button("Re-run") {
                    action {
                        mainController.loadImage()
                    }
                }
                button("Save image") {
                    action {
                        mainController.saveImage()
                    }
                }
                button("Quit") {
                    action {
                        mainController.quit()
                    }
                }
            }
        }

        center(DualImageView::class)

    }



}
