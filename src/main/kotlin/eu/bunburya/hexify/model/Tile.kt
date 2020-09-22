package eu.bunburya.hexify.model

interface Tile

data class PolygonTile(val points: List<Number>): Tile
data class CircleTile(val center: Pair<Number, Number>, val radius: Number): Tile
