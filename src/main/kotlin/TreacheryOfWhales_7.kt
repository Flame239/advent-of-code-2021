import kotlin.math.abs

val crabPositions: List<Int> by lazy {
    readFile("TreacheryOfWhales").split(",").map { it.toInt() }
}


fun getMostEfficientCrabsPosition(): Int {
    val position = crabPositions.sorted()[crabPositions.size / 2]
    return crabPositions.sumOf { abs(position - it) }
}

fun main() {
    println(getMostEfficientCrabsPosition())


}