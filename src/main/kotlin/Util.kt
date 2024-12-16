package io.github.houli

import java.util.stream.Stream

fun resourceLineStream(filename: String): Stream<String> =
    Any::class::class.java.getResourceAsStream(filename).bufferedReader().lines()

data class Point(val x: Int, val y: Int) {
    fun inBounds(width: Int, height: Int): Boolean = x in 0..<width && y in 0..<height

    operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)
}

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    fun toVector(): Point =
        when (this) {
            UP -> Point(0, -1)
            DOWN -> Point(0, 1)
            LEFT -> Point(-1, 0)
            RIGHT -> Point(1, 0)
        }

    fun turn90DegreesClockwise(): Direction =
        when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }

    fun turnsRequired(direction: Direction): Int =
        when (this) {
            UP ->
                when (direction) {
                    UP -> 0
                    RIGHT -> 1
                    DOWN -> 2
                    LEFT -> 1
                }
            RIGHT ->
                when (direction) {
                    UP -> 1
                    RIGHT -> 0
                    DOWN -> 1
                    LEFT -> 2
                }
            DOWN ->
                when (direction) {
                    UP -> 2
                    RIGHT -> 1
                    DOWN -> 0
                    LEFT -> 1
                }
            LEFT ->
                when (direction) {
                    UP -> 1
                    RIGHT -> 2
                    DOWN -> 1
                    LEFT -> 0
                }
        }
}

data class PointDirection(val point: Point, val direction: Direction)
