package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

data class Region(val plant: Char, val points: MutableSet<Point>) {
    val area: Int
        get() = points.size

    val perimeter: Int
        get() =
            points.sumOf { point ->
                val others =
                    listOf(
                        Point(point.x, point.y + 1),
                        Point(point.x, point.y - 1),
                        Point(point.x + 1, point.y),
                        Point(point.x - 1, point.y),
                    )
                4 - others.count { points.contains(it) }
            }

    val numberOfSides: Int
        get() =
            points.sumOf { point ->
                val topLeft = Point(point.x - 1, point.y - 1)
                val top = Point(point.x, point.y - 1)
                val topRight = Point(point.x + 1, point.y - 1)
                val left = Point(point.x - 1, point.y)
                val right = Point(point.x + 1, point.y)
                val bottomLeft = Point(point.x - 1, point.y + 1)
                val bottom = Point(point.x, point.y + 1)
                val bottomRight = Point(point.x + 1, point.y + 1)

                val topLeftExternalCornerExists = !points.contains(top) && !points.contains(left)
                val bottomLeftExternalCornerExists =
                    !points.contains(left) && !points.contains(bottom)
                val topRightExternalCornerExists = !points.contains(top) && !points.contains(right)
                val bottomRightExternalCornerExists =
                    !points.contains(right) && !points.contains(bottom)
                val topLeftInternalCornerExists =
                    points.contains(left) && !points.contains(topLeft) && points.contains(top)
                val bottomLeftInternalCornerExists =
                    points.contains(left) && !points.contains(bottomLeft) && points.contains(bottom)
                val topRightInternalCornerExists =
                    points.contains(top) && !points.contains(topRight) && points.contains(right)
                val bottomRightInternalCornerExists =
                    points.contains(right) &&
                        !points.contains(bottomRight) &&
                        points.contains(bottom)

                listOf(
                        topLeftExternalCornerExists,
                        bottomLeftExternalCornerExists,
                        topRightExternalCornerExists,
                        bottomRightExternalCornerExists,
                        topLeftInternalCornerExists,
                        bottomLeftInternalCornerExists,
                        topRightInternalCornerExists,
                        bottomRightInternalCornerExists,
                    )
                    .count { it }
            }
}

private fun testStream() = resourceLineStream("/12/test.txt")

private fun inputStream() = resourceLineStream("/12/input.txt")

private fun part1(): Int {
    val input = inputStream().toList()
    val numberOfLines = input.size
    val garden = Array(numberOfLines) { input[it].toCharArray() }
    val regions = gardenRegipns(garden)
    return regions.sumOf { it.area * it.perimeter }
}

private fun part2(): Int {
    val input = inputStream().toList()
    val numberOfLines = input.size
    val garden = Array(numberOfLines) { input[it].toCharArray() }
    val regions = gardenRegipns(garden)
    return regions.sumOf { it.area * it.numberOfSides }
}

private fun gardenRegipns(garden: Array<CharArray>): List<Region> {
    val regions = mutableListOf<Region>()
    val visited = mutableSetOf<Point>()
    val directions = listOf(Point(0, 1), Point(0, -1), Point(1, 0), Point(-1, 0))

    fun dfs(point: Point, region: Region) {
        visited.add(point)
        for (direction in directions) {
            val newPoint = Point(point.x + direction.x, point.y + direction.y)
            if (!visited.contains(newPoint) && newPoint.inBounds(garden[0].size, garden.size)) {
                val plant = garden[newPoint.y][newPoint.x]
                if (plant == region.plant) {
                    region.points.add(newPoint)
                    dfs(newPoint, region)
                }
            }
        }
    }

    for (y in garden.indices) {
        for (x in garden[y].indices) {
            val plant = garden[y][x]
            val point = Point(x, y)
            if (!visited.contains(point)) {
                val region = Region(plant, mutableSetOf(point))
                dfs(point, region)
                regions.add(region)
            }
        }
    }
    return regions
}
