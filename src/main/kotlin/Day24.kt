package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private val BOOLEAN_EQUATION_REGEX =
    """([a-z0-9]{3}) (AND|OR|XOR) ([a-z0-9]{3}) -> ([a-z0-9]{3})""".toRegex()

enum class BooleanOp {
    AND,
    OR,
    XOR,
}

data class BoolEquation(
    val leftVar: String,
    val op: BooleanOp,
    val rightVar: String,
    val resultVar: String,
) {
    fun eval(state: Map<String, Boolean>): Boolean {
        val left = state[leftVar] ?: error("Variable $leftVar not found")
        val right = state[rightVar] ?: error("Variable $rightVar not found")
        return when (op) {
            BooleanOp.AND -> left && right
            BooleanOp.OR -> left || right
            BooleanOp.XOR -> left xor right
        }
    }

    val isZOutput: Boolean
        get() = resultVar.startsWith('z')

    val isXYEquation: Boolean
        get() =
            (leftVar.startsWith('x') && rightVar.startsWith('y')) ||
                (leftVar.startsWith('y') && rightVar.startsWith('x'))
}

private fun testStream() = resourceLineStream("/24/test.txt")

private fun inputStream() = resourceLineStream("/24/input.txt")

private fun part1(): Long {
    val input = inputStream().toList()
    val initialStateLines = input.takeWhile { it.isNotBlank() }
    val initialState = buildInitialState(initialStateLines)
    val equationLines = input.drop(initialStateLines.size + 1)
    val equations = buildEquations(equationLines)

    val dependencies =
        equations.associate { equation ->
            equation to
                equations.filter {
                    it.leftVar == equation.resultVar || it.rightVar == equation.resultVar
                }
        }
    val sortedEquations = topologicalSort(dependencies)

    val endState =
        sortedEquations.fold(initialState) { state, equation ->
            state + (equation.resultVar to equation.eval(state))
        }
    return zValue(endState)
}

private fun part2(): String {
    val input = inputStream().toList()
    val initialStateLines = input.takeWhile { it.isNotBlank() }
    val initialState = buildInitialState(initialStateLines)
    val equationLines = input.drop(initialStateLines.size + 1)
    val equations = buildEquations(equationLines)

    val dependencies =
        equations.associate { equation ->
            equation to
                equations.filter {
                    it.resultVar == equation.leftVar || it.resultVar == equation.rightVar
                }
        }

    val numberOfInputBits = initialState.keys.count { it.startsWith('x') }

    val brokenResults = mutableSetOf<String>()
    for (equation in equations) {
        // Z are the output bits which are S bits of full adders
        // S bits of full adders must come from XOR except for most-significant bit
        if (
            equation.op != BooleanOp.XOR &&
                equation.isZOutput &&
                equation.resultVar != "z$numberOfInputBits"
        ) {
            brokenResults.add(equation.resultVar)
        }

        // XOR gates that have both an X and Y input should not output to Z except for
        // least-significant bit since there is no carry in for the least-significant bit
        if (
            equation.op == BooleanOp.XOR &&
                equation.isZOutput &&
                equation.isXYEquation &&
                equation.resultVar != "z00"
        ) {
            brokenResults.add(equation.resultVar)
        }

        // XOR gates that output things other than Z should have inputs from both X and Y
        if (equation.op == BooleanOp.XOR && !equation.isZOutput && !equation.isXYEquation) {
            brokenResults.add(equation.resultVar)
        }

        // AND gates that have both an X and Y input should not output to Z
        if (equation.op == BooleanOp.AND && equation.isZOutput && equation.isXYEquation) {
            brokenResults.add(equation.resultVar)
        }

        // OR gates should not have any inputs from X or Y
        if (
            equation.op == BooleanOp.OR &&
                (equation.leftVar.startsWith('x') ||
                    equation.leftVar.startsWith('y') ||
                    equation.rightVar.startsWith('x') ||
                    equation.rightVar.startsWith('y'))
        ) {
            brokenResults.add(equation.resultVar)
        }

        // Inputs to OR gates must always be outputs from two AND gates except for last bit
        if (equation.op == BooleanOp.OR && equation.resultVar != "z$numberOfInputBits") {
            val equations = dependencies.getValue(equation)
            if (equations.size != 2 || equations.any { it.op != BooleanOp.AND }) {
                brokenResults.addAll(
                    equations.filter { it.op != BooleanOp.AND }.map(BoolEquation::resultVar)
                )
            }
        }

        // Inputs to AND gates must always be outputs from an OR gate and an XOR gate if it's not an
        // XY equation except when the input comes from the least-significant bit since there is no
        // carry in for the least-significant bit
        if (equation.op == BooleanOp.AND && !equation.isXYEquation) {
            val equations = dependencies.getValue(equation)
            if (equations.size != 2 || equations.any { it.op == BooleanOp.AND }) {
                brokenResults.addAll(
                    equations
                        .filter { equation ->
                            val isAnd = equation.op == BooleanOp.AND
                            val isLSBXY =
                                (equation.leftVar == "x00" && equation.rightVar == "y00") ||
                                    (equation.leftVar == "y00" && equation.rightVar == "x00")
                            isAnd && !isLSBXY
                        }
                        .map(BoolEquation::resultVar)
                )
            }
        }
    }

    return brokenResults.sorted().joinToString(",")
}

private fun buildInitialState(lines: List<String>): Map<String, Boolean> =
    lines.associate { line ->
        val (varName, value) = line.split(": ")
        varName to (value == "1")
    }

private fun buildEquations(lines: List<String>): List<BoolEquation> =
    lines.map { line ->
        val match = BOOLEAN_EQUATION_REGEX.matchEntire(line) ?: error("Invalid equation: $line")
        val (leftVar, op, rightVar, resultVar) = match.destructured
        BoolEquation(leftVar, BooleanOp.valueOf(op), rightVar, resultVar)
    }

private fun zValue(state: Map<String, Boolean>): Long {
    val zVariables = state.filterKeys { it.startsWith('z') }.entries.sortedBy { it.key }
    return zVariables.mapIndexed { index, entry -> if (entry.value) 1L shl index else 0L }.sum()
}

fun <T> topologicalSort(graph: Map<T, List<T>>): List<T> {
    val incomingEdges = mutableMapOf<T, Int>()
    for ((vertex, successors) in graph) {
        if (vertex !in incomingEdges) {
            incomingEdges[vertex] = 0
        }
        for (successor in successors) {
            incomingEdges[successor] = incomingEdges.getOrDefault(successor, 0) + 1
        }
    }

    val queue = ArrayDeque<T>()
    for ((vertex, edges) in incomingEdges) {
        if (edges == 0) {
            queue += vertex
        }
    }

    val result = mutableListOf<T>()

    while (queue.isNotEmpty()) {
        val vertex = queue.removeFirst()
        result += vertex

        for (successor in graph[vertex].orEmpty()) {
            incomingEdges[successor] = incomingEdges.getOrDefault(successor, 0) - 1
            if (incomingEdges[successor] == 0) {
                queue += successor
            }
        }
    }

    if (result.size != incomingEdges.size) {
        error("Graph contains a cycle, topological sort not possible!")
    }

    return result
}
