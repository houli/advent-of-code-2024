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
            PointerMove.UP ->
                when (other) {
                    PointerMove.UP -> emptyList()
                    PointerMove.DOWN -> listOf(PointerMove.DOWN)
                    PointerMove.LEFT -> listOf(PointerMove.DOWN, PointerMove.LEFT)
                    PointerMove.RIGHT -> listOf(PointerMove.DOWN, PointerMove.RIGHT)
                    PointerMove.A -> listOf(PointerMove.RIGHT)
                }
            PointerMove.DOWN ->
                when (other) {
                    PointerMove.UP -> listOf(PointerMove.UP)
                    PointerMove.DOWN -> emptyList()
                    PointerMove.LEFT -> listOf(PointerMove.LEFT)
                    PointerMove.RIGHT -> listOf(PointerMove.RIGHT)
                    PointerMove.A -> listOf(PointerMove.UP, PointerMove.RIGHT)
                }
            PointerMove.LEFT ->
                when (other) {
                    PointerMove.UP -> listOf(PointerMove.RIGHT, PointerMove.UP)
                    PointerMove.DOWN -> listOf(PointerMove.RIGHT)
                    PointerMove.LEFT -> emptyList()
                    PointerMove.RIGHT -> listOf(PointerMove.RIGHT, PointerMove.RIGHT)
                    PointerMove.A -> listOf(PointerMove.RIGHT, PointerMove.RIGHT, PointerMove.UP)
                }
            PointerMove.RIGHT ->
                when (other) {
                    PointerMove.UP -> listOf(PointerMove.LEFT, PointerMove.UP)
                    PointerMove.DOWN -> listOf(PointerMove.LEFT)
                    PointerMove.LEFT -> listOf(PointerMove.LEFT, PointerMove.LEFT)
                    PointerMove.RIGHT -> emptyList()
                    PointerMove.A -> listOf(PointerMove.UP)
                }
            PointerMove.A ->
                when (other) {
                    PointerMove.UP -> listOf(PointerMove.LEFT)
                    PointerMove.DOWN -> listOf(PointerMove.LEFT, PointerMove.DOWN)
                    PointerMove.LEFT -> listOf(PointerMove.DOWN, PointerMove.LEFT, PointerMove.LEFT)
                    PointerMove.RIGHT -> listOf(PointerMove.DOWN)
                    PointerMove.A -> emptyList()
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
            KeyPadButton.ZERO ->
                when (other) {
                    KeyPadButton.ZERO -> emptyList()
                    KeyPadButton.ONE -> listOf(PointerMove.UP, PointerMove.LEFT)
                    KeyPadButton.TWO -> listOf(PointerMove.UP)
                    KeyPadButton.THREE -> listOf(PointerMove.UP, PointerMove.RIGHT)
                    KeyPadButton.FOUR -> listOf(PointerMove.UP, PointerMove.UP, PointerMove.LEFT)
                    KeyPadButton.FIVE -> listOf(PointerMove.UP, PointerMove.UP)
                    KeyPadButton.SIX -> listOf(PointerMove.UP, PointerMove.UP, PointerMove.RIGHT)
                    KeyPadButton.SEVEN ->
                        listOf(PointerMove.UP, PointerMove.UP, PointerMove.UP, PointerMove.LEFT)
                    KeyPadButton.EIGHT -> listOf(PointerMove.UP, PointerMove.UP, PointerMove.UP)
                    KeyPadButton.NINE ->
                        listOf(PointerMove.UP, PointerMove.UP, PointerMove.UP, PointerMove.RIGHT)
                    KeyPadButton.A -> listOf(PointerMove.RIGHT)
                }
            KeyPadButton.ONE ->
                when (other) {
                    KeyPadButton.ZERO -> listOf(PointerMove.RIGHT, PointerMove.DOWN)
                    KeyPadButton.ONE -> emptyList()
                    KeyPadButton.TWO -> listOf(PointerMove.RIGHT)
                    KeyPadButton.THREE -> listOf(PointerMove.RIGHT, PointerMove.RIGHT)
                    KeyPadButton.FOUR -> listOf(PointerMove.UP)
                    KeyPadButton.FIVE -> listOf(PointerMove.UP, PointerMove.RIGHT)
                    KeyPadButton.SIX -> listOf(PointerMove.UP, PointerMove.RIGHT, PointerMove.RIGHT)
                    KeyPadButton.SEVEN -> listOf(PointerMove.UP, PointerMove.UP)
                    KeyPadButton.EIGHT -> listOf(PointerMove.UP, PointerMove.UP, PointerMove.RIGHT)
                    KeyPadButton.NINE ->
                        listOf(PointerMove.UP, PointerMove.UP, PointerMove.RIGHT, PointerMove.RIGHT)
                    KeyPadButton.A -> listOf(PointerMove.RIGHT, PointerMove.RIGHT, PointerMove.DOWN)
                }
            KeyPadButton.TWO ->
                when (other) {
                    KeyPadButton.ZERO -> listOf(PointerMove.DOWN)
                    KeyPadButton.ONE -> listOf(PointerMove.LEFT)
                    KeyPadButton.TWO -> emptyList()
                    KeyPadButton.THREE -> listOf(PointerMove.RIGHT)
                    KeyPadButton.FOUR -> listOf(PointerMove.LEFT, PointerMove.UP)
                    KeyPadButton.FIVE -> listOf(PointerMove.UP)
                    KeyPadButton.SIX -> listOf(PointerMove.UP, PointerMove.RIGHT)
                    KeyPadButton.SEVEN -> listOf(PointerMove.LEFT, PointerMove.UP, PointerMove.UP)
                    KeyPadButton.EIGHT -> listOf(PointerMove.UP, PointerMove.UP)
                    KeyPadButton.NINE -> listOf(PointerMove.UP, PointerMove.UP, PointerMove.RIGHT)
                    KeyPadButton.A -> listOf(PointerMove.DOWN, PointerMove.RIGHT)
                }
            KeyPadButton.THREE ->
                when (other) {
                    KeyPadButton.ZERO -> listOf(PointerMove.LEFT, PointerMove.DOWN)
                    KeyPadButton.ONE -> listOf(PointerMove.LEFT, PointerMove.LEFT)
                    KeyPadButton.TWO -> listOf(PointerMove.LEFT)
                    KeyPadButton.THREE -> emptyList()
                    KeyPadButton.FOUR -> listOf(PointerMove.LEFT, PointerMove.LEFT, PointerMove.UP)
                    KeyPadButton.FIVE -> listOf(PointerMove.LEFT, PointerMove.UP)
                    KeyPadButton.SIX -> listOf(PointerMove.UP)
                    KeyPadButton.SEVEN ->
                        listOf(PointerMove.LEFT, PointerMove.LEFT, PointerMove.UP, PointerMove.UP)
                    KeyPadButton.EIGHT -> listOf(PointerMove.LEFT, PointerMove.UP, PointerMove.UP)
                    KeyPadButton.NINE -> listOf(PointerMove.UP, PointerMove.UP)
                    KeyPadButton.A -> listOf(PointerMove.DOWN)
                }
            KeyPadButton.FOUR ->
                when (other) {
                    KeyPadButton.ZERO ->
                        listOf(PointerMove.RIGHT, PointerMove.DOWN, PointerMove.DOWN)
                    KeyPadButton.ONE -> listOf(PointerMove.DOWN)
                    KeyPadButton.TWO -> listOf(PointerMove.DOWN, PointerMove.RIGHT)
                    KeyPadButton.THREE ->
                        listOf(PointerMove.DOWN, PointerMove.RIGHT, PointerMove.RIGHT)
                    KeyPadButton.FOUR -> emptyList()
                    KeyPadButton.FIVE -> listOf(PointerMove.RIGHT)
                    KeyPadButton.SIX -> listOf(PointerMove.RIGHT, PointerMove.RIGHT)
                    KeyPadButton.SEVEN -> listOf(PointerMove.UP)
                    KeyPadButton.EIGHT -> listOf(PointerMove.UP, PointerMove.RIGHT)
                    KeyPadButton.NINE ->
                        listOf(PointerMove.UP, PointerMove.RIGHT, PointerMove.RIGHT)
                    KeyPadButton.A ->
                        listOf(
                            PointerMove.RIGHT,
                            PointerMove.RIGHT,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                        )
                }
            KeyPadButton.FIVE ->
                when (other) {
                    KeyPadButton.ZERO -> listOf(PointerMove.DOWN, PointerMove.DOWN)
                    KeyPadButton.ONE -> listOf(PointerMove.LEFT, PointerMove.DOWN)
                    KeyPadButton.TWO -> listOf(PointerMove.DOWN)
                    KeyPadButton.THREE -> listOf(PointerMove.DOWN, PointerMove.RIGHT)
                    KeyPadButton.FOUR -> listOf(PointerMove.LEFT)
                    KeyPadButton.FIVE -> emptyList()
                    KeyPadButton.SIX -> listOf(PointerMove.RIGHT)
                    KeyPadButton.SEVEN -> listOf(PointerMove.LEFT, PointerMove.UP)
                    KeyPadButton.EIGHT -> listOf(PointerMove.UP)
                    KeyPadButton.NINE -> listOf(PointerMove.UP, PointerMove.RIGHT)
                    KeyPadButton.A -> listOf(PointerMove.DOWN, PointerMove.DOWN, PointerMove.RIGHT)
                }
            KeyPadButton.SIX ->
                when (other) {
                    KeyPadButton.ZERO ->
                        listOf(PointerMove.LEFT, PointerMove.DOWN, PointerMove.DOWN)
                    KeyPadButton.ONE -> listOf(PointerMove.LEFT, PointerMove.LEFT, PointerMove.DOWN)
                    KeyPadButton.TWO -> listOf(PointerMove.LEFT, PointerMove.DOWN)
                    KeyPadButton.THREE -> listOf(PointerMove.DOWN)
                    KeyPadButton.FOUR -> listOf(PointerMove.LEFT, PointerMove.LEFT)
                    KeyPadButton.FIVE -> listOf(PointerMove.LEFT)
                    KeyPadButton.SIX -> emptyList()
                    KeyPadButton.SEVEN -> listOf(PointerMove.LEFT, PointerMove.LEFT, PointerMove.UP)
                    KeyPadButton.EIGHT -> listOf(PointerMove.LEFT, PointerMove.UP)
                    KeyPadButton.NINE -> listOf(PointerMove.UP)
                    KeyPadButton.A -> listOf(PointerMove.DOWN, PointerMove.DOWN)
                }
            KeyPadButton.SEVEN ->
                when (other) {
                    KeyPadButton.ZERO ->
                        listOf(
                            PointerMove.RIGHT,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                        )
                    KeyPadButton.ONE -> listOf(PointerMove.DOWN, PointerMove.DOWN)
                    KeyPadButton.TWO ->
                        listOf(PointerMove.DOWN, PointerMove.DOWN, PointerMove.RIGHT)
                    KeyPadButton.THREE ->
                        listOf(
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                            PointerMove.RIGHT,
                            PointerMove.RIGHT,
                        )
                    KeyPadButton.FOUR -> listOf(PointerMove.DOWN)
                    KeyPadButton.FIVE -> listOf(PointerMove.DOWN, PointerMove.RIGHT)
                    KeyPadButton.SIX ->
                        listOf(PointerMove.DOWN, PointerMove.RIGHT, PointerMove.RIGHT)
                    KeyPadButton.SEVEN -> emptyList()
                    KeyPadButton.EIGHT -> listOf(PointerMove.RIGHT)
                    KeyPadButton.NINE -> listOf(PointerMove.RIGHT, PointerMove.RIGHT)
                    KeyPadButton.A ->
                        listOf(
                            PointerMove.RIGHT,
                            PointerMove.RIGHT,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                        )
                }
            KeyPadButton.EIGHT ->
                when (other) {
                    KeyPadButton.ZERO ->
                        listOf(PointerMove.DOWN, PointerMove.DOWN, PointerMove.DOWN)
                    KeyPadButton.ONE -> listOf(PointerMove.LEFT, PointerMove.DOWN, PointerMove.DOWN)
                    KeyPadButton.TWO -> listOf(PointerMove.DOWN, PointerMove.DOWN)
                    KeyPadButton.THREE ->
                        listOf(PointerMove.DOWN, PointerMove.DOWN, PointerMove.RIGHT)
                    KeyPadButton.FOUR -> listOf(PointerMove.LEFT, PointerMove.DOWN)
                    KeyPadButton.FIVE -> listOf(PointerMove.DOWN)
                    KeyPadButton.SIX -> listOf(PointerMove.DOWN, PointerMove.RIGHT)
                    KeyPadButton.SEVEN -> listOf(PointerMove.LEFT)
                    KeyPadButton.EIGHT -> emptyList()
                    KeyPadButton.NINE -> listOf(PointerMove.RIGHT)
                    KeyPadButton.A ->
                        listOf(
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                            PointerMove.RIGHT,
                        )
                }
            KeyPadButton.NINE ->
                when (other) {
                    KeyPadButton.ZERO ->
                        listOf(
                            PointerMove.LEFT,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                        )
                    KeyPadButton.ONE ->
                        listOf(
                            PointerMove.LEFT,
                            PointerMove.LEFT,
                            PointerMove.DOWN,
                            PointerMove.DOWN,
                        )
                    KeyPadButton.TWO -> listOf(PointerMove.LEFT, PointerMove.DOWN, PointerMove.DOWN)
                    KeyPadButton.THREE -> listOf(PointerMove.DOWN, PointerMove.DOWN)
                    KeyPadButton.FOUR ->
                        listOf(PointerMove.LEFT, PointerMove.LEFT, PointerMove.DOWN)
                    KeyPadButton.FIVE -> listOf(PointerMove.LEFT, PointerMove.DOWN)
                    KeyPadButton.SIX -> listOf(PointerMove.DOWN)
                    KeyPadButton.SEVEN -> listOf(PointerMove.LEFT, PointerMove.LEFT)
                    KeyPadButton.EIGHT -> listOf(PointerMove.LEFT)
                    KeyPadButton.NINE -> emptyList()
                    KeyPadButton.A -> listOf(PointerMove.DOWN, PointerMove.DOWN, PointerMove.DOWN)
                }
            KeyPadButton.A ->
                when (other) {
                    KeyPadButton.ZERO -> listOf(PointerMove.LEFT)
                    KeyPadButton.ONE -> listOf(PointerMove.UP, PointerMove.LEFT, PointerMove.LEFT)
                    KeyPadButton.TWO -> listOf(PointerMove.LEFT, PointerMove.UP)
                    KeyPadButton.THREE -> listOf(PointerMove.UP)
                    KeyPadButton.FOUR ->
                        listOf(PointerMove.UP, PointerMove.UP, PointerMove.LEFT, PointerMove.LEFT)
                    KeyPadButton.FIVE -> listOf(PointerMove.LEFT, PointerMove.UP, PointerMove.UP)
                    KeyPadButton.SIX -> listOf(PointerMove.UP, PointerMove.UP)
                    KeyPadButton.SEVEN ->
                        listOf(
                            PointerMove.UP,
                            PointerMove.UP,
                            PointerMove.UP,
                            PointerMove.LEFT,
                            PointerMove.LEFT,
                        )
                    KeyPadButton.EIGHT ->
                        listOf(PointerMove.LEFT, PointerMove.UP, PointerMove.UP, PointerMove.UP)
                    KeyPadButton.NINE -> listOf(PointerMove.UP, PointerMove.UP, PointerMove.UP)
                    KeyPadButton.A -> emptyList()
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
