package eu.bunburya.hexify.model

import javafx.scene.paint.Color
import kotlin.math.max
import kotlin.math.min

interface ColorAggregator {

    companion object {

        val availableAggregators = arrayListOf(
            "mean",
            "median",
            "mode",
            "count",
            "range"
        )
        fun factory(name: String): ColorAggregator {
            return when (name) {
                "mean" -> MeanAggregator()
                "median" -> MedianAggregator()
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

class MeanAggregator: ColorAggregator {

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

class ModeAggregator: ColorAggregator {

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

class MedianAggregator: ColorAggregator {

    private val redValues = mutableListOf<Double>()
    private val greenValues = mutableListOf<Double>()
    private val blueValues = mutableListOf<Double>()

    private fun median(values: Collection<Double>): Double {
        val sorted = values.sorted()
        val n = sorted.size / 2
        return if (sorted.size % 2 == 0) (sorted[n] + sorted[n+1]) / 2 else sorted[n]
    }

    private val medianRed: Double get() = median(redValues)
    private val medianGreen: Double get() = median(greenValues)
    private val medianBlue: Double get() = median(blueValues)


    override val aggregateColor: Color get() = Color(medianRed, medianGreen, medianBlue, 1.0)

    override fun add(color: Color) {
        redValues.add(color.red)
        greenValues.add(color.green)
        blueValues.add(color.blue)
    }
}

class CountAggregator: ColorAggregator {
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

class RangeAggregator: ColorAggregator {

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