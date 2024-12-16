package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private enum class Move {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    fun toVector(): Point =
        when (this) {
            UP -> Point(0, -1)
            DOWN -> Point(0, 1)
            LEFT -> Point(-1, 0)
            RIGHT -> Point(1, 0)
        }

    companion object {
        fun fromChar(c: Char): Move =
            when (c) {
                '^' -> UP
                'v' -> DOWN
                '<' -> LEFT
                '>' -> RIGHT
                else -> throw IllegalArgumentException("Invalid move: $c")
            }
    }
}

private enum class Tile {
    EMPTY,
    WALL,
    BOX,
    BIG_BOX_START,
    BIG_BOX_END;

    companion object {
        fun fromChar(c: Char): Tile =
            when (c) {
                '.',
                '@' -> EMPTY
                '#' -> WALL
                'O' -> BOX
                else -> throw IllegalArgumentException("Invalid tile: $c")
            }
    }
}

private sealed interface BinaryTree<out T> {
    data class Leaf<T>(val value: T) : BinaryTree<T>

    data class Branch<T>(val value: T, val left: BinaryTree<T>, val right: BinaryTree<T>) :
        BinaryTree<T>
}

private sealed interface TreeNode {
    data class BigBox(val start: Point, val end: Point) : TreeNode

    object Wall : TreeNode

    object Empty : TreeNode
}

private fun testStream() = resourceLineStream("/15/test.txt")

private fun inputStream() = resourceLineStream("/15/input.txt")

private fun part1(): Int {
    val input = inputStream().toList()
    val gridLines = input.takeWhile { it.isNotEmpty() }
    val moveLines = input.drop(gridLines.size + 1)

    val grid = gridLines.map { line -> line.map(Tile::fromChar).toTypedArray() }.toTypedArray()
    val moves = moveLines.flatMap { line -> line.map(Move::fromChar) }
    val initialY = gridLines.indexOfFirst { it.contains('@') }
    val initialX = gridLines[initialY].indexOf('@')

    var currentPosition = Point(initialX, initialY)
    moves.forEach { move ->
        // Determine if there are boxes in the direction of the move that can be moved
        val boxes = boxesUntilEmptyInDirection(grid, currentPosition, move)
        if (boxes.isNotEmpty()) {
            // Move boxes
            val endMove = boxes.last() + move.toVector()
            val initialMove = boxes.first()
            grid[endMove.y][endMove.x] = Tile.BOX
            grid[initialMove.y][initialMove.x] = Tile.EMPTY
        }

        // Move player
        val next = currentPosition + move.toVector()
        val nextTile = grid[next.y][next.x]
        if (nextTile == Tile.EMPTY) {
            currentPosition = next
        }
    }

    return grid
        .mapIndexed { y, row ->
            row.mapIndexed { x, tile ->
                when (tile) {
                    Tile.BOX -> (100 * y) + x
                    else -> 0
                }
            }
        }
        .flatten()
        .sum()
}

private fun part2(): Int {
    val input = inputStream().toList()
    val gridLines = input.takeWhile { it.isNotEmpty() }
    val moveLines = input.drop(gridLines.size + 1)

    val grid =
        doubleUpGrid(
            gridLines.map { line -> line.map(Tile::fromChar).toTypedArray() }.toTypedArray()
        )
    val moves = moveLines.flatMap { line -> line.map(Move::fromChar) }
    val initialY = gridLines.indexOfFirst { it.contains('@') }
    val initialX = gridLines[initialY].indexOf('@')

    var currentPosition = Point(initialX * 2, initialY)

    moves.forEach { move ->
        val boxTree = boxTreeUntilEmptyInDirection(grid, currentPosition, move)
        if (boxTree != null) {
            // Move boxes
            // Remove duplicate moves that overwrite things and cause problems
            val nodeList = binaryTreeToList(boxTree).distinct()
            nodeList.forEach { node ->
                when (node) {
                    is TreeNode.BigBox -> {
                        val newStart = node.start + move.toVector()
                        val newEnd = node.end + move.toVector()
                        grid[node.start.y][node.start.x] = Tile.EMPTY
                        grid[node.end.y][node.end.x] = Tile.EMPTY
                        grid[newStart.y][newStart.x] = Tile.BIG_BOX_START
                        grid[newEnd.y][newEnd.x] = Tile.BIG_BOX_END
                    }
                    else -> {}
                }
            }
        }
        // Move player
        val next = currentPosition + move.toVector()
        val nextTile = grid[next.y][next.x]
        if (nextTile == Tile.EMPTY) {
            currentPosition = next
        }
    }

    return grid
        .mapIndexed { y, row ->
            row.mapIndexed { x, tile ->
                when (tile) {
                    Tile.BIG_BOX_START -> (100 * y) + x
                    else -> 0
                }
            }
        }
        .flatten()
        .sum()
}

private fun boxesUntilEmptyInDirection(
    grid: Array<Array<Tile>>,
    position: Point,
    move: Move,
): List<Point> {
    val boxes = mutableListOf<Point>()
    var currentPosition = position
    while (true) {
        val nextPosition = currentPosition + move.toVector()
        if (!nextPosition.inBounds(grid[0].size, grid.size)) {
            break
        }
        val nextTile = grid[nextPosition.y][nextPosition.x]
        if (nextTile == Tile.WALL) {
            // Can't move anything if there's a wall at the end of the list of boxes
            return emptyList()
        } else if (nextTile == Tile.BOX) {
            boxes.add(nextPosition)
        } else if (nextTile == Tile.EMPTY) {
            break
        }
        currentPosition = nextPosition
    }
    return boxes
}

