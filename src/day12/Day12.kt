package day12

import readInput
import java.util.LinkedList
import java.util.Queue

fun main() {

    data class RowCol(
        val row: Int,
        val col: Int
    )

    var destP: RowCol? = null
    val sourceDestinations = mutableListOf<RowCol>()

    fun List<String>.initMatrix(matrix: Array<CharArray>, part1: Boolean) {
        val rowSize = size
        val colSize = this[0].length
        for (row in 0 until rowSize) {
            for (col in 0 until colSize) {
                matrix[row][col] = this[row][col]
                if (matrix[row][col] == 'S') sourceDestinations.add(RowCol(row, col))
                if (matrix[row][col] == 'a' && !part1) sourceDestinations.add(RowCol(row, col))
                if (matrix[row][col] == 'E') destP = RowCol(row, col)
            }
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

            fun visitNeighbour(currentPosition:RowCol){
                if (currentPosition.row !in rowInbound || currentPosition.col !in colInbound) return
                var nextElevation = grid[currentPosition.row][currentPosition.col]
                if (nextElevation == 'E') nextElevation = 'z'
                if (
                    currentElevation + 1 == nextElevation ||
                    currentElevation >= nextElevation
                ) {
                    if (!visited.contains(currentPosition)) {
                        visited.add(currentPosition)
                        queue.add(Pair(currentPosition, distance + 1))
                    }
                }
            }

            visitNeighbour(RowCol(nodeP.row - 1, nodeP.col))
            visitNeighbour(RowCol(nodeP.row + 1, nodeP.col))
            visitNeighbour(RowCol(nodeP.row, nodeP.col - 1))
            visitNeighbour(RowCol(nodeP.row, nodeP.col + 1))
        }

        return -1
    }

    fun part1(input: List<String>): Int {
        val rowSize = input.size
        val colSize = input[0].length

        sourceDestinations.clear()
        val grid = Array(rowSize) { CharArray(colSize) }
        input.initMatrix(grid, true)

        return findShortestPath(sourceDestinations[0], grid)
    }

    fun part2(input: List<String>): Int {
        val rowSize = input.size
        val colSize = input[0].length

        sourceDestinations.clear()
        val grid = Array(rowSize) { CharArray(colSize) }
        input.initMatrix(grid, false)

        var shortPath = Int.MAX_VALUE
        sourceDestinations.forEach {
            val result = findShortestPath(it, grid)
            if (result < shortPath && result != -1) shortPath = result
        }

        return shortPath
    }

    val input = readInput("/day12/Day12")
    println(part1(input))
    println(part2(input))
}



