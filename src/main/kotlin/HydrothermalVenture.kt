import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.count
import kotlin.math.sign

val segments: List<Segment> by lazy {
    readFile("HydrothermalVenture").split("\n").map {
        val points = it.split(" -> ")
        val begin = points[0].split(",")
        val end = points[1].split(",")
        Segment(
            Integer.parseInt(begin[0]),
            Integer.parseInt(begin[1]),
            Integer.parseInt(end[0]),
            Integer.parseInt(end[1])
        )
    }
}

fun findDangerousPoints(filter: (Segment) -> Boolean = { true }): Int {
    val visited = mk.zeros<Int>(1000, 1000)
    segments.filter(filter).forEach {
        val xStep = (it.x2 - it.x1).sign
        val yStep = (it.y2 - it.y1).sign

        var curX = it.x1
        var curY = it.y1

        visited[curX, curY] = visited[curX, curY] + 1

        while (curX != it.x2 || curY != it.y2) {
            curX += xStep
            curY += yStep
            visited[curX, curY] = visited[curX, curY] + 1
        }
    }

    return visited.count { it >= 2 }
}

fun main() {
    println(findDangerousPoints { (it.x1 == it.x2) || (it.y1 == it.y2) })
    println(findDangerousPoints())
}

data class Segment(val x1: Int, val y1: Int, val x2: Int, val y2: Int)