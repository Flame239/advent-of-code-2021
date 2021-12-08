val digitLines: List<DigitLine> by lazy {
    readFile("SevenSegments").split("\n").map {
        val (signal, output) = it.split(" | ")
        DigitLine(signal.split(" ").map { it.sortAlphabetically() }, output.split(" ").map { it.sortAlphabetically() })
    }
}

val mapCharsToDigit = mapOf(
    Pair("abcefg", "0"),
    Pair("cf", "1"),
    Pair("acdeg", "2"),
    Pair("acdfg", "3"),
    Pair("bcdf", "4"),
    Pair("abdfg", "5"),
    Pair("abdefg", "6"),
    Pair("acf", "7"),
    Pair("abcdefg", "8"),
    Pair("abcdfg", "9"),
)

val commonCharsFor5 = listOf('a', 'd', 'g')
val commonCharsFor6 = listOf('a', 'b', 'f', 'g')

val digits1 = listOf('c', 'f')
val digits4 = listOf('b', 'c', 'd', 'f')
val digits7 = listOf('a', 'c', 'f')

fun countUniqueDigits(): Int =
    digitLines.sumOf { it.outputDigits.count { d -> d.length == 2 || d.length == 3 || d.length == 4 || d.length == 7 } }

fun findMappingsAndCalculate(): Int {
    var total = 0
    for (line in digitLines) {
        val mapping = mappingsTemplate()

        restrictMappingForNonUniqueDigit(line.signalDigits, mapping, 5, commonCharsFor5)
        restrictMappingForNonUniqueDigit(line.signalDigits, mapping, 6, commonCharsFor6)

        restrictMappingForUniqueDigit(line.signalDigits, mapping, digits1)
        restrictMappingForUniqueDigit(line.signalDigits, mapping, digits7)
        restrictMappingForUniqueDigit(line.signalDigits, mapping, digits4)

        repeat(7) {
            mapping.filter { it.value.size == 1 }.forEach {
                val decidedChar = it.value.first()
                val originalKey = it.key
                mapping.forEach { entry -> if (entry.key != originalKey) entry.value.remove(decidedChar) }
            }
        }

        val charMapping = mapping.mapValues { it.value.first() }

        val actualDigits = line.outputDigits
            .map { it.map { c -> charMapping[c]!! }.sorted().joinToString(separator = "") }

        val resultNumber = actualDigits.map { mapCharsToDigit[it]!! }.joinToString(separator = "").toInt()

        total += resultNumber
    }

    return total
}

fun restrictMappingForNonUniqueDigit(
    inputDigits: List<String>,
    mapping: MutableMap<Char, MutableSet<Char>>,
    charsLen: Int,
    commonChars: List<Char>
) {
    val curCommon = inputDigits
        .filter { it.length == charsLen }
        .map { it.toSet() }
        .reduce { acc, set -> set.intersect(acc) }

    curCommon.forEach { mapping[it] = mapping[it]!!.intersect(commonChars) as MutableSet }
}

fun restrictMappingForUniqueDigit(
    inputDigits: List<String>,
    mapping: MutableMap<Char, MutableSet<Char>>,
    digitChars: List<Char>
) {
    val curChars = inputDigits
        .filter { it.length == digitChars.size }
        .map { it.toSet() }
        .first()

    curChars.forEach { mapping[it] = mapping[it]!!.intersect(digitChars) as MutableSet }
}

fun mappingsTemplate(): MutableMap<Char, MutableSet<Char>> {
    val mapping = mutableMapOf<Char, MutableSet<Char>>()
    for (i in 0 until 7) {
        mapping['a' + i] = mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g')
    }
    return mapping
}

fun main() {
    println(countUniqueDigits())
    println(findMappingsAndCalculate())
}

data class DigitLine(val signalDigits: List<String>, val outputDigits: List<String>)