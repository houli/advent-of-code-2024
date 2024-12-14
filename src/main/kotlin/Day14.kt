package io.github.houli

import java.awt.image.BufferedImage
import java.io.File
import java.util.stream.Stream
import javax.imageio.ImageIO

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

data class Robot(val position: Point, val velocity: Point) {
    fun wrappingMove(width: Int, height: Int): Robot {
        val newPosition =
            Point((position.x + velocity.x).mod(width), (position.y + velocity.y).mod(height))
        return Robot(newPosition, velocity)
    }
}

private fun testStream() = resourceLineStream("/14/test.txt")

private fun inputStream() = resourceLineStream("/14/input.txt")

private fun part1(): Int {
    return doTheThing(101, 103, inputStream())
}

private fun part2(): Int {
    return doTheThing(101, 103, inputStream(), true)
}

private fun doTheThing(
    width: Int,
    height: Int,
    input: Stream<String>,
    part2: Boolean = false,
): Int {
    var robots = parseRobots(input.toList())
    val iterationsToDo = if (part2) 6446 else 100
    repeat(iterationsToDo) {
        robots = robots.map { it.wrappingMove(width, height) }
        if (part2 && (it + 1) == iterationsToDo) {
            val image = createRobotImage(robots, width, height)
            ImageIO.write(image, "png", File("imageoutput/${it+1}.png"))
            return iterationsToDo
        }
    }

    val illegalX = width / 2
    val illegalY = height / 2

    val topLeft = robots.filter { it.position.x < illegalX && it.position.y < illegalY }.size
    val topRight = robots.filter { it.position.x > illegalX && it.position.y < illegalY }.size
    val bottomLeft = robots.filter { it.position.x < illegalX && it.position.y > illegalY }.size
    val bottomRight = robots.filter { it.position.x > illegalX && it.position.y > illegalY }.size

    return topLeft * topRight * bottomLeft * bottomRight
}

private fun createRobotImage(robots: List<Robot>, width: Int, height: Int): BufferedImage {
    val image = BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)
    val robotColor = 0xFFFFFF
    robots.forEach { robot -> image.setRGB(robot.position.x, robot.position.y, robotColor) }
    return image
}

private fun parseRobots(input: List<String>): List<Robot> {
    val robotRegex = """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".toRegex()
    return input.map { line ->
        val (x, y, vx, vy) = robotRegex.find(line)!!.destructured
        Robot(Point(x.toInt(), y.toInt()), Point(vx.toInt(), vy.toInt()))
    }
}
