val fishes: List<Int> by lazy {
    readFile("Lanternfish").split(",").map { it.toInt() }
}

fun fishesAfter(fishes: List<Int>, days: Int): Long {
    val fishesWithTimer = LongArray(9)
    fishes.forEach { fishesWithTimer[it]++ }

    for (i in 0 until days) {
        val doneFishes = fishesWithTimer[0]

        fishesWithTimer[0] = fishesWithTimer[1]
        fishesWithTimer[1] = fishesWithTimer[2]
        fishesWithTimer[2] = fishesWithTimer[3]
        fishesWithTimer[3] = fishesWithTimer[4]
        fishesWithTimer[4] = fishesWithTimer[5]
        fishesWithTimer[5] = fishesWithTimer[6]
        fishesWithTimer[6] = fishesWithTimer[7]
        fishesWithTimer[7] = fishesWithTimer[8]

        fishesWithTimer[6] += doneFishes
        fishesWithTimer[8] = doneFishes
    }

    return fishesWithTimer.sum()
}

fun main() {
    println(fishesAfter(fishes, 80))
    println(fishesAfter(fishes, 256))
}