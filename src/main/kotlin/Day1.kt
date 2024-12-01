package io.github.houli

import kotlin.math.abs

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

fun testStream() = resourceLineStream("/01/test.txt")

fun inputStream() = resourceLineStream("/01/input.txt")

fun part1(): Int {
    val pairs =
        inputStream().map { line -> line.split("\\s+".toRegex()).map(String::toInt) }.toList()

    val lefts = pairs.map { it[0] }.sorted()
    val rights = pairs.map { it[1] }.sorted()

    return lefts.zip(rights).map { abs(it.second - it.first) }.sum()
}

fun part2(): Int {
    val pairs =
        inputStream().map { line -> line.split("\\s+".toRegex()).map(String::toInt) }.toList()

    val lefts = pairs.map { it[0] }
    val rights = pairs.map { it[1] }

    return lefts.map { left -> left * rights.count { right -> left == right } }.sum()
}
