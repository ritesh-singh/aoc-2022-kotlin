package day03

import readInput

fun main() {

    fun Char.priority() = if (isUpperCase()) this - 'A' + 27 else this - 'a' + 1

    fun part1(input: List<String>): Int {
        return input.sumOf { item ->
            val (first, second) = item.chunked(item.length / 2)
            val matchedChar = first.toCharArray().intersect(second.toSet()).first()
            matchedChar.priority()
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf { items ->
            val matchedChar = items[0].toCharArray().intersect(items[1].toSet()).intersect(items[2].toSet()).first()
            matchedChar.priority()
        }
    }

    val input = readInput("/day03/Day03")
    println(part1(input))
    println(part2(input))
}
