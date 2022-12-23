package day22

import readInput

fun List<CharArray>.printGrid() {
    for (row in 0 until this.size) {
        for (col in 0 until this[row].size) {
            print(this[row][col])
            print(" ")
        }
        println()
    }

    println("-----------------------------------------")
}

enum class Direction {
    LEFT,
    RIGHT,
    DOWN,
    UP
}

fun main() {

    fun solve1(lines: List<String>): Int {
        val input = lines.dropLast(2).map {
            it.toCharArray()
        }
        var movement = lines.takeLast(1)[0]

        fun clockWise(row: Int, col: Int): Direction {
            return when {
                input[row][col] == 'r' -> Direction.DOWN
                input[row][col] == 'l' -> Direction.UP
                input[row][col] == 'u' -> Direction.RIGHT
                input[row][col] == 'd' -> Direction.LEFT
                else -> throw IllegalStateException()
            }
        }

        fun antiClockWise(row: Int, col: Int): Direction {
            return when {
                input[row][col] == 'r' -> Direction.UP
                input[row][col] == 'l' -> Direction.DOWN
                input[row][col] == 'u' -> Direction.LEFT
                input[row][col] == 'd' -> Direction.RIGHT
                else -> throw IllegalStateException()
            }
        }

        var currentRow = 0
        var currentCol = input[0].indexOfFirst { !it.isWhitespace() }

        fun indexOfLast(row: Int, col: Int): Int {
            var rowIndex = 0
            try {
                for (r in row until input.size) {
                    if (!input[r][col].isWhitespace()) {
                        rowIndex = r
                    }
                }
            } catch (ex: Exception) { }
            return rowIndex
        }

        fun indexOfFirst(row: Int, col: Int): Int {
            var rowIndex = 0
            try {
                for (r in row downTo 0){
                    if (!input[r][col].isWhitespace()) {
                        rowIndex = r
                    }
                }
            }catch (ex:Exception){}
            return rowIndex
        }

        fun moveRight(moves: Int) {
            var col = currentCol
            input[currentRow][col] = 'r'
            var steps = moves
            while (steps-- > 0) {
                if (col + 1 == input[currentRow].size) {
                    val nextCol = input[currentRow].indexOfFirst { !it.isWhitespace() }
                    if (input[currentRow][nextCol] == '#') break
                    col = nextCol
                    input[currentRow][col] = 'r'
                    currentCol = col
                    continue
                }
                if (input[currentRow][col + 1] == '#') break
                input[currentRow][++col] = 'r'
                currentCol = col
            }
        }

        fun moveLeft(moves: Int) {
            var col = currentCol
            input[currentRow][col] = 'l'
            var steps = moves
            while (steps-- > 0) {
                if (col - 1 == input[currentRow].indexOfFirst { !it.isWhitespace() } - 1) {
                    val nextCol = input[currentRow].size-1
                    if (input[currentRow][nextCol] == '#') break
                    col = nextCol
                    input[currentRow][col] = 'l'
                    currentCol = col
                    continue
                }

                if (input[currentRow][col - 1] == '#') break
                input[currentRow][--col] = 'l'
                currentCol = col
            }
        }

        fun moveDown(moves: Int) {
            var row = currentRow
            input[row][currentCol] = 'd'
            var steps = moves
            while (steps-- > 0) {
                if (row + 1 == indexOfLast(row, currentCol) + 1) {
                    val nextRow = indexOfFirst(row,currentCol)
                    if (input[nextRow][currentCol] == '#') break
                    row = nextRow
                    input[row][currentCol] = 'd'
                    currentRow = row
                    continue
                }

                if (input[row + 1][currentCol] == '#') break
                input[++row][currentCol] = 'd'
                currentRow = row
            }
        }

        fun moveUp(moves: Int) {
            var row = currentRow
            input[row][currentCol] = 'u'
            var steps = moves
            while (steps -- > 0){
                if (row - 1 == indexOfFirst(row, currentCol) - 1) {
                    val nextRow = indexOfLast(row,currentCol)
                    if (input[nextRow][currentCol] == '#') break
                    row = nextRow
                    input[row][currentCol] = 'u'
                    currentRow = row
                    continue
                }
                if (input[row - 1][currentCol] == '#') break
                input[--row][currentCol] = 'u'
                currentRow = row
            }
        }

        while (movement.isNotEmpty()) {
            when (movement[0]) {
                'R' -> {
                    val moves = movement.substring(1).takeWhile { it.isDigit() }.let {
                        movement = movement.substring(it.length + 1)
                        it.toInt()
                    }
                    when (clockWise(currentRow, currentCol)) {
                        Direction.UP -> moveUp(moves)
                        Direction.DOWN -> moveDown(moves)
                        Direction.LEFT -> moveLeft(moves)
                        Direction.RIGHT -> moveRight(moves)
                    }
                }
                'L' -> {
                    val moves = movement.substring(1).takeWhile { it.isDigit() }.let {
                        movement = movement.substring(it.length + 1)
                        it.toInt()
                    }
                    when (antiClockWise(currentRow, currentCol)) {
                        Direction.UP -> moveUp(moves)
                        Direction.DOWN -> moveDown(moves)
                        Direction.LEFT -> moveLeft(moves)
                        Direction.RIGHT -> moveRight(moves)
                    }
                }
                else -> {
                    // move right
                    val moves = movement.takeWhile { it.isDigit() }.let {
                        movement = movement.substring(it.length)
                        it.toInt()
                    }
                    moveRight(moves)
                }
            }
        }

        val facingValue = when (input[currentRow][currentCol]) {
            'r' -> 0
            'l' -> 2
            'u' -> 3
            'd' -> 1
            else -> throw IllegalStateException()
        }

        return (currentRow + 1) * 1000 + 4 * (currentCol + 1) + facingValue
    }

    fun solve2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("/day22/Day22_test")
    println(solve1(testInput))
//    println(solve1(testInput))
//    println(solve2(testInput))


    println("--------------------------------")

    val input = readInput("/day22/Day22")
    println(solve1(input))
//    println(solve2(input))


}
