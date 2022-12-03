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
        var sum = 0
        input.forEach { items ->
            val first = items.substring(0 until items.length / 2)
            val second = items.substring(items.length / 2 until items.length)

            val char = first.toCharArray().intersect(second.toSet()).first()
            sum += (hashMapSmall[char] ?: hashMapBig[char])!!
        }

        return sum
    }


    fun part2(input: List<String>): Int {
        var sum = 0
        var i = 0
        while (i < input.size){
            val char = input[i++].toCharArray().intersect(input[i++].toSet()).intersect(input[i++].toSet()).first()
            sum += (hashMapSmall[char] ?: hashMapBig[char])!!
        }

        return sum
    }

    val input = readInput("/day03/Day03")
    println(part1(input))
    println(part2(input))
}
