package day02

import readInput

fun main() {
    fun part1(input: List<String>): Int = calculatePositions(input, trackAim = false)
        .let { (horizontal, depth) -> horizontal * depth }

    fun part2(input: List<String>): Int = calculatePositions(input, trackAim = true)
        .let { (horizontal, depth) -> horizontal * depth }

    val testInput = readInput(name = "Day02_test", day = "day02")
    check(part1(testInput) == 8)
    check(part2(testInput) == 40)

    val input = readInput(name = "Day02", day = "day02")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

fun calculatePositions(input: List<String>, trackAim: Boolean): Pair<Int, Int> {
    var aim = 0
    var depth = 0
    var horizontal = 0

    for (instruction in input) {
        val (direction, amount) = instruction.split(" ")
        when (direction) {
            "forward" -> {
                if (trackAim) depth += amount.toInt() * aim
                horizontal += amount.toInt()
            }
            "down" -> if (trackAim) aim += amount.toInt() else depth += amount.toInt()
            "up" -> if (trackAim) aim -= amount.toInt() else depth -= amount.toInt()
        }
    }

    return horizontal to depth
}