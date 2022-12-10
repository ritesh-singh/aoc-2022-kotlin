package day09

import readInput
import kotlin.math.abs

fun main() {

    data class Position(val row: Int, val col: Int)

    val size = 5000
    val middle = size / 2

    var head = Position(row = middle, col = middle)
    var tail = Position(row = middle, col = middle)

    var answer = 1

    val visited = hashSetOf<Position>()

    fun update() {
        if (visited.contains(tail)) return
        ++answer
        visited.add(tail)
    }

    fun moveTail(direction:String) {
        // horizontal
        if (head.row == tail.row) {
            if (abs(head.col - tail.col) < 2) return
            tail = if (head.col > tail.col) { // right
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
            tail = if (head.row < tail.row) { // up
                tail.copy(row = tail.row - 1)
            } else { // down
                tail.copy(row = tail.row + 1)
            }
            update()
            return
        }


        // diagonal

        if (abs(head.row - tail.row) < 2 && abs(head.col - tail.col) < 2) return

//        if (direction == "U" || direction == "D"){
//            if (abs(head.row  - tail.row) < 2) return
//        }
//
//
//        if (direction == "R" || direction == "L"){
//            if (abs(head.col  - tail.col) < 2) return
//        }

        // up
        if (head.row < tail.row) {
            if (head.col > tail.col) { // right
                tail = tail.copy(
                    row = tail.row - 1,
                    col = tail.col + 1
                )
                update()
                return
            }

            // left
            tail = tail.copy(
                row = tail.row - 1,
                col = tail.col - 1
            )
            update()
            return
        }

        // diagonal - down

        if (head.col > tail.col) { // right
            tail = tail.copy(
                row = tail.row + 1,
                col = tail.col + 1
            )
            update()
            return
        }

        // left
        tail = tail.copy(
            row = tail.row + 1,
            col = tail.col - 1
        )
        update()
    }

    fun part1(input: List<String>): Int {
        head = Position(middle, middle)
        tail = Position(middle, middle)
        answer = 1
        visited.clear()
        visited.add(tail)

        input.forEach {
            val (direction, step) = it.split(" ").map { it.trim() }
            var stepCount = step.toInt()

            while (stepCount > 0) {
                when (direction) {
                    "R" -> {
                        head = head.copy(col = head.col + 1)
                    }
                    "L" -> {
                        head = head.copy(col = head.col - 1)
                    }
                    "U" -> {
                        head = head.copy(row = head.row - 1)
                    }
                    "D" -> {
                        head = head.copy(row = head.row + 1)
                    }
                }
                moveTail(direction)
                --stepCount
            }
        }
        return answer
    }

    val testInput = readInput("/day09/Day09_test")
    println(part1(testInput))


    println("----------------------------------------")


    val input = readInput("/day09/Day09")
    println(part1(input))
}
