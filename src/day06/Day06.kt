package day06

import readInput

fun main() {
    fun part1(input: List<String>): Long = calculatePopulationGrowth(input, days = 80)
    fun part2(input: List<String>): Long = calculatePopulationGrowth(input, days = 256)

    val testInput = readInput(name = "Day06_test", day = "day06")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readInput(name = "Day06", day = "day06")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

private fun calculatePopulationGrowth(input: List<String>, days: Int = 80): Long {
    val initialTimers = input.flatMap { it.split(',') }
        .map { it.toLong() }
        .let { timers ->
            listOf(
                timers.count { it == 0L }.toLong(),
                timers.count { it == 1L }.toLong(),
                timers.count { it == 2L }.toLong(),
                timers.count { it == 3L }.toLong(),
                timers.count { it == 4L }.toLong(),
                timers.count { it == 5L }.toLong(),
                timers.count { it == 6L }.toLong(),
                timers.count { it == 7L }.toLong(),
                timers.count { it == 8L }.toLong(),
            )
        }

    return recursive(initialTimers, days).sum()
}

private fun recursive(input: List<Long>, day: Int): List<Long> =
    if (day == 0) {
        input
    } else {
        recursive(
            input = listOf(
                input[1],
                input[2],
                input[3],
                input[4],
                input[5],
                input[6],
                input[7] + input[0],
                input[8],
                input[0]
            ),
            day = day - 1
        )
    }