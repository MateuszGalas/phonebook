package phonebook

import java.io.File
import kotlin.system.exitProcess

class PhoneBook {
    private val book: List<String>

    init {
        val file = File("C:\\Users\\Mateusz\\IdeaProjects\\Phone Book\\directory.txt")
        if (!file.exists()) exitProcess(1)
        book = file.readLines()
    }

    fun findNumbers(file: File) {
        if (!file.exists()) exitProcess(1)
        val find = file.readLines()
        var counter = 0

        println(book.contains(""))

        println("Start searching...")

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
        val duration = end - start

        println(
            "Found $counter / ${find.size} entries. Time taken: " +
                    "${duration / 60000} min. ${(duration % 60000) / 1000} sec. ${duration % 1000} ms."
        )
    }

    fun bubbleSort() {
        TODO()
    }

    fun jumpSearch() {

    }
}

fun main() {
    PhoneBook().findNumbers(File("C:\\Users\\Mateusz\\IdeaProjects\\Phone Book\\find.txt"))
}
