package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/20/test.txt")

private fun inputStream() = resourceLineStream("/20/input.txt")

private fun part1(): Int {
    return commonSolution(inputStream().toList(), 2)
}

private fun part2(): Int {
    return commonSolution(inputStream().toList(), 20)
}

private fun commonSolution(input: List<String>, cheatCostMax: Int, minimumSaving: Int = 100): Int {
    val width = input[0].length
    val height = input.size
    val walls = findWalls(input)

    val initialY = input.indexOfFirst { it.contains('S') }
    val initialX = input[initialY].indexOf('S')
    val initialPosition = Point(initialX, initialY)

    val goalY = input.indexOfFirst { it.contains('E') }
    val goalX = input[goalY].indexOf('E')
    val goalPosition = Point(goalX, goalY)

    val path = findPath(walls, width, height, initialPosition, goalPosition)

    val amountToDrop = minimumSaving + 1
    val possibleCheatStarts = path.dropLast(amountToDrop)
    return possibleCheatStarts
        .mapIndexed { i, cheatStart ->
            val cheatEndStart = i + amountToDrop
            val possibleCheatEnds = path.drop(cheatEndStart)

            possibleCheatEnds
                .filterIndexed { j, cheatEnd ->
                    val cheatCost = cheatStart.manhattanDistance(cheatEnd)
                    if (cheatCost <= cheatCostMax) {
                        val realIndex = cheatEndStart + j
                        val cost = cheatCost + i + (path.size - realIndex)
                        val saving = path.size - cost
                        saving >= minimumSaving
                    } else {
                        false
                    }
                }
                .count()
        }
        .sum()
}

private fun findPath(
    walls: Set<Point>,
    width: Int,
    height: Int,
    start: Point,
    end: Point,
): List<Point> {
    val path = mutableListOf<Point>(start)
    var current = start
    var previous = Point(-1, -1)

    while (current != end) {
        val next =
            Direction.entries.firstNotNullOf { direction ->
                val point = current + direction.toVector()
                if (point !in walls && point.inBounds(width, height) && point != previous) {
                    point
                } else {
                    null
                }
            }
        path.add(next)
        previous = current
        current = next
    }

    return path
}

private fun findWalls(input: List<String>): Set<Point> =
    input
        .flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, char ->
                if (char == '#') {
                    Point(x, y)
                } else {
                    null
                }
            }
        }
        .toSet()
