package io.github.houli

import java.util.PriorityQueue

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private enum class Day18Tile {
    CORRUPTED,
    EMPTY,
}

private fun testStream() = resourceLineStream("/18/test.txt")

private fun inputStream() = resourceLineStream("/18/input.txt")

private fun part1(): Int {
    val input = inputStream().toList()
    val corruptedTilesSubset = corruptedTiles(input).take(1024).toSet()
    val size = 71
    val grid =
        Array(size) { y ->
            Array(size) { x ->
                if (Point(x, y) in corruptedTilesSubset) Day18Tile.CORRUPTED else Day18Tile.EMPTY
            }
        }

    val graph = buildGraph(grid)
    val initialPosition = Point(0, 0)
    val endPosition = Point(size - 1, size - 1)

    return dijkstra(graph, initialPosition, endPosition)
}

private fun part2(): Point {
    val input = inputStream().toList()
    val allCorruptedTiles = corruptedTiles(input)
    val size = 71
    for (i in 1..allCorruptedTiles.size) {
        val corruptedTilesSubset = allCorruptedTiles.take(i).toSet()
        val grid =
            Array(size) { y ->
                Array(size) { x ->
                    if (Point(x, y) in corruptedTilesSubset) Day18Tile.CORRUPTED
                    else Day18Tile.EMPTY
                }
            }

        val graph = buildGraph(grid)
        val initialPosition = Point(0, 0)
        val endPosition = Point(size - 1, size - 1)

        val cost = dijkstra(graph, initialPosition, endPosition)
        if (cost == Int.MAX_VALUE) {
            return allCorruptedTiles[i - 1]
        }
    }

    // No max cost route found
    return Point(-1, -1)
}

private fun dijkstra(
    graph: Map<Point, Set<Pair<Int, Point>>>,
    initialPosition: Point,
    goalPosition: Point,
): Int {
    val costs = mutableMapOf<Point, Int>(initialPosition to 0).withDefault { Int.MAX_VALUE }
    val priorityQueue =
        PriorityQueue<Pair<Int, Point>>(compareBy { it.first }).apply { add(0 to initialPosition) }
    val visited = mutableSetOf<Pair<Int, Point>>()

    while (priorityQueue.isNotEmpty()) {
        val (currentCost, currentPoint) = priorityQueue.poll()
        val added = visited.add(currentCost to currentPoint)
        if (added) {
            graph[currentPoint]?.forEach { (costOfMove, newPoint) ->
                val newCost = currentCost + costOfMove
                val bestCost = costs.getValue(newPoint)
                if (newCost < bestCost) {
                    costs[newPoint] = newCost
                    priorityQueue.add(newCost to newPoint)
                }
            }
        }
    }

    return costs.getValue(goalPosition)
}

private fun corruptedTiles(input: List<String>): List<Point> =
    input.map { line ->
        val (x, y) = line.split(",").map(String::toInt)
        Point(x, y)
    }

private fun buildGraph(grid: Array<Array<Day18Tile>>): Map<Point, Set<Pair<Int, Point>>> =
    buildMap {
        grid.forEachIndexed { y, row ->
            row.forEachIndexed { x, tile ->
                if (tile == Day18Tile.EMPTY) {
                    val point = Point(x, y)
                    val adjacentNodeCosts =
                        Direction.entries
                            .mapNotNull { direction ->
                                val newPoint = point + direction.toVector()
                                if (
                                    newPoint.inBounds(grid[0].size, grid.size) &&
                                        grid[newPoint.y][newPoint.x] == Day18Tile.EMPTY
                                ) {
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
