val cave: Array<IntArray> by lazy {
    readFile("SmokeBasin").split("\n").map { it.map { it.digitToInt() }.toIntArray() }.toTypedArray()
}


fun findLowestPoints(): List<Pair<Int, Int>> {
    val h = cave.size
    val w = cave[0].size

    val lowestPoints = mutableListOf<Pair<Int, Int>>()

    for (i in 0 until h) {
        for (j in 0 until w) {
            var lowest = true
            val cur = cave[i][j]
            if (i >= 1) {
                lowest = lowest and (cur < cave[i - 1][j])
            }
            if (i <= h - 2) {
                lowest = lowest and (cur < cave[i + 1][j])
            }
            if (j >= 1) {
                lowest = lowest and (cur < cave[i][j - 1])
            }
            if (j <= w - 2) {
                lowest = lowest and (cur < cave[i][j + 1])
            }
            if (lowest) {
                lowestPoints.add(Pair(i, j))
            }
        }
    }
    return lowestPoints
}


fun findRiskLevel(): Int = findLowestPoints().sumOf { (i, j) -> cave[i][j] + 1 }

fun findBasins(): Int {
    val lowestPoints = findLowestPoints()
    val basinSizes = IntArray(lowestPoints.size)
    val visited = Array(cave.size) { BooleanArray(cave[0].size) }
    lowestPoints.forEachIndexed { index, (i, j) ->
        dfs(cave, visited, i, j, basinSizes, index)
    }

    return basinSizes.sortedArrayDescending().take(3).reduce(Int::times)
}

fun dfs(cave: Array<IntArray>, visited: Array<BooleanArray>, i: Int, j: Int, basinSizes: IntArray, curBasin: Int) {
    visited[i][j] = true
    basinSizes[curBasin]++
    if (i >= 1 && !visited[i - 1][j] && isStillBasin(cave[i][j], cave[i - 1][j])) {
        dfs(cave, visited, i - 1, j, basinSizes, curBasin)
    }
    if (i <= cave.size - 2 && !visited[i + 1][j] && isStillBasin(cave[i][j], cave[i + 1][j])) {
        dfs(cave, visited, i + 1, j, basinSizes, curBasin)
    }
    if (j >= 1 && !visited[i][j - 1] && isStillBasin(cave[i][j], cave[i][j - 1])) {
        dfs(cave, visited, i, j - 1, basinSizes, curBasin)
    }
    if (j <= cave[0].size - 2 && !visited[i][j + 1] && isStillBasin(cave[i][j], cave[i][j + 1])) {
        dfs(cave, visited, i, j + 1, basinSizes, curBasin)
    }
}

fun isStillBasin(curVal: Int, nextVal: Int) = curVal < nextVal && nextVal != 9

fun main() {
    println(findRiskLevel())
    println(findBasins())
}