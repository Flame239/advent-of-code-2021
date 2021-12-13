val caveGraph: HashMap<String, MutableList<String>> by lazy {
    val paths = readFile("PassagePathing").split("\n").map { it.split("-").zipWithNext()[0] }
    val graph: HashMap<String, MutableList<String>> = HashMap()
    paths.forEach {
        graph.getOrPut(it.first) { mutableListOf() }.add(it.second)
        graph.getOrPut(it.second) { mutableListOf() }.add(it.first)
    }
    graph
}

fun countPaths(): Int {
    return dfs(caveGraph, hashSetOf("start"), "start")
}

fun countPathsWIthDoubleVisit(): Int {
    return dfsWithDoubleVisit(caveGraph, hashSetOf(), null, "start")
}

fun dfs(graph: HashMap<String, MutableList<String>>, visited: HashSet<String>, vertex: String): Int {
    var totalPaths = 0
    if (isSmallCave(vertex)) {
        visited.add(vertex)
    }
    for (adjacent in graph[vertex]!!) {
        if (visited.contains(adjacent)) {
            continue
        }
        if (adjacent == "end") {
            totalPaths++
        } else {
            totalPaths += dfs(graph, visited, adjacent)
        }
    }

    visited.remove(vertex)

    return totalPaths
}

fun dfsWithDoubleVisit(
    graph: HashMap<String, MutableList<String>>,
    visited: HashSet<String>,
    doubleVisited: String?,
    vertex: String
): Int {
    var curDoubleVisited = doubleVisited
    if (visited.contains(vertex)) {
        if (doubleVisited == null && vertex != "start") {
            curDoubleVisited = vertex
        } else {
            return 0
        }
    }
    println(curDoubleVisited)

    var totalPaths = 0
    if (isSmallCave(vertex)) {
        visited.add(vertex)
    }
    for (adjacent in graph[vertex]!!) {
        if (adjacent == "start") {
            continue
        }
        if (adjacent == "end") {
            totalPaths++
        } else {
            totalPaths += dfsWithDoubleVisit(graph, visited, curDoubleVisited, adjacent)
        }
    }

    if (vertex != curDoubleVisited) {
        visited.remove(vertex)
    }

    return totalPaths
}

fun isSmallCave(name: String) = Character.isLowerCase(name[0])

fun main() {
    println(countPaths())
    println(countPathsWIthDoubleVisit())
}
