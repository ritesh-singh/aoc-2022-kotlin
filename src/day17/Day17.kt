package day17

import readInput

data class Position(val x: Long, val y: Long)

val firstRock = listOf(
    Position(0 + 2, 3),
    Position(1 + 2, 3),
    Position(2 + 2, 3),
    Position(3 + 2, 3),
)
val secondRock = listOf(
    Position(1 + 2, 5),
    Position(0 + 2, 4),
    Position(1 + 2, 4),
    Position(2 + 2, 4),
    Position(1 + 2, 3),
)
val thirdRock = listOf(
    Position(2 + 2, 5),
    Position(2 + 2, 4),
    Position(0 + 2, 3),
    Position(1 + 2, 3),
    Position(2 + 2, 3)
)
val fourthRock = listOf(
    Position(0 + 2, 6),
    Position(0 + 2, 5),
    Position(0 + 2, 4),
    Position(0 + 2, 3),
)
val fifthRock = listOf(
    Position(0 + 2, 4),
    Position(1 + 2, 4),
    Position(0 + 2, 3),
    Position(1 + 2, 3),
)


fun solve(input: List<String>, maxRocks:Long): Long {
    val jetPattern = input[0]
    var hotGasIndex = 0

    val rocks = listOf(firstRock, secondRock, thirdRock, fourthRock, fifthRock)

    val blockedPosition = hashSetOf<Position>().apply {
        for (x in 0..6L) add(Position(x, -1))
    }

    fun rightPosBlocked(rock: List<Position>): Boolean {
        val end = rock.any { it.x + 1 > 6 }
        var right = false
        for (blockPos in blockedPosition) {
            val isBlocked = rock.any {
                it.copy(x = it.x + 1) == blockPos
            }
            if (isBlocked) {
                right = true
                break
            }
        }
        return end || right
    }
    fun leftPosBlocked(rock: List<Position>): Boolean {
        val end = rock.any { it.x - 1 < 0 }
        var left = false
        for (blockPos in blockedPosition) {
            val isBlocked = rock.any {
                it.copy(x = it.x - 1) == blockPos
            }
            if (isBlocked) {
                left = true
                break
            }
        }
        return end || left
    }
    fun downPosBlocked(rock: List<Position>): Boolean {
        var isBlocked = false
        for (blockPos in blockedPosition) {
            isBlocked = rock.any {
                it.copy(y = it.y - 1) == blockPos
            }
            if (isBlocked) break
        }
        return isBlocked
    }
    fun List<Position>.move(gasIndex: Int): List<Position> {
        if (jetPattern[gasIndex] == '<' && !leftPosBlocked(this)) {
            return this.map { it.copy(x = it.x - 1, y = it.y) }
        }
        if (jetPattern[gasIndex] == '>' && !rightPosBlocked(this)) {
            return this.map { it.copy(x = it.x + 1, y = it.y) }
        }
        return this
    }
    fun List<Position>.drop() = this.map { it.copy(x = it.x, y = it.y - 1) }


    var totalRock = 1L
    var rockIndex = 0
    var currentHeight = 0L

    data class State(val jetIndex: Int, val rockType: Int, )
    val seen = hashMapOf<State, Pair<Long, Long>>()

    while (totalRock < maxRocks) {

        if (totalRock > 500) {
            if (!seen.contains(State(jetIndex = hotGasIndex, rockType = rockIndex))) {
                seen[State(hotGasIndex, rockIndex)] = Pair(totalRock, currentHeight)
            } else {
                val (prevTotalRock, prevHeight) = seen[State(hotGasIndex, rockIndex)]!!
                val cyclePeriod = totalRock - prevTotalRock
                if (totalRock % cyclePeriod == maxRocks % cyclePeriod) {
                    val cycleHeight = currentHeight - prevHeight
                    val remainingRocks = maxRocks - totalRock
                    val cyclesRemaining = (remainingRocks / cyclePeriod) + 1
                    return prevHeight + (cycleHeight * cyclesRemaining)
                }
            }
        }

        var currentRock = rocks[rockIndex]
        currentRock = currentRock.map {
            it.copy(y = it.y + currentHeight)
        }
        var count = 3
        while (true) {
            // move left or right
            currentRock = currentRock.move(hotGasIndex)
            hotGasIndex = (hotGasIndex + 1) % jetPattern.length

            // one unit down - first three are not blocked
            if (count > 0){
                currentRock = currentRock.drop()
                count--
                continue
            }

            // fall one unit down
            if (!downPosBlocked(currentRock)) {
                currentRock = currentRock.drop()
            } else {
                blockedPosition.addAll(currentRock)
                currentHeight = if (totalRock == 1L) {
                    1
                } else {
                    (blockedPosition.groupBy { it.y }.count() - 1).toLong()
                }
                break
            }
        }

        rockIndex = (rockIndex + 1) % 5
        totalRock++
    }

    return currentHeight

}

fun main() {

    fun part1(input: List<String>): Long = solve(input, 2023)

    fun part2(input: List<String>): Long = solve(input, 1000000000001L)

    val input = readInput("/day17/Day17")
    println(part1(input))
    println(part2(input))
}
