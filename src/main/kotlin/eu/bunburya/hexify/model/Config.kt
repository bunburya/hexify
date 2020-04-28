package eu.bunburya.hexify.model

import kotlin.reflect.KClass

class Config {

    var hexSize = 15
    var hexAggregator = "mode"
    var filters: MutableList<String> = mutableListOf()

}