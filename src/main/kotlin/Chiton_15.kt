val risks: Array<IntArray> by lazy {
    readFile("Chiton").split("\n").map { it.map { it.digitToInt() }.toIntArray() }.toTypedArray()
}

fun findLeastRiskyPath(): Int {
    val w = risks.size
    val h = risks[0].size

    val edges = mutableListOf<Edge>()

    for (i in 0 until h) {
        for (j in 0 until w) {
            if (i < h - 1) {
                edges.add(Edge(coordsToString(i, j), coordsToString(i + 1, j), risks[i][j] + risks[i + 1][j]))
            }
            if (i > 0) {
                edges.add(Edge(coordsToString(i, j), coordsToString(i - 1, j), risks[i][j] + risks[i - 1][j]))
            }
            if (j < w - 1) {
                edges.add(Edge(coordsToString(i, j), coordsToString(i, j + 1), risks[i][j] + risks[i][j + 1]))
            }
            if (j > 0) {
                edges.add(Edge(coordsToString(i, j), coordsToString(i, j - 1), risks[i][j] + risks[i][j - 1]))
            }
        }
    }

    val graph = Graph(edges, true)
    graph.dijkstra(coordsToString(0, 0))
    val dist = graph.getDistanceTo(coordsToString(h - 1, w - 1))

    return (dist - risks[0][0] + risks[h - 1][w - 1]) / 2
}

fun main() {
    println(findLeastRiskyPath())
}


fun coordsToString(i: Int, j: Int) = "$i,$j"
