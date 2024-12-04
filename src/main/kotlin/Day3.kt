package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/03/test.txt")

private fun inputStream() = resourceLineStream("/03/input.txt")

private fun part1(): Int {
    return inputStream().toList().sumOf { line ->
        val matches = """mul\((\d+),(\d+)\)""".toRegex().findAll(line)

        matches.sumOf {
            val (left, right) = it.destructured
            left.toInt() * right.toInt()
        }
    }
}

private fun part2(): Int {
    val input = inputStream().toList().joinToString("")
    val matches = """mul\((\d+),(\d+)\)|do\(\)|don't\(\)""".toRegex().findAll(input)

    return matches
        .fold(0 to true) { acc, match ->
            val (sum, enabled) = acc
            val instruction = match.groupValues[0]

            if (instruction.startsWith("don't")) {
                sum to false
            } else if (instruction.startsWith("do")) {
                sum to true
            } else if (enabled) {
                val (left, right) = match.destructured
                (sum + left.toInt() * right.toInt()) to true
            } else {
                acc
            }
        }
        .first
}
