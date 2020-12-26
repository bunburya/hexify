package eu.bunburya.hexify.view

import javafx.scene.control.Label
import tornadofx.*

class StatusBarView : View("Status Bar") {

    private var statusLabel: Label by singleAssign()

    fun update(text: String) {
        statusLabel.text = text
    }

    override val root = hbox {
        statusLabel = label("Welcome to Mosaify.")
    }

}
