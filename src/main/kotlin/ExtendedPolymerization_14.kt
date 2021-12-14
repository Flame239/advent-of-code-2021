val polymerInput: PolymerInput by lazy {
    val lines = readFile("ExtendedPolymerization").split("\n")
    val initialPolymer = lines[0]

    val rulesMap = mutableMapOf<String, String>()
    lines.drop(2).map { line ->
        line.split(" -> ").also { rulesMap[it[0]] = it[1] }
    }
    PolymerInput(initialPolymer, rulesMap)
}

fun polymerGrowSimulation(): String {
    val (poly, rulesMap) = polymerInput

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
    }

    return curPolymer.toString()
}

fun countFrequencies(): Int {
    val result = polymerGrowSimulation()
    val frequencies = IntArray(26)
    result.forEach { frequencies[it - 'A']++ }
    return frequencies.filter { it != 0 }.sorted().let { it.last() - it.first() }
}

fun polymerGrowOptimized(): Long {
    val (poly, rulesMap) = polymerInput

    val frequencies = LongArray(26)
    poly.forEach {
        frequencies[it - 'A']++
    }
    var pairFrequencies = mutableMapOf<String, Long>()
    poly.zipWithNext { a, b -> a.toString().plus(b) }.forEach {
        pairFrequencies.merge(it, 1, Long::plus)
    }

    repeat(40) {
        val newPairFrequencies = mutableMapOf<String, Long>()
        pairFrequencies.forEach { (pair, count) ->
            val insertion = rulesMap[pair]
            if (insertion == null) {
                newPairFrequencies[pair] = count
            } else {
                frequencies[insertion[0] - 'A'] += count
                newPairFrequencies.merge(pair[0] + insertion, count, Long::plus)
                newPairFrequencies.merge(insertion + pair[1], count, Long::plus)
            }
        }
        pairFrequencies = newPairFrequencies
    }

    return frequencies.filter { it != 0L }.sorted().let { it.last() - it.first() }
}

fun main() {
    println(countFrequencies())
    println(polymerGrowOptimized())
}

data class PolymerInput(val initialPolymer: String, val rules: Map<String, String>)