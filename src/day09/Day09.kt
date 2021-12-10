package day09

import readInput
import toInt
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int = calculatePart1(input)
    fun part2(input: List<String>): Int = calculatePart2(input)

    val testInput = readInput(name = "Day09_test", day = "day09")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput(name = "Day09", day = "day09")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    // println(part2(input))

}

private fun calculatePart1(input: List<String>): Int {
    val grid = input.asGrid
    val points = mutableListOf<Int>()

    (grid.indices).forEach { y ->
        (0 until grid.first().size).forEach { x ->
            grid[y][x].let { point ->
                if (
                        (grid.getOrNull(y)?.getOrNull(x - 1) ?: 9999) > point &&
                        (grid.getOrNull(y)?.getOrNull(x + 1) ?: 9999) > point &&
                        (grid.getOrNull(y - 1)?.getOrNull(x) ?: 9999) > point &&
                        (grid.getOrNull(y + 1)?.getOrNull(x) ?: 9999) > point
                ) {
                    points.add(point)
                }
            }
        }
    }

    return points.sum() + points.size
}


private fun calculatePart2(input: List<String>): Int {
    val points = input.asPoints

    val basins = mutableListOf<List<Point>>()

    points.forEach { point ->
        if (point !in basins.flatten()) {
            val basin = point.getBasin(points)
            //println("Basin for $point: $basin" )
            if (basin.isNotEmpty() && basin.none { it in basins.flatten() }) {
                basins.add(basin)
            }
        }
    }

    basins.forEach {
        println(it)
        println(it.size)
    }

    basins.sortByDescending { it.size }
    return basins.map { it.size }.take(3).reduce { acc, i -> acc * i }
}

private data class Point(val value: Int, val x: Int, val y: Int) {
    fun getBasin(grid: List<Point>): List<Point> =
            grid.mapNotNull { point ->
                if ((abs(point.x - x) + abs(point.y - y)) == abs(point.value - value)) {
                    point
                } else {
                    null
                }
            }.filter { it.value != 9 }
}

private val List<String>.asGrid: List<List<Int>>
    get() = map { line ->
        line.map { it.toInt }
    }

private val List<String>.asPoints: List<Point>
    get() = mapIndexed { y, line ->
        line.mapIndexed { x, value -> Point(value = value.toInt, x = x, y = y) }
    }.flatten()

