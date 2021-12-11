import java.util.*

val brackets: List<String> by lazy {
    readFile("SyntaxScoring").split("\n").map { it.trim() }
}

val errorScorePerBracket = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)

val completeScorePerBracket = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4
)

const val openBrackets = "([{<"

val closedBracketToOpen = mapOf(
    ')' to '(',
    ']' to '[',
    '}' to '{',
    '>' to '<'
)

val openBracketToClose = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>'
)

fun findSyntaxErrorScoreForCorrupted(): Int {
    val stack = ArrayDeque<Char>()
    var totalError = 0
    brackets.forEach outer@{ s ->
        s.forEach { c ->
            if (isOpening(c)) {
                stack.push(c)
            } else {
                val correctOpen = closedBracketToOpen[c]
                if (stack.pop() != correctOpen) {
                    totalError += errorScorePerBracket[c]!!
                    return@outer
                }
            }
        }
    }

    return totalError
}

fun completeLines(): Long {
    val counts = mutableListOf<Long>()
    brackets.forEach outer@{ s ->
        val stack = ArrayDeque<Char>()
        s.forEach { c ->
            if (isOpening(c)) {
                stack.push(c)
            } else {
                val correctOpen = closedBracketToOpen[c]
                if (stack.pop() != correctOpen) {
                    return@outer
                }
            }
        }

        val curCount = stack.fold(0L) { acc, c ->
            acc * 5 + completeScorePerBracket[openBracketToClose[c]]!!
        }
        counts.add(curCount)
    }

    return counts.sorted()[counts.size / 2];
}

fun isOpening(c: Char): Boolean {
    return openBrackets.contains(c)
}

fun main() {
    println(findSyntaxErrorScoreForCorrupted())
    println(completeLines())
}