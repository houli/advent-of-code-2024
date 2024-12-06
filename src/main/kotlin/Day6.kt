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
    val lineLength = lines[0].length

    val chars = Array(numberOfLines) { lines[it].toCharArray() }

    val initialY = chars.indexOfFirst { it.contains('^') }
    val initialX = chars[initialY].indexOf('^')
    var currentPosition = Point(initialX, initialY)

    var direction = Direction.UP
    val visited = mutableSetOf<Point>(currentPosition)

    while (true) {
        val next = nextPosition(currentPosition, direction)
        if (next.x !in 0..<lineLength || next.y !in 0..<numberOfLines) {
            break
        } else if (chars[next.y][next.x] == '#') {
            direction = turn90Degrees(direction)
        } else {
            visited.add(currentPosition)
            currentPosition = next
        }
    }

    return visited.size
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

private fun part2(): Int {
    val lines = inputStream().toList()
    val numberOfLines = lines.size
    val lineLength = lines[0].length

    val chars = Array(numberOfLines) { lines[it].toCharArray() }

    val initialY = chars.indexOfFirst { it.contains('^') }
    val initialX = chars[initialY].indexOf('^')

    var loopCausingObstaclesCount = 0
    for (i in 0..<chars.size) {
        for (j in 0..<chars[i].size) {
            if (chars[i][j] == '#') {
                // Ignore existing obstacles
                continue
            }

            if (i == initialY && j == initialX) {
                // Can't place obstacle at initial position
                continue
            }

            var direction = Direction.UP
            var currentPosition = Point(initialX, initialY)
            val visited = mutableSetOf<PointDirection>(PointDirection(currentPosition, direction))
            var escaped = false

            while (true) {
                val next = nextPosition(currentPosition, direction)
                if (visited.contains(PointDirection(next, direction))) {
                    loopCausingObstaclesCount++
                    break
                } else if (next.x !in 0..<lineLength || next.y !in 0..<numberOfLines) {
                    escaped = true
                    break
                } else if (chars[next.y][next.x] == '#' || (next.y == i && next.x == j)) {
                    // Obstacle or simulated obstacle
                    direction = turn90Degrees(direction)
                } else {
                    visited.add(PointDirection(currentPosition, direction))
                    currentPosition = next
                }
            }
        }
    }

    return loopCausingObstaclesCount
}
