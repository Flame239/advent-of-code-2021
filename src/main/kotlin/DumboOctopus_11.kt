fun getEnergyLevels(): Array<IntArray> {
    val lists: List<List<Int>> = readFile("DumboOctopus").split("\n").map { it.map { c -> c.digitToInt() } }
    return Array(lists.size) { i -> IntArray(lists[i].size) { j -> lists[i][j] } }
}

fun countFlashes(): Int {
    val energyLevels = getEnergyLevels()
    var totalFlashes = 0

    val h = energyLevels.size
    val w = energyLevels[0].size

    repeat(100) {
        val flashed = Array(h) { BooleanArray(w) }
        for (i in 0 until h) {
            for (j in 0 until w) {
                incrementRecursively(energyLevels, flashed, i, j)
            }
        }
        totalFlashes += flashed.sumOf { arr -> arr.count { it } }
    }

    return totalFlashes
}

fun firstSynchronousFlashStep(): Int {
    val energyLevels = getEnergyLevels()
    val h = energyLevels.size
    val w = energyLevels[0].size

    for (step in 1 until Integer.MAX_VALUE) {
        val flashed = Array(h) { BooleanArray(w) }
        for (i in 0 until h) {
            for (j in 0 until w) {
                incrementRecursively(energyLevels, flashed, i, j)
            }
        }
        if (flashed.all { arr -> arr.all { it } }) {
            return step
        }
    }
    return -1
}

fun incrementRecursively(energyLevels: Array<IntArray>, flashed: Array<BooleanArray>, i: Int, j: Int) {
    val outOfBounds = outOfBounds(i, j, energyLevels.size, energyLevels[0].size)
    if (outOfBounds || flashed[i][j]) {
        return
    }
    energyLevels[i][j]++
    if (energyLevels[i][j] <= 9) {
        return
    }
    flashed[i][j] = true
    energyLevels[i][j] = 0
    incrementRecursively(energyLevels, flashed, i + 1, j)
    incrementRecursively(energyLevels, flashed, i + 1, j + 1)
    incrementRecursively(energyLevels, flashed, i + 1, j - 1)

    incrementRecursively(energyLevels, flashed, i - 1, j)
    incrementRecursively(energyLevels, flashed, i - 1, j + 1)
    incrementRecursively(energyLevels, flashed, i - 1, j - 1)

    incrementRecursively(energyLevels, flashed, i, j + 1)
    incrementRecursively(energyLevels, flashed, i, j - 1)
}

fun outOfBounds(i: Int, j: Int, maxI: Int, maxJ: Int) = i < 0 || i >= maxI || j < 0 || j >= maxJ

fun main() {
    println(countFlashes())
    println(firstSynchronousFlashStep())
}