package io.github.houli

import java.util.PriorityQueue

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private enum class MazeTile {
    WALL,
    EMPTY;

    companion object {
        fun fromChar(c: Char): MazeTile =
            when (c) {
                '.',
                'E',
                'S' -> EMPTY
                '#' -> WALL
                else -> throw IllegalArgumentException("Invalid tile: $c")
            }
    }
}

private fun testStream() = resourceLineStream("/16/test.txt")

private fun inputStream() = resourceLineStream("/16/input.txt")

private fun part1(): Int {
    val input = inputStream().toList()
    val maze = input.map { line -> line.map(MazeTile::fromChar) }

    val initialY = input.indexOfFirst { it.contains('S') }
    val initialX = input[initialY].indexOf('S')
    val initialPosition = Point(initialX, initialY)

    val goalY = input.indexOfFirst { it.contains('E') }
    val goalX = input[goalY].indexOf('E')
    val goalPosition = Point(goalX, goalY)

    val graph = buildGraph(maze)

    val (cost, _) =
        dijkstraModified(graph, PointDirection(initialPosition, Direction.RIGHT), goalPosition)
    return cost
}

private fun part2(): Int {
    val input = inputStream().toList()
    val maze = input.map { line -> line.map(MazeTile::fromChar) }

    val initialY = input.indexOfFirst { it.contains('S') }
    val initialX = input[initialY].indexOf('S')
    val initialPosition = Point(initialX, initialY)

    val goalY = input.indexOfFirst { it.contains('E') }
    val goalX = input[goalY].indexOf('E')
    val goalPosition = Point(goalX, goalY)

    val graph = buildGraph(maze)

    val (_, pointsVisitedOnBestPaths) =
        dijkstraModified(graph, PointDirection(initialPosition, Direction.RIGHT), goalPosition)
    return pointsVisitedOnBestPaths.size
}

private fun dijkstraModified(
    graph: Map<PointDirection, Set<Pair<Int, PointDirection>>>,
    initialPosition: PointDirection,
    goalPosition: Point,
): Pair<Int, Set<Point>> {
    val costs =
        mutableMapOf<PointDirection, Int>(initialPosition to 0).withDefault { Int.MAX_VALUE }
    val priorityQueue =
        PriorityQueue<Pair<Int, PointDirection>>(compareBy { it.first }).apply {
            add(0 to initialPosition)
        }
    val visited = mutableSetOf<Pair<Int, PointDirection>>()
    val allPointsVisitedToReachPointDirectionWithBestScore =
        mutableMapOf<PointDirection, Set<Point>>()

    while (priorityQueue.isNotEmpty()) {
        val (currentCost, currentPointDirection) = priorityQueue.poll()
        val added = visited.add(currentCost to currentPointDirection)
        if (added) {
            graph[currentPointDirection]?.forEach { (costOfMove, newPointDirection) ->
                val newCost = currentCost + costOfMove
                val bestCost = costs.getValue(newPointDirection)
                if (newCost <= bestCost) {
                    costs[newPointDirection] = newCost

                    // Find the superset of points needed to get to this point in this direction at
                    // best cost or less
                    val superSetOfPointsVisited =
                        allPointsVisitedToReachPointDirectionWithBestScore[currentPointDirection]
                            ?.plus(newPointDirection.point) ?: setOf(newPointDirection.point)
                    if (newCost == bestCost) {
                        val current =
                            allPointsVisitedToReachPointDirectionWithBestScore[newPointDirection]
                        allPointsVisitedToReachPointDirectionWithBestScore[newPointDirection] =
                            current?.plus(superSetOfPointsVisited) ?: superSetOfPointsVisited
                    } else {
                        allPointsVisitedToReachPointDirectionWithBestScore[newPointDirection] =
                            superSetOfPointsVisited
                    }

                    priorityQueue.add(newCost to newPointDirection)
                }
            }
        }
    }

    val bestEndDirection =
        Direction.entries.minBy { direction ->
            costs.getValue(PointDirection(goalPosition, direction))
        }
    val bestEndPointDirection = PointDirection(goalPosition, bestEndDirection)
    val pointsVisitedOnBestPaths =
        allPointsVisitedToReachPointDirectionWithBestScore[bestEndPointDirection]!!.plus(
            initialPosition.point
        )

    return costs.getValue(bestEndPointDirection) to pointsVisitedOnBestPaths
}

private fun buildGraph(
    maze: List<List<MazeTile>>
): Map<PointDirection, Set<Pair<Int, PointDirection>>> = buildMap {
    maze.forEachIndexed { y, row ->
        row.forEachIndexed { x, mazeTile ->
            if (mazeTile == MazeTile.EMPTY) {
                Direction.entries.forEach { currentDirection ->
                    val currentPoint = Point(x, y)
                    val currentPointDirection = PointDirection(currentPoint, currentDirection)
                    val adjacentNodeCosts =
                        Direction.entries
                            .mapNotNull { newDirection ->
                                val newPoint = currentPoint + newDirection.toVector()
                                if (
                                    newPoint.inBounds(maze[0].size, maze.size) &&
                                        maze[newPoint.y][newPoint.x] == MazeTile.EMPTY
                                ) {
                                    val costOfMove =
                                        (currentDirection.turnsRequired(newDirection) * 1000) + 1
                                    val newPointDirection = PointDirection(newPoint, newDirection)
                                    costOfMove to newPointDirection
                                } else {
                                    null
                                }
                            }
                            .toSet()
                    put(currentPointDirection, adjacentNodeCosts)
                }
            }
        }
    }
}
