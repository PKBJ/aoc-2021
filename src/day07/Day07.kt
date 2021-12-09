package day07

import asIntList
import readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int = calculateCheapestPosition(input, factorialFuelCost = false)
    fun part2(input: List<String>): Int = calculateCheapestPosition(input, factorialFuelCost = true)

    val testInput = readInput(name = "Day07_test", day = "day07")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput(name = "Day07", day = "day07")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

private fun calculateCheapestPosition(input: List<String>, factorialFuelCost: Boolean): Int =
    input.asIntList.let { crabPositions ->
        val (min, max) = crabPositions.minOf { it } to crabPositions.maxOf { it }
        (min..max).minOf { position ->
            crabPositions.sumOf {
                if (!factorialFuelCost) abs(it - position) else factorial(abs(it - position))
            }
        }
    }

private fun factorial(n: Int): Int = if (n == 0) 0 else n + factorial(n - 1)