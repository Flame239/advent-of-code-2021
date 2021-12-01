private val dummy = object {}

fun readFile(filename: String): String {
    return dummy::class.java.getResource("$filename").readText()
}