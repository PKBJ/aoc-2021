package day08

import readInput

fun main() {
    fun part1(input: List<String>): Int = calculatePart1(input)
    fun part2(input: List<String>): Int = calculatePart2(input)

    val testInput = readInput(name = "Day08_test", day = "day08")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput(name = "Day08", day = "day08")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

private fun calculatePart1(input: List<String>): Int =
    input.asPatternList.sumOf { it.uniqueSignals(listOf(2, 3, 4, 7)) }

private fun calculatePart2(input: List<String>): Int = input.asPatternList.sumOf { it.bruteForceOutputInt() }

private val List<String>.asPatternList: List<Pattern>
    get() = map {
        it.split("|").let { (pattern, output) ->
            Pattern(
                pattern = pattern.trim().split(" "),
                output = output.trim().split(" ")
            )
        }

    }

/**
 * Multipliers:
 *  a = 2
 *  b = 3
 *  c = 4
 *  d = 5
 *  e = 6
 *  f = 7
 *  g = 8
 *
 *  Unique numbers:
 *  0 = a + b + c + e + f + g 		= 2 * 3 * 4 * 6 * 7 * 8     = 8064
 *  1 = c + f                 		= 4 * 7				        = 28
 *  2 = a + c + d + e + g     		= 2 * 4 * 5 * 6 * 8         = 1920
 *  3 = a + c + d + f + g     		= 2 * 4 * 5 * 7 * 8         = 2240
 *  4 = b + c + d + f         		= 3 * 4 * 5 * 7             = 420
 *  5 = a + b + d + f + g    		= 2 * 3 * 5 * 7 * 8         = 1680
 *  6 = a + b + d + e + f + g 		= 2 * 3 * 5 * 6 * 7 * 8     = 10080
 *  7 = a + c + f			   		= 2 * 4 * 7				    = 56
 *  8 = a + b + c + d + e + f + g  = 2 * 3 * 4 * 5 * 6 * 7 * 8  = 40320
 *  9 = a + b + c + d + f + g      = 2 * 3 * 4 * 5 * 7 * 8      = 6720
 */
private val uniqueNumbers = listOf(8064, 28, 1920, 2240, 420, 1680, 10080, 56, 40320, 6720)

private data class Pattern(val pattern: List<String>, val output: List<String>) {
    fun uniqueSignals(signalLengths: List<Int>): Int = output.count { it.length in signalLengths }

    fun bruteForceOutputInt(): Int {
        (2..8).forEach { a ->
            (2..8).forEach { b ->
                (2..8).forEach { c ->
                    (2..8).forEach { d ->
                        (2..8).forEach { e ->
                            (2..8).forEach { f ->
                                (2..8).forEach { g ->
                                    listOf(a, b, c, d, e, f, g).let { loop ->
                                        if (
                                            loop.distinct().size == 7 &&
                                            pattern.map { it.toNumber(loop) }.all { uniqueNumbers.contains(it) }
                                        ) {
                                            return output.map { it.toNumber(loop) }.toOutputInt
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return -1
    }

    private fun String.toNumber(ints: List<Int>): Int = map {
        when (it) {
            'a' -> ints[0]
            'b' -> ints[1]
            'c' -> ints[2]
            'd' -> ints[3]
            'e' -> ints[4]
            'f' -> ints[5]
            'g' -> ints[6]
            else -> 0
        }
    }
        .reduce { sum, i -> sum * i }

    private val List<Int>.toOutputInt: Int
        get() = joinToString("") {
            when (it) {
                uniqueNumbers[0] -> "0"
                uniqueNumbers[1] -> "1"
                uniqueNumbers[2] -> "2"
                uniqueNumbers[3] -> "3"
                uniqueNumbers[4] -> "4"
                uniqueNumbers[5] -> "5"
                uniqueNumbers[6] -> "6"
                uniqueNumbers[7] -> "7"
                uniqueNumbers[8] -> "8"
                uniqueNumbers[9] -> "9"
                else -> "-1"
            }
        }
            .toInt()


}
