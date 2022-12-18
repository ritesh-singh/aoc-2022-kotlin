package day15

import readInput
import java.awt.Point
import kotlin.math.abs

fun main() {

    data class SensorBeacon(
        val sensor: Point,
        val beacon: Point,
        val manhattanDistance: Int
    )

    fun parseInput(input: List<String>): List<SensorBeacon> {
        val sensorBeacons = mutableListOf<SensorBeacon>()
        input.forEach {
            it.split(":").map { it.trim() }.map {
                it.substringAfter("at").trim()
            }.let {
                val (sx, sy) = it[0].split(",").mapIndexed { index, s ->
                    if (index == 0) s.trim().substringAfter("x=").trim().toInt()
                    else s.trim().substringAfter("y=").trim().toInt()
                }

                val (bx, by) = it[1].split(",").mapIndexed { index, s ->
                    if (index == 0) s.trim().substringAfter("x=").trim().toInt()
                    else s.trim().substringAfter("y=").trim().toInt()
                }

                val dist = abs(sx - bx) + abs(sy - by)

                sensorBeacons.add(
                    SensorBeacon(
                        sensor = Point(sx, sy),
                        beacon = Point(bx, by),
                        manhattanDistance = dist
                    )
                )
            }
        }
        return sensorBeacons
    }

    fun part1(input: List<String>, Y: Int): Int {
        val sensorBeacons = parseInput(input)
        val intersectionsRange = mutableListOf<IntRange>()

        sensorBeacons.forEach {
            val x = it.manhattanDistance - abs(Y - it.sensor.y)
            // intersection distance
            if (x > 0) intersectionsRange.add(it.sensor.x - x..it.sensor.x + x)
        }

        var set: Set<Int> = emptySet()
        for (i in 0 until intersectionsRange.size) {
            set = set.union(intersectionsRange[i])
        }

        return set.size - 1 // -1, exclude beacon in the row
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("/day15/Day15_test")
//    println(part1(testInput, 10))
    println(part2(testInput))

    println("----------------------")

    val input = readInput("/day15/Day15")
//    println(part1(input, 2000000))
    println(part2(input))

}
