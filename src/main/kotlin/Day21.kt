package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

enum class PointerMove {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    A;

    fun moveTo(other: PointerMove): List<PointerMove> =
        when (this) {
            UP ->
                when (other) {
                    UP -> emptyList()
                    DOWN -> listOf(DOWN)
                    LEFT -> listOf(DOWN, LEFT)
                    RIGHT -> listOf(DOWN, RIGHT)
                    A -> listOf(RIGHT)
                }
            DOWN ->
                when (other) {
                    UP -> listOf(UP)
                    DOWN -> emptyList()
                    LEFT -> listOf(LEFT)
                    RIGHT -> listOf(RIGHT)
                    A -> listOf(UP, RIGHT)
                }
            LEFT ->
                when (other) {
                    UP -> listOf(RIGHT, UP)
                    DOWN -> listOf(RIGHT)
                    LEFT -> emptyList()
                    RIGHT -> listOf(RIGHT, RIGHT)
                    A -> listOf(RIGHT, RIGHT, UP)
                }
            RIGHT ->
                when (other) {
                    UP -> listOf(LEFT, UP)
                    DOWN -> listOf(LEFT)
                    LEFT -> listOf(LEFT, LEFT)
                    RIGHT -> emptyList()
                    A -> listOf(UP)
                }
            A ->
                when (other) {
                    UP -> listOf(LEFT)
                    DOWN -> listOf(LEFT, DOWN)
                    LEFT -> listOf(DOWN, LEFT, LEFT)
                    RIGHT -> listOf(DOWN)
                    A -> emptyList()
                }
        }
}

enum class KeyPadButton {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    A;

