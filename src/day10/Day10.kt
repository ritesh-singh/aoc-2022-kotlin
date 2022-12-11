package day10

import readInput
import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        var (X, cycles, index) = Triple(1, 0, 0)

        var counter = 0
        var answer = 0

        while (true) {
            ++cycles
            if (
                cycles == 20 || cycles == 60
                || cycles == 100 || cycles == 140
                || cycles == 180 || cycles == 220
            ) {
                answer += cycles * X
                if (cycles == 220) break
            }

            when (input[index].substringBefore(" ").trim()) {
                "noop" -> ++index
                else -> {
                    ++counter
                    if (counter == 2) {
                        val value = input[index].substringAfter(" ").trim().toInt()
                        X += value
                        counter = 0
                        ++index
                    }
                }
            }
        }
        return answer
    }

    fun Array<CharArray>.printGrid() {
        for (i in 0 until this.size) {
            for (j in 0 until this[0].size) {
                print(this[i][j])
                print(" ")
            }
            println()
        }
        println("---------------------------")
    }

    fun part2(input: List<String>): Int {
        val rowSize = 6
        val colSize = 40
        var spritePosition = CharArray(size = 40) { '.' }.also {
            it[0] = '#'
            it[1] = '#'
            it[2] = '#'
        }

        val grid = Array(rowSize) { CharArray(colSize) { '.' } }
        var (X, cycles, index) = Triple(1, 0, 0)

        var counter = 0

        var row = 0
        var col = 0


        while (true) {
            if (cycles in 0 until 40) row = 0
            if (cycles in 40 until 80) row = 1
            if (cycles in 80 until 120) row = 2
            if (cycles in 120 until 160) row = 3
            if (cycles in 160 until 200) row = 4
            if (cycles in 200 until 240) row = 5

            if (cycles == 40) col = 0
            if (cycles == 80) col = 0
            if (cycles == 120) col = 0

            if (cycles == 160) col = 0
            if (cycles == 200) col = 0
            if (cycles == 240) col = 0

            if (spritePosition[col] == '#') {
                grid[row][col] = '#'
            } else {
                grid[row][col] = '.'
            }

            when (input[index].substringBefore(" ").trim()) {
                "noop" -> ++index
                else -> {
                    ++counter
                    if (counter == 2) {
                        val value = input[index].substringAfter(" ").trim().toInt()
                        X += value

                        spritePosition = CharArray(size = 40) { '.' }
                        try {
                            spritePosition[abs(X) - 1] = '#'
                            spritePosition[abs(X)] = '#'
                            spritePosition[abs(X) + 1] = '#'
                        } catch (_: IndexOutOfBoundsException) {}
                        counter = 0
                        ++index
                    }
                }
            }

            ++cycles
            ++col
            if (cycles == 240) break
        }


        grid.printGrid()
        return input.size
    }


    val input = readInput("/day10/Day10")
    println(part1(input))
    part2(input)
}
