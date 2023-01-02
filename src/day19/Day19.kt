package day19

import readInput

private class RobotFactory(lines: List<String>) {

    private data class BluePrint(
        val id: Int,
        val oreRReq: Int, // ore
        val clayRReq: Int, // ore
        val obsidianRReq: Pair<Int, Int>, // ore and clay
        val geodeRReq: Pair<Int, Int> // ore and obsidian
    )

    private val bluePrints = mutableListOf<BluePrint>()

    init {
        lines.forEach { line ->
            val chunks = line.split(".")
            bluePrints.add(
                BluePrint(
                    id = line.substringBefore(":").substringAfter("Blueprint").trim().toInt(),
                    oreRReq = chunks[0].substringAfter("costs").substringBefore("ore").trim().toInt(),
                    clayRReq = chunks[1].substringAfter("costs").substringBefore("ore").trim().toInt(),
                    obsidianRReq = Pair(
                        chunks[2].substringAfter("costs").substringBefore("ore").trim().toInt(),
                        chunks[2].substringAfter("and").substringBefore("clay").trim().toInt()
                    ),
                    geodeRReq = Pair(
                        chunks[3].substringAfter("costs").substringBefore("ore").trim().toInt(),
                        chunks[3].substringAfter("and").substringBefore("obsidian").trim().toInt()
                    )
                )
            )
        }
    }

    private data class State(
        var ore: Int = 0,
        var clay: Int = 0,
        var obsidian: Int = 0,
        var geodes: Int = 0,
        var oreRobot: Int = 1,
        var clayRobot: Int = 0,
        var obsidianRobot: Int = 0,
        var geodeRobot: Int = 0,
        var time: Int = 24
    )

    private fun largestGeodeOpened(bluePrint: BluePrint): Int {
        val queue = ArrayDeque<State>()
        queue.addLast(State())

        var maxGeodes = Int.MIN_VALUE

        val seen = hashSetOf<State>()

        while (queue.isNotEmpty()) {
            val currState = queue.removeFirst()
            if (seen.contains(currState))
                continue
            seen.add(currState)
            if (currState.time <= 0) {
                maxGeodes = maxOf(maxGeodes, currState.geodes)
                continue
            }
            queue.addLast(
                currState.copy(
                    ore = currState.ore + currState.oreRobot,
                    clay = currState.clay + currState.clayRobot,
                    obsidian = currState.obsidian + currState.obsidianRobot,
                    geodes = currState.geodes + currState.geodeRobot,
                    time = currState.time - 1
                )
            )

            if (currState.ore >= bluePrint.oreRReq) {
                queue.addLast(
                    currState.copy(
                        ore = currState.ore - bluePrint.oreRReq + currState.oreRobot,
                        clay = currState.clay + currState.clayRobot,
                        obsidian = currState.obsidian + currState.obsidianRobot,
                        geodes = currState.geodes + currState.geodeRobot,
                        oreRobot = currState.oreRobot + 1,
                        time = currState.time - 1
                    )
                )
            }

            if (currState.ore >= bluePrint.clayRReq) {
                queue.addLast(
                    currState.copy(
                        ore = currState.ore - bluePrint.clayRReq + currState.oreRobot,
                        clay = currState.clay + currState.clayRobot,
                        obsidian = currState.obsidian + currState.obsidianRobot,
                        geodes = currState.geodes + currState.geodeRobot,
                        clayRobot = currState.clayRobot + 1,
                        time = currState.time - 1
                    )
                )
            }

            if (currState.ore >= bluePrint.obsidianRReq.first && currState.clay >= bluePrint.obsidianRReq.second) {
                queue.addLast(
                    currState.copy(
                        ore = currState.ore - bluePrint.obsidianRReq.first + currState.oreRobot,
                        clay = currState.clay - bluePrint.obsidianRReq.second + currState.clayRobot,
                        obsidian = currState.obsidian + currState.obsidianRobot,
                        geodes = currState.geodes + currState.geodeRobot,
                        obsidianRobot = currState.obsidianRobot + 1,
                        time = currState.time - 1
                    )
                )
            }

            if (currState.ore >= bluePrint.geodeRReq.first && currState.obsidian >= bluePrint.geodeRReq.second) {
                queue.addLast(
                    currState.copy(
                        ore = currState.ore  - bluePrint.geodeRReq.first + currState.oreRobot,
                        obsidian = currState.obsidian - bluePrint.geodeRReq.second + currState.obsidianRobot,
                        clay = currState.clay + currState.clayRobot,
                        geodes = currState.geodes + currState.geodeRobot,
                        geodeRobot = currState.geodeRobot + 1,
                        time = currState.time - 1
                    )
                )
            }
        }

        return maxGeodes
    }

    fun answer() {
        println(
            bluePrints.map {
                it.id * largestGeodeOpened(it)
            }.sum()
        )
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val robotFactory = RobotFactory(lines = input)
        robotFactory.answer()

        return 0
    }


    val testInput = readInput("/day19/Day19_test")
//    println(part1(testInput))

    println("-----------------------------")

    val input = readInput("/day19/Day19")
    println(part1(input))

}

