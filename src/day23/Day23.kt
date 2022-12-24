package day23

import readInput

private data class Position(val row: Int, val col: Int)

private fun Position.north() = this.copy(row = this.row - 1)
private fun Position.south() = this.copy(row = this.row + 1)
private fun Position.west() = this.copy(col = this.col - 1)
private fun Position.east() = this.copy(col = this.col + 1)
private fun Position.northWest() = this.copy(row = this.row - 1, col = this.col - 1)
private fun Position.northEast() = this.copy(row = this.row - 1, col = this.col + 1)
private fun Position.southWest() = this.copy(row = this.row + 1, col = this.col - 1)
private fun Position.southEast() = this.copy(row = this.row + 1, col = this.col + 1)

private fun Position.eightAdjPositions() =
    listOf(
        this.north(), this.south(), this.west(), this.east(),
        this.northWest(), this.northEast(), this.southWest(), this.southEast(),
    )

private const val LAND = '.'
private const val ELVES = '#'

private fun Char.isElves() = this == ELVES

private class Grove(lines: List<String>) {

    private val elvesPositions = hashSetOf<Position>()

    init {
        lines.forEachIndexed { index, string ->
            string.forEachIndexed { charIdx, c ->
                if (c.isElves()) elvesPositions.add(Position(row = index, col = charIdx))
            }
        }
    }

    fun printElves() {
        val minRow = elvesPositions.minOf { it.row }
        val maxRow = elvesPositions.maxOf { it.row }
        val minCol = elvesPositions.minOf { it.col }
        val maxCol = elvesPositions.maxOf { it.col }

        for (row in minRow..maxRow) {
            for (col in minCol..maxCol) {
                if (elvesPositions.contains(Position(row, col))) {
                    print(ELVES)
                } else {
                    print(LAND)
                }
                print(" ")
            }
            println()
        }

        println("--------------------------------------------")
    }

    private enum class Directions { N, S, W, E }

    private var fourDirectionsToLook = ArrayDeque(listOf(Directions.N, Directions.S, Directions.W, Directions.E))
    private fun rotateDirections() = fourDirectionsToLook.addLast(fourDirectionsToLook.removeFirst())

    private fun Position.elvesExistsInAnyDirection() = this.eightAdjPositions().any { elvesPositions.contains(it) }
    private fun List<Position>.noElvesInAllDirection() = this.all { !elvesPositions.contains(it) }

    private fun firstSecondHalf():Boolean {
        val elvesToMove = elvesPositions.filter { it.elvesExistsInAnyDirection() }
        val whereToMove = hashMapOf<Position, Position>()
        elvesToMove.forEach elves@{ elves ->
            fourDirectionsToLook.forEach { directions ->
                val lookup = when (directions) {
                    Directions.N -> listOf(elves.north(), elves.northEast(), elves.northWest())
                    Directions.S -> listOf(elves.south(), elves.southEast(), elves.southWest())
                    Directions.W -> listOf(elves.west(), elves.northWest(), elves.southWest())
                    Directions.E -> listOf(elves.east(), elves.northEast(), elves.southEast())
                }
                if (lookup.noElvesInAllDirection()) {
                    whereToMove[elves] = when (directions) {
                        Directions.N -> elves.north()
                        Directions.S -> elves.south()
                        Directions.W -> elves.west()
                        Directions.E -> elves.east()
                    }
                    return@elves
                }
            }
        }
        val duplicateMovePositions = whereToMove.values.groupingBy { it }.eachCount().filter { it.value >= 2 }.keys
        val elvesEligibleToMove = whereToMove.filter { !duplicateMovePositions.contains(it.value) }
        elvesEligibleToMove.forEach { (from, to) ->
            elvesPositions.remove(Position(from.row, from.col))
            elvesPositions.add(Position(to.row, to.col))
        }
        return elvesToMove.isNotEmpty()
    }

    fun moveElves():Boolean {
        val canMove = firstSecondHalf()
        rotateDirections()
        return canMove
    }

    fun emptyGroundTiles(): Int {
        val minRow = elvesPositions.minOf { it.row }
        val maxRow = elvesPositions.maxOf { it.row }
        val minCol = elvesPositions.minOf { it.col }
        val maxCol = elvesPositions.maxOf { it.col }
        val totalRow = maxRow - minRow + 1
        val totalCol = maxCol - minCol + 1
        return (totalRow * totalCol) - elvesPositions.size
    }
}

fun main() {

    fun solve1(lines: List<String>): Int {
        val grove = Grove(lines = lines)
        val rounds = 10
        repeat(rounds) {
            grove.moveElves()
        }
        return grove.emptyGroundTiles()
    }

    fun solve2(lines: List<String>): Int {
        val grove = Grove(lines = lines)
        var rounds = 0
        while (true){
            rounds++
            val canMove = grove.moveElves()
            if (!canMove)
                break
        }
        return rounds
    }

    val input = readInput("/day23/Day23")
    println(solve1(input))
    println(solve2(input))
}

