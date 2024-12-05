package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun testStream() = resourceLineStream("/05/test.txt")

private fun inputStream() = resourceLineStream("/05/input.txt")

sealed interface OrderingRule {
    data class Before(val num: Int) : OrderingRule

    data class After(val num: Int) : OrderingRule
}

private fun part1(): Int {
    val lines = inputStream().toList()
    val (ruleLines, updateLines) =
        lines.takeWhile { it.isNotEmpty() } to (lines.dropWhile { it.isNotEmpty() }.drop(1))

    val rules = buildRules(ruleLines)
    val updates = updateLines.asSequence().map { line -> line.split(",").map(String::toInt) }

    return updates.sumOf { update ->
        if (isValidUpdate(update, rules)) {
            update[update.lastIndex / 2]
        } else {
            0
        }
    }
}

private fun part2(): Int {
    val lines = inputStream().toList()
    val (ruleLines, updateLines) =
        lines.takeWhile { it.isNotEmpty() } to (lines.dropWhile { it.isNotEmpty() }.drop(1))

    val rules = buildRules(ruleLines)
    val updates = updateLines.asSequence().map { line -> line.split(",").map(String::toInt) }

    val incorrectUpdates = updates.filter { update -> !isValidUpdate(update, rules) }

    return incorrectUpdates
        .map { update ->
            val comparator = Comparator { num1: Int, num2: Int ->
                val rulesForNum1 = rules.getOrDefault(num1, emptyList())
                val relevantRule =
                    rulesForNum1.find { rule ->
                        when (rule) {
                            is OrderingRule.Before -> rule.num == num2
                            is OrderingRule.After -> rule.num == num2
                        }
                    }
                relevantRule?.let {
                    when (it) {
                        is OrderingRule.Before -> -1
                        is OrderingRule.After -> 1
                    }
                } ?: 0
            }
            update.sortedWith(comparator)
        }
        .sumOf { update -> update[update.lastIndex / 2] }
}

private fun buildRules(ruleLines: List<String>): Map<Int, List<OrderingRule>> = buildMap {
    ruleLines.forEach { line ->
        val (num1, num2) = line.split("|").map(String::toInt)
        val num1Rule = OrderingRule.Before(num2)
        val num2Rule = OrderingRule.After(num1)

        merge(num1, listOf(num1Rule)) { current, _ -> current + num1Rule }
        merge(num2, listOf(num2Rule)) { current, _ -> current + num2Rule }
    }
}

private fun isValidUpdate(update: List<Int>, rules: Map<Int, List<OrderingRule>>): Boolean {
    val updateNums = update.toSet()

    return update.withIndex().all { num ->
        val rulesForNum = rules.getOrDefault(num.value, emptyList())

        rulesForNum.all { rule ->
            when (rule) {
                is OrderingRule.Before -> {
                    if (!updateNums.contains(rule.num)) {
                        true
                    } else {
                        val firstIndexOfRuleNum = update.indexOf(rule.num)
                        num.index < firstIndexOfRuleNum
                    }
                }

                is OrderingRule.After -> {
                    if (!updateNums.contains(rule.num)) {
                        true
                    } else {
                        val lastIndexOfRuleNum = update.lastIndexOf(rule.num)
                        num.index > lastIndexOfRuleNum
                    }
                }
            }
        }
    }
}
