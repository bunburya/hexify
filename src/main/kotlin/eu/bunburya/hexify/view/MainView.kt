package eu.bunburya.hexify.view

import eu.bunburya.hexify.controller.MainController
import tornadofx.*

class MainView : View("Hexify") {

    private val mainController: MainController by inject()

    override val root = borderpane {
        top {
            hbox {
                button("Open file") {
                    action {
                        mainController.openFile()
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
                button("Hexify") {
                    action {
                        mainController.hexify()
                    }
                }
                // TODO:  Change this to "Hexify"
                button("Re-run") {
                    action {
                        mainController.rerun()
                    }
                }
                button("Save image") {
                    action {
                        mainController.saveFile()
                    }
                }
                button("Quit") {
                    action {
                        mainController.quit()
                    }
                }
            }
        }

        center(ImageContainerView::class)
        //center(InputImageView::class)

        bottom(StatusBarView::class)

    }



}
