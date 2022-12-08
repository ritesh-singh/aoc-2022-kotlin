package day08

import readInput

fun main() {

    fun List<String>.initMatrix(matrix: Array<IntArray>) {
        val rowSize = size
        val colSize = this[0].length
        for (row in 0 until rowSize) {
            for (col in 0 until colSize) {
                matrix[row][col] = this[row][col].digitToInt()
            }
        }
    }

    fun part1(input: List<String>): Int {
        val rowSize = input.size
        val colSize = input[0].length

        val matrix = Array(rowSize) { IntArray(colSize) }
        input.initMatrix(matrix)


        val visibleATreeAroundTheEdges = (rowSize * 2) + ((colSize - 2) * 2)

        var internalVisibleTree = 0
        for (row in 1 until rowSize - 1) {
            for (col in 1 until colSize - 1) {
                val currentValue = matrix[row][col]

                // check left
                var leftPass = true
                for (left in 0 until col) {
                    if (matrix[row][left] >= currentValue) {
                        leftPass = false
                        break
                    }
                }
                if (leftPass) {
                    ++internalVisibleTree
                    continue
                }

                // check right
                var rightPass = true
                for (right in col + 1 until colSize) {
                    if (matrix[row][right] >= currentValue) {
                        rightPass = false
                        break
                    }
                }
                if (rightPass) {
                    ++internalVisibleTree
                    continue
                }

                // check top
                var topPass = true
                for (top in 0 until row) {
                    if (matrix[top][col] >= currentValue) {
                        topPass = false
                        break
                    }
                }
                if (topPass) {
                    ++internalVisibleTree
                    continue
                }

                // check bottom
                var bottomPass = true
                for (bottom in row + 1 until rowSize) {
                    if (matrix[bottom][col] >= currentValue) {
                        bottomPass = false
                        break
                    }
                }

                if (bottomPass) {
                    ++internalVisibleTree
                }

            }
        }

        return visibleATreeAroundTheEdges + internalVisibleTree

    }

    fun part2(input: List<String>): Int {
        val rowSize = input.size
        val colSize = input[0].length

        val matrix = Array(rowSize) { IntArray(colSize) }
        input.initMatrix(matrix)

        var answer = Int.MIN_VALUE
        for (row in 1 until rowSize - 1) {
            for (col in 1 until colSize - 1) {
                val currentHeight = matrix[row][col]

                var leftScore = 0
                for (left in col - 1 downTo 0) {
                    if (matrix[row][left] <= currentHeight) ++leftScore
                    if (matrix[row][left] >= currentHeight) break
                }

                var rightScore = 0
                for (right in col + 1 until colSize) {
                    if (matrix[row][right] <= currentHeight) ++rightScore
                    if (matrix[row][right] >= currentHeight) break
                }


                var topScore = 0
                for (top in row - 1 downTo 0) {
                    if (matrix[top][col] <= currentHeight) ++topScore
                    if (matrix[top][col] >= currentHeight) break
                }

                var bottomScore = 0
                for (bottom in row + 1 until rowSize) {
                    if (matrix[bottom][col] <= currentHeight) ++bottomScore
                    if (matrix[bottom][col] >= currentHeight) break
                }

                val totalScore = leftScore * rightScore * topScore * bottomScore
                if (totalScore > answer) answer = totalScore
            }
        }
        return answer
    }

    val input = readInput("/day08/Day08")
    println(part1(input))
    println(part2(input))
}
