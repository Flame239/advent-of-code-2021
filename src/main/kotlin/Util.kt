private val dummy = object {}

fun readFile(filename: String): String {
    return dummy::class.java.getResource("$filename").readText()
}

infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

fun String.sortAlphabetically() = String(toCharArray().apply { sort() })

/** ¯\_(ツ)_/¯ */
fun hexCharToBinaryString(char: Char): String {
    return when (char) {
        '0' -> "0000"
        '1' -> "0001"
        '2' -> "0010"
        '3' -> "0011"
        '4' -> "0100"
        '5' -> "0101"
        '6' -> "0110"
        '7' -> "0111"
        '8' -> "1000"
        '9' -> "1001"
        'A' -> "1010"
        'B' -> "1011"
        'C' -> "1100"
        'D' -> "1101"
        'E' -> "1110"
        'F' -> "1111"
        else -> error("Not a hex char")
    }
}