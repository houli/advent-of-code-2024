package io.github.houli

import kotlin.math.log10

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/11/test.txt")

private fun inputStream() = resourceLineStream("/11/input.txt")

private fun part1(): Long {
    val stones = inputStream().toList().joinToString("").split(" ").map(String::toLong)
    var stonesCount = stones.groupingBy { it }.eachCount().mapValues { it.value.toLong() }

    repeat(25) {
        stonesCount = blink(stonesCount)
    }
    return stonesCount.map { it.value }.sum()
}

private fun part2(): Long {
    val stones = inputStream().toList().joinToString("").split(" ").map(String::toLong)
    var stonesCount = stones.groupingBy { it }.eachCount().mapValues { it.value.toLong() }

    repeat(75) {
        stonesCount = blink(stonesCount)
    }
    return stonesCount.map { it.value }.sum()
}

private fun blink(stones: Map<Long, Long>): Map<Long, Long> {
    val new = mutableMapOf<Long, Long>()
    stones.forEach { (stone, count) ->
        val stones = step(stone)
        stones.forEach { newStone ->
            new.merge(newStone, count, Long::plus)
        }
    }
    return new
}

private fun step(stone: Long): List<Long> =
    if (stone == 0L) {
        listOf(1L)
    } else {
        val numberOfDigits = log10(stone.toDouble()).toInt() + 1
        if (numberOfDigits % 2 == 0) {
            val stoneString = stone.toString()
            val half = numberOfDigits / 2
            val first = stoneString.take(half).toLong()
            val second = stoneString.drop(half).dropWhile { it == '0' }.toLongOrNull() ?: 0L
            listOf(first, second)
        } else {
            listOf(stone * 2024L)
        }
    }
