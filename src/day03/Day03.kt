package day03

import asDecimal
import binaryInvert
import bitFlip
import readInput

fun main() {
    fun part1(input: List<String>): Int = calculatePart1(input)
    fun part2(input: List<String>): Int = calculatePart2(input)

    val testInput = readInput(name = "Day03_test", day = "day03")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput(name = "Day03", day = "day03")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

private fun calculatePart1(input: List<String>): Int {
    if (input.isEmpty()) return 0
    val inputSize = input.first().length

    return (0 until inputSize)
        .joinToString(separator = "") { index ->
            val ones = input.count { it[index] == '1' }
            val zeroes = input.size - ones
            if (ones > zeroes) "1" else "0"
        }
        .let { gamma ->
            gamma.asDecimal * gamma.binaryInvert.asDecimal
        }
}

private fun calculatePart2(input: List<String>): Int {
    if (input.isEmpty()) return 0
    val oxygenGenerator = recursivePart2(input = input, searchMode = SearchMode.OxygenGenerator)
    val co2Scrubber = recursivePart2(input = input, searchMode = SearchMode.CO2Scrubber)
    return oxygenGenerator.asDecimal * co2Scrubber.asDecimal
}

private fun recursivePart2(input: List<String>, bitIndex: Int = 0, searchMode: SearchMode): String =
    if (input.size == 1) {
        input.first()
    } else {
        recursivePart2(
            input = input
                .count { it[bitIndex] == '1' }
                .let { ones ->
                    val mostCommonBit = if (ones >= input.size - ones) '1' else '0'
                    input.filter {
                        it[bitIndex] == when (searchMode) {
                            SearchMode.CO2Scrubber -> mostCommonBit.bitFlip
                            SearchMode.OxygenGenerator -> mostCommonBit
                        }
                    }
                },
            bitIndex = bitIndex + 1,
            searchMode = searchMode
        )
    }

private sealed class SearchMode {
    object CO2Scrubber : SearchMode()
    object OxygenGenerator : SearchMode()
}