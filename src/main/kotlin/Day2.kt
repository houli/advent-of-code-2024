package io.github.houli

import kotlin.math.absoluteValue

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/02/test.txt")

private fun inputStream() = resourceLineStream("/02/input.txt")

private fun part1(): Int {
    return inputStream().toList().count { line ->
        val levels = line.split(" ").map(String::toInt).toList()
        isSafe(levels)
    }
}

private fun part2(): Int {
    return inputStream().toList().count { report ->
        val levels = report.split(" ").map(String::toInt).toList()
        if (!isSafe(levels)) {
            levels.indices.any { isSafe(levels.toMutableList().apply { removeAt(it) }) }
        } else {
            true
        }
    }
}

private fun isSafe(levels: Collection<Int>): Boolean {
    val levelPairs = levels.windowed(2)

    val increasing = levelPairs.all { (a, b) -> a < b }
    val decreasing = levelPairs.all { (a, b) -> a > b }

    val adjacentCriteria =
        levelPairs.all { (a, b) ->
            val diff = (a - b).absoluteValue
            diff >= 1 && diff <= 3
        }

    return (increasing || decreasing) && adjacentCriteria
}
