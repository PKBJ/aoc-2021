package day10

import readInput
import java.util.*

fun main() {
    fun part1(input: List<String>): Int = calculatePart1(input)
    fun part2(input: List<String>): Long = calculatePart2(input)

    val testInput = readInput(name = "Day10_test", day = "day10")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput(name = "Day10", day = "day10")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

private fun calculatePart1(input: List<String>): Int =
    input.map { Chunk(it) }.sumOf { syntaxErrorPoints.getOrDefault(it.corruptedChar, 0) }

private fun calculatePart2(input: List<String>): Long = input
    .map { Chunk(it) }
    .filter { it.corruptedChar == null && it.isIncomplete }
    .map { it.stackValue }
    .sorted()
    .let { it[it.size / 2] }

private val autoCompletePoints = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)
private val openToCloseMap = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
private val openSet = setOf('(', '[', '{', '<')
private val syntaxErrorPoints = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

private data class Chunk(val chunk: String) {
    private val stack = Stack<Char>().apply {
        chunk.forEach {
            if (corruptedChar != null) {
                // Do nothing
            } else if (it in openSet) {
                push(it)
            } else if (!isEmpty() && openToCloseMap[peek()] == it) {
                pop()
            } else if (!isEmpty() && openToCloseMap[peek()] != it) {
                corruptedChar = it
            }
        }
    }

    var corruptedChar: Char? = null
    val isIncomplete: Boolean get() = stack.isNotEmpty()

    val stackValue: Long
        get() {
            var result = 0L
            while (!stack.isEmpty()) {
                val popped = stack.pop()
                val value = autoCompletePoints.getOrDefault(popped, 0)
                result = (result * 5) + value
            }
            return result
        }
}