    fun moveTo(other: KeyPadButton): List<PointerMove> =
        when (this) {
            ZERO ->
                when (other) {
                    ZERO -> emptyList()
                    ONE -> listOf(PointerMove.UP, PointerMove.LEFT)
                    TWO -> listOf(PointerMove.UP)
                    THREE -> listOf(PointerMove.UP, PointerMove.RIGHT)
                    FOUR -> listOf(PointerMove.UP, PointerMove.UP, PointerMove.LEFT)
                    FIVE -> listOf(PointerMove.UP, PointerMove.UP)
                    SIX -> listOf(PointerMove.UP, PointerMove.UP, PointerMove.RIGHT)
                    SEVEN ->
                        listOf(PointerMove.UP, PointerMove.UP, PointerMove.UP, PointerMove.LEFT)
                    EIGHT -> listOf(PointerMove.UP, PointerMove.UP, PointerMove.UP)
                    NINE ->
                        listOf(PointerMove.UP, PointerMove.UP, PointerMove.UP, PointerMove.RIGHT)
                    A -> listOf(PointerMove.RIGHT)
                }
            ONE ->
                when (other) {
                    ZERO -> listOf(PointerMove.RIGHT, PointerMove.DOWN)
                    ONE -> emptyList()
                    TWO -> listOf(PointerMove.RIGHT)
                    THREE -> listOf(PointerMove.RIGHT, PointerMove.RIGHT)
                    FOUR -> listOf(PointerMove.UP)
                    FIVE -> listOf(PointerMove.UP, PointerMove.RIGHT)
                    SIX -> listOf(PointerMove.UP, PointerMove.RIGHT, PointerMove.RIGHT)
                    SEVEN -> listOf(PointerMove.UP, PointerMove.UP)
                    EIGHT -> listOf(PointerMove.UP, PointerMove.UP, PointerMove.RIGHT)
                    NINE ->
                        listOf(PointerMove.UP, PointerMove.UP, PointerMove.RIGHT, PointerMove.RIGHT)
                    A -> listOf(PointerMove.RIGHT, PointerMove.RIGHT, PointerMove.DOWN)
                }
            TWO ->
                when (other) {
                    ZERO -> listOf(PointerMove.DOWN)
                    ONE -> listOf(PointerMove.LEFT)
                    TWO -> emptyList()
                    THREE -> listOf(PointerMove.RIGHT)
                    FOUR -> listOf(PointerMove.LEFT, PointerMove.UP)
                    FIVE -> listOf(PointerMove.UP)
                    SIX -> listOf(PointerMove.UP, PointerMove.RIGHT)
                    SEVEN -> listOf(PointerMove.LEFT, PointerMove.UP, PointerMove.UP)
                    EIGHT -> listOf(PointerMove.UP, PointerMove.UP)
                    NINE -> listOf(PointerMove.UP, PointerMove.UP, PointerMove.RIGHT)
                    A -> listOf(PointerMove.DOWN, PointerMove.RIGHT)
                }
            THREE ->
                when (other) {
                    ZERO -> listOf(PointerMove.LEFT, PointerMove.DOWN)
                    ONE -> listOf(PointerMove.LEFT, PointerMove.LEFT)
                    TWO -> listOf(PointerMove.LEFT)
                    THREE -> emptyList()
                    FOUR -> listOf(PointerMove.LEFT, PointerMove.LEFT, PointerMove.UP)
                    FIVE -> listOf(PointerMove.LEFT, PointerMove.UP)
                    SIX -> listOf(PointerMove.UP)
                    SEVEN ->
                        listOf(PointerMove.LEFT, PointerMove.LEFT, PointerMove.UP, PointerMove.UP)
                    EIGHT -> listOf(PointerMove.LEFT, PointerMove.UP, PointerMove.UP)
                    NINE -> listOf(PointerMove.UP, PointerMove.UP)
                    A -> listOf(PointerMove.DOWN)
                }
            FOUR ->
                when (other) {
                    ZERO -> listOf(PointerMove.RIGHT, PointerMove.DOWN, PointerMove.DOWN)
                    ONE -> listOf(PointerMove.DOWN)
                    TWO -> listOf(PointerMove.DOWN, PointerMove.RIGHT)
                    THREE -> listOf(PointerMove.DOWN, PointerMove.RIGHT, PointerMove.RIGHT)
                    FOUR -> emptyList()
                    FIVE -> listOf(PointerMove.RIGHT)
                    SIX -> listOf(PointerMove.RIGHT, PointerMove.RIGHT)
                    SEVEN -> listOf(PointerMove.UP)
                    EIGHT -> listOf(PointerMove.UP, PointerMove.RIGHT)
                    NINE -> listOf(PointerMove.UP, PointerMove.RIGHT, PointerMove.RIGHT)
                    A ->
                        listOf(
                            PointerMove.RIGHT,
                            PointerMove.RIGHT,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                        )
                }
            FIVE ->
                when (other) {
                    ZERO -> listOf(PointerMove.DOWN, PointerMove.DOWN)
                    ONE -> listOf(PointerMove.LEFT, PointerMove.DOWN)
                    TWO -> listOf(PointerMove.DOWN)
                    THREE -> listOf(PointerMove.DOWN, PointerMove.RIGHT)
                    FOUR -> listOf(PointerMove.LEFT)
                    FIVE -> emptyList()
                    SIX -> listOf(PointerMove.RIGHT)
                    SEVEN -> listOf(PointerMove.LEFT, PointerMove.UP)
                    EIGHT -> listOf(PointerMove.UP)
                    NINE -> listOf(PointerMove.UP, PointerMove.RIGHT)
                    A -> listOf(PointerMove.DOWN, PointerMove.DOWN, PointerMove.RIGHT)
                }
            SIX ->
                when (other) {
                    ZERO -> listOf(PointerMove.LEFT, PointerMove.DOWN, PointerMove.DOWN)
                    ONE -> listOf(PointerMove.LEFT, PointerMove.LEFT, PointerMove.DOWN)
                    TWO -> listOf(PointerMove.LEFT, PointerMove.DOWN)
                    THREE -> listOf(PointerMove.DOWN)
                    FOUR -> listOf(PointerMove.LEFT, PointerMove.LEFT)
                    FIVE -> listOf(PointerMove.LEFT)
                    SIX -> emptyList()
                    SEVEN -> listOf(PointerMove.LEFT, PointerMove.LEFT, PointerMove.UP)
                    EIGHT -> listOf(PointerMove.LEFT, PointerMove.UP)
                    NINE -> listOf(PointerMove.UP)
                    A -> listOf(PointerMove.DOWN, PointerMove.DOWN)
                }
            SEVEN ->
                when (other) {
                    ZERO ->
                        listOf(
                            PointerMove.RIGHT,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                        )
                    ONE -> listOf(PointerMove.DOWN, PointerMove.DOWN)
                    TWO -> listOf(PointerMove.DOWN, PointerMove.DOWN, PointerMove.RIGHT)
                    THREE ->
                        listOf(
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                            PointerMove.RIGHT,
                            PointerMove.RIGHT,
                        )
                    FOUR -> listOf(PointerMove.DOWN)
                    FIVE -> listOf(PointerMove.DOWN, PointerMove.RIGHT)
                    SIX -> listOf(PointerMove.DOWN, PointerMove.RIGHT, PointerMove.RIGHT)
                    SEVEN -> emptyList()
                    EIGHT -> listOf(PointerMove.RIGHT)
                    NINE -> listOf(PointerMove.RIGHT, PointerMove.RIGHT)
                    A ->
                        listOf(
                            PointerMove.RIGHT,
                            PointerMove.RIGHT,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                        )
                }
            EIGHT ->
                when (other) {
                    ZERO -> listOf(PointerMove.DOWN, PointerMove.DOWN, PointerMove.DOWN)
                    ONE -> listOf(PointerMove.LEFT, PointerMove.DOWN, PointerMove.DOWN)
                    TWO -> listOf(PointerMove.DOWN, PointerMove.DOWN)
                    THREE -> listOf(PointerMove.DOWN, PointerMove.DOWN, PointerMove.RIGHT)
                    FOUR -> listOf(PointerMove.LEFT, PointerMove.DOWN)
                    FIVE -> listOf(PointerMove.DOWN)
                    SIX -> listOf(PointerMove.DOWN, PointerMove.RIGHT)
                    SEVEN -> listOf(PointerMove.LEFT)
                    EIGHT -> emptyList()
                    NINE -> listOf(PointerMove.RIGHT)
                    A ->
                        listOf(
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                            PointerMove.RIGHT,
                        )
                }
            NINE ->
                when (other) {
                    ZERO ->
                        listOf(
                            PointerMove.LEFT,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                        )
                    ONE ->
                        listOf(
                            PointerMove.LEFT,
                            PointerMove.LEFT,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                        )
                    TWO -> listOf(PointerMove.LEFT, PointerMove.DOWN, PointerMove.DOWN)
                    THREE -> listOf(PointerMove.DOWN, PointerMove.DOWN)
                    FOUR -> listOf(PointerMove.LEFT, PointerMove.LEFT, PointerMove.DOWN)
                    FIVE -> listOf(PointerMove.LEFT, PointerMove.DOWN)
                    SIX -> listOf(PointerMove.DOWN)
                    SEVEN -> listOf(PointerMove.LEFT, PointerMove.LEFT)
                    EIGHT -> listOf(PointerMove.LEFT)
                    NINE -> emptyList()
                    A -> listOf(PointerMove.DOWN, PointerMove.DOWN, PointerMove.DOWN)
                }
            A ->
                when (other) {
                    ZERO -> listOf(PointerMove.LEFT)
                    ONE -> listOf(PointerMove.UP, PointerMove.LEFT, PointerMove.LEFT)
                    TWO -> listOf(PointerMove.LEFT, PointerMove.UP)
                    THREE -> listOf(PointerMove.UP)
                    FOUR ->
                        listOf(PointerMove.UP, PointerMove.UP, PointerMove.LEFT, PointerMove.LEFT)
                    FIVE -> listOf(PointerMove.LEFT, PointerMove.UP, PointerMove.UP)
                    SIX -> listOf(PointerMove.UP, PointerMove.UP)
                    SEVEN ->
                        listOf(
                            PointerMove.UP,
                            PointerMove.UP,
                            PointerMove.UP,
                            PointerMove.LEFT,
                            PointerMove.LEFT,
                        )
                    EIGHT ->
                        listOf(PointerMove.LEFT, PointerMove.UP, PointerMove.UP, PointerMove.UP)
                    NINE -> listOf(PointerMove.UP, PointerMove.UP, PointerMove.UP)
                    A -> emptyList()
                }
        }

