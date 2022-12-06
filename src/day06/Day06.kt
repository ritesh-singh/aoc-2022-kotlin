package day06

import readInput

fun main() {

    fun solution(input: List<String>, size: Int): Int {
        input.first().windowed(size)
            .forEachIndexed { index, string ->
                if (string.toSet().size == size) return index + size
            }

        return Int.MAX_VALUE
    }

    val input = readInput("/day06/Day06")
    println(solution(input, size = 4))
    println(solution(input, size = 14))

}
