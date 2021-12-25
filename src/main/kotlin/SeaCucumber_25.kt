private fun getSeaCucumbersMap(): Array<CharArray> {
    return readFile("SeaCucumber").split("\n").map { it.toCharArray() }.toTypedArray()
}

fun move(map: Array<CharArray>): Boolean {
    val h = map.size
    val w = map[0].size

    var moved = false
    var i = 0
    var j = 0
    while (i < h) {
        j = 0
        val last = if (map[i][0] == '.') w else w - 1
        while (j < last) {
            val next = (j + 1) % w
            if (map[i][j] == '>' && map[i][next] == '.') {
                moved = true
                map[i][j] = '.'
                map[i][next] = '>'
                j++
            }
            j++
        }
        i++
    }

    j = 0
    while (j < w) {
        i = 0
        val last = if (map[0][j] == '.') h else h - 1
        while (i < last) {
            val next = (i + 1) % h
            if (map[i][j] == 'v' && map[next][j] == '.') {
                moved = true
                map[i][j] = '.'
                map[next][j] = 'v'
                i++
            }
            i++
        }
        j++
    }
    return moved
}

fun findStepWhenStopsMoving(map: Array<CharArray>): Int {
    var step = 0
    while (move(map)) step++
    return step + 1
}

fun main() {
    println(findStepWhenStopsMoving(getSeaCucumbersMap()))
}
