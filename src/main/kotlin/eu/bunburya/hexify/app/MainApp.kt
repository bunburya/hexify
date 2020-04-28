package eu.bunburya.hexify.app

import eu.bunburya.hexify.view.MainView
import javafx.stage.Stage
import tornadofx.App

class MainApp: App(MainView::class) {

    lateinit var imageUrl: String

    /*override fun start(stage: Stage) {
        // Placeholder for now
        imageUrl = parameters.named["image-url"]
    }*/
}