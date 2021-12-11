package day11

import readInput
import toInt
import java.util.*

fun main() {
    fun part1(input: List<String>): Int = calculatePart1(input)
    fun part2(input: List<String>): Int = calculatePart2(input)

    val testInput = readInput(name = "Day11_test", day = "day11")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput(name = "Day11", day = "day11")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

private fun calculatePart1(input: List<String>): Int =
    input.asGrid.let { grid -> (0 until 100).sumOf { grid.progressOneStep() } }

private fun calculatePart2(input: List<String>): Int = input.asGrid.let { grid ->
    var day = 0
    do {
        grid.progressOneStep()
        day += 1
    } while (!grid.all { row -> row.all { it == 0 } })
    return@let day
}

// Return number of flashes for the step
private fun MutableList<MutableList<Int>>.progressOneStep(): Int {
    var flashCount = 0
    val shouldFlash = Stack<Pair<Int, Int>>()
    // Step one, increase all energy levels with one
    (0 until size).forEach { y ->
        (0 until size).forEach { x ->
            this[y][x] = this[y][x] + 1
            if (this[y][x] > 9) {
                shouldFlash.add(y to x)
            }
        }
    }
    // Flash until stack is empty
    while (!shouldFlash.isEmpty()) {
        flashCount += 1
        val flash = shouldFlash.pop()
        ((flash.first - 1)..(flash.first + 1)).forEach { y ->
            ((flash.second - 1)..(flash.second + 1)).forEach { x ->
                if (getOrNull(y)?.getOrNull(x) != null) {
                    this[y][x] = this[y][x] + 1
                    if (this[y][x] == 10 && (y != flash.first || x != flash.second)) {
                        shouldFlash.add(y to x)
                    }
                }
            }
        }
    }
    // Reset energy levels
    (0 until size).forEach { y ->
        (0 until size).forEach { x ->
            if (this[y][x] > 9) {
                this[y][x] = 0
            }
        }
    }

    return flashCount
}

private val List<String>.asGrid: MutableList<MutableList<Int>>
    get() = map { line ->
        line.map { it.toInt }.toMutableList()
    }.toMutableList()

