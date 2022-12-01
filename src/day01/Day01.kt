package day01

import readInput

fun main() {

    fun getMax(inputs: List<String>): Int {
        var sum = 0
        return mutableListOf(0).apply {
            inputs.forEach { calorie ->
                if (calorie.isBlank()) {
                    this.add(sum)
                    sum = 0
                } else {
                    sum += calorie.toInt()
                }
            }
        }.max()
    }

    fun getFirstThreeMax(inputs: List<String>): Int {
        var sum = 0
        return mutableListOf(0).apply {
            inputs.forEach { calorie ->
                if (calorie.isBlank()) {
                    this.add(sum)
                    sum = 0
                } else {
                    sum += calorie.toInt()
                }
            }
        }.sorted().takeLast(3).sum()
    }

    fun part1(input: List<String>): Int = getMax(input)

    fun part2(input: List<String>): Int = getFirstThreeMax(input)

    val input = readInput("/day01/Day01")
    println(part1(input))
    println(part2(input))
}
