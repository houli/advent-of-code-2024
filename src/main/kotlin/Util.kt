package io.github.houli

import java.util.stream.Stream

fun resourceLineStream(filename: String): Stream<String> =
    Any::class::class.java.getResourceAsStream(filename).bufferedReader().lines()

data class Point(val x: Int, val y: Int)
