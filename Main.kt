package phonebook

import java.io.File
import kotlin.math.sqrt
import kotlin.system.exitProcess

class PhoneBook {
    val book: MutableList<String>
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

/*    fun quickSort(leftIndex: Int, rightIndex: Int) {
        val pivotIndex = (leftIndex + rightIndex) / 2
        var l = leftIndex
        var r = rightIndex
        //val queue = ArrayDeque<String>()
        if(l >= r) return

        val value = book[pivotIndex].split(" ")
        val pivot = if (value.size > 2) "${value[1]} ${value[2]}" else value[1]
        //book.removeAt(pivotIndex)
        //queue.add(pivot)

        while (true) {
            var left: String
            var right: String

            do  {
                if (l > rightIndex) break
                val valueLeft = book[l++].split(" ")
                left = if (valueLeft.size > 2) "${valueLeft[1]} ${valueLeft[2]}" else valueLeft[1]
            } while (pivot > left)

            do {
                if (r < leftIndex) break
                val valueRight = book[r--].split(" ")
                right = if (valueRight.size > 2) "${valueRight[1]} ${valueRight[2]}" else valueRight[1]
            } while (pivot < right)

            if (l < r) {
                book[l] = book[r].also { book[r] = book[l] }
            } else {
                break
            }
        }

        if (r > leftIndex) quickSort(leftIndex, r)
        if (l < rightIndex) quickSort(l, rightIndex)

    }*/


    /* This function takes last element as pivot, places
       the pivot element at its correct position in sorted
       array, and places all smaller (smaller than pivot)
       to left of pivot and all greater elements to right
       of pivot */
    fun partition(low: Int, high: Int): Int {

        // pivot
        val value = book[high].split(" ")
        val pivot = if (value.size > 2) "${value[1]} ${value[2]}" else value[1]
        // Index of smaller element and
        // indicates the right position
        // of pivot found so far
        var i = low - 1
        for (j in low..high - 1) {

            // If current element is smaller
            // than the pivot
            val element = book[j].split(" ")
            val elementValue = if (element.size > 2) "${element[1]} ${element[2]}" else element[1]

            if (elementValue < pivot) {

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

    /* The main function that implements QuickSort
              arr[] --> Array to be sorted,
              low --> Starting index,
              high --> Ending index
     */
    fun quickSort(low: Int, high: Int) {
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
        var counter = 0
        val start = System.currentTimeMillis()

        loop@for (element in find) {
            var low = 0
            var high = book.lastIndex


            while (low != high) {
                var mid = (low + high) / 2
                val list = book[mid].split(" ")
                val elementInBook = if (list.size > 2) "${list[1]} ${list[2]}" else list[1]

                if (element == elementInBook) {
                    counter++
                    continue@loop
                }
                if (element > elementInBook) {
                    low = mid
                } else {
                    high = mid
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
    PhoneBook().run {
        println("Start searching (linear search)...")
        findNumbers()
        println()
        println("Start searching (bubble sort + jump search)...")
        //bubbleSort()
        //jumpSearch()
        val start = System.currentTimeMillis()
        quickSort(0, book.lastIndex)
        val end = System.currentTimeMillis()
        val duration = end - start
        println(
            "Time taken: " +
                    "${duration / 60000} min. ${(duration % 60000) / 1000} sec. ${duration % 1000} ms."
        )
        binarySearch()
    }
}
