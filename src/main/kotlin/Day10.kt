package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

data class TrailheadValue(val score: Int, val rating: Int)

private fun testStream() = resourceLineStream("/10/test.txt")

private fun inputStream() = resourceLineStream("/10/input.txt")

private fun part1(): Int {
    val input = inputStream().toList()
    val numberOfLines = input.size
    val lineLength = input[0].length
    val elevationMap = Array(numberOfLines) { input[it].map(Char::digitToInt).toIntArray() }
    val trailheadPoints = buildTrailheadPoints(elevationMap, lineLength, numberOfLines)

    return trailheadPoints.sumOf { trailheadPointScoreAndRating(it, elevationMap).score }
}

private fun part2(): Int {
    val input = inputStream().toList()
    val numberOfLines = input.size
    val lineLength = input[0].length
    val elevationMap = Array(numberOfLines) { input[it].map(Char::digitToInt).toIntArray() }
    val trailheadPoints = buildTrailheadPoints(elevationMap, lineLength, numberOfLines)

    return trailheadPoints.sumOf { trailheadPointScoreAndRating(it, elevationMap).rating }
}

private fun trailheadPointScoreAndRating(
    trailheadPoint: Point,
    elevationMap: Array<IntArray>,
): TrailheadValue {
    val directions = listOf(Point(1, 0), Point(0, 1), Point(-1, 0), Point(0, -1))
    val height = elevationMap.size
    val width = elevationMap[0].size
    val paths = mutableSetOf<List<Point>>()

    fun visitAllIncreasingNodes(
        currentPoint: Point,
        currentElevation: Int = 0,
        currentPath: List<Point> = emptyList(),
    ) {
        if (currentElevation == 9) {
            paths.add(currentPath)
            return
        } else {
            directions.forEach { direction ->
                val nextPoint = Point(currentPoint.x + direction.x, currentPoint.y + direction.y)
                if (nextPoint.inBounds(width, height)) {
                    val nextElevation = elevationMap[nextPoint.y][nextPoint.x]
                    val targetElevation = elevationMap[currentPoint.y][currentPoint.x] + 1
                    if (nextElevation == targetElevation) {
                        visitAllIncreasingNodes(
                            nextPoint,
                            currentElevation + 1,
                            currentPath.plus(nextPoint),
                        )
                    }
                }
            }
        }
    }

    visitAllIncreasingNodes(trailheadPoint)

    val score = paths.map { path -> path.last() }.toSet().size
    val rating = paths.size
    return TrailheadValue(score, rating)
}

private fun buildTrailheadPoints(elevationMap: Array<IntArray>, width: Int, height: Int) =
    buildList {
        for (y in 0..<height) {
            for (x in 0..<width) {
                if (elevationMap[y][x] == 0) {
                    add(Point(x, y))
                }
            }
        }
    }
