package day13

import readInput

fun main() {
    fun part1(input: List<String>): Int = calculatePart1(input)
    fun part2(input: List<String>): Int = calculatePart2(input)

    val testInput = readInput(name = "Day13_test", day = "day13")
    check(part1(testInput) == 17)
    check(part2(testInput) == 0) // Check logs, it prints the letter O in ascii art

    val input = readInput(name = "Day13", day = "day13")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

private fun calculatePart1(input: List<String>): Int =
        input.asPoints.let { points ->
            val (dir, value) = input.asFoldInstructions.first()
            points
                    .map { if (dir == "x") it.foldX(value) else it.foldY(value) }
                    .distinct()
                    .size
        }

private fun calculatePart2(input: List<String>): Int {
    val points = input.asPoints
    val foldInstructions = input.asFoldInstructions

    var result: List<Point> = points

    foldInstructions.forEach { (dir, value) ->
        result = result.map { if (dir == "x") it.foldX(value) else it.foldY(value) }
    }

    result = result.distinct()

    val maxX = result.maxOf { it.x }
    val minX = result.minOf { it.x }
    val maxY = result.maxOf { it.y }
    val minY = result.minOf { it.y }

    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            if (minX == x) {
                println()
            }
            if (result.firstOrNull { point -> point.x == x && point.y == y } != null) {
                print("#")
            } else {
                print(".")
            }
        }
    }
    println()
    println()
    return 0
}

private data class Point(val x: Int, val y: Int) {
    fun foldY(foldY: Int): Point = Point(
            x = x,
            y = if (y > foldY) {
                foldY - (y - foldY)
            } else {
                y
            }
    )

    fun foldX(foldX: Int) = Point(
            x = if (x > foldX) {
                foldX - (x - foldX)
            } else {
                x
            },
            y = y
    )
}

private val List<String>.asPoints: List<Point>
    get() = filterNot { it.startsWith("fold") || it.isEmpty() }
            .map { point ->
                point
                        .split(",")
                        .let { (x, y) -> Point(x.toInt(), y.toInt()) }
            }

private val List<String>.asFoldInstructions: List<Pair<String, Int>>
    get() = filter { it.startsWith("fold") }
            .map { line ->
                line
                        .split(" ")
                        .let { instruction ->
                            instruction[2].split("=")
                                    .let {
                                        it[0] to it[1].toInt()
                                    }
                        }
            }