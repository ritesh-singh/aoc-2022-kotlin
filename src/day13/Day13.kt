package day13

import readInput
import java.util.Stack

sealed class Packet {

    data class ListsPacket(val packet: MutableList<Packet>) : Packet() {
        fun isEmpty() = packet.isEmpty()
        override fun toString(): String {
            return packet.toString()
        }
    }

    data class IntegerPacket(val packet: Int) : Packet() {
        fun toList() = ListsPacket(packet = mutableListOf(this))
        override fun toString(): String {
            return packet.toString()
        }
    }
}

fun main() {

    fun buildPacket(packet: String): Packet {
        val stack = Stack<Packet>()
        stack.push(Packet.ListsPacket(mutableListOf()))

        var dupPacket = packet
        while (dupPacket.isNotEmpty()) {
            when (dupPacket[0]) {
                '[' -> {
                    stack.push(Packet.ListsPacket(mutableListOf()))
                    dupPacket = dupPacket.substring(1)
                }
                ']' -> {
                    val topItem = stack.pop()
                    (stack.peek() as Packet.ListsPacket).packet.add(topItem)
                    dupPacket = dupPacket.substring(1)
                }
                ',' -> {
                    dupPacket = dupPacket.substring(1)
                }
                else -> {
                    // it's a digit
                    val digString = StringBuilder()
                    var i = 0
                    while (dupPacket[i].isDigit()) {
                        digString.append(dupPacket[i++])
                    }
                    (stack.peek() as Packet.ListsPacket).packet.add(Packet.IntegerPacket(digString.toString().toInt()))
                    dupPacket = dupPacket.substring(digString.length)
                }
            }
        }
        return stack.pop()
    }

    fun Packet.toListPacket(): Packet.ListsPacket {
        if (this is Packet.IntegerPacket)
            return this.toList()
        return this as Packet.ListsPacket
    }

    data class ReturnType(val rightOrder: Boolean, val breakOut: Boolean)

    fun packetInRightOrder(
        leftPacket: Packet.ListsPacket,
        rightPacket: Packet.ListsPacket,
    ): ReturnType {
        while (leftPacket.packet.isNotEmpty() && rightPacket.packet.isNotEmpty()) {
            val f = leftPacket.packet.first()
            val s = rightPacket.packet.first()
            if (f is Packet.IntegerPacket && s is Packet.IntegerPacket) {
                if (f.packet < s.packet) return ReturnType(rightOrder = true, breakOut = true)
                if (f.packet > s.packet) return ReturnType(rightOrder = false, breakOut = true)
                leftPacket.packet.removeFirst()
                rightPacket.packet.removeFirst()
                continue
            }
            if (f is Packet.IntegerPacket && s is Packet.ListsPacket) {
                leftPacket.packet.removeFirst()
                rightPacket.packet.removeFirst()
                val pair = packetInRightOrder(f.toListPacket(), s)
                if (pair.breakOut) return pair
            }
            if (f is Packet.ListsPacket && s is Packet.IntegerPacket) {
                leftPacket.packet.removeFirst()
                rightPacket.packet.removeFirst()
                val pair = packetInRightOrder(f, s.toListPacket())
                if (pair.breakOut) return pair
            }
            if (f is Packet.ListsPacket && s is Packet.ListsPacket) {
                leftPacket.packet.removeFirst()
                rightPacket.packet.removeFirst()
                val pair = packetInRightOrder(f, s)
                if (pair.breakOut) return pair
            }
        }

        return if (leftPacket.packet.isEmpty() && rightPacket.packet.isNotEmpty()) {
            ReturnType(rightOrder = true, breakOut = true)
        } else if (leftPacket.packet.isNotEmpty()) {
            ReturnType(rightOrder = false, breakOut = true)
        } else {
            ReturnType(rightOrder = false, breakOut = false)
        }
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        input.filter { it.isNotEmpty() }
            .windowed(2, step = 2)
            .forEachIndexed { index, list ->
                val (left, right) = list.map { buildPacket(it) }
                val rightOrder = packetInRightOrder(
                    (left as Packet.ListsPacket).packet.first() as Packet.ListsPacket,
                    (right as Packet.ListsPacket).packet.first() as Packet.ListsPacket
                )
                if (rightOrder.rightOrder) {
                    sum += index + 1
                }
            }

        return sum
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("/day13/Day13_test")
    println(part1(testInput))

    println("-----------------------------------------------")

    val input = readInput("/day13/Day13")
    println(part1(input))

}
