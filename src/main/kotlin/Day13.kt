package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

data class LinearEquation(val coefficient1: Long, val coefficient2: Long, val constant: Long) {
    fun multiplyBy(scalar: Long) =
        LinearEquation(coefficient1 * scalar, coefficient2 * scalar, constant * scalar)

    fun subtract(other: LinearEquation) =
        LinearEquation(
            coefficient1 - other.coefficient1,
            coefficient2 - other.coefficient2,
            constant - other.constant,
        )
}

data class LinearEquationSet(val equation1: LinearEquation, val equation2: LinearEquation)

private fun testStream() = resourceLineStream("/13/test.txt")

private fun inputStream() = resourceLineStream("/13/input.txt")

private fun part1(): Long {
    val equationSets = parseEquationSets(inputStream().toList())
    return equationSets.sumOf { equationSet ->
        linearEquationSolution(equationSet)?.let { (x, y) -> x * 3 + y } ?: 0
    }
}

private fun part2(): Long {
    val equationSets = parseEquationSets(inputStream().toList(), 10_000_000_000_000)
    return equationSets.sumOf { equationSet ->
        linearEquationSolution(equationSet)?.let { (x, y) -> x * 3 + y } ?: 0
    }
}

private fun parseEquationSets(
    input: List<String>,
    randomFudgeFactor: Long = 0,
): List<LinearEquationSet> {
    val equationRegex = """Button [AB]: X\+(\d+), Y\+(\d+)""".toRegex()
    val prizeRegex = """Prize: X=(\d+), Y=+(\d+)""".toRegex()

    val chunks = input.chunked(4).map { it.take(3) }
    return chunks.map { chunk ->
        val (firstX, firstY) = equationRegex.find(chunk[0])!!.destructured
        val (secondX, secondY) = equationRegex.find(chunk[1])!!.destructured
        val (prizeX, prizeY) = prizeRegex.find(chunk[2])!!.destructured
        val firstEquation =
            LinearEquation(firstX.toLong(), secondX.toLong(), prizeX.toLong() + randomFudgeFactor)
        val secondEquation =
            LinearEquation(firstY.toLong(), secondY.toLong(), prizeY.toLong() + randomFudgeFactor)
        LinearEquationSet(firstEquation, secondEquation)
    }
}

private fun linearEquationSolution(equationSet: LinearEquationSet): Pair<Long, Long>? {
    val first = equationSet.equation1
    val second = equationSet.equation2
    val firstScaled = first.multiplyBy(second.coefficient2)
    val secondScaled = second.multiplyBy(first.coefficient2)
    val cancelled = firstScaled.subtract(secondScaled)
    val xSolution = cancelled.constant / cancelled.coefficient1
    val xRemainder = cancelled.constant.rem(cancelled.coefficient1)
    val firstSubstituted =
        LinearEquation(
            coefficient1 = 0,
            coefficient2 = first.coefficient2,
            constant = first.constant - (first.coefficient1 * xSolution),
        )
    val ySolution = firstSubstituted.constant / firstSubstituted.coefficient2
    val yRemainder = firstSubstituted.constant.rem(firstSubstituted.coefficient2)

    return if (xRemainder == 0L && yRemainder == 0L) {
        xSolution to ySolution
    } else {
        null
    }
}
