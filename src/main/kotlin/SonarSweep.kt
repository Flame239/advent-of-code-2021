fun increments() {
    val depths = readFile("SonarSweep").split("\n").map { Integer.parseInt(it) }
    var increments = 0

    for (i in 0..(depths.size - 2)) {
        if (depths[i + 1] > depths[i]) {
            increments++
        }
    }

    println(increments)
}

fun slidingWindowIncrements() {
    val depths = readFile("SonarSweep").split("\n").map { Integer.parseInt(it) }
    var increments = 0

    var curSum = depths[0] + depths[1] + depths[2]

    for (i in 0..(depths.size - 4)) {
        val nextSum = curSum - depths[i] + depths[i + 3]
        if (nextSum > curSum) {
            increments++
        }
        curSum = nextSum
    }
    println(increments)
}

fun main() {
    slidingWindowIncrements()
}