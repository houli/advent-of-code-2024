package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/08/test.txt")

private fun inputStream() = resourceLineStream("/08/input.txt")

private fun part1(): Int {
    val lines = inputStream().toList()
    val numberOfLines = lines.size
    val lineLength = lines[0].length
    val appearances = buildAppearanceMap(lines)

    val antinodes =
        appearances
            .flatMap { (_, points) ->
                generate2Combinations(points).flatMap { (a, b) ->
                    val distanceFirstSecond = b.x - a.x to b.y - a.y
                    val firstAntinode =
                        Point(a.x - distanceFirstSecond.first, a.y - distanceFirstSecond.second)
                    val secondAntinode =
                        Point(b.x + distanceFirstSecond.first, b.y + distanceFirstSecond.second)
                    listOf(firstAntinode, secondAntinode).filter {
                        pointInBounds(it, lineLength, numberOfLines)
                    }
                }
            }
            .toSet()

    return antinodes.size
}

private fun part2(): Int {
    val lines = inputStream().toList()
    val numberOfLines = lines.size
    val lineLength = lines[0].length
    val appearances = buildAppearanceMap(lines)

    val antinodes =
        appearances
            .flatMap { (_, points) ->
                generate2Combinations(points).flatMap { (a, b) ->
                    val distanceFirstSecond = b.x - a.x to b.y - a.y
                    val firstAntinodes =
                        generateSequence(a) { point ->
                                Point(
                                    point.x - distanceFirstSecond.first,
                                    point.y - distanceFirstSecond.second,
                                )
                            }
                            .takeWhile { pointInBounds(it, lineLength, numberOfLines) }

                    val secondAntinodes =
                        generateSequence(b) { point ->
                                Point(
                                    point.x + distanceFirstSecond.first,
                                    point.y + distanceFirstSecond.second,
                                )
                            }
                            .takeWhile { pointInBounds(it, lineLength, numberOfLines) }

                    firstAntinodes + secondAntinodes
                }
            }
            .toSet()

    return antinodes.size
}

private fun buildAppearanceMap(lines: List<String>): Map<Char, List<Point>> = buildMap {
    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            if (char != '.') {
                val point = Point(x, y)
                merge(char, listOf(point)) { current, _ -> current + point }
            }
        }
    }
}

private fun pointInBounds(point: Point, width: Int, height: Int): Boolean =
    point.x in 0..<width && point.y in 0..<height

private fun <T> generate2Combinations(list: List<T>): List<List<T>> =
    list.indices.flatMap { i -> (i + 1 until list.size).map { j -> listOf(list[i], list[j]) } }
