package phonebook

import java.io.File
import kotlin.math.sqrt
import kotlin.system.exitProcess

class PhoneBook {
    private val book: MutableList<String>
    private val find: List<String>
    private var linearSearchDuration = 0L

    init {
        val directoryFile = File("C:\\Users\\Mateusz\\IdeaProjects\\Phone Book\\directory.txt")
        val findFile = File("C:\\Users\\Mateusz\\IdeaProjects\\Phone Book\\find.txt")
        if (!directoryFile.exists()) exitProcess(1)
        if (!findFile.exists()) exitProcess(1)
        book = directoryFile.readLines().toMutableList()
        find = findFile.readLines()
    }

    fun linearSearch() {
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
                val element = book[i].substringAfter(" ")
                val nextElement = book[i + 1].substringAfter(" ")

                if (element > nextElement) {
                    book[i] = book[i + 1].also { book[i + 1] = book[i] }
                    sorted = false
                }
            }
            val end = System.currentTimeMillis()
            duration = end - start
            if (duration > linearSearchDuration * 10) {
                linearSearchDuration = duration
                linearSearch()
                println(
                    "Sorting time: " +
                            "${duration / 60000} min. ${(duration % 60000) / 1000} sec. ${duration % 1000} ms. " +
                            "- STOPPED, moved to linear search"
                )
                duration = linearSearchDuration - duration
                println(
                    "Searching time: " +
                            "${duration / 60000} min. ${(duration % 60000) / 1000} sec. ${duration % 1000} ms. "
                )
                return
            }
        }

        println(
            "Sorting time: " +
                    "${duration / 60000} min. ${(duration % 60000) / 1000} sec. ${duration % 1000} ms."
        )
        jumpSearch()
    }

    private fun jumpSearch() {
        val step = sqrt(book.size.toDouble()).toInt()
        var counter = 0

        val start = System.currentTimeMillis()
        for (element in find) {
            var n = 0

            loop@ while (n <= book.lastIndex) {
                val elementInBook = book[n].substringAfter(" ")

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

    /*     This function takes last element as pivot, places
           the pivot element at its correct position in sorted
           array, and places all smaller (smaller than pivot)
           to left of pivot and all greater elements to right
           of pivot */
    private fun partition(low: Int, high: Int): Int {

        // pivot
        val pivot = book[high].substringAfter(" ")
        // Index of smaller element and
        // indicates the right position
        // of pivot found so far
        var i = low - 1
        for (j in low..high - 1) {

            // If current element is smaller
            // than the pivot
            val element = book[j].substringAfter(" ")
            if (element < pivot) {

                // Increment index of
                // smaller element
                i++
                book[i] = book[j].also { book[j] = book[i] }
                //swap(i, j)
            }
        }
        book[i + 1] = book[high].also { book[high] = book[i + 1] }
        return i + 1
    }

    /*     The main function that implements QuickSort
                  arr[] --> Array to be sorted,
                  low --> Starting index,
                  high --> Ending index*/

    private fun quickSort(low: Int, high: Int) {
        if (low < high) {

            // pi is partitioning index, arr[p]
            // is now at right place
            val pi = partition(low, high)

            // Separately sort elements before
            // partition and after partition
            quickSort(low, pi - 1)
            quickSort(pi + 1, high)
        }
    }

    fun binarySearch() {
        val start = System.currentTimeMillis()
        quickSort(0, book.lastIndex)
        val start2 = System.currentTimeMillis()
        var counter = 0

        loop@ for (element in find) {
            var low = 0
            var high = book.lastIndex


            while (low != high) {
                val mid = (low + high) / 2
                val elementInBook = book[mid].substringAfter(" ")

                if (element == elementInBook) {
                    counter++
                    continue@loop
                } else if (element > elementInBook) {
                    low = mid
                } else {
                    high = mid
                }
            }
        }
        val end = System.currentTimeMillis()
        val duration = end - start
        val searchTime = end - start2
        val sortingTime = duration - searchTime

        println(
            "Found $counter / ${find.size} entries. Time taken: " +
                    "${duration / 60000} min. ${(duration % 60000) / 1000} sec. ${duration % 1000} ms."
        )
        println(
            "Sorting time: ${sortingTime / 60000} min. ${(sortingTime % 60000) / 1000} sec. ${sortingTime % 1000} ms."
        )
        println(
            "Searching time: ${searchTime / 60000} min. ${(searchTime % 60000) / 1000} sec. ${searchTime % 1000} ms."
        )
    }
}

fun main() {
    PhoneBook().run {
        println("Start searching (linear search)...")
        linearSearch()

        println()
        println("Start searching (bubble sort + jump search)...")
        bubbleSort()

        println()
        println("Start searching (quick sort + binary search)...")
        binarySearch()
    }
}
