package day04

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return input.map {
            it.split(",", "-").map { it.toInt() }
        }.count {
            val (first, second, third, fourth) = it
            (first in third..fourth && second in third..fourth) || (third in first..second && fourth in first..second)
        }
    }

    fun part2(input: List<String>): Int {
        return input.map {
            it.split(",", "-").map { it.toInt() }
        }.count {
            val (first, second, third, fourth) = it
            (first in third..fourth || second in third..fourth) || (third in first..second || fourth in first..second)
        }
    }

    val input = readInput("/day04/Day04")
    println(part1(input))
    println(part2(input))
}
