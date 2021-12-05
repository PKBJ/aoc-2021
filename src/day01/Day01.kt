package day01

import asIntList
import readInput

fun main() {
    fun part1(input: List<String>): Int = countSlidingWindowIncreased(input.asIntList, windowSize = 2)
    fun part2(input: List<String>): Int = countSlidingWindowIncreased(input.asIntList, windowSize = 4)

    val testInput = readInput(name = "Day01_test", "day01")
    check(part1(testInput) == 1)
    check(part2(testInput) == 0)

    val input = readInput(name = "Day01", "day01")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))
}

fun countSlidingWindowIncreased(input: List<Int>, windowSize: Int) =
    if (input.size < windowSize) {
        0
    } else {
        (windowSize..input.size).count { stopIndex ->
            val startIndex = stopIndex - windowSize
            input.subList(startIndex, stopIndex - 1).sum() < input.subList(startIndex + 1, stopIndex).sum()
        }
    }
