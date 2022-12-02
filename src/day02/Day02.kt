package day02

import readInput

fun main() {

    // Y defeats A
    // Z defeats B
    // X defeats C
    val defeatMap = mapOf(
        "A" to "Y",
        "B" to "Z",
        "C" to "X"
    )

    // X loses to B
    // Y loses to C
    // Z loses to A
    val loseMap = mapOf(
        "B" to "X",
        "C" to "Y",
        "A" to "Z"
    )

    val drawMap = mapOf(
        "A" to "X",
        "B" to "Y",
        "C" to "Z"
    )

    val rockPaperScissorScore = mapOf(
        "X" to 1,
        "Y" to 2,
        "Z" to 3
    )

    val LOST = 0
    val DRAW = 3
    val WIN = 6


    fun getTotalScorePart1(opponent: String, you: String): Int {
        return when (you) {
            defeatMap[opponent] -> { // win
                WIN + rockPaperScissorScore[you]!!
            }

            drawMap[opponent] -> { // draw
                DRAW + rockPaperScissorScore[you]!!
            }

            else -> { // lost
                LOST + rockPaperScissorScore[you]!!
            }
        }
    }

    fun getTotalScorePart2(opponent: String, you: String): Int {
        return when (you) {
            "X" -> { // need to lose
                LOST + rockPaperScissorScore[loseMap[opponent]]!!
            }

            "Y" -> { // need to draw
                DRAW + rockPaperScissorScore[drawMap[opponent]]!!
            }

            else -> {
                // need to win
                WIN + rockPaperScissorScore[defeatMap[opponent]]!!
            }
        }
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach {
            val (first, second) = it.split(" ").map { it.trim() }
            sum += getTotalScorePart1(opponent = first, you = second)
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        input.forEach {
            val (first, second) = it.split(" ").map { it.trim() }
            sum += getTotalScorePart2(first, second)
        }
        return sum
    }

    val input = readInput("/day02/Day02")
    println(part1(input))
    println(part2(input))
}
