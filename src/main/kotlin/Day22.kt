package io.github.houli

import java.util.stream.Stream
import kotlin.math.absoluteValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/22/test.txt")

private fun inputStream() = resourceLineStream("/22/input.txt")

private fun part1(): Long {
    val input = inputStream().toList().map(String::toLong)
    return input.sumOf { number -> secretNumberSequence(number).elementAt(2000) }
}

private fun part2(): Long {
    val input = inputStream().toList().map(String::toLong)
    val permutations = permutationsStream(4, setOf(*(-9..9L).toList().toTypedArray())).toList()
    val inputMaps =
        input.map { number ->
            val map = mutableMapOf<List<Long>, Long>()
            val secretNumbers = secretNumberSequence(number).take(2001)
            val lastDigits = lastDigitSequence(secretNumbers)
            lastDigits.windowed(5, 1, false).forEach { (a, b, c, d, e) ->
                val first = b - a
                val second = c - b
                val third = d - c
                val fourth = e - d
                val perm = listOf(first, second, third, fourth)

                if (!map.containsKey(perm)) {
                    map[perm] = e
                }
            }
            map
        }
    return runBlocking(Dispatchers.Default) {
            permutations
                .map { permutation -> async { inputMaps.sumOf { map -> map[permutation] ?: 0 } } }
                .awaitAll()
        }
        .maxOrNull() ?: 0
}

private fun lastDigitSequence(sequence: Sequence<Long>): Sequence<Long> =
    sequence.map { it.absoluteValue % 10 }

private fun secretNumberSequence(initialSecret: Long): Sequence<Long> =
    generateSequence(initialSecret) { current ->
        val first = prune(mix(current * 64, current))
        val second = prune(mix(first / 32, first))
        prune(mix(second * 2048, second))
    }

private fun mix(num: Long, secret: Long): Long = num xor secret

private fun prune(num: Long): Long = num % 16777216

private fun permutationsStream(size: Int, validNumbers: Set<Long>): Stream<List<Long>> {
    fun permutationsStreamInternal(current: List<Long>): Stream<List<Long>> {
        if (current.size == size) {
            return Stream.of(current)
        }

        return Stream.of(*validNumbers.toTypedArray()).flatMap { number ->
            permutationsStreamInternal(current + number)
        }
    }

    return permutationsStreamInternal(emptyList())
}
