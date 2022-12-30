package day16

import readInput

class Graph(lines: List<String>) {
    private data class Valve(val name: String, val flowRate: Int) {
        override fun toString() = "$name($flowRate)"
    }

    private val graph = hashMapOf<Valve, List<Valve>>()
    private var valvesWithPosFlowRates = hashSetOf<Valve>()

    init {
        lines.forEach { string ->
            val name = string.substringAfter("Valve").substringBefore("has").trim()
            val flowRate = string.substringAfter("flow rate=").substringBefore(";").trim().toInt()
            graph[Valve(name = name, flowRate = flowRate)] = if (string.contains("valves")) {
                string.substringAfter("valves").trim().split(",").map { it.trim() }
                    .map { Valve(name = it, flowRate = flowRate) }
            } else {
                string.substringAfter("valve").trim().split(",").map { it.trim() }.map { Valve(name = it, flowRate) }
            }.toList()
        }
        graph.forEach { (key, canReachTo) ->
            graph[key] =
                canReachTo.map { valve -> valve.copy(flowRate = graph.keys.find { it.name == valve.name }!!.flowRate) }
            if (key.flowRate > 0) valvesWithPosFlowRates.add(key)
        }
    }

    private val shortestDistMap = hashMapOf<Pair<Valve, Valve>, Int>()

    private fun findShortestDistance(src: Valve, dst: Valve): Int {
        if (shortestDistMap.contains(Pair(src, dst))) return shortestDistMap[Pair(src, dst)]!!

        val deque = ArrayDeque<Pair<Valve, Int>>()
        deque.addLast(Pair(src, 0))

        val visited = hashSetOf<Valve>()
        while (deque.isNotEmpty()) {
            val (valve, dist) = deque.removeFirst()
            if (dst == valve) return dist
            visited.add(valve)
            for (neighbor in graph[valve]!!) {
                if (!visited.contains(neighbor)) {
                    deque.addLast(Pair(neighbor, dist + 1))
                }
            }
        }
        error("Destination node un-reachable")
    }

    fun maxPressureReleased(): Int {
        data class State(
            val minutesLeft: Int,
            val openedValves: HashSet<Valve>,
            val currentValve: Valve,
            val pressureReleased: Int,
        )

        var maxPressureRelieved = Int.MIN_VALUE

        val stateQueue = ArrayDeque<State>()
        stateQueue.addLast(
            State(
                minutesLeft = 30,
                openedValves = hashSetOf(),
                currentValve = Valve(name = "AA", flowRate = 0),
                pressureReleased = 0
            )
        )

        while (stateQueue.isNotEmpty()) {
            val currentState = stateQueue.removeFirst()
            if (currentState.openedValves.size == valvesWithPosFlowRates.size || currentState.minutesLeft <= 0) {
                maxPressureRelieved = maxOf(maxPressureRelieved, currentState.pressureReleased)
                continue
            }
            for (valvesToOpen in valvesWithPosFlowRates - currentState.openedValves) {
                val shortPath = findShortestDistance(currentState.currentValve, valvesToOpen)
                val state = State(
                    minutesLeft = currentState.minutesLeft - shortPath - 1,
                    openedValves = hashSetOf<Valve>().apply {
                        add(valvesToOpen)
                        addAll(currentState.openedValves)
                    },
                    currentValve = valvesToOpen,
                    pressureReleased = currentState.pressureReleased + ((currentState.minutesLeft - shortPath - 1) * valvesToOpen.flowRate)
                )
                stateQueue.addLast(state)
            }
        }

        return maxPressureRelieved
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val graph = Graph(lines = input)
        return graph.maxPressureReleased()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("/day16/Day16_test")
    println(part1(testInput))

    println("----------------------")

    val input = readInput("/day16/Day16")
    println(part1(input))

}

