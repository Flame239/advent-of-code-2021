val binaryPacket: String by lazy {
    readFile("PacketDecoder").map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")
}

fun parsePacket(packet: String, start: Int = 0): Pair<Packet, Int> {
    val (version, typeId) = parseHeader(packet, start)
    if (typeId == 4) {
        val (value, endIndex) = parseValueBody(packet, start + 6)
        return Pair(ValuePacket(value, version, typeId), endIndex)
    } else {
        val (subPackets, endIndex) = parseOperatorBody(packet, start + 6)
        return Pair(OperatorPacket(subPackets, version, typeId), endIndex)
    }
}

fun parseHeader(packet: String, start: Int): Pair<Int, Int> {
    val version = packet.substring(start, start + 3).toInt(2)
    val typeId = packet.substring(start + 3, start + 6).toInt(2)
    return Pair(version, typeId)
}

fun parseValueBody(packet: String, start: Int): Pair<Long, Int> {
    var curIndex = start
    val value = StringBuilder()
    while (packet[curIndex] == '1') {
        value.append(packet, curIndex + 1, curIndex + 5)
        curIndex += 5
    }
    value.append(packet, curIndex + 1, curIndex + 5)
    return Pair(value.toString().toLong(2), curIndex + 5)
}

fun parseOperatorBody(packet: String, start: Int): Pair<List<Packet>, Int> {
    val subPackets = mutableListOf<Packet>()
    val lengthTypeId = packet[start]
    var endIndex: Int
    if (lengthTypeId == '0') {
        val startIndex = start + 1 + 15
        endIndex = start + 1 + 15
        val totalLen = packet.substring(start + 1, endIndex).toInt(2)
        while (totalLen > (endIndex - startIndex)) {
            val (curPacket, curEndIndex) = parsePacket(packet, endIndex)
            subPackets.add(curPacket)
            endIndex = curEndIndex
        }
    } else {
        endIndex = start + 1 + 11
        val subPacketsCount = packet.substring(start + 1, endIndex).toInt(2)
        repeat(subPacketsCount) {
            val (curPacket, curEndIndex) = parsePacket(packet, endIndex)
            subPackets.add(curPacket)
            endIndex = curEndIndex
        }
    }

    return Pair(subPackets, endIndex)
}

fun versionsSum(packet: Packet): Long {
    var versionSum = 0L
    versionSum += packet.version
    if (packet is OperatorPacket) {
        versionSum += packet.subPackets.sumOf { versionsSum(it) }
    }
    return versionSum
}

fun calculate(packet: Packet): Long {
    if (packet is ValuePacket) {
        return packet.value
    }
    if (packet is OperatorPacket) {
        return when (packet.typeId) {
            0 -> packet.subPackets.sumOf { calculate(it) }
            1 -> packet.subPackets.fold(1L) { acc, p -> acc * calculate(p) }
            2 -> packet.subPackets.minOf { calculate(it) }
            3 -> packet.subPackets.maxOf { calculate(it) }
            5 -> if (calculate(packet.subPackets[0]) > calculate(packet.subPackets[1])) 1 else 0
            6 -> if (calculate(packet.subPackets[0]) < calculate(packet.subPackets[1])) 1 else 0
            7 -> if (calculate(packet.subPackets[0]) == calculate(packet.subPackets[1])) 1 else 0
            else -> error("Unknown operator packet type")
        }
    }
    error("Unknown packet type")
}

fun main() {
    val (packet, endIndex) = parsePacket(binaryPacket)
    println(versionsSum(packet))
    println(calculate(packet))
}

open class Packet(val version: Int, val typeId: Int) {}

class ValuePacket(val value: Long, version: Int, typeId: Int) : Packet(version, typeId) {}

class OperatorPacket(val subPackets: List<Packet>, version: Int, typeId: Int) : Packet(version, typeId) {}
