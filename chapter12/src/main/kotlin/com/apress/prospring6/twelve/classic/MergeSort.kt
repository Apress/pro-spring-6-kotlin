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
class MergeSort(arr: IntArray) : AbstractSort(arr) {
    init {
        name = "MergeSort"
    }

    override fun sort(arr: IntArray) {
        sort(arr, 0, arr.size - 1)
    }

    /**
     * []//youtu.be/XaqR3G_NVoo" target="_blank"> How it works">&quot;https://youtu.be/XaqR3G_NVoo&quot; target=&quot;_blank&quot;&gt; How it works
     *
     * @param arr int array to be sorted
     * @param low lower limit of the interval to be sorted
     * @param high higher limit of the interval to be sorted
     */
    private fun sort(arr: IntArray, low: Int, high: Int) {
        val sb = StringBuilder("Call sort of ")
            .append("[low,high]: [")
            .append(low).append(" ").append(high)
            .append("] ")
        for (i in low..high) {
            sb.append(arr[i]).append(" ")
        }
        // LOGGER.info(sb.toString());
        if (low < high) {
            val middle = (low + high) / 2

            //sort lower half of the interval
            sort(arr, low, middle)
            //sort upper half of the interval
            sort(arr, middle + 1, high)

            // merge the two intervals
            merge(arr, low, high, middle)
        }
    }

    private fun merge(arr: IntArray, low: Int, high: Int, middle: Int) {
        val leftLength = middle - low + 1
        val rightLength = high - middle
        val left = IntArray(leftLength)
        val right = IntArray(rightLength)
        for (i in 0 until leftLength) {
            left[i] = arr[low + i]
        }
        for (i in 0 until rightLength) {
            right[i] = arr[middle + 1 + i]
        }
        var i = 0
        var j = 0
        var k = low
        while (i < leftLength && j < rightLength) {
            if (left[i] <= right[j]) {
                arr[k] = left[i]
                i++
            } else {
                arr[k] = right[j]
                j++
            }
            k++
        }
        while (i < leftLength) {
            arr[k] = left[i]
            i++
            k++
        }
        while (j < rightLength) {
            arr[k] = right[j]
            j++
            k++
        }
        val sb = StringBuilder("Called merge of [low, high, middle]: [")
            .append(low).append(" ").append(high).append(" ").append(middle)
            .append("]) ")
        for (z in low..high) {
            sb.append(arr[z]).append(" ")
        }
        //LOGGER.info(sb.toString());
    }
}
