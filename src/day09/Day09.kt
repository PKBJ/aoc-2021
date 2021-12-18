package day09

import asGrid
import readInput
import toInt

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
    println(part2(input))

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
    val pointGrid = input.asPoints
    val basins = mutableListOf<List<Point>>()

    pointGrid.forEach { row ->
        row.forEach { point ->
            if (point !in basins.flatten()) {
                val basin = point.getBasin(pointGrid)
                //println("Basin for $point: $basin" )
                if (basin.isNotEmpty() && basin.none { it in basins.flatten() }) {
                    basins.add(basin)
                }
            }
        }
    }

    basins.sortByDescending { it.size }
    return basins.map { it.size }.take(3).reduce { acc, i -> acc * i }
}

private data class Point(val value: Int, val x: Int, val y: Int) {
    private val basin = mutableListOf<Point>()

    fun getBasin(grid: List<List<Point>>): List<Point> = traverse(this, grid).let { basin }

    private fun traverse(point: Point, grid: List<List<Point>>) {
        if (point.value >= 9) {
            return
        }

        basin.add(point)

        val right = grid.getOrNull(point.y)?.getOrNull(point.x + 1)
        val left = grid.getOrNull(point.y)?.getOrNull(point.x - 1)
        val up = grid.getOrNull(point.y - 1)?.getOrNull(point.x)
        val down = grid.getOrNull(point.y + 1)?.getOrNull(point.x)

        if (right != null && !basin.contains(right) && right.value < 9) {
            traverse(right, grid)
        }
        if (left != null && !basin.contains(left) && left.value < 9) {
            traverse(left, grid)
        }
        if (up != null && !basin.contains(up) && up.value < 9) {
            traverse(up, grid)
        }
        if (down != null && !basin.contains(down) && down.value < 9) {
            traverse(down, grid)
        }
    }
}

private val List<String>.asPoints: List<List<Point>>
    get() = mapIndexed { y, line ->
        line.mapIndexed { x, value -> Point(value = value.toInt, x = x, y = y) }
    }

