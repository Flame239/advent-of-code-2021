import kotlin.math.abs

private val area = Area(211, 232, -124, -69)

private val hittingV0xPerStep = findHittingV0xPerStep()

fun findHittingV0xPerStep(): Map<Int, List<Int>> {
    val possibleXSteps = mutableListOf<AxisHit>()
    for (Vx0 in 0..area.maxX) {
        var curX = 0
        for (step in 0..Vx0) {
            curX += (Vx0 - step)
            if (area.containsX(curX)) {
                possibleXSteps.add(AxisHit(Vx0, step))
            }
        }
        if (area.containsX(curX)) {
            println("Would stay forever when Vx0 = $Vx0")
        }
    }
    return possibleXSteps.groupBy { it.step }.mapValues { it.value.map { axisHit -> axisHit.v0 } }
}

fun getHittingV0xPerStep(steps: Int): List<Int> =
    if (steps >= 21) listOf(21) else hittingV0xPerStep[steps] ?: emptyList()

fun findAllPossibleVelocitiesCount(maxVY: Int): Int {
    var total = 0
    for (Vy0 in area.minY..maxVY) {
        var curStep = 0
        var curY = 0
        val complementaryVx0 = mutableSetOf<Int>()
        while (curY >= area.minY) {
            curY += (Vy0 - curStep)
            if (area.containsY(curY)) {
                complementaryVx0.addAll(getHittingV0xPerStep(curStep))
            }
            curStep++
        }
        total += complementaryVx0.size
    }
    return total
}

// if Vy > 0 => we end up in 0 again after 2*Vy steps. Then we should hit the area with maximum possible next step
// For this we should make a step of `area.minY` and end up in `area.minY`(inside the area).
// We can't take step which is more, because we will not end up in area,
// And if we take step less, then highest height will be less.
// So Vy0 = |area.minY| - 1
fun findHighestPossibleTrajectoryPosition(): Int {
    // for ANY step we can find xV such as after this number of steps we end up in area by x
    // for ex: 21 -> when we reach curXV = 0 we will be in area and will stay forever
    // for other steps we could find such xV as well

    val maxVY = abs(area.minY) - 1
    // max Y will be when Y velocity will drop to 0, and = sum of geom progression
    return maxVY * (maxVY + 1) / 2
}

fun main() {
    println(findHighestPossibleTrajectoryPosition())
    println(findAllPossibleVelocitiesCount(abs(area.minY) - 1))
}

data class Area(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int) {
    fun containsX(x: Int): Boolean = x in minX..maxX
    fun containsY(y: Int): Boolean = y in minY..maxY
}

data class AxisHit(val v0: Int, val step: Int)
