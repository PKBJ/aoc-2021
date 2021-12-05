package day04

import readInput

fun main() {
    fun part1(input: List<String>): Int = calculatePart1(input)
    fun part2(input: List<String>): Int = calculatePart2(input)

    val testInput = readInput(name = "Day04_test", day = "day04")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput(name = "Day04", day = "day04")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

private fun calculatePart1(input: List<String>): Int {
    val boards = input.drop(1).asBoards
    val bingoNumbers = readNumbers(input.first(), delimiter = ",")

    for (i in 5..bingoNumbers.size) {
        val currentBingoNumbers = bingoNumbers.take(i)
        val bingoBoard: Board? = boards.firstOrNull { it.bingo(currentBingoNumbers) }

        bingoBoard?.let {
            return bingoBoard.unmarkedSum(currentBingoNumbers) * bingoNumbers[i - 1]
        }
    }

    return -1
}

private fun calculatePart2(input: List<String>): Int {
    var boards = input.drop(1).asBoards
    val bingoNumbers = readNumbers(input.first(), delimiter = ",")

    for (i in 5..bingoNumbers.size) {
        val currentBingoNumbers = bingoNumbers.take(i)

        val bingoBoards: List<Board> = boards.filter { it.bingo(currentBingoNumbers) }
        boards = boards.minus(bingoBoards.toSet())

        if (boards.isEmpty()) {
            return bingoBoards.first().unmarkedSum(currentBingoNumbers) * bingoNumbers[i - 1]
        }
    }

    return -1
}

private val List<String>.asBoards: List<Board>
    get() = filter { it.isNotEmpty() }
        .let { boardInput ->
            boardInput.mapIndexedNotNull { index, _ ->
                if (index % 5 == 0) {
                    Board(
                        listOf(
                            readNumbers(boardInput[index]),
                            readNumbers(boardInput[index + 1]),
                            readNumbers(boardInput[index + 2]),
                            readNumbers(boardInput[index + 3]),
                            readNumbers(boardInput[index + 4])
                        )
                    )
                } else {
                    null
                }
            }
        }

private fun readNumbers(line: String, delimiter: String = " ") = line
    .split(delimiter)
    .filter { it.isNotBlank() }
    .map { it.toInt() }

private data class Board(val rows: List<List<Int>>) {
    fun bingo(numbers: List<Int>): Boolean =
        rows.any { numbers.containsAll(it) } ||
                (rows.indices).any {
                    numbers.containsAll(rows.map { row -> row[it] })
                }

    fun unmarkedSum(numbers: List<Int>) = rows.flatten().filter { !numbers.contains(it) }.sum()
}