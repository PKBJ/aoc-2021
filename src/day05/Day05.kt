package day05

import readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int = calculateOverlappingLines(input, allowDiagonals = false)
    fun part2(input: List<String>): Int = calculateOverlappingLines(input, allowDiagonals = true)

    val testInput = readInput(name = "Day05_test", day = "day05")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput(name = "Day05", day = "day05")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

private fun calculateOverlappingLines(input: List<String>, allowDiagonals: Boolean): Int {
    val lines = input.readLines
    val maxX = lines.maxOf { it.maxX }
    val maxY = lines.maxOf { it.maxY }

    val result: MutableList<MutableList<Int>> = MutableList(maxX + 1) {
        MutableList(maxY + 1) { 0 }
    }

    lines
        .filter { it.isStraight || (allowDiagonals && it.is45Degree) }
        .forEach {
            it.mark(result)
        }

    return result.flatten().count { it > 1 }
}

private val List<String>.readLines
    get() = map { line ->
        val startToEnd = line.split(" -> ")
        val start = startToEnd[0].split(",").map { it.toInt() }
        val end = startToEnd[1].split(",").map { it.toInt() }
        Line(x1 = start[0], x2 = end[0], y1 = start[1], y2 = end[1])
    }


data class Line(val x1: Int, val x2: Int, val y1: Int, val y2: Int) {

    private val absX = abs(x1 - x2)
    private val absY = abs(y1 - y2)
    private val minX = minOf(x1, x2)
    private val minY = minOf(y1, y2)

    val isStraight: Boolean = x1 == x2 || y1 == y2
    val is45Degree: Boolean = (!isStraight && (absX == absY))

    val maxX = maxOf(x1, x2)
    val maxY = maxOf(y1, y2)

    fun mark(grid: MutableList<MutableList<Int>>) {
        if (isStraight) {
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    grid[x][y] = grid[x][y] + 1
                }
            }
        } else if (is45Degree) {
            for (step in 0..absX) {
                val x = when {
                    x1 < x2 -> x1 + step
                    else -> x1 - step
                }

                val y = when {
                    y1 < y2 -> y1 + step
                    else -> y1 - step
                }

                grid[x][y] = grid[x][y] + 1
            }
        }
    }

    override fun toString(): String = "[$x1,$y1] -> [$x2,$y2]"
}
