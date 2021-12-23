import AmphipodType.valueOf
import kotlin.math.abs
import kotlin.math.min

private fun getAmphipodsState(filename: String, roomSize: Int): List<Amphipod> {
    return readFile(filename).split("\n").drop(2).take(roomSize).flatMapIndexed { roomPos, s ->
        s.filter { it != '#' && it != ' ' }.mapIndexed { room, c -> Amphipod(valueOf(c.toString()), room, roomPos) }
    }
}

private val roomIdToPositionsInHallway = mapOf(
    0 to 2,
    1 to 4,
    2 to 6,
    3 to 8
) //(+ 1) * 2

private val roomPositionsInHallway = roomIdToPositionsInHallway.values

fun main() {
    // TODO: mark amphs that are already in place
    // None in my input
    println(findMinEnergy(getAmphipodsState("Amphipod", 2)))
    println(findMinEnergy(getAmphipodsState("Amphipod_full", 4)))
}

fun findMinEnergy(initState: List<Amphipod>): Int {
    var minEnergySpent = Integer.MAX_VALUE
    val visitedStates = hashMapOf(State(initState) to 0)
    var states = listOf(State(initState))
    while (states.isNotEmpty()) {
        val nextStates = states.flatMap { nextStates(it, visitedStates) }
        val (finished, notFinished) = nextStates.partition { it.finished }
        val curMinEnergySpent = finished.minOfOrNull { it.energySpent } ?: Integer.MAX_VALUE
        minEnergySpent = min(minEnergySpent, curMinEnergySpent)

        states = notFinished
        println("${states.size}")
    }
    return minEnergySpent
}

fun nextStates(state: State, visitedStates: HashMap<State, Int>): List<State> {
    val nextStates = mutableListOf<State>()
    state.amphipods.forEachIndexed { aIndex, a ->
        if (a.inInitRoom && canGetOutOfTheRoom(state, a)) {

            val initHallwayPos = roomIdToPositionsInHallway[a.initRoom]!!
            val targetHallwayPos = roomIdToPositionsInHallway[a.type.targetRoom]!!

            // shortcut: right to target room
            if (canMoveToTargetRoom(state, a, initHallwayPos, targetHallwayPos)) {
                val nextState = moveToTargetRoom(state, a, aIndex, initHallwayPos, targetHallwayPos, a.position + 1)
                addStateIfNeeded(nextState, nextStates, visitedStates)
                return@forEachIndexed
            }

            // can move only to hallway
            for (hallwayPos in (initHallwayPos + 1) until 11) {
                if (state.isHallwayPosOccupied(hallwayPos)) break
                if (hallwayPos in roomPositionsInHallway) continue
                val nextState = moveToHall(state, a, aIndex, initHallwayPos, hallwayPos)
                addStateIfNeeded(nextState, nextStates, visitedStates)
            }
            for (hallwayPos in (initHallwayPos - 1) downTo 0) {
                if (state.isHallwayPosOccupied(hallwayPos)) break
                if (hallwayPos in roomPositionsInHallway) continue
                val nextState = moveToHall(state, a, aIndex, initHallwayPos, hallwayPos)
                addStateIfNeeded(nextState, nextStates, visitedStates)
            }
        } else if (a.inHallway) {
            // can move only to target room
            val targetHallwayPos = roomIdToPositionsInHallway[a.type.targetRoom]!!
            if (!canMoveToTargetRoom(state, a, a.position, targetHallwayPos)) {
                return@forEachIndexed
            }
            val nextState = moveToTargetRoom(state, a, aIndex, a.position, targetHallwayPos, 0)
            addStateIfNeeded(nextState, nextStates, visitedStates)
        } else if (a.inTargetRoom) {
            // nothing to do here
        }
    }
    return nextStates
}

fun moveToHall(state: State, a: Amphipod, aIndex: Int, initHallwayPos: Int, hallwayPos: Int): State {
    val newAmphipodsState = state.amphipods.toMutableList()
    newAmphipodsState[aIndex] = a.moveToHallway(hallwayPos)
    val energySpend = ((a.position + 1) + abs(hallwayPos - initHallwayPos)) * a.type.energyPerMove
    return State(newAmphipodsState, state.energySpent + energySpend)
}

