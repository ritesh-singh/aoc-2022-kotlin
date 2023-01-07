package day24

import readInput

private class Valley(lines: List<String>) {

    private fun Char.clearGround() = this == '.'
    private fun Char.wall() = this == '#'
    private fun Char.upB() = this == '^'
    private fun Char.downB() = this == 'v'
    private fun Char.leftB() = this == '<'
    private fun Char.rightB() = this == '>'

    private var rowSize = lines.size
    private var colSize = lines[0].length

    private fun Position.up() = this.copy(row = if (this.row - 1 == 0) rowSize - 2 else this.row - 1)
    private fun Position.down() = this.copy(row = if (this.row + 1 == rowSize - 1) 1 else this.row + 1)
    private fun Position.left() = this.copy(col = if (this.col - 1 == 0) colSize - 2 else this.col - 1)
    private fun Position.right() = this.copy(col = if (this.col + 1 == colSize - 1) 1 else this.col + 1)

    private fun Position.nextBPos(type: Char): Position {
        return when {
            type.upB() -> this.up()
            type.downB() -> this.down()
            type.leftB() -> this.left()
            type.rightB() -> this.right()
            else -> throw IllegalStateException()
        }
    }

    data class Position(
        val row: Int,
        val col: Int
    ) {
        val blizzards = mutableListOf<Char>()
    }

    private val map = hashSetOf<Position>()

    init {
        for (row in lines.indices) {
            for (col in lines[0].indices) {
                if (!lines[row][col].clearGround() && !lines[row][col].wall()) {
                    map.add(Position(row, col).also { it.blizzards.add(lines[row][col]) })
                }
            }
        }
    }

    private fun HashSet<Position>.moveBlizzard(): HashSet<Position> {
        val newBPositions = hashSetOf<Position>()

        this.map { pos ->
            pos.blizzards.forEach { bl ->
                val (row, col) = pos.nextBPos(bl)
                if (newBPositions.contains(Position(row, col))) {
                    newBPositions.find { it == Position(row, col) }!!.blizzards.add(bl)
                } else {
                    newBPositions.add(
                        Position(row, col).also { it.blizzards.add(bl) }
                    )
                }
            }
        }

        return newBPositions
    }

    private fun Position.inBRange(): Boolean {
        return this.row in 1..rowSize - 2 && this.col in 1..colSize - 2
    }

    private fun HashSet<Position>.moveElve(elvePos: Position, endPos: Position, goingDown: Boolean): List<Position> {
        val positions = mutableListOf<Position>()
        if (!this.contains(elvePos)) positions.add(elvePos) // don't move, if open

        val up = elvePos.copy(row = elvePos.row - 1)
        val down = elvePos.copy(row = elvePos.row + 1)
        val left = elvePos.copy(col = elvePos.col - 1)
        val right = elvePos.copy(col = elvePos.col + 1)

        if ((up.inBRange() || (!goingDown && up == endPos)) && !this.contains(up)) positions.add(up)
        if ((down.inBRange() || (goingDown && down == endPos)) && !this.contains(down)) positions.add(down)
        if (left.inBRange() && !this.contains(left)) positions.add(left)
        if (right.inBRange() && !this.contains(right)) positions.add(right)

        return positions
    }

    fun HashSet<Position>.printMap() {
        for (row in 1..rowSize - 2) {
            for (col in 1..colSize - 2) {
                if (this.contains(Position(row, col)))
                    print(this.find { it == Position(row, col) }!!.blizzards.count())
                else print(".")
                print(" ")
            }
            println()
        }
        println()
        println()
    }

    fun HashSet<Position>.printMap(elvesPos: Position) {
        for (row in 1..rowSize - 2) {
            for (col in 1..colSize - 2) {
                if (this.contains(Position(row, col)))
                    print(this.find { it == Position(row, col) }!!.blizzards.count())
                else if (Position(row, col) == elvesPos)
                    print("E")
                else print(".")
                print(" ")
            }
            println()
        }
        println()
        println()
    }

    private fun lcm(n1: Int, n2: Int): Int {
        fun gcd(): Int {
            var res = minOf(n1, n2)
            while (res > 0) {
                if (n1 % res == 0 && n2 % res == 0) {
                    break
                }
                res--
            }
            return res
        }

        return n1 * n2 / gcd()
    }

    private fun blizzardMaps(maxTime: Int): HashMap<Int, HashSet<Position>> {
        val hashMap = hashMapOf<Int, HashSet<Position>>()
        hashMap[0] = map

        for (time in 1..maxTime) {
            hashMap[time] = hashMap[time - 1]!!.moveBlizzard()
        }

        return hashMap
    }

    private fun findShortestPathInMinutes(
        lcm: Int,
        src: Position,
        dst: Position,
        bMap: HashMap<Int, HashSet<Position>>,
        startTime: Int,
        goingDown: Boolean,
    ): Int {
        data class State(
            val elvePos: Position = src,
            val time: Int = startTime,
        )

        val visited = hashSetOf<State>()

        val queue = ArrayDeque<State>()
        queue.addLast(State())

        while (queue.isNotEmpty()) {
            val currState = queue.removeFirst()

            if (currState.elvePos == dst) {
                return currState.time
            }

            if (visited.contains(currState)) continue
            visited.add(currState)

            val newMap = bMap[(currState.time + 1) % lcm]!!

            newMap.moveElve(elvePos = currState.elvePos, endPos = dst, goingDown = goingDown).forEach {
                val newState = State(elvePos = it, time = currState.time + 1)
                queue.addLast(newState)
            }
        }

        return Int.MAX_VALUE
    }

    fun solve(part1: Boolean): Int {
        val lcm = lcm(rowSize - 2, colSize - 2)
        val blizzardMaps = blizzardMaps(lcm)
        val downTime = findShortestPathInMinutes(
            lcm = lcm,
            src = Position(0, 1),
            dst = Position(rowSize - 1, colSize - 2),
            bMap = blizzardMaps,
            startTime = 0,
            goingDown = true
        )
        if (part1) return downTime
        val backTripTime = findShortestPathInMinutes(
            lcm = lcm,
            src = Position(rowSize - 1, colSize - 2),
            dst = Position(0, 1),
            bMap = blizzardMaps,
            startTime = downTime,
            goingDown = false
        )
        return findShortestPathInMinutes(
            lcm = lcm,
            src = Position(0, 1),
            dst = Position(rowSize - 1, colSize - 2),
            bMap = blizzardMaps,
            startTime = backTripTime,
            goingDown = true
        )
    }
}

fun main() {

    fun solve1(lines: List<String>): Int {
        val valley = Valley(lines = lines)
        return valley.solve(part1 = true)
    }

    fun solve2(lines: List<String>): Int {
        val valley = Valley(lines = lines)
        return valley.solve(part1 = false)
    }

    val input = readInput("/day24/Day24")
    println(solve1(input))
    println(solve2(input))
}
