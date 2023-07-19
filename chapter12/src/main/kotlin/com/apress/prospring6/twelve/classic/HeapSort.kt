/*
Freeware License, some rights reserved

Copyright (c) 2023 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy
of this software and associated documentation files (the "Software"),
to work with the Software within the limits of freeware distribution and fair use.
This includes the rights to use, copy, and modify the Software for personal use.
Users are also allowed and encouraged to submit corrections and modifications
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for
commercial use in any way, or for a user's educational materials such as books
or blog articles without prior permission from the copyright holder.

The above copyright notice and this permission notice need to be included
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
package com.apress.prospring6.twelve.classic

/**
 * Created by iuliana on 24/10/2022
 */
class HeapSort(arr: IntArray) : AbstractSort(arr) {
    init {
        name = "HeapSort"
    }

    /**
     * []//youtu.be/Xw2D9aJRBY4" target="_blank"> How it works">&quot;https://youtu.be/Xw2D9aJRBY4&quot; target=&quot;_blank&quot;&gt; How it works
     *
     * @param arr int array to be sorted
     */
    override fun sort(arr: IntArray) {
        val n = arr.size
        for (i in n / 2 - 1 downTo 0) {
            buildHeap(arr, n, i)
        }
        for (i in n - 1 downTo 0) {
            val temp = arr[0]
            arr[0] = arr[i]
            arr[i] = temp
            buildHeap(arr, i, 0)
        }
    }

    /**
     * * Builds a heap tree from this piece of array
     */
    private fun buildHeap(arr: IntArray?, n: Int, i: Int) {
        var largest = i
        val l = 2 * i + 1
        val r = 2 * i + 2
        if (l < n && arr!![l] > arr[largest]) largest = l
        if (r < n && arr!![r] > arr[largest]) largest = r
        if (largest != i) {
            val swap = arr!![i]
            arr[i] = arr[largest]
            arr[largest] = swap
            buildHeap(arr, n, largest)
        }
    }
}
