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
                button("Mosaify") {
                    action {
                        mainController.mosaify()
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

        bottom(StatusBarView::class)

    }



}
