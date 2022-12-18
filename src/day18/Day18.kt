package day18

import readInput
import java.util.Stack

fun main() {

    data class Coord(
        val x: Int,
        val y: Int,
        val z: Int
    )

    fun List<String>.toCoordsSet(): Set<Coord> {
        return buildSet {
            this@toCoordsSet.forEach {
                add(
                    it.split(",").map {
                        it.trim().toInt()
                    }.let {
                        Coord(it[0], it[1], it[2])
                    }
                )
            }
        }
    }

    fun part1(input: List<String>): Int {
        val cubes = input.toCoordsSet()

        var sf = 0
        cubes.forEach {
            // check for all 6 faces, if they are not adjacents to other cubes
            if (!cubes.contains(Coord(it.x + 1, it.y, it.z))) sf++
            if (!cubes.contains(Coord(it.x - 1, it.y, it.z))) sf++
            if (!cubes.contains(Coord(it.x, it.y + 1, it.z))) sf++
            if (!cubes.contains(Coord(it.x, it.y - 1, it.z))) sf++
            if (!cubes.contains(Coord(it.x, it.y, it.z + 1))) sf++
            if (!cubes.contains(Coord(it.x, it.y, it.z - 1))) sf++
        }

        return sf
    }

    fun part2(input: List<String>): Int {
        val cubes = input.toCoordsSet()

        val minCoord = Coord(x = cubes.minOf { it.x }, y = cubes.minOf { it.y }, z = cubes.minOf { it.z })
        val maxCoord = Coord(x = cubes.maxOf { it.x }, y = cubes.maxOf { it.y }, z = cubes.maxOf { it.z })

        fun Coord.external() = x < minCoord.x || y < minCoord.y || z < minCoord.z
                || x > maxCoord.x || y > maxCoord.y || z > maxCoord.z

        fun Coord.neighbors() = listOf(
            copy(x = x + 1),
            copy(x = x - 1),
            copy(y = y + 1),
            copy(y = y - 1),
            copy(z = z + 1),
            copy(z = z - 1),
        )

        fun canMoveOut(src: Coord): Boolean {
            val stack = Stack<Coord>()
            stack.push(src)

            val visited = hashSetOf<Coord>()
            visited.add(src)

            while (stack.isNotEmpty()) {
                val current = stack.pop()
                if (cubes.contains(current)) continue

                if (current.external()) return true

                for (neighbor in current.neighbors()) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor)
                        stack.push(neighbor)
                    }
                }
            }
            return false
        }

        var sf = 0
        cubes.forEach {
            if (canMoveOut(Coord(it.x + 1, it.y, it.z))) sf++
            if (canMoveOut(Coord(it.x - 1, it.y, it.z))) sf++
            if (canMoveOut(Coord(it.x, it.y + 1, it.z))) sf++
            if (canMoveOut(Coord(it.x, it.y - 1, it.z))) sf++
            if (canMoveOut(Coord(it.x, it.y, it.z + 1))) sf++
            if (canMoveOut(Coord(it.x, it.y, it.z - 1))) sf++
        }

        return sf
    }

    val input = readInput("/day18/Day18")
    println(part1(input))
    println(part2(input))


}
