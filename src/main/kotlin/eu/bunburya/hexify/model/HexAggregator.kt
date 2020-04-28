package eu.bunburya.hexify.model

import javafx.scene.paint.Color
import java.lang.Math.pow
import kotlin.math.max
import kotlin.math.min

interface HexAggregator {

    companion object {

        val availableAggregators = arrayListOf(
            "mean",
            "mode",
            "count",
            "range"
        )
        fun factory(name: String): HexAggregator {
            return when (name) {
                "mean" -> MeanAggregator()
                "mode" -> ModeAggregator()
                "count" -> CountAggregator()
                "range" -> RangeAggregator()
                else -> throw IllegalArgumentException("$name not a valid aggregator name.")
            }
        }
    }

    fun add(color: Color)
    val aggregateColor: Color
}

class MeanAggregator: HexAggregator {

    private var totalRed = 0.0
    private var totalGreen = 0.0
    private var totalBlue = 0.0
    private var number = 0
    private val averageRed: Double get() = totalRed / number
    private val averageGreen: Double get() = totalGreen / number
    private val averageBlue: Double get() = totalBlue / number

    override val aggregateColor: Color get() = Color(averageRed, averageGreen, averageBlue, 1.0)

    override fun add(color: Color) {
        totalRed += color.red
        totalGreen += color.green
        totalBlue += color.blue
        number++
    }
}

class ModeAggregator: HexAggregator {

    private val colorCount: MutableMap<Color, Int> = mutableMapOf()

    override fun add(color: Color) {
        colorCount[color] = colorCount.getOrDefault(color, 0) + 1
    }

    override val aggregateColor: Color get() {
        val modeCount = colorCount.values.max()
        val modes = colorCount.keys.filter { colorCount[it] == modeCount }
        return if (modes.size == 1) {
            modes[0]
        } else {
            Color(
                modes.sumByDouble { it.red } / modes.size,
                modes.sumByDouble { it.green } / modes.size,
                modes.sumByDouble { it.blue } / modes.size,
                1.0
            )
        }
    }
}

class CountAggregator: HexAggregator {
    private val uniqueColors: MutableSet<Color> = mutableSetOf()
    private var pixelCount = 0
    override fun add(color: Color) {
        uniqueColors.add(color)
        pixelCount++
    }

    override val aggregateColor: Color get() {
        val gray = uniqueColors.size.toDouble() / pixelCount
        return Color(gray, gray, gray, 1.0)
    }
}

class RangeAggregator: HexAggregator {

    private val colors: MutableList<Color> = mutableListOf()
    private var pixelCount = 0
    private var maxRed = 0.0
    private var minRed = 1.0
    private var maxGreen = 0.0
    private var minGreen = 1.0
    private var maxBlue = 0.0
    private var minBlue = 1.0

    override fun add(color: Color) {
        maxRed = max(maxRed, color.red)
        minRed = min(minRed, color.red)
        maxGreen = max(maxGreen, color.green)
        minGreen = min(minGreen, color.green)
        maxBlue = max(maxBlue, color.blue)
        minBlue = min(minBlue, color.blue)
    }

    override val aggregateColor: Color get() {
        return Color(
            max(maxRed - minRed, 0.0),
            max(maxGreen - minGreen, 0.0),
            max(maxBlue - minBlue, 0.0),
            1.0
        )
    }

}