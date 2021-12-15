val risks: Array<IntArray> by lazy {
    readFile("Chiton").split("\n").map { it.map { it.digitToInt() }.toIntArray() }.toTypedArray()
}

fun findLeastRiskyPath(field: Array<IntArray>): Int {
    val h = field.size
    val w = field[0].size

    val edges = mutableListOf<Edge>()

    for (i in 0 until h) {
        for (j in 0 until w) {
            if (i < h - 1) {
                edges.add(Edge(coordsToString(i, j), coordsToString(i + 1, j), field[i][j] + field[i + 1][j]))
            }
            if (i > 0) {
                edges.add(Edge(coordsToString(i, j), coordsToString(i - 1, j), field[i][j] + field[i - 1][j]))
            }
            if (j < w - 1) {
                edges.add(Edge(coordsToString(i, j), coordsToString(i, j + 1), field[i][j] + field[i][j + 1]))
            }
            if (j > 0) {
                edges.add(Edge(coordsToString(i, j), coordsToString(i, j - 1), field[i][j] + field[i][j - 1]))
            }
        }
    }

    val graph = Graph(edges, true)
    graph.dijkstra(coordsToString(0, 0))
    val dist = graph.getDistanceTo(coordsToString(h - 1, w - 1))

    return (dist - field[0][0] + field[h - 1][w - 1]) / 2
}

fun getBigField(field: Array<IntArray>): Array<IntArray> {
    val h = field.size
    val w = field[0].size
    val bigH = field.size * 5
    val bigW = field[0].size * 5

    val bigField = Array(bigH) { IntArray(bigW) }

    for (i in 0 until bigH) {
        for (j in 0 until bigW) {
            val delta = i / h + j / w
            var newVal = field[i % 100][j % 100] + delta
            if (newVal > 9) newVal -= 9
            bigField[i][j] = newVal
        }
    }

    return bigField
}

fun main() {
    println(findLeastRiskyPath(risks))
    println(findLeastRiskyPath(getBigField(risks)))
}


fun coordsToString(i: Int, j: Int) = "$i,$j"
