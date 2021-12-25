val instructions: List<Instruction> by lazy {
    readFile("ALU").split("\n").map { l ->
        l.split(" ").let { if (it.size == 2) Instruction(it[0], it[1], "") else Instruction(it[0], it[1], it[2]) }
    }
}

val mapVarToInd = mapOf(
    "w" to 0,
    "x" to 1,
    "y" to 2,
    "z" to 3
)

val variables = IntArray(4)

fun main() {
    // https://github.com/dphilipson/advent-of-code-2021/blob/master/src/days/day24.rs
    // max = 99999795919456
    // min = 45311191516111
    val input = "45311191516111".map { it.digitToInt() }
    var inputIndex = 0

    instructions.forEach { i ->
        val varIndex = mapVarToInd[i.arg1]!!
        when (i.op) {
            "inp" -> {
                variables[varIndex] = input[inputIndex]
                inputIndex++
            }
            "add" -> {
                variables[varIndex] = calculate(variables[varIndex], i.arg2) { a, b -> a + b }
            }
            "mul" -> {
                variables[varIndex] = calculate(variables[varIndex], i.arg2) { a, b -> a * b }
            }
            "div" -> {
                variables[varIndex] = calculate(variables[varIndex], i.arg2) { a, b -> a / b }
            }
            "mod" -> {
                variables[varIndex] = calculate(variables[varIndex], i.arg2) { a, b -> a % b }
            }
            "eql" -> {
                variables[varIndex] = calculate(variables[varIndex], i.arg2) { a, b -> if (a == b) 1 else 0 }
            }
        }
    }

    println(variables.contentToString())
}


fun calculate(arg1: Int, arg2: String, op: (Int, Int) -> Int): Int {
    val arg2Value = mapVarToInd[arg2]?.let { variables[it] } ?: arg2.toInt()
    return op(arg1, arg2Value)
}

data class Instruction(val op: String, val arg1: String, val arg2: String)



