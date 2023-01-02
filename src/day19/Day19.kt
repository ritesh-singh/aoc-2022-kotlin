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

    private fun largestGeodeOpened(bluePrint: BluePrint, part1:Boolean): Int {
        val queue = ArrayDeque<State>()
        queue.addLast(State().copy(time = if (part1) 24 else 32))

        var maxGeodes = Int.MIN_VALUE

        val visited = hashSetOf<State>()

        // bfs on all possible states
        while (queue.isNotEmpty()) {
            var currState = queue.removeFirst()
            if (visited.contains(currState))
                continue
            visited.add(currState)
            if (currState.time <= 0) {
                maxGeodes = maxOf(maxGeodes, currState.geodes)
                continue
            }

            // Reduce state by removing robots required per minute
            val maxOresRequired = listOf(bluePrint.oreRReq, bluePrint.clayRReq, bluePrint.obsidianRReq.first, bluePrint.geodeRReq.first).max() // find max ores required for a robot
            if (currState.oreRobot >= maxOresRequired) { // max ores which can be utilized for building any type of robot per minute
                currState = currState.copy(
                    oreRobot = maxOresRequired
                )
            }
            if (currState.clayRobot >= bluePrint.obsidianRReq.second) { // max clays which can be utilized for building any type of robot per minute
                currState = currState.copy(
                    clayRobot = bluePrint.obsidianRReq.second
                )
            }
            if (currState.obsidianRobot >= bluePrint.geodeRReq.second) { // max obsidian which can be utilized for building any type of robot per minute
                currState = currState.copy(
                    obsidianRobot = bluePrint.geodeRReq.second
                )
            }

            // Reduce state by removing resources not required per minute
            if (currState.ore >= currState.time * maxOresRequired - currState.oreRobot * (currState.time - 1)) {
                currState = currState.copy(
                    ore = currState.time * maxOresRequired - currState.oreRobot * (currState.time - 1)
                )
            }
            if (currState.clay >= currState.time * bluePrint.obsidianRReq.second - currState.clayRobot * (currState.time - 1)) {
                currState = currState.copy(
                    clay = currState.time * bluePrint.obsidianRReq.second - currState.clayRobot * (currState.time - 1)
                )
            }
            if (currState.obsidian >= currState.time * bluePrint.geodeRReq.second - currState.obsidianRobot *(currState.time-1)){
                currState = currState.copy(
                    obsidian = currState.time * bluePrint.geodeRReq.second - currState.obsidianRobot * (currState.time - 1)
                )
            }

            queue.addLast( // don't buy any robot, just collect resources
                currState.copy(
                    ore = currState.ore + currState.oreRobot,
                    clay = currState.clay + currState.clayRobot,
                    obsidian = currState.obsidian + currState.obsidianRobot,
                    geodes = currState.geodes + currState.geodeRobot,
                    time = currState.time - 1
                )
            )

            if (currState.ore >= bluePrint.oreRReq) { // buy ore robot from prev robots and collect resources
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

            if (currState.ore >= bluePrint.clayRReq) { // buy clay robot from prev robots and collect resources
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
                queue.addLast( // buy obsidian robot from prev robots and collect resources
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
                queue.addLast( // buy geode robot from prev robots and collect resources
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

    fun answer(part1: Boolean):Int {
        if (part1) {
            return bluePrints.sumOf {
                it.id * largestGeodeOpened(it, true)
            }
        }
        return bluePrints.take(3)
            .map {
                largestGeodeOpened(it, false)
            }.fold(1) { a, b -> a * b }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val robotFactory = RobotFactory(lines = input)
        return robotFactory.answer(true)
    }


    fun part2(input: List<String>): Int {
        val robotFactory = RobotFactory(lines = input)
        return robotFactory.answer(false)
    }

    val input = readInput("/day19/Day19")
    println(part1(input))
    println(part2(input))

}

