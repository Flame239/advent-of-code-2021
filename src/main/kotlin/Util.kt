private val dummy = object {}

fun readFile(filename: String): String {
    return dummy::class.java.getResource("$filename").readText()
}

infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}