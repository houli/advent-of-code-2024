package io.github.houli

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

sealed interface FilesystemEntry {
    data class FileBlock(val id: Int, val size: Int) : FilesystemEntry

    data class EmptyBlock(val size: Int) : FilesystemEntry
}

private fun testStream() = resourceLineStream("/09/test.txt")

private fun inputStream() = resourceLineStream("/09/input.txt")

private fun part1(): Long {
    val input = inputStream().toList().joinToString("").map(Char::digitToInt)
    val filesystem = buildFilesystem(input)
    val newFilesystem = compactFilesystemFragmented(filesystem.toMutableList())
    return checksum(newFilesystem)
}

private fun part2(): Long {
    val input = inputStream().toList().joinToString("").map(Char::digitToInt)
    val filesystem = buildFilesystem(input)
    val newFilesystem = compactFilesystemDefragmented(filesystem.toMutableList())
    return checksum(newFilesystem)
}

private fun buildFilesystem(input: List<Int>): List<FilesystemEntry> =
    input.mapIndexed { index, size ->
        if (index % 2 == 0) {
            FilesystemEntry.FileBlock(index / 2, size)
        } else {
            FilesystemEntry.EmptyBlock(size)
        }
    }

private fun compactFilesystemFragmented(
    filesystem: MutableList<FilesystemEntry>
): List<FilesystemEntry> {
    fun lastBlocksAreEmpty(filesystem: List<FilesystemEntry>): Boolean {
        val firstEmptyBlockIndex = filesystem.indexOfFirst { it is FilesystemEntry.EmptyBlock }
        return firstEmptyBlockIndex == -1 ||
            filesystem.indexOfLast { it is FilesystemEntry.FileBlock } < firstEmptyBlockIndex
    }

    while (!lastBlocksAreEmpty(filesystem)) {
        val lastFileBlockIndex = filesystem.indexOfLast { it is FilesystemEntry.FileBlock }
        val lastFileBlock = filesystem[lastFileBlockIndex] as FilesystemEntry.FileBlock
        val firstEmptyBlockIndex = filesystem.indexOfFirst { it is FilesystemEntry.EmptyBlock }
        val firstEmptyBlock = filesystem[firstEmptyBlockIndex] as FilesystemEntry.EmptyBlock

        if (firstEmptyBlock.size == lastFileBlock.size) {
            filesystem[firstEmptyBlockIndex] = lastFileBlock
            filesystem[lastFileBlockIndex] = FilesystemEntry.EmptyBlock(lastFileBlock.size)
        } else if (firstEmptyBlock.size < lastFileBlock.size) {
            filesystem[firstEmptyBlockIndex] =
                FilesystemEntry.FileBlock(lastFileBlock.id, firstEmptyBlock.size)
            filesystem[lastFileBlockIndex] =
                FilesystemEntry.FileBlock(
                    lastFileBlock.id,
                    lastFileBlock.size - firstEmptyBlock.size,
                )
        } else {
            filesystem[firstEmptyBlockIndex] =
                FilesystemEntry.FileBlock(lastFileBlock.id, lastFileBlock.size)
            filesystem[lastFileBlockIndex] = FilesystemEntry.EmptyBlock(lastFileBlock.size)
            filesystem.add(
                firstEmptyBlockIndex + 1,
                FilesystemEntry.EmptyBlock(firstEmptyBlock.size - lastFileBlock.size),
            )
        }
    }

    return filesystem
}

private fun compactFilesystemDefragmented(
    filesystem: MutableList<FilesystemEntry>
): List<FilesystemEntry> {
    val highestIdFileBlock =
        filesystem
            .filterIsInstance<FilesystemEntry.FileBlock>()
            .maxBy(FilesystemEntry.FileBlock::id)

    (highestIdFileBlock.id downTo 0).forEach { id ->
        val fileBlockIndex =
            filesystem.indexOfFirst { it is FilesystemEntry.FileBlock && it.id == id }
        if (fileBlockIndex != -1) {
            val fileBlock = filesystem[fileBlockIndex] as FilesystemEntry.FileBlock
            val placeToPutBlock =
                filesystem.indexOfFirst {
                    it is FilesystemEntry.EmptyBlock && it.size >= fileBlock.size
                }

            if (placeToPutBlock != -1 && placeToPutBlock < fileBlockIndex) {
                val emptyBlock = filesystem[placeToPutBlock] as FilesystemEntry.EmptyBlock
                if (emptyBlock.size == fileBlock.size) {
                    filesystem[placeToPutBlock] = fileBlock
                    filesystem[fileBlockIndex] = FilesystemEntry.EmptyBlock(fileBlock.size)
                } else {
                    filesystem[placeToPutBlock] = fileBlock
                    filesystem[fileBlockIndex] = FilesystemEntry.EmptyBlock(fileBlock.size)
                    filesystem.add(
                        placeToPutBlock + 1,
                        FilesystemEntry.EmptyBlock(emptyBlock.size - fileBlock.size),
                    )
                }
            }
        }
    }

    return filesystem
}

private fun checksum(filesystem: List<FilesystemEntry>): Long =
    filesystem
        .flatMap { entry ->
            when (entry) {
                is FilesystemEntry.FileBlock -> List(entry.size) { entry.id }
                is FilesystemEntry.EmptyBlock -> List(entry.size) { -1 }
            }
        }
        .mapIndexed { index, id ->
            if (id != -1) {
                id * index.toLong()
            } else {
                0
            }
        }
        .sum()
