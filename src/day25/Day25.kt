package day25

import readInput
import kotlin.math.pow

// Only part 1 is done, as all previous stars are needed for part2

fun main() {

    fun convertToDecimal(lines: List<String>): Long {
        var total = 0L
        lines.forEach {
            var decimal = 0L
            it.forEachIndexed { index, c ->
                when {
                    c.isDigit() -> {
                        decimal += c.digitToInt() * 5.0.pow(it.length - 1 - index).toLong()
                    }
                    c == '-' -> {
                        decimal += -1 * 5.0.pow(it.length - 1 - index).toLong()
                    }
                    c == '=' -> {
                        decimal += -2 * 5.0.pow(it.length - 1 - index).toLong()
                    }
                }
            }
            total += decimal
        }
        return total
    }


    fun convertToSNAFU(decimal: Long):String {
        fun convertToBase5():String {
            val base5 = StringBuilder()
            var number = decimal
            while (number > 0L){
                base5.append(number.mod(5))
                number /= 5
            }
            return base5.toString().reversed()
        }
        var base5 = convertToBase5()
        for(idx in base5.length-1 downTo 0){
            when {
                base5[idx] == '3' -> {
                    if (idx == 0){
                        base5 = base5.replaceRange(idx..idx,"1=")
                        continue
                    }
                    base5 = base5.replaceRange(idx..idx,"=")
                    base5 = base5.replaceRange(idx-1 until idx,(base5[idx-1].digitToInt()+1).toString())
                }
                base5[idx] == '4' -> {
                    if (idx == 0){
                        base5 = base5.replaceRange(idx..idx,"1-")
                        continue
                    }
                    base5 = base5.replaceRange(idx..idx,"-")
                    base5 = base5.replaceRange(idx-1 until idx,(base5[idx-1].digitToInt()+1).toString())
                }
                base5[idx].digitToInt() > 4 -> {
                    if (idx == 0){
                        base5 = base5.replaceRange(idx..idx,"10")
                        continue
                    }
                    base5 = base5.replaceRange(idx..idx,"0")
                    base5 = base5.replaceRange(idx-1 until idx,(base5[idx-1].digitToInt()+1).toString())
                }
            }
        }

        return base5
    }

    fun solve1(lines: List<String>): String {
        val decimal = convertToDecimal(lines)
        return convertToSNAFU(decimal)
    }

    fun solve2(lines: List<String>): Int {
        return 0
    }

    val testInput = readInput("/day25/Day25_test")
    println(solve1(testInput))
//    println(solve2(testInput))

    println("--------------------------------------")

    val input = readInput("/day25/Day25")
    println(solve1(input))
//    println(solve2(input))

}
