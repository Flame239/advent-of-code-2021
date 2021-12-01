val depths: List<Int> by lazy {
    readFile("SonarSweep").split("\n").map { Integer.parseInt(it) }
}

fun increments() {
    var increments = 0

    for (i in 0..(depths.size - 2)) {
        if (depths[i + 1] > depths[i]) {
            increments++
        }
    }

    println(increments)
}

fun slidingWindowIncrements() {
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

fun bothIncrements() {
    var windowIncrements = 0
    var normalIncrements = 0

    var curSum = depths[0] + depths[1] + depths[2]

    for (i in 0..(depths.size - 4)) {
        val nextSum = curSum - depths[i] + depths[i + 3]
        if (nextSum > curSum) {
            windowIncrements++
        }
        curSum = nextSum

        if (depths[i + 1] > depths[i]) {
            normalIncrements++
        }
    }

    for (i in (depths.size - 3)..(depths.size - 2)) {
        if (depths[i + 1] > depths[i]) {
            normalIncrements++
        }
    }

    println(normalIncrements)
    println(windowIncrements)
}

fun main() {
    increments()
    slidingWindowIncrements()
    bothIncrements()
}