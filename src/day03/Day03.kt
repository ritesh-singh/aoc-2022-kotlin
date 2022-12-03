package day03

import readInput

fun main() {

    fun Char.priority() = if (isUpperCase()) this - 'A' + 27 else this - 'a' + 1

    fun part1(input: List<String>): Int {
        return input.sumOf { item ->
            item.chunked(item.length / 2)
                .map { it.toSet() }
                .reduce { first, second -> first.intersect(second) }
                .first().priority()
        }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toSet() }
            .chunked(3)
            .map { it.reduce { first, second -> first.intersect(second) } }
            .sumOf { it.first().priority() }
    }

    val input = readInput("/day03/Day03")
    println(part1(input))
    println(part2(input))
}
