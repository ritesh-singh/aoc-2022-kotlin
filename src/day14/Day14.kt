package day14

import readInput
import java.awt.Point
import java.util.Stack


fun main() {

    val rocksPositionSet = hashSetOf<Point>()
    val sandPositionSet = hashSetOf<Point>()

    var minX = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var maxY = Int.MIN_VALUE

    fun resetGlobalStuff() {
        rocksPositionSet.clear()
        sandPositionSet.clear()
        minX = Int.MAX_VALUE
        maxX = Int.MIN_VALUE
        maxY = Int.MIN_VALUE
    }

    fun parseInput(input: List<String>) {
        resetGlobalStuff()
        input.forEach {
            it.split("->").map { it.trim() }.windowed(2)
                .forEach {
                    val from = it[0].split(",").map { it.trim().toInt() }.let { Point(it[0], it[1]) }
                    val to = it[1].split(",").map { it.trim().toInt() }.let { Point(it[0], it[1]) }
                    maxX = maxOf(maxX, maxOf(from.x, to.x))
                    minX = minOf(minX, minOf(from.x, to.x))
                    maxY = maxOf(maxY, maxOf(from.y, to.y))

                    if (from.x == to.x) { // horizontal
                        var colRange = from.y..to.y
                        if (to.y < from.y) colRange = to.y..from.y
                        for (col in colRange) rocksPositionSet.add(Point(from.x, col))
                    } else { // vertical
                        var rowRange = from.x..to.x
                        if (to.x < from.x) rowRange = to.x..from.x
                        for (row in rowRange) rocksPositionSet.add(Point(row, to.y))
                    }
                }
        }
    }

    fun part1(input: List<String>): Int {
        parseInput(input)
        fun produceSand(currentPosition: Point): Int {
            if (currentPosition.y > maxY || currentPosition.x < minX || currentPosition.x > maxX) return -1

            val down = Point(currentPosition.x, currentPosition.y + 1)
            if (!rocksPositionSet.contains(down) && !sandPositionSet.contains(down)) return produceSand(down)

            val downleft = Point(currentPosition.x - 1, currentPosition.y + 1)
            if (!rocksPositionSet.contains(downleft) && !sandPositionSet.contains(downleft)) return produceSand(downleft)

            val downRight = Point(currentPosition.x + 1, currentPosition.y + 1)
            if (!rocksPositionSet.contains(downRight) && !sandPositionSet.contains(downRight)) return produceSand(downRight)

            sandPositionSet.add(currentPosition)
            return 0
        }

        while (true) {
            val value = produceSand(Point(500, 0))
            if (value == -1) break
        }

        return sandPositionSet.size
    }

    fun part2(input: List<String>): Int {
        parseInput(input)
        maxY += 2

        fun dfs(posn: Point): Point {
            val stack = Stack<Point>()
            stack.push(posn)

            while (stack.isNotEmpty()) {
                val curr = stack.pop()

                val down = Point(curr.x, curr.y + 1)
                val downleft = Point(curr.x - 1, curr.y + 1)
                val downRight = Point(curr.x + 1, curr.y + 1)
                when {
                    !rocksPositionSet.contains(down) && !sandPositionSet.contains(down) && down.y < maxY -> stack.push(down)
                    !rocksPositionSet.contains(downleft) && !sandPositionSet.contains(downleft) && downleft.y < maxY -> stack.push(downleft)
                    !rocksPositionSet.contains(downRight) && !sandPositionSet.contains(downRight) && downRight.y < maxY -> stack.push(downRight)
                    else -> sandPositionSet.add(curr)
                }

                if (down == Point(500, 0) || downleft == Point(500, 0) || downRight == Point(500, 0)) return Point(500, 0)
            }

            return posn
        }

        while (true){
            dfs(Point(500,0))
            val downBlocker = Point(500,1)
            val leftBlocker = Point(499,1)
            val rightBlocker = Point(501,1)
            if (
                (rocksPositionSet.contains(downBlocker) || sandPositionSet.contains(downBlocker))
                &&
                (rocksPositionSet.contains(leftBlocker) || sandPositionSet.contains(leftBlocker))
                &&
                (rocksPositionSet.contains(rightBlocker) || sandPositionSet.contains(rightBlocker))
            ) break
        }

        return sandPositionSet.size + 1
    }


    val testInput = readInput("/day14/Day14_test")
    println(part1(testInput))
    println(part2(testInput))

    println("----------------------")

    val input = readInput("/day14/Day14")
    println(part1(input))
    println(part2(input))
}