private fun boxTreeUntilEmptyInDirection(
    grid: Array<Array<Tile>>,
    position: Point,
    move: Move,
): BinaryTree<TreeNode>? {
    fun buildBinaryTree(currentPosition: Point): BinaryTree<TreeNode> {
        val nextPosition =
            if (move == Move.RIGHT) {
                currentPosition + move.toVector() + move.toVector()
            } else {
                currentPosition + move.toVector()
            }
        val nextTile = grid[nextPosition.y][nextPosition.x]
        if (move == Move.LEFT || move == Move.RIGHT) {
            // Left/right, check for empty, wall, or big box end/start
            val (desiredTile, bigBoxStartPosition, bigBoxEndPosition) =
                if (move == Move.LEFT) {
                    val start = Point(nextPosition.x - 1, nextPosition.y)
                    Triple(Tile.BIG_BOX_END, start, nextPosition)
                } else {
                    val end = Point(nextPosition.x + 1, nextPosition.y)
                    Triple(Tile.BIG_BOX_START, nextPosition, end)
                }

            if (nextTile == Tile.EMPTY) {
                return BinaryTree.Leaf(TreeNode.Empty)
            } else if (nextTile == Tile.WALL) {
                return BinaryTree.Leaf(TreeNode.Wall)
            } else if (nextTile == desiredTile) {
                return BinaryTree.Branch(
                    TreeNode.BigBox(bigBoxStartPosition, bigBoxEndPosition),
                    buildBinaryTree(bigBoxStartPosition),
                    BinaryTree.Leaf(TreeNode.Empty),
                )
            }
        } else {
            // Up/down, check for empty, wall, or either big box tile
            if (nextTile == Tile.EMPTY) {
                return BinaryTree.Leaf(TreeNode.Empty)
            } else if (nextTile == Tile.WALL) {
                return BinaryTree.Leaf(TreeNode.Wall)
            } else if (nextTile == Tile.BIG_BOX_START) {
                val bigBoxEndPosition = Point(nextPosition.x + 1, nextPosition.y)
                return BinaryTree.Branch(
                    TreeNode.BigBox(nextPosition, bigBoxEndPosition),
                    buildBinaryTree(nextPosition),
                    buildBinaryTree(bigBoxEndPosition),
                )
            } else if (nextTile == Tile.BIG_BOX_END) {
                val bigBoxStartPosition = Point(nextPosition.x - 1, nextPosition.y)
                return BinaryTree.Branch(
                    TreeNode.BigBox(bigBoxStartPosition, nextPosition),
                    buildBinaryTree(bigBoxStartPosition),
                    buildBinaryTree(nextPosition),
                )
            }
        }
        throw IllegalStateException("Shouldn't happen")
    }

    val nextPosition = position + move.toVector()
    val nextTile = grid[nextPosition.y][nextPosition.x]

    val toReturn =
        if (nextTile == Tile.BIG_BOX_START || nextTile == Tile.BIG_BOX_END) {
            val (bigBoxStartPosition, bigBoxEndPosition) =
                if (nextTile == Tile.BIG_BOX_START) {
                    nextPosition to Point(nextPosition.x + 1, nextPosition.y)
                } else {
                    Point(nextPosition.x - 1, nextPosition.y) to nextPosition
                }
            val rightNode =
                if (move == Move.LEFT || move == Move.RIGHT) {
                    BinaryTree.Leaf(TreeNode.Empty)
                } else {
                    buildBinaryTree(bigBoxEndPosition)
                }

            BinaryTree.Branch(
                TreeNode.BigBox(bigBoxStartPosition, bigBoxEndPosition),
                buildBinaryTree(bigBoxStartPosition),
                rightNode,
            )
        } else {
            null
        }

    return if (toReturn != null && !binaryTreeContainsWall(toReturn)) {
        toReturn
    } else {
        null
    }
}

private fun binaryTreeToList(tree: BinaryTree<TreeNode>): List<TreeNode> =
    when (tree) {
        is BinaryTree.Leaf -> listOf(tree.value)
        is BinaryTree.Branch ->
            binaryTreeToList(tree.left) + binaryTreeToList(tree.right) + listOf(tree.value)
    }

private fun binaryTreeContainsWall(tree: BinaryTree<TreeNode>): Boolean =
    when (tree) {
        is BinaryTree.Leaf -> tree.value == TreeNode.Wall
        is BinaryTree.Branch ->
            tree.value == TreeNode.Wall ||
                binaryTreeContainsWall(tree.left) ||
                binaryTreeContainsWall(tree.right)
    }

private fun doubleUpGrid(grid: Array<Array<Tile>>): Array<Array<Tile>> {
    val newGrid = Array(grid.size) { Array(grid[0].size * 2) { Tile.EMPTY } }
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            val originalGridTile = grid[y][x]
            val (first, second) =
                if (originalGridTile == Tile.BOX) {
                    Tile.BIG_BOX_START to Tile.BIG_BOX_END
                } else {
                    originalGridTile to originalGridTile
                }

            val baseX = x * 2
            newGrid[y][baseX] = first
            newGrid[y][baseX + 1] = second
        }
    }
    return newGrid
}
