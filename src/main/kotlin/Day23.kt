package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

data class PC(val first: Char, val second: Char) {
    fun toDisplayString(): String = "$first$second"
}

private fun testStream() = resourceLineStream("/23/test.txt")

private fun inputStream() = resourceLineStream("/23/input.txt")

private fun part1(): Int {
    val input = inputStream().toList()
    val pcsStartingWithT = mutableSetOf<PC>()
    val connections =
        input
            .map { line ->
                val (first, second) = line.split("-")
                val firstPc = PC(first[0], first[1])
                val secondPc = PC(second[0], second[1])
                if (firstPc.first == 't') {
                    pcsStartingWithT.add(firstPc)
                }
                if (secondPc.first == 't') {
                    pcsStartingWithT.add(secondPc)
                }

                setOf(firstPc, secondPc)
            }
            .toSet()

    return pcsStartingWithT
        .flatMap { pc ->
            val connectionsForPc = connections.filter { connection -> connection.contains(pc) }
            val otherPcs = connectionsForPc.flatMap { connection -> connection - pc }

            otherPcs.flatMap { other ->
                val possibleThirdPcs =
                    connections
                        .filter { connection -> connection.contains(other) }
                        .flatMap { connection -> connection - other }
                val thirdPcs =
                    possibleThirdPcs.filter { third -> connections.contains(setOf(pc, third)) }
                thirdPcs.map { third -> setOf(pc, other, third) }
            }
        }
        .toSet()
        .size
}

private fun part2(): String {
    val input = inputStream().toList()
    val computers = mutableSetOf<PC>()
    val connections =
        input
            .map { line ->
                val (first, second) = line.split("-")
                val firstPc = PC(first[0], first[1])
                val secondPc = PC(second[0], second[1])
                computers.add(firstPc)
                computers.add(secondPc)

                setOf(firstPc, secondPc)
            }
            .toSet()
    val computerConnections = buildMap {
        computers.forEach { computer ->
            val connectionsForComputer =
                connections.filter { connection -> connection.contains(computer) }
            val otherComputers =
                connectionsForComputer.flatMap { connection -> connection - computer }.toSet()
            put(computer, otherComputers)
        }
    }

    return computerConnections
        .map { (computer, connectedComputers) ->
            connectedComputers
                .map { connectedComputer ->
                    theThing(
                        listOf(connectedComputer),
                        (connectedComputers - connectedComputer).toList(),
                        connections,
                    )
                }
                .maxBy { it.size }
                .plus(computer)
        }
        .maxBy { it.size }
        .map(PC::toDisplayString)
        .sorted()
        .joinToString(",")
}

private fun theThing(accum: List<PC>, remaining: List<PC>, connections: Set<Set<PC>>): List<PC> {
    if (remaining.isEmpty()) {
        return accum
    } else {
        val current = remaining.first()
        val allSoFarConnected = accum.all { pc -> connections.contains(setOf(pc, current)) }
        return if (allSoFarConnected) {
            theThing(accum.plus(current), remaining.drop(1), connections)
        } else {
            accum
        }
    }
}
