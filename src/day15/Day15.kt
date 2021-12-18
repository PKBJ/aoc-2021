package day15

import readInput
import toInt

fun main() {
    fun part1(input: List<String>): Int = calculatePart1(input)
    fun part2(input: List<String>): Int = calculatePart2(input)

    val testInput = readInput(name = "Day15_test", day = "day15")
    check(part1(testInput) == 40)
    // check(part2(testInput) == 0)

    val input = readInput(name = "Day15", day = "day15")
    println("=== Part 1 ===")
    // println(part1(input))
    println("=== Part 2 ===")
    // println(part2(input))

}

private fun calculatePart1(input: List<String>): Int {
    val points = input.asPoints

    fun shortestPath(prevPoints: List<Point> = emptyList(), point: Point, currentValue: Int = 0): Int? {
        if (point.isEnd) {
            println(prevPoints)
            return currentValue + point.value
        } else {
            val up = points.getOrNull(point.y - 1)?.getOrNull(point.x)
            val down = points.getOrNull(point.y + 1)?.getOrNull(point.x)
            val left = points.getOrNull(point.y)?.getOrNull(point.x - 1)
            val right = points.getOrNull(point.y)?.getOrNull(point.x + 1)

            val nextPoints = listOfNotNull(up, down, left, right).filter { it !in prevPoints }.sortedBy { it.value }

            return if (nextPoints.isEmpty()) {
                println("No valid paths left")

                null
            } else {
                shortestPath(prevPoints = prevPoints + point, point = nextPoints.first(), currentValue = currentValue + point.value)
            }
        }

    }

    val shortest = shortestPath(point = points[0][0])

    println(shortest)
    return shortest ?: 0
}


private fun calculatePart2(input: List<String>): Int {
    return 0
}

private data class Point(val value: Int, val x: Int, val y: Int, val isEnd: Boolean) {
    override fun toString(): String = "[$x,$y]"
}

private val List<String>.asPoints: List<List<Point>>
    get() = mapIndexed { y, line ->
        line.mapIndexed { x, value ->
            Point(
                    value = value.toInt,
                    x = x,
                    y = y,
                    isEnd = x == line.length - 1 && y == size - 1
            )
        }
    }