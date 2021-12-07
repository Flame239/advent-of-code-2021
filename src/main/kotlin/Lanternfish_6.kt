val fishes: List<Int> by lazy {
    readFile("Lanternfish").split(",").map { it.toInt() }
}

fun fishesAfter(fishes: List<Int>, days: Int): Long {
    val fishesWithTimer = LongArray(9)
    fishes.forEach { fishesWithTimer[it]++ }

    repeat(days) {
        val doneFishes = fishesWithTimer[0]

        for (i in 0..7) {
            fishesWithTimer[i] = fishesWithTimer[i + 1]
        }

        fishesWithTimer[6] += doneFishes
        fishesWithTimer[8] = doneFishes
    }

    return fishesWithTimer.sum()
}

fun main() {
    println(fishesAfter(fishes, 80))
    println(fishesAfter(fishes, 256))
}