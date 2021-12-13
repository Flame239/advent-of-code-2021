fun getOrigamiInput(): OrigamiInput {
    val lines = readFile("TransparentOrigami").split("\n")
    val paper = lines.takeWhile { s -> s.isNotEmpty() }.map { s -> s.split(",").map { it.toInt() }.zipWithNext()[0] }
    val maxX = paper.maxOf { it.first }
    val maxY = paper.maxOf { it.second }
    val paperArray = Array(maxY + 1) { BooleanArray(maxX + 1) }
    paper.forEach { paperArray[it.second][it.first] = true }

    val foldings = lines.takeLastWhile { s -> s.isNotEmpty() }.map {
        val split = it.substring(11).split("=")
        Folding(if (split[0] == "y") FoldingDirection.UP else FoldingDirection.LEFT, split[1].toInt())
    }

    return OrigamiInput(paperArray, foldings)
}

fun applyFolding(paper: Array<BooleanArray>, w: Int, h: Int, folding: Folding): Pair<Int, Int> {
    if (folding.direction == FoldingDirection.UP) {
        for (x in 0 until w) {
            for (y in folding.position + 1 until h) {
                val newY = 2 * folding.position - y
                paper[newY][x] = paper[newY][x] || paper[y][x]
            }
        }
        return Pair(w, folding.position)
    } else {
        for (x in folding.position + 1 until w) {
            for (y in 0 until h) {
                val newX = 2 * folding.position - x
                paper[y][newX] = paper[y][newX] || paper[y][x]
            }
        }
        return Pair(folding.position, h)
    }
}

fun countPoints(): Int {
    val (paper, folding) = getOrigamiInput()

    val (newW, newH) = applyFolding(paper, paper[0].size, paper.size, folding[0])

    var dotsCount = 0
    for (x in 0 until newW) {
        for (y in 0 until newH) {
            if (paper[y][x]) {
                dotsCount++
            }
        }
    }

    return dotsCount
}

fun printPaperAfterFolding() {
    val (paper, foldings) = getOrigamiInput()
    var w = paper[0].size
    var h = paper.size

    foldings.forEach {
        val (newW, newH) = applyFolding(paper, w, h, it)
        w = newW
        h = newH
    }

    for (y in 0 until h) {
        for (x in 0 until w) {
            print(if (paper[y][x]) "* " else "  ")
        }
        println()
    }
}

fun main() {
    println(countPoints())
    printPaperAfterFolding()
}

data class OrigamiInput(val paper: Array<BooleanArray>, val folding: List<Folding>)

data class Folding(val direction: FoldingDirection, val position: Int)

enum class FoldingDirection {
    UP, LEFT
}