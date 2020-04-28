package eu.bunburya.hexify.view

import eu.bunburya.hexify.controller.MainController
import eu.bunburya.hexify.model.Filter
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.Spinner
import tornadofx.*

class ConfigView : View("Hexify -> Configure") {

    private val mainController: MainController by inject()

    var hexSizeSpinner: Spinner<Int> by singleAssign()
    var aggregatorDropdown: ComboBox<String> by singleAssign()
    var filterListView: ListView<String> by singleAssign()

    override val root = form {
        fieldset("Configure") {
            field("Hexagon size (pixels from centre to each corner)") {
                hexSizeSpinner = spinner(1, 40, mainController.userConfig.hexSize)
            }
            field("Colour aggregation method") {
                aggregatorDropdown = combobox {
                    items = FXCollections.observableArrayList(mainController.availableAggregators)
                    selectionModel.select(mainController.userConfig.hexAggregator)
                }
            }
            field("Filters") {
                filterListView = listview {
                    var i = 0
                    selectionModel.selectionMode = SelectionMode.MULTIPLE
                    for (filterName in mainController.availableFilters.keys) {
                        items.add(filterName)
                        if (filterName in mainController.userConfig.filters) {
                            selectionModel.select(i)
                        }
                        i++
                    }
                }
            }
            buttonbar {
                button("Save") {
                    action {
                        mainController.userConfig.apply {
                            hexSize = hexSizeSpinner.value
                            hexAggregator = aggregatorDropdown.value
                            filters = filterListView.selectionModel.selectedItems
                        }
                        close()
                    }
                }
                button("Cancel") {
                    action {
                        close()
                    }
                }
            }
        }

    }

}
