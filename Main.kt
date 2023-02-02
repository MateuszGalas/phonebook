package phonebook

import java.io.File
import kotlin.math.sqrt
import kotlin.system.exitProcess

class PhoneBook {
    private val book: MutableList<String>
    private val find: List<String>
    private var linearSearchDuration = 0L

    init {
        val directoryFile = File("C:\\Users\\Mateusz\\IdeaProjects\\Phone Book\\small_directory.txt")
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
                if (j.matches(""".+$i""".toRegex())) {
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
        //println(book.size)

        while (!sorted) {
            sorted = true

            for (i in 0 until book.lastIndex - 1) {
                val element = book[i].split(" ")
                val nextElement = book[i + 1].split(" ")
                if (element[1] > nextElement[1]) {
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
            loop@for (i in book.indices step step) {
                var index = i
                if (i + step > book.lastIndex) index = book.lastIndex
                val list = book[index].split(" ")
                val elementInBook = if (list.size > 2) "${list[1]} ${list[2]}" else list[1]

                if (element == elementInBook) {
                    counter++
                    break
                } else if (element > elementInBook) {
                    continue
                } else if (index == 0) {
                    break
                } else {
                    for (j in index downTo index - step) {
                        if (book[j].matches(""".+$element""".toRegex())) {
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
}

fun main() {
    //PhoneBook().findNumbers(File("C:\\Users\\Mateusz\\IdeaProjects\\Phone Book\\find.txt"))
    PhoneBook().run {
        println("Start searching (linear search)...")
        findNumbers()
        println()
        println("Start searching (bubble sort + jump search)...")
        bubbleSort()
        //findNumbers()
        //jumpSearch()
    }
}
