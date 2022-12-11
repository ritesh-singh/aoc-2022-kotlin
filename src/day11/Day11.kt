package day11

import readInput


fun main() {

    data class Monkey(
        val items: ArrayDeque<Long>,
        val op: String,
        val divBy: Long,
        val throwWhenTrue: Int,
        val throwWhenFalse: Int,
        var inspectCount: Long = 0L
    )

    fun buildMonkey(input: List<String>): List<Monkey> {
        return buildList {
            input.filter { it.isNotBlank() }
                .chunked(6).map { it.map { it.trim() } }
                .forEach { list ->
                    add(
                        Monkey(
                            items = ArrayDeque<Long>().also { dequeue ->
                                list[1].substringAfter(":")
                                    .trim().split(",").map { it.trim().toLong() }
                                    .forEach { dequeue.add(it) }
                            },
                            op = list[2].substringAfter("old").trim(),
                            divBy = list[3].substringAfter("by").trim().toLong(),
                            throwWhenTrue = list[4].substringAfter("monkey").trim().toInt(),
                            throwWhenFalse = list[5].substringAfter("monkey").trim().toInt()
                        )
                    )
                }
        }
    }

    fun solve(input: List<String>, part1:Boolean): Long {
        val monkeys = buildMonkey(input)
        val modBy = monkeys.map { it.divBy }.reduce { a, b -> a * b }

        repeat(if (part1) 20 else 10000) {
            monkeys.forEach { monkey ->
                val ops = monkey.op.split(" ").map { it.trim() }
                monkey.items.forEach { item ->
                    var worryLevel = item
                    if (ops[0] == "*") {
                        worryLevel *= if (ops[1].toLongOrNull() == null) worryLevel else ops[1].toLong()
                    } else {
                        worryLevel += if (ops[1].toLongOrNull() == null) worryLevel else ops[1].toLong()
                    }
                    if (part1) {
                        worryLevel = Math.floorDiv(worryLevel, 3)
                    } else {
                        worryLevel %= modBy
                    }

                    worryLevel %= modBy
                    val throwTo = if (worryLevel % monkey.divBy == 0L) {
                        monkey.throwWhenTrue
                    } else {
                        monkey.throwWhenFalse
                    }
                    monkeys[throwTo].items.addLast(worryLevel)
                }
                monkey.inspectCount += monkey.items.size
                monkey.items.clear()
            }
        }

        return monkeys.sortedByDescending { it.inspectCount }.let {
            it[0].inspectCount * it[1].inspectCount
        }
    }

    val input = readInput("/day11/Day11")
    println(solve(input, true))
    println(solve(input, false))
}
