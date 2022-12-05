package day05

import readInput
import java.lang.StringBuilder

fun main() {

    fun getMap(input:List<String>):HashMap<Int,ArrayDeque<Char>> {
        val crates = input.takeWhile { it.isNotEmpty() }

        val stackIndexMap = hashMapOf<Int,Int>().also { map ->
            crates.last().forEachIndexed { index, c ->
                if (c.isDigit()) {
                    map[index] = c.digitToInt()
                }
            }
        }

        val hashMap = hashMapOf<Int, ArrayDeque<Char>>()
        crates.filterIndexed { index, _ -> index != crates.size - 1 }
            .forEachIndexed { _, s ->
                s.forEachIndexed { index, c ->
                    if (c.isLetter()){
                        val posn = stackIndexMap[index]!!
                        if (hashMap.containsKey(posn)){
                            val dequeue = hashMap[posn]
                            dequeue!!.addLast(c)
                        } else {
                            hashMap[posn] = ArrayDeque<Char>().also {
                                it.addLast(c)
                            }
                        }
                    }
                }
            }
        return hashMap
    }

    fun solvePart1(input: List<String>): String {
        val hashMap = getMap(input)
        val procedure = input.takeLastWhile { it.isNotEmpty() }
        procedure.forEach {
            val (move, from, to) = it.split(" ")
                .filter {
                    it.toIntOrNull() != null
                }.map { it.toInt() }
            hashMap[to] = hashMap[to]!!.also { dequeue ->
                var copyMove = move
                while (copyMove > 0){
                    dequeue.addFirst(hashMap[from]!!.removeFirst())
                    --copyMove
                }
            }
        }

        val strBuilder = StringBuilder()
        for ((_,value ) in hashMap){
            strBuilder.append(value.removeFirst())
        }
        return strBuilder.toString()
    }

    fun solvePart2(input: List<String>): String {
        val hashMap = getMap(input)
        val procedure = input.takeLastWhile { it.isNotEmpty() }
        procedure.forEach {
            val (move, from, to) = it.split(" ")
                .filter {
                    it.toIntOrNull() != null
                }.map { it.toInt() }
            hashMap[to] = hashMap[to]!!.also { dequeue ->
                var copyMove = move
                val newDequeue = ArrayDeque<Char>()
                while (copyMove > 0){
                    newDequeue.addFirst(hashMap[from]!!.removeFirst())
                    --copyMove
                }

                while (newDequeue.isNotEmpty()){
                    dequeue.addFirst(newDequeue.removeFirst())
                }
            }
        }

        val strBuilder = StringBuilder()
        for ((_,value ) in hashMap){
            strBuilder.append(value.removeFirst())
        }

        return strBuilder.toString()
    }

    val input = readInput("/day05/Day05")
    println(solvePart1(input))
    println(solvePart2(input))

}
