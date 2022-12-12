package day12

import readInput
import java.util.LinkedList
import java.util.Queue

fun main() {

    data class RowCol(
        val row: Int,
        val col: Int
    )

    var sourceP: RowCol? = null
    var destP: RowCol? = null

    val sourceDestinations = mutableListOf<RowCol>()

    fun List<String>.initMatrix(matrix: Array<CharArray>) {
        val rowSize = size
        val colSize = this[0].length
        for (row in 0 until rowSize) {
            for (col in 0 until colSize) {
                matrix[row][col] = this[row][col]
                if (matrix[row][col] == 'S') {
                    sourceP = RowCol(row, col)
                    sourceDestinations.add(RowCol(row, col))
                }

                if (matrix[row][col] == 'a'){
                    sourceDestinations.add(RowCol(row, col))
                }

                if (matrix[row][col] == 'E') {
                    destP = RowCol(row, col)
                }
            }
        }
    }


    fun Array<CharArray>.printMatrix() {
        for (i in 0 until this.size) {
            for (j in 0 until this[0].size) {
                print(this[i][j])
                print(" ")
            }
            println()
        }
    }


    fun findShortestPath(srcDest: RowCol, grid: Array<CharArray>): Int {
        val rowInbound = grid.indices
        val colInbound = 0 until grid[0].size

        val queue: Queue<Pair<RowCol, Int>> = LinkedList()
        queue.add(Pair(srcDest, 0))

        val visited = hashSetOf<RowCol>()
        visited.add(srcDest)

        while (queue.isNotEmpty()) {
            val (nodeP, distance) = queue.remove()
            if (nodeP == destP) return distance

            var currentElevation = grid[nodeP.row][nodeP.col]
            if (currentElevation == 'S') currentElevation = 'a'

            fun up() {
                val up = RowCol(nodeP.row - 1, nodeP.col)
                if (up.row !in rowInbound || up.col !in colInbound) return
                var nextElevation = grid[up.row][up.col]
                if (nextElevation == 'E') nextElevation = 'z'
                if (
                    currentElevation == nextElevation ||
                    currentElevation + 1 == nextElevation ||
                    currentElevation > nextElevation
                ){
                    if (!visited.contains(up)){
                        visited.add(up)
                        queue.add(Pair(up, distance + 1))
                    }
                }
            }

            fun down() {
                val down = RowCol(nodeP.row + 1, nodeP.col)
                if (down.row !in rowInbound || down.col !in colInbound) return
                var nextElevation = grid[down.row][down.col]
                if (nextElevation == 'E') nextElevation = 'z'
                if (
                    currentElevation == nextElevation ||
                    currentElevation + 1 == nextElevation ||
                    currentElevation > nextElevation
                ){
                    if (!visited.contains(down)){
                        visited.add(down)
                        queue.add(Pair(down, distance + 1))
                    }
                }
            }

            fun left() {
                val left = RowCol(nodeP.row, nodeP.col - 1)
                if (left.row !in rowInbound || left.col !in colInbound) return
                var nextElevation = grid[left.row][left.col]
                if (nextElevation == 'E') nextElevation = 'z'
                if (
                    currentElevation == nextElevation ||
                    currentElevation + 1 == nextElevation ||
                    currentElevation > nextElevation
                ){
                    if (!visited.contains(left)){
                        visited.add(left)
                        queue.add(Pair(left, distance + 1))
                    }
                }
            }

            fun right() {
                val right = RowCol(nodeP.row, nodeP.col + 1)
                if (right.row !in rowInbound || right.col !in colInbound) return
                var nextElevation = grid[right.row][right.col]
                if (nextElevation == 'E') nextElevation = 'z'
                if (
                    currentElevation == nextElevation ||
                    currentElevation + 1 == nextElevation ||
                    currentElevation > nextElevation
                ){
                    if (!visited.contains(right)){
                        visited.add(right)
                        queue.add(Pair(right, distance + 1))
                    }
                }
            }

            up()
            down()
            left()
            right()
        }

        return -1
    }

    fun part1(input: List<String>): Int {
        val rowSize = input.size
        val colSize = input[0].length

        val grid = Array(rowSize) { CharArray(colSize) }
        input.initMatrix(grid)

        return findShortestPath(sourceP!!, grid)
    }


    fun part2(input: List<String>): Int {
        val rowSize = input.size
        val colSize = input[0].length

        val grid = Array(rowSize) { CharArray(colSize) }
        input.initMatrix(grid)


        var short = Int.MAX_VALUE
        sourceDestinations.forEach {
            val result = findShortestPath(it, grid)
            if (result < short && result != -1){
                short = result
            }
        }

        return short
    }

    val testInput = readInput("/day12/Day12_test")
    println(part1(testInput))
    println(part2(testInput))

    println("----------------------------------------------")

    val input = readInput("/day12/Day12")
    println(part1(input))
    println(part2(input))
}



