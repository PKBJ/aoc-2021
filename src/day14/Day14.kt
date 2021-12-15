package day14

import readInput

private val inputToNumber = mapOf(
        'B' to 0,
        'C' to 1,
        'F' to 2,
        'H' to 3,
        'K' to 4,
        'N' to 5,
        'O' to 6,
        'P' to 7,
        'S' to 8,
        'V' to 9
)

fun main() {
    fun part1(input: List<String>): Long = calculatePart12(input, numberOfSteps = 10)
    fun part2(input: List<String>): Long = calculatePart12(input, numberOfSteps = 40)

    val testInput = readInput(name = "Day14_test", day = "day14")
    //check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529L)

    val input = readInput(name = "Day14", day = "day14")
    println("=== Part 1 ===")
    //println(part1(input))
    println("=== Part 2 ===")
    //println(part2(input))

}

private fun calculatePart12(input: List<String>, numberOfSteps: Int): Long {
    val insertionRules = input.asInsertionRules
    val polymerTemplate = input.asPolymerTemplate

    println("Insertion rules size: ${insertionRules.size}")

    val insertionRulesMin = insertionRules.values.groupBy { it }.minByOrNull { it.value.size }!!
    val insertionRulesMax = insertionRules.values.groupBy { it }.maxByOrNull { it.value.size }!!

    val ratioMax = insertionRulesMax.value.size / (insertionRules.size * 1f)
    val ratioMin = insertionRulesMin.value.size / (insertionRules.size * 1f)

    println("Insertion rules min: ${insertionRulesMin.key}, ${insertionRulesMin.value.size}, ratio: $ratioMin")
    println("Insertion rules max: ${insertionRulesMax.key}, ${insertionRulesMax.value.size}, ratio: $ratioMax ")

    fun count(currentSize: Long, step: Int): Long {
        println("Current size: $currentSize, step: $step")
        return if (step == 0) currentSize else count(currentSize = (currentSize * 2) - 1, step = step - 1)
    }

    val totalSize = count(currentSize = polymerTemplate.length.toLong(), step = numberOfSteps)

    println("Number of max: ${totalSize * ratioMax}" )
    println("Number of min: ${totalSize * ratioMin}" )
    println("======")
    return 0
}

private fun calculatePart11(input: List<String>, numberOfSteps: Int): Long {
    val start = System.currentTimeMillis()

    val insertionRules = input.asInsertionRules1
    val polymerTemplate = input.asPolymerTemplate1

    val resultList = mutableListOf(
            polymerTemplate.count { it == inputToNumber['B'] }.toLong(),
            polymerTemplate.count { it == inputToNumber['C'] }.toLong(),
            polymerTemplate.count { it == inputToNumber['F'] }.toLong(),
            polymerTemplate.count { it == inputToNumber['H'] }.toLong(),
            polymerTemplate.count { it == inputToNumber['K'] }.toLong(),
            polymerTemplate.count { it == inputToNumber['N'] }.toLong(),
            polymerTemplate.count { it == inputToNumber['O'] }.toLong(),
            polymerTemplate.count { it == inputToNumber['P'] }.toLong(),
            polymerTemplate.count { it == inputToNumber['S'] }.toLong(),
            polymerTemplate.count { it == inputToNumber['V'] }.toLong(),
    )

    fun traverse(left: Int, right: Int, step: Int) {
        if (step > 0) {
            val newDigit = insertionRules[left][right]
            resultList[newDigit]++
            traverse(left = left, right = newDigit, step = step - 1)
            traverse(left = newDigit, right = right, step = step - 1)
        }
    }

    polymerTemplate.forEachIndexed { index, char ->
        // if (index == 0) {
        polymerTemplate.getOrNull(index + 1)?.let {
            println("Checking char: " + (char.toString() + it.toString()))
            traverse(left = char, right = it, step = numberOfSteps)
            println("Char: $char$it done in ${System.currentTimeMillis() - start} ms")
        }
        // }
    }

    //resultList.filter {  }
    //println("B: $b, C: $c, F: $f, H: $h,  H: $k, N: $n, O: $o, P: $p, S: $s, V: $v")
    return resultList.maxOf { it } - resultList.filter { it > 0 }.minOf { it }
}

