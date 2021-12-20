fun getTrenchMap(): TrenchMapInput {
    val lines = readFile("TrenchMap").split("\n")
    val enhancer = lines[0].map { if (it == '#') '1' else '0' }
    val image = Array(lines.size - 2) { CharArray(lines[2].length) { '0' } }
    lines.drop(2).forEachIndexed { index, line ->
        line.forEachIndexed { sIndex, char ->
            image[index][sIndex] = if (char == '#') '1' else '0'
        }
    }
    return TrenchMapInput(enhancer, image)
}

fun enhanceImage(enhancer: List<Char>, image: Array<CharArray>, defaultChar: Char): Pair<Char, Array<CharArray>> {
    val enhancedImage = Array(image.size + 2) { CharArray(image[0].size + 2) { defaultChar } }
    for (i in enhancedImage.indices) {
        for (j in enhancedImage[0].indices) {
            val binaryIndex = listOf(
                image.getOrNull(i - 2)?.getOrNull(j - 2) ?: defaultChar,
                image.getOrNull(i - 2)?.getOrNull(j - 1) ?: defaultChar,
                image.getOrNull(i - 2)?.getOrNull(j) ?: defaultChar,
                image.getOrNull(i - 1)?.getOrNull(j - 2) ?: defaultChar,
                image.getOrNull(i - 1)?.getOrNull(j - 1) ?: defaultChar,
                image.getOrNull(i - 1)?.getOrNull(j) ?: defaultChar,
                image.getOrNull(i)?.getOrNull(j - 2) ?: defaultChar,
                image.getOrNull(i)?.getOrNull(j - 1) ?: defaultChar,
                image.getOrNull(i)?.getOrNull(j) ?: defaultChar
            ).joinToString("").toInt(2)
            enhancedImage[i][j] = enhancer[binaryIndex]
        }
    }
    val newDefaultChar = enhancer[defaultChar.toString().repeat(9).toInt(2)]
    return Pair(newDefaultChar, enhancedImage)
}

fun lightUpCount(enhancementsCount: Int): Int {
    var (enhancer, image) = getTrenchMap()
    var defaultChar = '0'
    repeat(enhancementsCount) {
        val (newDefaultChar, enhancedImage) = enhanceImage(enhancer, image, defaultChar)
        defaultChar = newDefaultChar
        image = enhancedImage
    }
    return image.sumOf { it.count { c -> c == '1' } }
}

fun main() {
    println(lightUpCount(2))
    println(lightUpCount(50))
}

data class TrenchMapInput(val enhancer: List<Char>, val image: Array<CharArray>)