    companion object {
        fun fromChar(c: Char): KeyPadButton =
            when (c) {
                '0' -> ZERO
                '1' -> ONE
                '2' -> TWO
                '3' -> THREE
                '4' -> FOUR
                '5' -> FIVE
                '6' -> SIX
                '7' -> SEVEN
                '8' -> EIGHT
                '9' -> NINE
                'A' -> A
                else -> throw IllegalArgumentException("Invalid char: $c")
            }
    }
}

class KeyPad {
    private var currentButton = KeyPadButton.A

    fun moveTo(button: KeyPadButton): List<PointerMove> {
        val moves = currentButton.moveTo(button).plus(PointerMove.A)
        currentButton = button
        return moves
    }
}

class DirectionalPad {
    private var currentButton = PointerMove.A

    fun moveTo(button: PointerMove): List<PointerMove> {
        val moves = currentButton.moveTo(button).plus(PointerMove.A)
        currentButton = button
        return moves
    }
}

private fun testStream() = resourceLineStream("/21/test.txt")

private fun inputStream() = resourceLineStream("/21/input.txt")

private fun part1(): Long {
    val input = inputStream().toList()
    val numberButtonPairs = parseInput(input)
    return commonSolution(numberButtonPairs, 2)
}

private fun part2(): Long {
    val input = inputStream().toList()
    val numberButtonPairs = parseInput(input)
    return commonSolution(numberButtonPairs, 25)
}

private fun commonSolution(
    numberButtonPairs: List<Pair<Long, List<KeyPadButton>>>,
    numberOfRobotDirectionalPads: Int,
): Long {
    val cache = mutableMapOf<Pair<Int, List<PointerMove>>, Long>()
    fun dpadCost(moves: List<PointerMove>, layer: Int): Long {
        if (cache.containsKey(layer to moves)) {
            return cache[layer to moves]!!
        }

        val dpad = DirectionalPad()
        if (layer == 0) {
            return moves.size.toLong()
        } else {
            val sum =
                moves.sumOf { move ->
                    val newMoves = dpad.moveTo(move)
                    dpadCost(newMoves, layer - 1)
                }
            cache[layer to moves] = sum
            return sum
        }
    }

    return numberButtonPairs.sumOf { (number, buttons) ->
        val keyPad = KeyPad()
        val moveLength =
            buttons.sumOf { button ->
                dpadCost(keyPad.moveTo(button), numberOfRobotDirectionalPads)
            }
        number * moveLength
    }
}

private fun parseInput(input: List<String>): List<Pair<Long, List<KeyPadButton>>> =
    input.map { line ->
        val number = line.substring(0, 3).toLong()
        val buttons = line.map(KeyPadButton::fromChar)
        number to buttons
    }
