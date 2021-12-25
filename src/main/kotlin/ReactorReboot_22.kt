import RebootOperation.ON
import kotlin.math.max
import kotlin.math.min

private val steps: List<RebootStep> by lazy {
    readFile("ReactorReboot").split("\n").map {
        val (operation, remaining) = it.split(" ")
        val (x, y, z) = remaining.split(",").map { parseRange(it) }
        RebootStep(RebootOperation.valueOf(operation.uppercase()), Cube(x, y, z))
    }
}

fun stupidIntersection(): Int {
    val onPoints = mutableSetOf<V3>()
    steps.forEach {
        for (x in max(it.x.first, -50)..min(it.x.last, 50)) {
            for (y in max(it.y.first, -50)..min(it.y.last, 50)) {
                for (z in max(it.z.first, -50)..min(it.z.last, 50)) {
                    if (it.operation == ON) {
                        onPoints.add(V3(x, y, z))
                    } else {
                        onPoints.remove(V3(x, y, z))
                    }
                }
            }
        }
    }

    return onPoints.size
}

private fun cleverIntersection(): Long {
    val xs = steps.flatMap { listOf(it.x.first, it.x.last + 1) }.sorted()
    val ys = steps.flatMap { listOf(it.y.first, it.y.last + 1) }.sorted()
    val zs = steps.flatMap { listOf(it.z.first, it.z.last + 1) }.sorted()

    val n = xs.size

    val compressedGrid = Array(n) { Array(n) { IntArray(n) } }

    steps.forEach { s ->
        for (x in xs.indexOf(s.x.first) until xs.indexOf(s.x.last + 1)) {
            for (y in ys.indexOf(s.y.first) until ys.indexOf(s.y.last + 1)) {
                for (z in zs.indexOf(s.z.first) until zs.indexOf(s.z.last + 1)) {
                    compressedGrid[x][y][z] = if (s.operation == ON) 1 else 0
                }
            }
        }
    }

    var totalOn = 0L
    for (x in 0 until n - 1) {
        for (y in 0 until n - 1) {
            for (z in 0 until n - 1) {
                totalOn += compressedGrid[x][y][z].toLong() * (xs[x + 1] - xs[x]) * (ys[y + 1] - ys[y]) * (zs[z + 1] - zs[z])
            }
        }
    }

    return totalOn
}

fun main() {
    println(stupidIntersection())
    println(cleverIntersection())
}

private fun parseRange(s: String): IntRange {
    val (beg, end) = s.substring(2).split("..").map { it.toInt() }
    return IntRange(beg, end)
}

private open class Cube(val x: IntRange, val y: IntRange, val z: IntRange) {
    override fun toString(): String {
        return "(${x.first}..${x.last},${y.first}..${y.last},${z.first}..${z.last})"
    }
}

private data class RebootStep(val operation: RebootOperation, val cube: Cube) {
    val x: IntRange by cube::x
    val y: IntRange by cube::y
    val z: IntRange by cube::z
}

private enum class RebootOperation {
    ON, OFF
}