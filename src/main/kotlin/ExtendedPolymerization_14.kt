val polymerInput: PolymerInput by lazy {
    val lines = readFile("ExtendedPolymerization").split("\n")
    val initialPolymer = lines[0]

    val rules = lines.drop(2).map {
        val (pair, insert) = it.split(" -> ")
        Rule(pair, insert)
    }
    PolymerInput(initialPolymer, rules)
}

fun polymerGrowSimulation(): String {
    val (poly, rules) = polymerInput

    val rulesMap = mutableMapOf<String, String>()
    rules.forEach { rulesMap[it.charPair] = it.insertChar }

    var curPolymer = StringBuilder(poly)
    repeat(10) {
        val curLen = curPolymer.length
        val nextPoly = StringBuilder(curLen)
        for (i in 0 until curLen - 1) {
            nextPoly.append(curPolymer[i])
            val replacement = rulesMap[curPolymer.substring(i, i + 2)] ?: ""
            nextPoly.append(replacement)
        }
        nextPoly.append(curPolymer.last())
        curPolymer = nextPoly

        println(curPolymer)
    }

    return curPolymer.toString()
}


fun countFrequencies(): Int {
    val result = polymerGrowSimulation()
    val frequencies = IntArray(26)
    result.forEach { frequencies[it - 'A']++ }
    frequencies.sort()
    return frequencies.last() - frequencies.first { it != 0 }
}

fun polymerGrowOptimized() {
    // just keep track of pairs of chars
}

fun main() {
    println(countFrequencies())
}

data class PolymerInput(val initialPolymer: String, val rules: List<Rule>)
data class Rule(val charPair: String, val insertChar: String)