fun moveToTargetRoom(
    state: State,
    a: Amphipod,
    aIndex: Int,
    initHallwayPos: Int,
    targetHallwayPos: Int,
    additionalSteps: Int
): State {
    val newAmphipodsState = state.amphipods.toMutableList()
    val siblings = state.amphipods.filter { s -> a.type == s.type && s.inTargetRoom }
    val targetRoomPos = state.roomSize - 1 - siblings.size
    newAmphipodsState[aIndex] = a.moveToTargetRoom(targetRoomPos)
    val energySpent =
        (additionalSteps + abs(targetHallwayPos - initHallwayPos) + (targetRoomPos + 1)) * a.type.energyPerMove
    return State(newAmphipodsState, state.energySpent + energySpent)
}

fun canMoveToTargetRoom(state: State, a: Amphipod, initHallwayPos: Int, targetHallwayPos: Int): Boolean {
    for (hallwayPos in initHallwayPos towardExclusiveFrom targetHallwayPos) {
        if (state.isHallwayPosOccupied(hallwayPos)) return false
    }

    return canEnterTargetRoom(state, a)
}

fun canEnterTargetRoom(state: State, a: Amphipod) =
    state.amphipods.none { it.inInitRoom && (it.initRoom == a.type.targetRoom) }

fun canGetOutOfTheRoom(state: State, a: Amphipod) =
    state.amphipods.none { it.inInitRoom && it.initRoom == a.initRoom && it.position < a.position }

fun addStateIfNeeded(nextState: State, nextStates: MutableList<State>, visitedStates: HashMap<State, Int>) {
    val energySpentInPrev = visitedStates[nextState]
    // if we haven't yet reached this position or we reached it now with less spend energy than before
    if (energySpentInPrev == null || nextState.energySpent < energySpentInPrev) {
        visitedStates[nextState] = nextState.energySpent
        nextStates.add(nextState)
    }
}

class State(val amphipods: List<Amphipod>, val energySpent: Int = 0) {
    val rooms = 4
    val roomSize = amphipods.size / rooms

    val finished: Boolean
        get() = amphipods.all { it.inTargetRoom }

    fun isHallwayPosOccupied(pos: Int) = amphipods.any { a -> a.inHallway && a.position == pos }

    override fun equals(other: Any?): Boolean = amphipods.containsAll((other as State).amphipods)

    override fun hashCode(): Int = amphipods.sumOf { it.hashCode() }

    private fun repr(room: Int, pos: Int): String {
        val occupants =
            amphipods.filter { it.inInitRoom && it.initRoom == room || it.inTargetRoom && it.type.targetRoom == room }
                .filter { it.position == pos }
        return if (occupants.isEmpty()) " " else occupants[0].type.toString()
    }

    private val hallwayString: String
        get() {
            val template = StringBuilder("...........")
            amphipods.filter { it.inHallway }.forEach { template[it.position] = it.type.toString()[0] }
            return template.toString()
        }

    override fun toString(): String {
        val s = StringBuilder(
            """
            #${hallwayString}#
            ###${repr(0, 0)}#${repr(1, 0)}#${repr(2, 0)}#${repr(3, 0)}###
            
        """.trimIndent()
        )
        for (row in 1 until roomSize) {
            s.appendLine("  #${repr(0, row)}#${repr(1, row)}#${repr(2, row)}#${repr(3, row)}#")
        }
        return s.toString()
    }
}

// hallway posititions are 0..10
// room positions are 0(upper) and 1 (lower)
data class Amphipod(
    val type: AmphipodType,
    val initRoom: Int,
    val position: Int,
    val inInitRoom: Boolean = true,
    val inHallway: Boolean = false,
    val inTargetRoom: Boolean = false,
) {
    fun moveToHallway(hallwayPos: Int): Amphipod =
        Amphipod(type, initRoom, hallwayPos, inInitRoom = false, inHallway = true, inTargetRoom = false)

    fun moveToTargetRoom(roomPos: Int): Amphipod =
        Amphipod(type, initRoom, roomPos, inInitRoom = false, inHallway = false, inTargetRoom = true)
}

enum class AmphipodType(val energyPerMove: Int, val targetRoom: Int) {
    A(1, 0), B(10, 1), C(100, 2), D(1000, 3)
}