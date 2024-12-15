package io.github.houli

import java.util.stream.Stream

fun resourceLineStream(filename: String): Stream<String> =
    Any::class::class.java.getResourceAsStream(filename).bufferedReader().lines()

data class Point(val x: Int, val y: Int) {
    fun inBounds(width: Int, height: Int): Boolean = x in 0..<width && y in 0..<height

    operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)
}
