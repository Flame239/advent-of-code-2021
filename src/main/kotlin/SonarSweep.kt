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

// 1737
// We dont have to calculate the sum
// Example: first 2 windows - [1,2,3] and [2,3,4] differs only in '1' and '4', so we only need to compare them
fun slidingWindowIncrements() {
    var increments = 0

    for (i in 0..(depths.size - 4)) {
        if (depths[i + 3] > depths[i]) {
            increments++
        }
    }
    println(increments)
}

fun bothIncrements() {
    var windowIncrements = 0
    var normalIncrements = 0

    for (i in 0..(depths.size - 4)) {
        if (depths[i + 3] > depths[i]) {
            windowIncrements++
        }

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