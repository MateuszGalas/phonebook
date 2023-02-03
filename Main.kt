package phonebook

import java.io.File
import kotlin.math.sqrt
import kotlin.system.exitProcess

class PhoneBook {
    private val book: MutableList<String>
    private val find: List<String>
    private var linearSearchDuration = 0L

    init {
        val directoryFile = File("C:\\Users\\Mateusz\\IdeaProjects\\Phone Book\\sorted.txt")
        val findFile = File("C:\\Users\\Mateusz\\IdeaProjects\\Phone Book\\find.txt")
        if (!directoryFile.exists()) exitProcess(1)
        if (!findFile.exists()) exitProcess(1)
        book = directoryFile.readLines().toMutableList()
        find = findFile.readLines()
    }

    fun findNumbers() {
        var counter = 0

        val start = System.currentTimeMillis()
        for (i in find) {
            for (j in book) {
                if (j.contains(i)) {
                    counter++
                    break
                }
            }
        }
        val end = System.currentTimeMillis()
        linearSearchDuration += end - start
        val duration = linearSearchDuration

        println(
            "Found $counter / ${find.size} entries. Time taken: " +
                    "${duration / 60000} min. ${(duration % 60000) / 1000} sec. ${duration % 1000} ms."
        )
    }

    fun bubbleSort() {
        val start = System.currentTimeMillis()
        var sorted = false
        var duration = 0L

        while (!sorted) {
            sorted = true

            for (i in 0 until book.lastIndex) {
                val value = book[i].split(" ")
                val nextValue = book[i + 1].split(" ")
                val element = if (value.size > 2) "${value[1]} ${value[2]}" else value[1]
                val nextElement = if (nextValue.size > 2) "${nextValue[1]} ${nextValue[2]}" else nextValue[1]

                if (element > nextElement) {
                    book[i] = book[i + 1].also { book[i + 1] = book[i] }
                    sorted = false
                }
            }
            val end = System.currentTimeMillis()
            duration = end - start
            if (duration > linearSearchDuration * 10) {
                linearSearchDuration = duration
                findNumbers()
                println("Sorting time: " +
                        "${duration / 60000} min. ${(duration % 60000) / 1000} sec. ${duration % 1000} ms. " +
                        "- STOPPED, moved to linear search")
                duration = linearSearchDuration - duration
                        println("Searching time: " +
                        "${duration / 60000} min. ${(duration % 60000) / 1000} sec. ${duration % 1000} ms. ")
                return
            }
        }
        File("sorted.txt").writeText(book.joinToString("\n"))

        println(
            "Sorting time: " +
                    "${duration / 60000} min. ${(duration % 60000) / 1000} sec. ${duration % 1000} ms."
        )
        jumpSearch()
    }

    fun jumpSearch() {
        val step = sqrt(book.size.toDouble()).toInt()
        var counter = 0

        val start = System.currentTimeMillis()
        for (element in find) {
            var n = 0

            loop@while (n <= book.lastIndex) {
                val list = book[n].split(" ")
                val elementInBook = if (list.size > 2) "${list[1]} ${list[2]}" else list[1]

                if (element == elementInBook) {
                    counter++
                    break
                } else if (element > elementInBook) {
                    if (n + step in book.lastIndex until book.lastIndex + step) n = book.lastIndex else n += step
                    continue
                } else if (n == 0) {
                    break
                } else {
                    for (j in n downTo n - step) {
                        if (book[j].contains(element)) {
                            counter++
                            break@loop
                        }
                    }
                    break
                }
            }
        }
        val end = System.currentTimeMillis()
        val duration = end - start
        println(
            "Found $counter / ${find.size} entries. Time taken: " +
                    "${duration / 60000} min. ${(duration % 60000) / 1000} sec. ${duration % 1000} ms."
        )
    }

    fun quickSort() {
        TODO()
    }

    fun binarySearch() {
        TODO()
    }
}

fun main() {
    PhoneBook().run {
        println("Start searching (linear search)...")
        findNumbers()
        println()
        println("Start searching (bubble sort + jump search)...")
        bubbleSort()
        //jumpSearch()
    }
}
