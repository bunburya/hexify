package eu.bunburya.hexify.model

/**
 * An interface for a visible set of tiles that can overlay an image.
 */

interface Overlay {

    val tiles: MutableSet<out Tile>

    /**
     * Return the Tile which contains the point (x, y), or null if there is no such Tile.
     */
    fun getTile(x: Int, y: Int): Tile?

}

