package day01

import readInput

fun main() {

    fun getMax(firstNMax: Int, inputs: List<String>): Int {
        return mutableListOf(0).apply {
            inputs.forEach { calorie ->
                if (calorie.isBlank()) {
                    this.add(0)
                } else {
                    this[this.lastIndex] = this[this.lastIndex] + calorie.toInt()
                }
            }
        }.let {
            if (firstNMax == 1) {
                it.max()
            } else {
                it.sorted().takeLast(3).sum()
            }
        }
    }

    fun part1(input: List<String>): Int = getMax(1, input)

    fun part2(input: List<String>): Int = getMax(3, input)

    val input = readInput("/day01/Day01")
    println(part1(input))
    println(part2(input))
}
