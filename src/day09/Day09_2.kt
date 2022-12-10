package day09

import readInput
import kotlin.math.abs

fun main() {

    data class Position(val row: Int, val col: Int)

    val size = 50000000
    val middle = size / 2

    var headMain = Position(row = middle, col = middle)

    var tailsMap = hashMapOf<Int, Position>().apply {
        for (i in 1..9) {
            put(i, Position(row = middle, col = middle))
        }
    }

    var answer = 1

    // Tracking for tail - 9
    val visited = hashSetOf<Position>()

    fun update() {
        if (visited.contains(tailsMap[9])) return
        ++answer
        visited.add(tailsMap[9]!!)
    }

    fun moveTail(tailKey: Int, headKey: Int?, direction: String) {
        val head = if (headKey == null) headMain else tailsMap[headKey]!!
        val tail = tailsMap[tailKey]!!

        // horizontal
        if (head.row == tail.row) {
            if (abs(head.col - tail.col) < 2) return
            tailsMap[tailKey] = if (head.col > tail.col) { // right
                tail.copy(col = tail.col + 1)
            } else { // left
                tail.copy(col = tail.col - 1)
            }
            update()
            return
        }


        // vertical
        if (head.col == tail.col) {
            if (abs(head.row - tail.row) < 2) return
            tailsMap[tailKey] = if (head.row < tail.row) { // up
                tail.copy(row = tail.row - 1)
            } else { // down
                tail.copy(row = tail.row + 1)
            }
            update()
            return
        }

        // diagonal
        if (abs(head.row - tail.row) < 2 && abs(head.col - tail.col) < 2) return

        // up
        if (head.row < tail.row) {
            if (head.col > tail.col) { // right
                tailsMap[tailKey] = tail.copy(
                    row = tail.row - 1,
                    col = tail.col + 1
                )
                update()
                return
            }

            // left
            tailsMap[tailKey] = tail.copy(
                row = tail.row - 1,
                col = tail.col - 1
            )
            update()
            return
        }

        // diagonal - down

        if (head.col > tail.col) { // right
            tailsMap[tailKey] = tail.copy(
                row = tail.row + 1,
                col = tail.col + 1
            )
            update()
            return
        }

        // left
        tailsMap[tailKey] = tail.copy(
            row = tail.row + 1,
            col = tail.col - 1
        )
        update()
    }

    fun moveTails(direction: String) {
        for (i in 1..9) {
            if (i == 1) {
                moveTail(tailKey = i, headKey = null, direction = direction)
                continue
            }

            moveTail(tailKey = i, headKey = i - 1, direction = direction)
        }
    }

    fun part2(input: List<String>): Int {
        headMain = Position(middle, middle)
        answer = 1
        visited.clear()
        visited.add(tailsMap[9]!!)
        tailsMap = hashMapOf<Int, Position>().apply {
            for (i in 1..9) {
                put(i, Position(row = middle, col = middle))
            }
        }

        input.forEach {
            val (direction, step) = it.split(" ").map { it.trim() }
            var stepCount = step.toInt()

            while (stepCount > 0) {
                when (direction) {
                    "R" -> {
                        headMain = headMain.copy(col = headMain.col + 1)
                    }
                    "L" -> {
                        headMain = headMain.copy(col = headMain.col - 1)
                    }
                    "U" -> {
                        headMain = headMain.copy(row = headMain.row - 1)
                    }
                    "D" -> {
                        headMain = headMain.copy(row = headMain.row + 1)
                    }
                }
                moveTails(direction)
                --stepCount
            }
        }
        return answer
    }

    val testInput = readInput("/day09/Day09_test")
    println(part2(testInput))


    println("----------------------------------------")


    val input = readInput("/day09/Day09")
    println(part2(input))
}
