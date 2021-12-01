fun main() {
    val depths = readFile("SonarSweep").split("\n").map { Integer.parseInt(it) }
    var increments = 0

    for (i in 0..(depths.size - 2)) {
        if (depths[i + 1] > depths[i]) {
            increments++
        }
    }

    println(increments)
}