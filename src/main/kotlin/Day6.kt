package io.github.houli

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/06/test.txt")

private fun inputStream() = resourceLineStream("/06/input.txt")

private fun part1(): Int {
    val lines = inputStream().toList()
    val numberOfLines = lines.size
    val lab = Array(numberOfLines) { lines[it].toCharArray() }

    val initialY = lab.indexOfFirst { it.contains('^') }
    val initialX = lab[initialY].indexOf('^')
    val initialPosition = Point(initialX, initialY)

    return visitedPoints(initialPosition, Direction.UP, lab).size
}

private fun part2(): Int {
    val lines = inputStream().toList()
    val numberOfLines = lines.size
    val lab = Array(numberOfLines) { lines[it].toCharArray() }

    val initialY = lab.indexOfFirst { it.contains('^') }
    val initialX = lab[initialY].indexOf('^')
    val initialPosition = Point(initialX, initialY)

    // See which points are visited on a regular traversal and use those as potential obstacles
    return runBlocking(Dispatchers.Default) {
        visitedPoints(initialPosition, Direction.UP, lab)
            .map { async { simulatedObstacleCausesLoop(it, initialPosition, Direction.UP, lab) } }
            .awaitAll()
            .count { it }
    }
}

private fun nextPosition(point: Point, direction: Direction): Point =
    when (direction) {
        Direction.UP -> Point(point.x, point.y - 1)
        Direction.DOWN -> Point(point.x, point.y + 1)
        Direction.LEFT -> Point(point.x - 1, point.y)
        Direction.RIGHT -> Point(point.x + 1, point.y)
    }

private fun visitedPoints(
    initialPosition: Point,
    initialDirection: Direction,
    lab: Array<CharArray>,
): Set<Point> {
    val numberOfLines = lab.size
    val lineLength = lab[0].size

    var currentPosition = initialPosition
    var direction = initialDirection
    val visited = mutableSetOf<Point>(currentPosition)

    while (true) {
        val next = nextPosition(currentPosition, direction)
        if (next.x !in 0..<lineLength || next.y !in 0..<numberOfLines) {
            visited.add(currentPosition)
            break
        } else if (lab[next.y][next.x] == '#') {
            direction = direction.turn90DegreesClockwise()
        } else {
            visited.add(currentPosition)
            currentPosition = next
        }
    }

    return visited
}

private fun simulatedObstacleCausesLoop(
    simulatedObstaclePosition: Point,
    initialPosition: Point,
    initialDirection: Direction,
    lab: Array<CharArray>,
): Boolean {
    if (lab[simulatedObstaclePosition.y][simulatedObstaclePosition.x] == '#') {
        // Ignore existing obstacles
        return false
    }

    if (simulatedObstaclePosition == initialPosition) {
        // Can't place obstacle at initial position
        return false
    }

    val numberOfLines = lab.size
    val lineLength = lab[0].size

    var direction = initialDirection
    var currentPosition = initialPosition
    val turnPointDirections =
        mutableSetOf<PointDirection>(PointDirection(currentPosition, direction))

    while (true) {
        val next = nextPosition(currentPosition, direction)
        if (turnPointDirections.contains(PointDirection(next, direction))) {
            // Loop detected
            return true
        } else if (next.x !in 0..<lineLength || next.y !in 0..<numberOfLines) {
            break
        } else if (lab[next.y][next.x] == '#' || next == simulatedObstaclePosition) {
            // Obstacle or simulated obstacle
            turnPointDirections.add(PointDirection(next, direction))
            direction = direction.turn90DegreesClockwise()
        } else {
            currentPosition = next
        }
    }
    // Escaped
    return false
}
