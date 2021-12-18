package day16

import readInput

private const val HEADER_SIZE = 6
private const val LENGTH_TYPE_ID_SIZE = 1
private const val SUB_PACKETS_NUMBER_SIZE = 11
private const val SUB_PACKETS_LENGTH_SIZE = 15
private const val TYPE_ID_LITERAL = 4

fun main() {
    fun part1(input: List<String>): Int = calculatePart1(input)
    fun part2(input: List<String>): Long = calculatePart2(input)

    val testInput = readInput(name = "Day16_test", day = "day16")
    //check(part1(testInput) == 31)
    check(part2(testInput) == 1L)

    val input = readInput(name = "Day16", day = "day16")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

private fun calculatePart1(input: List<String>): Int = sumVersionNumbers(parsePacket(input.asInput))

private fun calculatePart2(input: List<String>): Long = evaluatePacket(parsePacket(input.asInput))

private fun evaluatePacket(packet: Packet): Long =
    when (packet) {
        is Packet.Literal -> packet.value
        else -> {
            val operator = packet as Packet.Operator
            when (operator.header.typeId) {
                0 -> operator.packets.sumOf { evaluatePacket(it) }
                1 -> {
                    if (operator.packets.size == 1) {
                        evaluatePacket(operator.packets.first())
                    } else {
                        operator.packets.map { evaluatePacket(it) }.reduce { acc, l -> acc * l }
                    }
                }
                2 -> operator.packets.map { evaluatePacket(it) }.minOf { it }
                3 -> operator.packets.map { evaluatePacket(it) }.maxOf { it }
                5 -> if (evaluatePacket(operator.packets.first()) > evaluatePacket(operator.packets.last())) 1 else 0
                6 -> if (evaluatePacket(operator.packets.first()) < evaluatePacket(operator.packets.last())) 1 else 0
                7 -> if (evaluatePacket(operator.packets.first()) == evaluatePacket(operator.packets.last())) 1 else 0
                else -> throw IllegalStateException("Unknown type id")
            }
        }
    }

private fun sumVersionNumbers(packet: Packet): Int =
    when (packet) {
        is Packet.Literal -> packet.header.version
        is Packet.Operator -> packet.header.version + packet.packets.sumOf { sumVersionNumbers(it) }
    }

private fun parsePacket(input: String): Packet {
    val header = Header(input = input.take(HEADER_SIZE))
    return when (header.typeId) {
        TYPE_ID_LITERAL -> parseLiteral(header = header, input = input.drop(HEADER_SIZE))
        else -> parseOperand(header = header, input = input.drop(HEADER_SIZE))
    }
}

private fun parseLiteral(header: Header, input: String): Packet =
    Packet.Literal(
        header = header,
        input = input
    )

private fun parseOperand(header: Header, input: String): Packet {
    val lengthTypeId = input.take(LENGTH_TYPE_ID_SIZE)
    val subPackets = mutableListOf<Packet>()
    if (lengthTypeId == "1") {
        val noOfSubPackets = input.drop(LENGTH_TYPE_ID_SIZE).take(SUB_PACKETS_NUMBER_SIZE).toInt(2)
        repeat(noOfSubPackets) {
            val packet = parsePacket(
                input = input
                    .drop(LENGTH_TYPE_ID_SIZE)
                    .drop(SUB_PACKETS_NUMBER_SIZE)
                    .drop(subPackets.sumOf { it.length })
            )
            subPackets.add(packet)
        }
    } else {
        val subPacketsLength = input.drop(LENGTH_TYPE_ID_SIZE).take(SUB_PACKETS_LENGTH_SIZE).toInt(2)
        while (subPackets.sumOf { it.length } < subPacketsLength) {
            val packet = parsePacket(
                input = input
                    .drop(LENGTH_TYPE_ID_SIZE)
                    .drop(SUB_PACKETS_LENGTH_SIZE)
                    .drop(subPackets.sumOf { it.length })
            )
            subPackets.add(packet)
        }
    }

    return Packet.Operator(
        header = header,
        mode = if (lengthTypeId == "0") Mode.LENGTH else Mode.NUMBER,
        packets = subPackets
    )
}

private val String.literalPart: String
    get() {
        var stopReached = false
        var result = ""
        var index = 0
        while (!stopReached && index < length) {
            val last = this[index] == '0'
            result += substring(index, index + 5)
            index += 5
            stopReached = last
        }
        return result
    }

private val String.parseLiteralValue: Long
    get() = (0 until (length / 5))
        .joinToString("") {
            val position = it * 5
            substring(position + 1, position + 5)
        }
        .toLong(2)

private data class Header(
    val input: String
) {
    val version = input.take(3).toInt(2)
    val typeId = input.takeLast(3).toInt(2)
    val size = HEADER_SIZE

    override fun toString(): String = "[Header: version: $version, type id: $typeId]"
}

private sealed class Packet(open val header: Header) {
    abstract val length: Int

    data class Literal(override val header: Header, val input: String) : Packet(header) {
        private val literalPart = input.literalPart
        override val length: Int = header.size + literalPart.length
        val value: Long = literalPart.parseLiteralValue

        override fun toString(): String {
            return "[Literal, $header, value: $value]"
        }
    }

    data class Operator(
        override val header: Header,
        val mode: Mode,
        val packets: List<Packet>
    ) : Packet(header) {
        override val length: Int = header.size + packets.sumOf { it.length } +
                1 +
                if (mode == Mode.LENGTH) SUB_PACKETS_LENGTH_SIZE else SUB_PACKETS_NUMBER_SIZE
    }

}

private enum class Mode {
    NUMBER,
    LENGTH
}


private val List<String>.asInput: String get() = first().map { it.toBinaryString }.joinToString("")

private val Char.toBinaryString
    get() = Integer.toBinaryString(toString().toInt(16)).let {
        if (it.length < 4) {
            (0 until 4 - it.length).joinToString("") { "0" } + it
        } else {
            it
        }
    }
