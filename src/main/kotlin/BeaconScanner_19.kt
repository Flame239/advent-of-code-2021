import kotlin.math.abs

val scanners: List<Scanner> by lazy {
    val lines = readFile("BeaconScanner").split("\n")
    val scanners = mutableListOf<Scanner>()
    var curLine = 1
    var curScanerId = 0
    var curBeacons = mutableListOf<V3>()
    var curScanner = Scanner(curScanerId, curBeacons)
    while (curLine < lines.size) {
        if (lines[curLine].isEmpty()) {
            scanners.add(curScanner)
            curScanerId++
            curBeacons = mutableListOf()
            curScanner = Scanner(curScanerId, curBeacons)
            curLine++
        } else {
            curBeacons.add(parseV3(lines[curLine]))
        }
        curLine++
    }
    scanners.add(curScanner)
    scanners
}

fun main() {
    val mainScanner = scanners[0]
    val scannersLeft = scanners.toMutableList()
    val scannerTranslations = mutableListOf<V3>()
    scannersLeft.removeAt(0)
    while (scannersLeft.isNotEmpty()) {
        for (i in scannersLeft.indices) {
            val curTranslation = overlap(mainScanner, scannersLeft[i])
            if (curTranslation != null) {
                scannerTranslations.add(curTranslation)
                scannersLeft.removeAt(i)
                break
            }
        }
    }

    println(mainScanner.beacons.size)

    val maxDist = scannerTranslations.maxOf { s1 ->
        scannerTranslations.maxOf { s2 -> s1.manhattanDistance(s2) }
    }
    println(maxDist)
}

fun overlap(s1: Scanner, s2: Scanner): V3? {
    val originBSet = s1.beacons.toHashSet()
    val s2rotations = getAllRotations(s2.beacons)
    for (originB in s1.beacons) {
        for (rotation in 0 until 24) {
            val curRotation: List<V3> = s2rotations[rotation]
            for (curB in curRotation) {
                val translation = V3(originB.x - curB.x, originB.y - curB.y, originB.z - curB.z)
                val curRotationTranslated = curRotation.map { it.translate(translation) }
                val overlap = curRotationTranslated.count { originBSet.contains(it) }
                if (overlap >= 12) {
                    s1.beacons.addAll(curRotationTranslated.filter { !originBSet.contains(it) })
                    return translation
                }
            }
        }
    }

    return null
}

fun parseV3(s: String): V3 {
    val (x, y, z) = s.split(",").map { it.toInt() }
    return V3(x, y, z)
}

fun getAllRotations(beacons: List<V3>): List<List<V3>> {
    val allRotationsT = beacons.map { it.rotations() }
    val allRotations = mutableListOf<List<V3>>()
    for (rotation in 0 until 24) {
        allRotations.add(allRotationsT.map { it[rotation] })
    }
    return allRotations
}

data class Scanner(val id: Int, val beacons: MutableList<V3>)

data class V3(val x: Int, val y: Int, val z: Int) {
    override fun toString(): String {
        return "($x,$y,$z)"
    }

    fun manhattanDistance(other: V3) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

    fun translate(translation: V3) = V3(x + translation.x, y + translation.y, z + translation.z)

    fun rotations(): List<V3> = listOf(
        V3(x, y, z),
        V3(-y, x, z),
        V3(-x, -y, z),
        V3(y, -x, z),
        V3(x, -y, -z),
        V3(y, x, -z),
        V3(-x, y, -z),
        V3(-y, -x, -z),
        V3(x, -z, y),
        V3(z, x, y),
        V3(-x, z, y),
        V3(-z, -x, y),
        V3(-y, -z, x),
        V3(z, -y, x),
        V3(y, z, x),
        V3(-z, y, x),
        V3(y, -z, -x),
        V3(z, y, -x),
        V3(-y, z, -x),
        V3(-z, -y, -x),
        V3(-x, -z, -y),
        V3(z, -x, -y),
        V3(x, z, -y),
        V3(-z, x, -y),
    )
}
