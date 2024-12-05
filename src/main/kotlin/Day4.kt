package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/04/test.txt")

private fun inputStream() = resourceLineStream("/04/input.txt")

private fun part1(): Int {
    val lines = inputStream().toList()
    val numberOfLines = lines.size
    val lineLength = lines[0].length
    val wordToMatch = "XMAS"

    val chars = Array(numberOfLines) { lines[it].toCharArray() }
    var count = 0

    for ((i, line) in chars.withIndex()) {
        for ((j, char) in line.withIndex()) {
            for (directionI in -1..1) {
                for (directionJ in -1..1) {
                    if (directionI == 0 && directionJ == 0) {
                        continue
                    }

                    for (k in 0..<wordToMatch.length) {
                        val nextI = i + directionI * k
                        val nextJ = j + directionJ * k

                        if (
                            nextI in 0..<numberOfLines &&
                                nextJ in 0..<lineLength &&
                                chars[nextI][nextJ] == wordToMatch[k]
                        ) {
                            if (k == wordToMatch.length - 1) {
                                count++
                            }
                        } else {
                            break
                        }
                    }
                }
            }
        }
    }
    return count
}

private fun part2(): Int {
    val lines = inputStream().toList()
    val numberOfLines = lines.size

    val chars = Array(numberOfLines) { lines[it].toCharArray() }
    val charsToMatch = setOf('S', 'M')

    var count = 0
    for ((i, line) in chars.withIndex()) {
        for ((j, char) in line.withIndex()) {
            if (char == 'A') {
                val topLeftChar = chars.getOrNull(i - 1)?.getOrNull(j - 1)
                val topRightChar = chars.getOrNull(i - 1)?.getOrNull(j + 1)
                val bottomLeftChar = chars.getOrNull(i + 1)?.getOrNull(j - 1)
                val bottomRightChar = chars.getOrNull(i + 1)?.getOrNull(j + 1)

                if (
                    setOf(topLeftChar, bottomRightChar) == charsToMatch &&
                        setOf(topRightChar, bottomLeftChar) == charsToMatch
                ) {
                    count++
                }
            }
        }
    }
    return count
}