private fun calculatePart1(input: List<String>, numberOfSteps: Int): Long {
    val insertionRules = input.asInsertionRules
    val polymerTemplate = input.asPolymerTemplate

    var b: Long = polymerTemplate.count { it == 'B' }.toLong()
    var c: Long = polymerTemplate.count { it == 'C' }.toLong()
    var f: Long = polymerTemplate.count { it == 'F' }.toLong()
    var h: Long = polymerTemplate.count { it == 'H' }.toLong()
    var k: Long = polymerTemplate.count { it == 'K' }.toLong()
    var n: Long = polymerTemplate.count { it == 'N' }.toLong()
    var o: Long = polymerTemplate.count { it == 'O' }.toLong()
    var p: Long = polymerTemplate.count { it == 'P' }.toLong()
    var s: Long = polymerTemplate.count { it == 'S' }.toLong()
    var v: Long = polymerTemplate.count { it == 'V' }.toLong()

    fun traverse(input: String, step: Int) {
        if (step > 0) {
            val newChar = insertionRules[input]!!.first()
            when (newChar) {
                'B' -> b += 1
                'C' -> c += 1
                'F' -> f += 1
                'H' -> h += 1
                'K' -> k += 1
                'N' -> n += 1
                'O' -> o += 1
                'P' -> p += 1
                'S' -> s += 1
                'V' -> v += 1
            }

            traverse(input = input.take(1) + newChar, step = step - 1)
            traverse(input = newChar + input.drop(1), step = step - 1)
        }
    }

    polymerTemplate.forEachIndexed { index, char ->
        polymerTemplate.getOrNull(index + 1)?.let {
            traverse(input = char.toString() + it, step = numberOfSteps)
        }
    }

    println("B: $b, C: $c, F: $f, H: $h,  H: $k, N: $n, O: $o, P: $p, S: $s, V: $v")
    return maxOf(b, c, f, h, k, n, o, p, s, v) - listOf(b, c, f, h, k, n, o, p, s, v).filter { it > 0 }.minOf { it }
}

private fun calculatePart2(input: List<String>): Long {
    val insertionRules = input.asInsertionRules
    val polymerTemplate = input.asPolymerTemplate
    val numberOfSteps = 40

    var result: String = polymerTemplate
    repeat(times = numberOfSteps) {
        var index = 0
        result = result.map { char ->
            index++
            char.toString() + result
                    .getOrNull(index)
                    ?.let { nextChar ->
                        insertionRules.getOrDefault(char.toString() + nextChar, "")
                    }
                    .orEmpty()
        }.joinToString("")
    }

    return result
            .groupBy { char -> char }
            .map { (_, count) -> count.size.toLong() }
            .let { charCounts ->
                charCounts.maxOf { it } - charCounts.minOf { it }
            }
}

private val List<String>.asInsertionRules: Map<String, String>
    get() = filter { it.contains("->") }.associate {
        val (instruction, result) = it.split("->")
        instruction.trim() to result.trim()
    }

private val List<String>.asInsertionRules1: List<List<Int>>
    get() {
        val list = MutableList(size = 10) { MutableList(size = 10) { -1 } }
        filter { it.contains("->") }
                .forEach {
                    val (instruction, result) = it.split("->")
                    val instruction1 = inputToNumber[instruction[0]]!!
                    val instruction2 = inputToNumber[instruction[1]]!!
                    list[instruction1][instruction2] = inputToNumber[result.trim().first()]!!
                }
        return list
    }

private val List<String>.asPolymerTemplate: String
    get() = filterNot { it.startsWith("->") || it.isEmpty() }.first()

private val List<String>.asPolymerTemplate1: List<Int>
    get() = filterNot { it.startsWith("->") || it.isEmpty() }.first().map {
        inputToNumber.getValue(it)
    }