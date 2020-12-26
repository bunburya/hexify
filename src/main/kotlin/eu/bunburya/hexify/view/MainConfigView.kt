package eu.bunburya.hexify.view

import eu.bunburya.hexify.controller.MainController
import eu.bunburya.hexify.model.Mosaic
import eu.bunburya.hexify.model.MosaicConfig
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.Spinner
import tornadofx.*

class MainConfigView : View("Hexify -> Configure") {

    private val mainController: MainController by inject()

    private var mosaicTypeDropdown: ComboBox<String> by singleAssign()
    private var aggregatorDropdown: ComboBox<String> by singleAssign()
    private var filterListView: ListView<String> by singleAssign()
    private lateinit var mosaicConfigView: MosaicConfigView
    private lateinit var mosaicConfigFieldset: Fieldset
    val selectedMosaicType = SimpleStringProperty()

    override val root = form {
        fieldset("Configure") {
            field("Mosaic type") {
                mosaicTypeDropdown = combobox(selectedMosaicType) {
                    items = FXCollections.observableArrayList(mainController.availableMosaicTypes)
                    selectionModel.select(mainController.userConfig.mosaicType)
                }
            }
            field("Colour aggregation method") {
                aggregatorDropdown = combobox {
                    items = FXCollections.observableArrayList(mainController.availableAggregators)
                    selectionModel.select(mainController.userConfig.aggregatorMethod)
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
        }

        mosaicConfigFieldset = fieldset()

        buttonbar {
            button("Save") {
                action {
                    mainController.userConfig.apply {
                        aggregatorMethod = aggregatorDropdown.value
                        filters = filterListView.selectionModel.selectedItems
                        mosaicType = mosaicTypeDropdown.value
                    }
                    mosaicConfigView.onSubmit()
                    //mainController.updateMosaic()
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

    init {
        mosaicConfigView = MosaicConfigView.factory(mosaicTypeDropdown.value)
        root.add(mosaicConfigView)
        selectedMosaicType.onChange {
            println("Replacing mosaic config view to $it.  Dropdown value is ${mosaicTypeDropdown.value}.")
            val newMosaicConfigView = MosaicConfigView.factory(mosaicTypeDropdown.value)
            mosaicConfigView.replaceWith(newMosaicConfigView)
            mosaicConfigView = newMosaicConfigView
        }
    }

}
