package io.github.houli

import kotlin.math.absoluteValue

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/01/test.txt")

private fun inputStream() = resourceLineStream("/01/input.txt")

private fun part1(): Int {
    val (lefts, rights) =
        inputStream()
            .toList()
            .map { line ->
                val left = line.substringBefore(" ").toInt()
                val right = line.substringAfterLast(" ").toInt()

                left to right
            }
            .unzip()

    val leftsSorted = lefts.sorted()
    val rightsSorted = rights.sorted()

    return leftsSorted.zip(rightsSorted).sumOf { (it.second - it.first).absoluteValue }
}

private fun part2(): Int {
    val (lefts, rights) =
        inputStream()
            .toList()
            .map { line ->
                val left = line.substringBefore(" ").toInt()
                val right = line.substringAfterLast(" ").toInt()

                left to right
            }
            .unzip()

    return lefts.sumOf { left -> left * rights.count { right -> left == right } }
}
