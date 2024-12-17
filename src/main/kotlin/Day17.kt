package io.github.houli

import kotlin.math.pow

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

sealed interface Instruction {
    data class Adv(val operand: Long) : Instruction

    data class Bxl(val operand: Long) : Instruction

    data class Bst(val operand: Long) : Instruction

    data class Jnz(val operand: Long) : Instruction

    data class Bxc(val operand: Long) : Instruction

    data class Out(val operand: Long) : Instruction

    data class Bdv(val operand: Long) : Instruction

    data class Cdv(val operand: Long) : Instruction
}

data class Registers(val a: Long, val b: Long, val c: Long)

class Computer(var registers: Registers, val instructions: List<Instruction>) {
    private var instructionPointer: Int = 0

    private val output: MutableList<Long> = mutableListOf<Long>()
    val halted: Boolean
        get() = instructionPointer >= instructions.size

    fun getOutput() = output.toList()

    fun step() {
        val currentInstruction = instructions[instructionPointer]

        val incrementInstructionPointer =
            when (currentInstruction) {
                is Instruction.Adv -> executeAdv(currentInstruction.operand)
                is Instruction.Bdv -> executeBdv(currentInstruction.operand)
                is Instruction.Bst -> executeBst(currentInstruction.operand)
                is Instruction.Bxc -> executeBxc()
                is Instruction.Bxl -> executeBxl(currentInstruction.operand)
                is Instruction.Cdv -> executeCdv(currentInstruction.operand)
                is Instruction.Jnz -> executeJnz(currentInstruction.operand)
                is Instruction.Out -> executeOut(currentInstruction.operand)
            }

        if (incrementInstructionPointer) {
            instructionPointer++
        }
    }

    private fun executeAdv(operand: Long): Boolean {
        val result = divInstructionImplementation(registers.a, operand)
        registers = registers.copy(a = result)
        return true
    }

    private fun executeBdv(operand: Long): Boolean {
        val result = divInstructionImplementation(registers.a, operand)
        registers = registers.copy(b = result)
        return true
    }

    private fun executeCdv(operand: Long): Boolean {
        val result = divInstructionImplementation(registers.a, operand)
        registers = registers.copy(c = result)
        return true
    }

    private fun executeBxl(operand: Long): Boolean {
        val result = xorInstructionImplementation(registers.b, operand)
        registers = registers.copy(b = result)
        return true
    }

    private fun executeBxc(): Boolean {
        val result = xorInstructionImplementation(registers.b, registers.c)
        registers = registers.copy(b = result)
        return true
    }

    private fun executeBst(operand: Long): Boolean {
        val result = comboOperandValue(operand) % 8
        registers = registers.copy(b = result)
        return true
    }

    private fun executeJnz(operand: Long): Boolean {
        if (registers.a != 0L) {
            instructionPointer = (operand / 2).toInt()
            return false
        }
        return true
    }

    private fun executeOut(operand: Long): Boolean {
        output.add(comboOperandValue(operand) % 8)
        return true
    }

    private fun xorInstructionImplementation(left: Long, right: Long): Long = left xor right

    private fun divInstructionImplementation(numerator: Long, operand: Long): Long {
        val denominator = divInstructionOperand(operand)
        return numerator / denominator
    }

    private fun divInstructionOperand(operand: Long): Long =
        2.0.pow(comboOperandValue(operand).toDouble()).toLong()

    private fun comboOperandValue(operand: Long): Long =
        when (operand) {
            0L,
            1L,
            2L,
            3L -> operand
            4L -> registers.a
            5L -> registers.b
            6L -> registers.c
            else -> throw IllegalArgumentException("Invalid operand: $operand")
        }
}

private fun testStream() = resourceLineStream("/17/test.txt")

private fun inputStream() = resourceLineStream("/17/input.txt")

private fun part1(): String {
    val input = inputStream().toList()
    val registerStrings = input.take(3)
    val instructionString = input.drop(4).joinToString("")
    val initialRegisters = parseInitialRegisters(registerStrings)
    val instructions = parseInstructions(instructionString)
    val computer = Computer(initialRegisters, instructions)

    while (!computer.halted) {
        computer.step()
    }

    return computer.getOutput().joinToString(",")
}

private fun part2(): Long {
    val input = inputStream().toList()
    val registerStrings = input.take(3)
    val instructionString = input.drop(4).joinToString("")
    val initialRegisters = parseInitialRegisters(registerStrings)
    val instructions = parseInstructions(instructionString)
    val desiredOutput = instructionString.substringAfter("Program: ").split(",").map(String::toLong)

    val endBits = listOf(0b000L, 0b001L, 0b010L, 0b011L, 0b100L, 0b101L, 0b110L, 0b111L)
    var toTest = listOf(0L)
    for (i in 1..16) {
        val expectedOutputForIteration = desiredOutput.takeLast(i)
        toTest =
            toTest.flatMap { test ->
                endBits.mapNotNull { bits ->
                    val number = (test shl 3) or bits
                    val computer = Computer(initialRegisters.copy(a = number), instructions)
                    while (!computer.halted) {
                        computer.step()
                    }

                    if (computer.getOutput() == expectedOutputForIteration) {
                        number
                    } else {
                        null
                    }
                }
            }
    }
    return toTest.first()
}

private fun parseInitialRegisters(input: List<String>): Registers {
    val registerRegex = """Register [ABC]: (\d+)""".toRegex()
    val (a, b, c) =
        input.map { line ->
            val (registerValue) = registerRegex.find(line)!!.destructured
            registerValue.toLong()
        }

    return Registers(a, b, c)
}

private fun parseInstructions(input: String): List<Instruction> {
    val afterProgram = input.substringAfter("Program: ")
    val instructionRegex = """(\d),(\d),?""".toRegex()

    return instructionRegex
        .findAll(afterProgram)
        .map { match ->
            val (opcode, operandString) = match.destructured
            val operand = operandString.toLong()
            when (opcode.toInt()) {
                0 -> Instruction.Adv(operand)
                1 -> Instruction.Bxl(operand)
                2 -> Instruction.Bst(operand)
                3 -> Instruction.Jnz(operand)
                4 -> Instruction.Bxc(operand)
                5 -> Instruction.Out(operand)
                6 -> Instruction.Bdv(operand)
                7 -> Instruction.Cdv(operand)
                else -> throw IllegalArgumentException("Invalid opcode: $opcode")
            }
        }
        .toList()
}
