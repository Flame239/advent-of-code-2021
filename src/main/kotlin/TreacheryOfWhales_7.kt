import kotlin.math.abs
import kotlin.math.min

val crabPositions: List<Int> by lazy {
    readFile("TreacheryOfWhales").split(",").map { it.toInt() }
}

fun getMostEfficientCrabsPosition(): Int {
    val position = crabPositions.sorted()[crabPositions.size / 2]
    return crabPositions.sumOf { abs(position - it) }
}

fun getMostEfficientCrabsPosition2(): Int {
    val min = crabPositions.minOrNull()!!
    val max = crabPositions.maxOrNull()!!

    var minCost = Integer.MAX_VALUE
    for (i in min..max) {
        val curCost = crabPositions.sumOf { arithmeticProgressionSum(abs(i - it)) }
        minCost = min(curCost, minCost)

    }
    return minCost
}

fun arithmeticProgressionSum(n: Int): Int = n * (n + 1) / 2

fun main() {
    println(getMostEfficientCrabsPosition())
    println(getMostEfficientCrabsPosition2())
}