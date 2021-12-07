val values: List<String> by lazy {
    readFile("PowerConsumption").split("\n").map { it.trim() }
}

fun getPower(): Int {
    val count = values.size
    val width = values[0].length

    val ones = IntArray(width)
    values.forEach {
        it.forEachIndexed { ind, c ->
            if (c == '1') {
                ones[ind]++
            }
        }
    }

    var gamma = ""
    for (cur in ones) {
        if (cur >= count / 2) {
            gamma = gamma.plus("1")
        } else {
            gamma = gamma.plus("0")
        }
    }

    var epsilon = gamma.map { if (it == '1') '0' else '1' }.joinToString(separator = "")

    println(gamma)
    println(epsilon)

    return Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2)
}

fun getLifeSupport(): Int {
    var oxygenValues = values.toMutableList()
    val width = values[0].length

    lateinit var oxygen: String

    for (i in 0 until width) {
        val ones = oxygenValues.count { it[i] == '1' }

        if (2 * ones >= oxygenValues.size) {
            oxygenValues.retainAll { it[i] == '1' }
        } else {
            oxygenValues.retainAll { it[i] == '0' }
        }
        if (oxygenValues.size == 1) {
            oxygen = oxygenValues[0]
            break
        }
    }

    lateinit var co2: String
    var co2Values = values.toMutableList()

    for (i in 0 until width) {
        val ones = co2Values.count { it[i] == '1' }

        if (2 * ones >= co2Values.size) {
            co2Values.retainAll { it[i] == '0' }
        } else {
            co2Values.retainAll { it[i] == '1' }
        }
        if (co2Values.size == 1) {
            co2 = co2Values[0]
            break
        }
    }

    println(oxygen)
    println(co2)

    return Integer.parseInt(oxygen, 2) * Integer.parseInt(co2, 2)
}


fun main() {
    println(getPower())
    println(getLifeSupport())
}
