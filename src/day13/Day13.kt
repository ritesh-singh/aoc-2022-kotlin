package day13

import readInput
import java.util.*

sealed class Packet {

    data class ListsPacket(val packet: MutableList<Packet>) : Packet() {
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

    fun packetInRightOrder(
        leftPacket: Packet.ListsPacket,
        rightPacket: Packet.ListsPacket,
    ): Pair<Boolean,Boolean> {
        var lCounter = 0
        var rCounter = 0
        while (lCounter < leftPacket.packet.size && rCounter < rightPacket.packet.size) {
            val f = leftPacket.packet[lCounter]
            val s = rightPacket.packet[rCounter]
            if (f is Packet.IntegerPacket && s is Packet.IntegerPacket) {
                if (f.packet < s.packet) return Pair(true, true)
                if (f.packet > s.packet) return Pair(false,true)
                lCounter++
                rCounter++
                continue
            }
            if (f is Packet.IntegerPacket && s is Packet.ListsPacket) {
                lCounter++
                rCounter++
                val pair = packetInRightOrder(f.toListPacket(), s)
                if (pair.second) return pair
            }
            if (f is Packet.ListsPacket && s is Packet.IntegerPacket) {
                lCounter++
                rCounter++
                val pair = packetInRightOrder(f, s.toListPacket())
                if (pair.second) return pair
            }
            if (f is Packet.ListsPacket && s is Packet.ListsPacket) {
                lCounter++
                rCounter++
                val pair = packetInRightOrder(f, s)
                if (pair.second) return pair
            }
        }

        return if (lCounter == leftPacket.packet.size && rCounter != rightPacket.packet.size) {
            Pair(true,true)
        } else if (lCounter != leftPacket.packet.size && rCounter == rightPacket.packet.size) {
            Pair(false,true)
        } else {
            Pair(false,false)
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
                if (rightOrder.first) {
                    sum += index + 1
                }
            }

        return sum
    }

    fun part2(input: List<String>): Int {
        val decoderKey1 = (buildPacket("[[2]]") as Packet.ListsPacket).packet.first() as Packet.ListsPacket
        val decoderKey2 = (buildPacket("[[6]]") as Packet.ListsPacket).packet.first() as Packet.ListsPacket
        val list = input.filter { it.isNotEmpty() }
            .map {
                (buildPacket(it) as Packet.ListsPacket).packet.first() as Packet.ListsPacket
            }.toMutableList().apply {
                add(decoderKey1)
                add(decoderKey2)
            }
        list.sortWith { o1, o2 ->
            val res = packetInRightOrder(o1, o2)
            if (res.first) -1 else 1
        }
        return (list.indexOf(decoderKey1)+1) * (list.indexOf(decoderKey2)+1)
    }

    val testInput = readInput("/day13/Day13_test")
    println(part1(testInput))
    println(part2(testInput))
    println("-----------------------------------------------")

    val input = readInput("/day13/Day13")
    println(part1(input))
    println(part2(input))

}
