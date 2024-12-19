package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/19/test.txt")

private fun inputStream() = resourceLineStream("/19/input.txt")

private fun part1(): Int {
    val input = inputStream().toList()
    val towels = input[0].split(", ")
    val desiredDesigns = input.drop(2)
    return desiredDesigns.count { design -> countsForDesign(design, towels) > 0 }
}

private fun part2(): Long {
    val input = inputStream().toList()
    val towels = input[0].split(", ")
    val desiredDesigns = input.drop(2)
    return desiredDesigns.sumOf { design -> countsForDesign(design, towels) }
}

private fun countsForDesign(design: String, towels: List<String>): Long {
    val counts = mutableMapOf<String, Long>()
    fun countsForDesignInternal(design: String): Long {
        if (design.isEmpty()) {
            return 1
        }

        val existing = counts[design]
        if (existing != null) {
            return existing
        }

        for (towel in towels) {
            if (design.startsWith(towel)) {
                val remainingDesign = design.substring(towel.length)
                val count = countsForDesignInternal(remainingDesign)
                counts.merge(design, count, Long::plus)
            }
        }
        return counts[design] ?: 0
    }

    return countsForDesignInternal(design)
}
