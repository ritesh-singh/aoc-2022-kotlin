package day20

import readInput
import java.util.UUID

data class Node(
    var uuid: UUID,
    var value: Long,
    var next: Node? = null,
    var prev: Node? = null
) {
    override fun toString(): String {
        return value.toString()
    }
}

class CircularLinkedList {

    private val listOfPointers = mutableListOf<Node>()

    private var zeroNode: Node? = null

    fun initList(input: List<String>, mulBy: Int) {
        var head: Node? = null
        var tail: Node? = null

        var curP: Node? = null
        for (idx in input.indices) {
            val value = input[idx].trim().toLong() * mulBy
            if (idx == 0) {
                head = Node(value = value, uuid = UUID.randomUUID())
                listOfPointers.add(head)
                curP = head

                if (head.value == 0L) zeroNode = head

                continue
            }
            val newNode = Node(value = value, uuid = UUID.randomUUID())
            curP!!.next = newNode
            newNode.prev = curP
            curP = newNode

            if (idx == input.size - 1) tail = newNode

            if (newNode.value == 0L) zeroNode = newNode
            listOfPointers.add(newNode)
        }
        head!!.prev = tail
        tail!!.next = head
    }

    fun mix(times: Int) {
        repeat(times) {
            listOfPointers.forEach {
                val itemToMove = it
                if (itemToMove.value == 0L) return@forEach

                var positionsToMove = itemToMove.value.mod(listOfPointers.size - 1)

                var insertAfter = itemToMove

                while (positionsToMove-- > 0) {
                    insertAfter = insertAfter.next!!
                }

                // delete
                itemToMove.prev!!.next = itemToMove.next
                itemToMove.next!!.prev = itemToMove.prev

                // insert in between node
                itemToMove.prev = insertAfter
                itemToMove.next = insertAfter.next
                insertAfter.next!!.prev = itemToMove
                insertAfter.next = itemToMove
            }
        }
    }

    fun answer(): Long {
        val list = listOf(1000, 2000, 3000)
        var sum = 0L
        list.forEach {
            var count = 1
            var temp = zeroNode
            while (count <= it) {
                temp = temp!!.next
                count++
            }
            sum += temp!!.value
        }
        return sum
    }
}

fun main() {

    fun solve(input: List<String>, part1: Boolean = true): Long {
        return CircularLinkedList().let {
            it.initList(input, if (part1) 1 else 811589153)
            it.mix(if (part1) 1 else 10)
            it.answer()
        }
    }

    val input = readInput("/day20/Day20")
    println(solve(input, part1 = true))
    println(solve(input, part1 = false))

}
