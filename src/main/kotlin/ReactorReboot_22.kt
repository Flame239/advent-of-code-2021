import kotlin.math.max
import kotlin.math.min

val steps: List<RebootStep> by lazy {
    readFile("ReactorReboot").split("\n").map {
        val (operation, remaining) = it.split(" ")
        val (x, y, z) = remaining.split(",").map { parseRange(it) }
        RebootStep(RebootOperation.valueOf(operation.uppercase()), x, y, z)
    }
}

fun stupidIntersection(): Int {
    val onPoints = mutableSetOf<V3>()
    steps.forEach {
        for (x in max(it.x.first, -50)..min(it.x.last, 50)) {
            for (y in max(it.y.first, -50)..min(it.y.last, 50)) {
                for (z in max(it.z.first, -50)..min(it.z.last, 50)) {
                    if (it.operation == RebootOperation.ON) {
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

fun main() {
    println(stupidIntersection())

//    var totalOn = 0L
//    val sortedSteps = steps.sortedWith(compareBy({ it.x.first }, { it.y.first }, { it.z.first }))
//    val onOperations = sortedSteps.filter { it.operation == RebootOperation.ON }
//    val offOperations = sortedSteps.filter { it.operation == RebootOperation.OFF }
//
//    for (i in onOperations.indices) {
//        val curOp = onOperations[i]
//        for (j in i + 1 until onOperations.size) {
//            if (curOp.intersect(onOperations[j])) {
//                println("$curOp intersects with ${onOperations[j]}")
//            }
//        }
//    }
}

fun parseRange(s: String): IntRange {
    val (beg, end) = s.substring(2).split("..").map { it.toInt() }
    return IntRange(beg, end)
}

data class RebootStep(val operation: RebootOperation, val x: IntRange, val y: IntRange, val z: IntRange) {
    fun intersect(otherStep: RebootStep) =
        x.intersect(otherStep.x) && y.intersect(otherStep.y) && z.intersect(otherStep.z)
}

enum class RebootOperation {
    ON, OFF
}