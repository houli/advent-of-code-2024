package io.github.houli

import java.util.Optional
import java.util.stream.Stream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

enum class Operator {
    ADD,
    MULTIPLY,
    CONCAT,
}

private fun testStream() = resourceLineStream("/07/test.txt")

private fun inputStream() = resourceLineStream("/07/input.txt")

private fun part1(): Long {
    val input = inputStream().toList()

    return runBlocking(Dispatchers.Default) {
            input
                .map { line ->
                    async {
                        val (targetString, numbersString) = line.split(":")
                        val target = targetString.toLong()
                        val numbers = numbersString.trim().split(" ").map(String::toLong)

                        val operatorPermutations =
                            permutationsStream(
                                numbers.size - 1,
                                setOf(Operator.ADD, Operator.MULTIPLY),
                            )
                        val solution =
                            operatorsSatisfyingTarget(target, numbers, operatorPermutations)

                        if (solution.isPresent) {
                            target
                        } else {
                            0
                        }
                    }
                }
                .awaitAll()
        }
        .sum()
}

private fun part2(): Long {
    val input = inputStream().toList()

    return runBlocking(Dispatchers.Default) {
            input
                .map { line ->
                    async {
                        val (targetString, numbersString) = line.split(":")
                        val target = targetString.toLong()
                        val numbers = numbersString.trim().split(" ").map(String::toLong)

                        val operatorPermutations = permutationsStream(numbers.size - 1)
                        val solution =
                            operatorsSatisfyingTarget(target, numbers, operatorPermutations)

                        if (solution.isPresent) {
                            target
                        } else {
                            0
                        }
                    }
                }
                .awaitAll()
        }
        .sum()
}

private fun operatorsSatisfyingTarget(
    target: Long,
    numbers: List<Long>,
    operatorPermutations: Stream<List<Operator>>,
): Optional<List<Operator>> {
    return operatorPermutations
        .filter { operators ->
            val result =
                numbers.foldIndexed(0L) { index, acc, number ->
                    if (index == 0) {
                        number
                    } else {
                        when (operators[index - 1]) {
                            Operator.ADD -> acc + number
                            Operator.MULTIPLY -> acc * number
                            Operator.CONCAT -> (acc.toString() + number.toString()).toLong()
                        }
                    }
                }
            result == target
        }
        .findAny()
}

private fun permutationsStream(
    size: Int,
    validOperators: Set<Operator> = Operator.entries.toSet(),
): Stream<List<Operator>> {
    fun permutationsStreamInternal(current: List<Operator>): Stream<List<Operator>> {
        if (current.size == size) {
            return Stream.of(current)
        }

        return Stream.of(*validOperators.toTypedArray()).flatMap { operator ->
            permutationsStreamInternal(current + operator)
        }
    }

    return permutationsStreamInternal(emptyList())
}
