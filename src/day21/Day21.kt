package day21

import readInput
import java.util.*


enum class Operator {
    PLUS,
    MINUS,
    DIVIDE,
    MUL
}

data class Node(
    val name: String,
    var value: Long? = null,
    var operator: Operator? = null,
    var left: Node? = null,
    var right: Node? = null
) {
    override fun toString(): String {
        return "name -> $name value -> $value"
    }
}

class MonkeyExpressionTree {
    private var root: Node? = null

    private fun operator(op: String): Operator {
        return when (op) {
            "+" -> Operator.PLUS
            "-" -> Operator.MINUS
            "*" -> Operator.MUL
            "/" -> Operator.DIVIDE
            else -> throw IllegalStateException()
        }
    }

    private fun buildTree(inputMap: Map<String, String>) {
        root = Node(name = "root")
        val stack = Stack<Node>()
        stack.push(root)
        while (stack.isNotEmpty()) {
            val current = stack.pop()
            val value  = inputMap[current.name]!!
            when {
                value.toIntOrNull() != null -> current.value = value.toLong()
                else -> {
                    val (operand1, operator, operand2) = value.split(" ").map { it.trim() }
                    current.left = Node(name = operand1)
                    current.right = Node(name = operand2)
                    current.operator = operator(operator)
                    stack.push(current.left)
                    stack.push(current.right)
                }
            }
        }
    }

    fun evaluateMonkeyTree(): Long {
        fun eval(node: Node): Long {
            if (node.value != null) return node.value!!
            return when (node.operator) {
                Operator.MUL -> eval(node.left!!) * eval(node.right!!)
                Operator.DIVIDE -> eval(node.left!!) / eval(node.right!!)
                Operator.PLUS -> eval(node.left!!) + eval(node.right!!)
                Operator.MINUS -> eval(node.left!!) - eval(node.right!!)
                else -> throw UnsupportedOperationException()
            }
        }
        return eval(root!!)
    }

    fun whatHumanNeedToYell():String {
        fun eval(node: Node): String {
            if (node.value != null) {
                if (node.name == "humn") return "X"
                return node.value.toString()
            }
            return when (node.operator) {
                Operator.MUL -> "(".plus(eval(node.left!!).plus("*").plus(eval(node.right!!))).plus(")")
                Operator.DIVIDE -> "(".plus(eval(node.left!!).plus("/").plus(eval(node.right!!))).plus(")")
                Operator.PLUS -> "(".plus(eval(node.left!!).plus("+").plus(eval(node.right!!))).plus(")")
                Operator.MINUS -> "(".plus(eval(node.left!!).plus("-").plus(eval(node.right!!))).plus(")")
                else -> throw UnsupportedOperationException()
            }
        }
        return (eval(root!!.left!!).plus("=").plus(eval(root!!.right!!)))
    }

    fun buildParseTree(input: List<String>) {
        val inputMap: Map<String, String> = input.map {
            it.split(":").map { it.trim() }
        }.associate { it.first() to it.last() }

        buildTree(inputMap)
    }
}

fun main() {

    fun solve1(input: List<String>): Long {
        return with(MonkeyExpressionTree()) {
            buildParseTree(input)
            evaluateMonkeyTree()
        }
    }

    fun solve2(input: List<String>):String {
        val equation = with(MonkeyExpressionTree()) {
            buildParseTree(input)
            whatHumanNeedToYell()
        }
        // I cheated for part 2, I get the equation and put it in https://www.mathpapa.com/equation-solver/
        // TODO -> Solve it programatically
        return equation
    }

    val input = readInput("/day21/Day21")
    println(solve1(input))
    println(solve2(input))
}
