package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/06/test.txt")

private fun inputStream() = resourceLineStream("/06/input.txt")

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
}

data class Point(val x: Int, val y: Int)

data class PointDirection(val point: Point, val direction: Direction)

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
    return visitedPoints(initialPosition, Direction.UP, lab).count {
        simulatedObstacleCausesLoop(it, initialPosition, Direction.UP, lab)
    }
}

private fun nextPosition(point: Point, direction: Direction): Point =
    when (direction) {
        Direction.UP -> Point(point.x, point.y - 1)
        Direction.DOWN -> Point(point.x, point.y + 1)
        Direction.LEFT -> Point(point.x - 1, point.y)
        Direction.RIGHT -> Point(point.x + 1, point.y)
    }

private fun turn90Degrees(direction: Direction): Direction =
    when (direction) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
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
            direction = turn90Degrees(direction)
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
    val visited = mutableSetOf<PointDirection>(PointDirection(currentPosition, direction))

    while (true) {
        val next = nextPosition(currentPosition, direction)
        if (visited.contains(PointDirection(next, direction))) {
            // Loop detected
            return true
        } else if (next.x !in 0..<lineLength || next.y !in 0..<numberOfLines) {
            break
        } else if (lab[next.y][next.x] == '#' || next == simulatedObstaclePosition) {
            // Obstacle or simulated obstacle
            direction = turn90Degrees(direction)
        } else {
            visited.add(PointDirection(currentPosition, direction))
            currentPosition = next
        }
    }
    // Escaped
    return false
}
