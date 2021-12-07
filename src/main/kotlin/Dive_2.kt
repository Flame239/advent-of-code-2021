val moves: List<String> by lazy {
    readFile("Dive").split("\n").map { it.trim() }
}

fun getFinalPosition(): Int {
    var x = 0
    var depth = 0
    moves.forEach {
        val split = it.split(" ")
        val amount = split[1].toInt()
        if (split[0] == "down") {
            depth += amount
        } else if (split[0] == "up") {
            depth -= amount
        } else {
            x += amount
        }
    }
    println(x)
    println(depth)

    return x * depth
}

fun getFinalPositionWithAim(): Int {
    var x = 0
    var depth = 0
    var aim = 0

    moves.forEach {
        val split = it.split(" ")
        val amount = split[1].toInt()
        if (split[0] == "down") {
            aim += amount
        } else if (split[0] == "up") {
            aim -= amount
        } else {
            x += amount
            depth += aim * amount
        }
    }
    println(x)
    println(depth)

    return x * depth
}

fun main() {
    println(getFinalPosition())
    println(getFinalPositionWithAim())
}