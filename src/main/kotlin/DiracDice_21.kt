// all is 0 based

private val p1Start = 3
private val p2Start = 9

fun simulateGame(): Int {
    val points = mutableListOf(0, 0)
    val positions = mutableListOf(p1Start, p2Start)
    var curPlayer = 0

    var nextDiePts = 0
    var dieRolls = 0

    while (true) {
        var curPts = 0
        repeat(3) {
            dieRolls++
            curPts += (nextDiePts + 1)
            nextDiePts = (nextDiePts + 1) % 100
        }

        positions[curPlayer] = (positions[curPlayer] + curPts) % 10
        points[curPlayer] += (positions[curPlayer] + 1)
        if (points[curPlayer] >= 1000) {
            return dieRolls * points[curPlayer xor 1]
        }
        curPlayer = curPlayer xor 1
    }
}

val diracDice3RollsOutcomes = mapOf(
    3 to 1,
    4 to 3,
    5 to 6,
    6 to 7,
    7 to 6,
    8 to 3,
    9 to 1
)

fun diracDiceGame(): Long {
    val winningUniCount = mutableListOf(0L, 0L)
    var gameStates = listOf(GameState(listOf(0, 0), listOf(p1Start, p2Start), 0, 1L))
    while (gameStates.isNotEmpty()) {
        val newGameStates = mutableListOf<GameState>()
        gameStates.forEach { game ->
            diracDice3RollsOutcomes.forEach { (pts, uniCount) ->
                val newPts = game.points.toMutableList()
                val newPos = game.positions.toMutableList()
                newPos[game.curPlayer] = (game.positions[game.curPlayer] + pts) % 10
                newPts[game.curPlayer] += newPos[game.curPlayer] + 1
                if (newPts[game.curPlayer] >= 21) {
                    winningUniCount[game.curPlayer] += game.uniCount * uniCount
                } else {
                    newGameStates.add(GameState(newPts, newPos, game.curPlayer xor 1, game.uniCount * uniCount))
                }
            }
        }
        gameStates = newGameStates
    }

    return winningUniCount.maxOrNull()!!
}

fun main() {
    println(simulateGame())
    println(diracDiceGame())
}

private data class GameState(
    val points: List<Int>,
    val positions: List<Int>,
    val curPlayer: Int,
    val uniCount: Long
)