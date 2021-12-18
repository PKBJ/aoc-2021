package day14

import readInput

fun main() {
    fun part1(input: List<String>): Long = calculatePolymerization(input, numberOfSteps = 10)
    fun part2(input: List<String>): Long = calculatePolymerization(input, numberOfSteps = 40)

    val testInput = readInput(name = "Day14_test", day = "day14")
    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529L)

    val input = readInput(name = "Day14", day = "day14")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

private fun calculatePolymerization(input: List<String>, numberOfSteps: Int): Long {
    val insertionRules = input.asInsertionRules
    val polymerTemplate = input.asPolymerTemplate

    val initialPolymerPairCounts: Map<String, Long> = insertionRules
        .mapValues { polymerTemplate.split(it.key).size.toLong() - 1 }

    return recursive(polymerPairCounts = initialPolymerPairCounts, step = numberOfSteps, insertionRules = insertionRules)
        .let { polymerPairCounts ->
            polymerPairCounts
                .map { it.key.first() }
                .distinct()
                .map { polymer ->
                    polymer to polymerPairCounts.filter { it.key.first() == polymer }.values.sum() +
                            (if (polymerTemplate.last() == polymer) 1 else 0)
                }
        }
        .let { polymerSums ->
            polymerSums.maxOf { it.second } - polymerSums.minOf { it.second }
        }
}

private fun recursive(polymerPairCounts: Map<String, Long>, step: Int, insertionRules: Map<String, String>): Map<String, Long> =
    if (step == 0) {
        polymerPairCounts
    } else {
        val newCounts = mutableMapOf<String, Long>()
        polymerPairCounts.forEach {
            val insertionRule = insertionRules.getValue(it.key)
            val left = it.key.first() + insertionRule
            newCounts[left] = newCounts.getOrDefault(left, defaultValue = 0) + it.value
            val right = insertionRule + it.key.last()
            newCounts[right] = newCounts.getOrDefault(right, defaultValue = 0) + it.value
        }

        recursive(polymerPairCounts = newCounts, step = step - 1, insertionRules)
    }

private val List<String>.asInsertionRules: Map<String, String>
    get() = filter { it.contains("->") }.associate {
        val (instruction, result) = it.split("->")
        instruction.trim() to result.trim()
    }

private val List<String>.asPolymerTemplate: String
    get() = filterNot { it.startsWith("->") || it.isEmpty() }.first()
