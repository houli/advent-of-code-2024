package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/18/test.txt")

private fun inputStream() = resourceLineStream("/18/input.txt")

private fun part1(): Int {
    val input = inputStream().toList()
    val corruptedTilesSubset = corruptedTiles(input).take(1024).toSet()
    val size = 71

    val graph = buildGraph(corruptedTilesSubset, size, size)
    val initialPosition = Point(0, 0)
    val endPosition = Point(size - 1, size - 1)

    return dijkstra(graph, initialPosition, endPosition)
}

private fun part2(): Point {
    val input = inputStream().toList()
    val allCorruptedTiles = corruptedTiles(input)
    val size = 71

    val initialPosition = Point(0, 0)
    val endPosition = Point(size - 1, size - 1)

    var max = allCorruptedTiles.size - 1
    var min = 0
    while (min <= max) {
        val mid = (min + max) / 2
        val corruptedTilesSubset = allCorruptedTiles.take(mid + 1).toSet()
        val graph = buildGraph(corruptedTilesSubset, size, size)

        val cost = dijkstra(graph, initialPosition, endPosition)
        if (cost == Int.MAX_VALUE) {
            max = mid - 1
        } else {
            min = mid + 1
        }
    }
    return allCorruptedTiles[min]
}

private fun corruptedTiles(input: List<String>): List<Point> =
    input.map { line ->
        val (x, y) = line.split(",").map(String::toInt)
        Point(x, y)
    }

private fun buildGraph(
    corruptedTiles: Set<Point>,
    width: Int,
    height: Int,
): Map<Point, Set<Pair<Int, Point>>> = buildMap {
    for (x in 0..<width) {
        for (y in 0..<height) {
            val point = Point(x, y)
            if (point !in corruptedTiles) {
                val adjacentNodeCosts =
                    Direction.entries
                        .mapNotNull { direction ->
                            val newPoint = point + direction.toVector()
                            if (newPoint.inBounds(width, height) && newPoint !in corruptedTiles) {
                                1 to newPoint
                            } else {
                                null
                            }
                        }
                        .toSet()
                put(point, adjacentNodeCosts)
            }
        }
    }
}
