val numbers: List<String> by lazy {
    readFile("Snailfish").split("\n")
}

fun lexemize(input: String): ArrayDeque<String> {
    val lexems = ArrayDeque<String>()
    var i = 0
    while (i < input.length) {
        val char = input[i]
        if (char.isDigit()) {
            var end = i
            while (input[end].isDigit()) end++
            lexems.add(input.substring(i, end))
            i = end
        } else {
            lexems.add(char.toString())
            i++
        }
    }
    return lexems
}

fun reduce(input: ArrayDeque<String>): ArrayDeque<String> {
    var wasReduced = true
    while (wasReduced) {
        wasReduced = false
        if (explode(input)) {
            wasReduced = true
            continue
        }
        if (split(input)) {
            wasReduced = true
        }
    }
    return input
}

fun explode(input: ArrayDeque<String>): Boolean {
    var balance = 0
    val size = input.size
    var i = 0
    while (i < size) {
        val lex = input[i]
        if (lex == "[") {
            balance++
            if (balance >= 5) {
                val isSimplePair = isNumber(input[i + 1]) && input[i + 2] == "," && isNumber(input[i + 3])
                if (isSimplePair) {
                    val left = input[i + 1].toInt()
                    val right = input[i + 3].toInt()
                    for (j in i downTo 0) {
                        if (isNumber(input[j])) {
                            input[j] = (input[j].toInt() + left).toString()
                            break
                        }
                    }
                    for (j in i + 4 until size) {
                        if (isNumber(input[j])) {
                            input[j] = (input[j].toInt() + right).toString()
                            break
                        }
                    }
                    input[i] = "0"
                    // wut
                    input.removeAt(i + 1)
                    input.removeAt(i + 1)
                    input.removeAt(i + 1)
                    input.removeAt(i + 1)
                    return true
                }
            }
        }
        if (lex == "]") balance--
        i++
    }
    return false
}

fun split(input: ArrayDeque<String>): Boolean {
    val size = input.size
    var i = 0
    while (i < size) {
        val lex = input[i]
        if (isNumber(lex) && lex.toInt() >= 10) {
            val lexVal = lex.toInt()
            input[i] = "["
            // wut #2
            input.add(i + 1, "]")
            input.add(i + 1, ((lexVal + 1) / 2).toString())
            input.add(i + 1, ",")
            input.add(i + 1, (lexVal / 2).toString())
            return true
        }
        i++
    }
    return false
}

fun calculate(numbers: List<String>): ArrayDeque<String> {
    return numbers.drop(1).fold(lexemize(numbers[0])) { lexems, s ->
        lexems.addFirst("[")
        lexems.addLast(",")
        lexems.addAll(lexemize(s))
        lexems.addLast("]")
        reduce(lexems)
    }
}

fun findMagnitude(lex: ArrayDeque<String>): Int {
    val numbers = ArrayDeque<Int>()
    for (i in lex.indices) {
        val curLex = lex[i]
        if (isNumber(curLex)) {
            numbers.add(curLex.toInt())
        }
        if (curLex == "]") {
            numbers.add(2 * numbers.removeLast() + 3 * numbers.removeLast())
        }
    }
    return numbers.removeLast()
}

fun findLargestMagnitude(numbers: List<String>): Int {
    return numbers.orderedPairs().maxOf { pair ->
        findMagnitude(reduce(lexemize("[${pair.first},${pair.second}]")))
    }
}

fun main() {
    println(findMagnitude(calculate(numbers)))
    println(findLargestMagnitude(numbers))
}

fun isNumber(s: String): Boolean = s[0].isDigit()