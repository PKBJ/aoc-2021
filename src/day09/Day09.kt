package day09

import readInput
import toInt

fun main() {
    fun part1(input: List<String>): Int = calculatePart1(input)
    fun part2(input: List<String>): Int = calculatePart2(input)

    val testInput = readInput(name = "Day09_test", day = "day09")
    check(part1(testInput) == 15)
    // check(part2(testInput) == 61229)

    val input = readInput(name = "Day09", day = "day09")
    println("=== Part 1 ===")
    //println(part1(input))
    println("=== Part 2 ===")
    // println(part2(input))

}

private fun calculatePart1(input: List<String>): Int {
    val grid = input.asGrid
    val points = mutableListOf<Int>()

    (0..4).forEach { y ->
        (0..9).forEach { x ->
            val point = grid[y][x]
            if (
                (if (x - 1 > 0) grid[y][x - 1] > point else true) &&
                (if (x + 1 < 10) grid[y][x + 1] > point else true) &&
                (if (y - 1 > 0) grid[y - 1][x] > point else true) &&
                (if (y + 1 < 5) grid[y + 1][x] > point else true)
            ) {
                points.add(point)
                println("adding $x,$y")
            }
        }
    }

    println(points)
    return 0
}


private fun calculatePart2(input: List<String>): Int = 0

private val List<String>.asGrid: List<List<Int>>
    get() = map { line ->
        line.map { it.toInt }
    }

