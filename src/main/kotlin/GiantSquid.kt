val input: Input by lazy {
    val lines = readFile("GiantSquid").split("\n")
    val numbers = lines[0].split(",").map { Integer.parseInt(it) }
    val boards = mutableListOf<Board>()

    var curRow = 2
    while (curRow < lines.size) {
        val curBoard: MutableList<List<Int>> = mutableListOf()
        for (i in 0 until 5) {
            curBoard.add(lines[curRow + i].split(Regex("\\s+")).map { Integer.parseInt(it) })
        }
        boards.add(Board(curBoard))

        curRow += 6
    }

    Input(numbers, boards)
}

fun findWinningBoard(): Int {
    val (numbers, boards) = input
    numbers.forEach { num ->
        boards.forEachIndexed { index, board ->
            if (board.mark(num)) {
                println("Won board index $index in number $num")
                return board.unmarkedSum() * num
            }
        }
    }

    return -1
}

fun findLastWinningBoard(): Int {
    val (numbers, boards) = input

    val finishedBoards = BooleanArray(boards.size)

    var lastWinningBoardResult: Int = -1

    numbers.forEach { num ->
        boards.forEachIndexed { index, board ->
            if (board.mark(num)) {
                if (!finishedBoards[index]) {
                    lastWinningBoardResult = board.unmarkedSum() * num
                    finishedBoards[index] = true
                }
            }
        }
    }

    return lastWinningBoardResult
}

fun main() {
    println(findWinningBoard())
    println(findLastWinningBoard())
}

data class Input(val numbers: List<Int>, val boards: List<Board>)

class Board(val rows: List<List<Int>>) {
    private val quintuples: MutableList<Quintuple> = mutableListOf()

    init {
        for (i in 0 until 5) {
            quintuples.add(Quintuple(rows[i]))
        }

        for (i in 0 until 5) {
            val curRow = mutableListOf<Int>()
            for (j in 0 until 5) {
                curRow.add(rows[j][i])
            }
            quintuples.add(Quintuple(curRow))
        }
    }

    fun mark(number: Int): Boolean {
        var win = false
        quintuples.forEach { win = win or it.mark(number) }
        return win
    }

    fun unmarkedSum(): Int = quintuples.map { it.unmarkedSum() }.sum() / 2
}

class Quintuple(private val numbers: List<Int>) {
    private val marked = BooleanArray(5)

    fun mark(number: Int): Boolean {
        numbers.forEachIndexed { index, num ->
            if (number == num) {
                marked[index] = true
            }
        }
        return marked.all { it }
    }

    fun unmarkedSum(): Int = numbers.filterIndexed { index, _ -> !marked[index] }.sum()
}