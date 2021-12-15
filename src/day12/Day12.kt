package day12

import readInput

fun main() {
    fun part1(input: List<String>): Int = calculateNumberOfPaths(input, canVisitOneSmallCaveTwice = false)
    fun part2(input: List<String>): Int = calculateNumberOfPaths(input, canVisitOneSmallCaveTwice = true)

    val testInput = readInput(name = "Day12_test", day = "day12")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val input = readInput(name = "Day12", day = "day12")
    println("=== Part 1 ===")
    println(part1(input))
    println("=== Part 2 ===")
    println(part2(input))

}

private fun calculateNumberOfPaths(input: List<String>, canVisitOneSmallCaveTwice: Boolean): Int {
    val caves = input.asCaves
    val completePaths = mutableListOf<List<Cave>>()

    fun visit(cave: Cave, previousCaves: List<Cave> = emptyList()) {
        val currentPath = previousCaves + cave
        if (cave.isEnd) {
            completePaths.add(currentPath)
        }

        cave.neighbours
            .map { nextCave -> caves.first { it.name == nextCave } }
            .forEach { nextCave ->
                if (currentPath.canVisitCave(nextCave, canVisitOneSmallCaveTwice)) {
                    visit(cave = nextCave, previousCaves = currentPath)
                }
            }
    }

    visit(cave = caves.first { it.isStart })

    return completePaths.size
}

private fun List<Cave>.canVisitCave(cave: Cave, canVisitOneSmallCaveTwice: Boolean = false): Boolean =
    when {
        cave.isStart && !contains(cave) ||
                cave.isEnd && !contains(cave) ||
                !cave.isSmall ||
                none { it.name == cave.name }
        -> true

        canVisitOneSmallCaveTwice &&
                !cave.isEnd &&
                !cave.isStart -> {
            !filter { it.isSmall }
                .groupBy { it.name }
                .map { it.key to it.value.size }
                .toMap()
                .any { it.value == 2 }
        }

        else -> false
    }

private data class Cave(val name: String, val neighbours: List<String>) {
    val isSmall = !name.isUpperCase
    val isEnd = name == "end"
    val isStart = name == "start"
}

private data class Path(val cave1: String, val cave2: String) {
    fun containsCave(cave: String) = cave1 == cave || cave2 == cave
}

private val String.isUpperCase get() = all { it.isUpperCase() }

private val List<String>.asCaves: List<Cave>
    get() = map {
        it
            .split("-")
            .let { (cave1, cave2) -> Path(cave1, cave2) }
    }
        .let { paths ->
            paths
                .flatMap { path -> listOf(path.cave1, path.cave2) }
                .distinct()
                .map { name ->
                    Cave(
                        name = name,
                        neighbours = paths
                            .filter { it.containsCave(name) }
                            .mapNotNull { path ->
                                when {
                                    path.cave1 == name -> path.cave2
                                    path.cave2 == name -> path.cave1
                                    else -> null
                                }
                            }
                    )
                }
        }
        .distinct()