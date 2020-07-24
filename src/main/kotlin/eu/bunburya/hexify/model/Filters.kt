package eu.bunburya.hexify.model

import javafx.scene.paint.Color

object Filter {

    // TODO:  Convert to base class, like HexAggregator, which takes hexified image as an input

    val availableFilters = mutableMapOf(
        "grayscale" to this::grayscale,
        "negative" to this::negative
    )

    fun grayscale(color: Color): Color {
        val gray = (color.red + color.green + color.blue) / 3
        return Color(gray, gray, gray, 1.0)
    }

    fun negative(color: Color): Color {
        return Color(
            1 - color.red,
            1 - color.green,
            1 - color.blue,
            1.0
        )
    }

}