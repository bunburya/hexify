package eu.bunburya.hexify.view

import eu.bunburya.hexify.controller.ImageController
import eu.bunburya.hexify.controller.ViewMode
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import tornadofx.*

class ImageContainerView : View("Image Container") {

    private val imageController: ImageController by inject()
    private val inputImageView: InputImageView by inject()
    private val outputImageView: OutputImageView by inject()
    private val viewToggle = ToggleGroup()

    lateinit var inputViewModeRadio: RadioButton
    lateinit var outputViewModeRadio: RadioButton
    lateinit var dualViewModeRadio: RadioButton

    var viewMode: ViewMode = ViewMode.INPUT     /* Initial value doesn't really matter; imageController
                                                   will re-set it to initialViewMode in any event, to
                                                   trigger the setter. */

        set(new) {
            println("Setting viewMode to $new.")
            field = new
            when (new) {
                /*ViewMode.INPUT -> showInputImage()
                ViewMode.OUTPUT -> showOutputImage()
                ViewMode.DUAL -> showDualImage()*/
                ViewMode.INPUT -> showInputImage()
                ViewMode.OUTPUT -> showOutputImage()
                ViewMode.DUAL -> showDualImage()
            }
        }

    override val root = borderpane {
        top {
            hbox {
                // TODO:  Link in properly with whatever is selected.
                inputViewModeRadio = radiobutton("Input Image", viewToggle) {
                    action {
                        showInputImage()
                    }
                }
                outputViewModeRadio = radiobutton("Output Image", viewToggle) {
                    action {
                        showOutputImage()
                    }
                }
                dualViewModeRadio = radiobutton("Dual Image", viewToggle) {
                    action {
                        showDualImage()
                    }
                }
            }
        }
    }

    fun showInputImage() {
        root.center = inputImageView.root
        inputViewModeRadio.isSelected = true
    }

    fun showOutputImage() {
        root.center = outputImageView.root
        outputViewModeRadio.isSelected = true
    }

    fun showDualImage() {
        root.center = gridpane {
            row {
                add(inputImageView)
                add(outputImageView)
                inputImageView.root.gridpaneColumnConstraints?.percentWidth = 50.0
                outputImageView.root.gridpaneColumnConstraints?.percentWidth = 50.0
            }
        }
        dualViewModeRadio.isSelected = true
    }

}
