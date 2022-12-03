package day03

import readInput

fun main() {

    val hashMapSmall = hashMapOf<Char,Int>()
    for (i in 0..26) {
        hashMapSmall['a' + i] = i + 1
    }

    val hashMapBig = hashMapOf<Char,Int>()
    for (i in 0..26) {
        hashMapBig['A' + i] = i + 27
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { item ->
            val (first, second) = item.chunked(item.length / 2)
            val matchedChar = first.toCharArray().intersect(second.toSet()).first()
            (hashMapSmall[matchedChar] ?: hashMapBig[matchedChar])!!
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf { items ->
            val matchedChar = items[0].toCharArray().intersect(items[1].toSet()).intersect(items[2].toSet()).first()
            (hashMapSmall[matchedChar] ?: hashMapBig[matchedChar])!!
        }
    }

    val input = readInput("/day03/Day03")
    println(part1(input))
    println(part2(input))
}
