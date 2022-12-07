package day07

import readInput
import java.util.*

enum class NodeType {
    DIR, FILE
}

data class MyNode(
    val name: String,
    val type: NodeType,
    val parent: MyNode? = null,
    val child: Stack<MyNode> = Stack<MyNode>(),
    var size: Long = 0L
) {
    override fun toString(): String {
        return "$name - $type - $size"
    }
}

// Command related
fun String.isCommand() = this == "$"
fun String.isCd() = this == "cd"
fun String.isLs() = this == "ls"
fun String.isDir() = this == "dir"

fun String.navigateToRoot() = this == "/"
fun String.navigateToParent() = this == ".."

fun main() {

    val tree = LinkedList<MyNode>()
    var rootNode: MyNode? = null
    var currentDir: MyNode? = null

    fun addFileInCurrentDirectory(fileSize: String, fileName: String) {
        if (currentDir == null) return
        currentDir!!.child.push(
            MyNode(
                name = "$fileSize-$fileName",
                parent = currentDir,
                type = NodeType.FILE,
                size = fileSize.toLong()
            )
        )
        currentDir!!.size = currentDir!!.size + fileSize.toLong()

        // add the file size to parent dirs
        var tempDir:MyNode? = currentDir?.parent
        while (tempDir != null && tempDir.type == NodeType.DIR){
            tempDir.size = tempDir.size + fileSize.toLong()
            tempDir = tempDir.parent
        }
    }

    fun addDirectoryInCurrentNode(dirName: String) {
        if (currentDir == null) return
        currentDir!!.child.push(
            MyNode(
                name = dirName,
                type = NodeType.DIR,
                parent = currentDir
            )
        )
    }

    // This is for cd -> / or .. or dir name
    fun navigateToInTree(to: String) {
        when {
            to.navigateToRoot() -> {
                if (tree.isEmpty()) {
                    val node = MyNode(name = to, type = NodeType.DIR, parent = null)
                    tree.add(node)
                    rootNode = node
                    currentDir = node
                } else {
                    currentDir = rootNode
                }
            }

            to.navigateToParent() -> currentDir = currentDir?.parent
            else -> {
                // navigate to given directory, like a,e,d
                val tempDir = currentDir

                // dfs
                val stack = Stack<MyNode>()
                stack.push(tempDir)

                while (stack.isNotEmpty()) {
                    val current = stack.pop() ?: return
                    for (node in current.child) {
                        if (node.type == NodeType.DIR && node.name == to) {
                            currentDir = node
                            return
                        }
                        if (node.type == NodeType.DIR){
                            stack.push(node)
                        }
                    }
                }

            }
        }
    }

    fun initTree(input: List<String>) {
        tree.clear()
        rootNode = null
        currentDir = null
        input.forEach {
            val lineInput = it.split(" ").map { it.trim() }
            if (lineInput[0].isCommand()) {
                when {
                    lineInput[1].isCd() -> {
                        val navigateTo = lineInput[2]
                        navigateToInTree(navigateTo)
                    }

                    lineInput[1].isLs() -> {
                        // do nothing as of now
                    }
                }
            } else if (lineInput[0].isDir()) {
                // add directory in current node
                addDirectoryInCurrentNode(lineInput[1])
            } else {
                // it's a file
                val fileSize = lineInput[0]
                val fileName = lineInput[1]
                addFileInCurrentDirectory(
                    fileSize = fileSize,
                    fileName = fileName
                )
            }
        }
    }

    fun part1(input: List<String>): Long {
        initTree(input)

        val list = mutableListOf<Long>()
        val tempRootNode = rootNode

        // dfs
        val stack = Stack<MyNode>()
        stack.push(tempRootNode)

        while (stack.isNotEmpty()) {
            val current = stack.pop() ?: break
            if (current.type == NodeType.DIR && current.size <= 100000) {
                list.add(current.size)
            }
            for (node in current.child) {
                if (node.type == NodeType.DIR){
                    stack.push(node)
                }
            }
        }

        return list.sum()
    }

    fun part2(input: List<String>): Long {
        initTree(input)

        val list = mutableListOf<Long>()
        val tempRootNode = rootNode

        // dfs
        val stack = Stack<MyNode>()
        stack.push(tempRootNode)

        while (stack.isNotEmpty()) {
            val current = stack.pop() ?: break
            if (current.type == NodeType.DIR) {
                list.add(current.size)
            }
            for (node in current.child) {
                if (node.type == NodeType.DIR){
                    stack.push(node)
                }
            }
        }


        val unusedSpace:Long = 70000000 - rootNode!!.size
        val needed = 30000000 - unusedSpace
        return list.sorted().find { it >= needed }!!
    }


    val input = readInput("/day07/Day07")
    check(part1(input) == 1350966L)
    check(part2(input) == 6296435L)
}
