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
package com.apress.prospring6.twenty.boot

import com.apress.prospring6.twenty.boot.model.Singer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.time.Period
import java.util.function.*
import java.util.function.Function

/**
 * Created by iuliana on 08/04/2023
 */
class SimpleProgrammingTest {
    var singers: List<Singer> = listOf(
        Singer().apply{firstName="John"; lastName = "Mayer"; birthDate = LocalDate.of(1977, 10, 16)},
        Singer().apply{firstName="B.B."; lastName = "King"; birthDate = LocalDate.of(1929, 9, 16)},
        Singer().apply{firstName="Peggy"; lastName = "Lee"; birthDate = LocalDate.of(1920, 5, 26)},
        Singer().apply{firstName="Ella"; lastName = "Fitzgerald"; birthDate = LocalDate.of(1917, 4, 25)}
    )
    var computeAge  = { singer: Singer ->
            Pair(
                singer,
                Period.between(singer.birthDate, LocalDate.now()).years
            )
        }
    var checkAge = { pair:Pair<Singer, Int> -> pair.second > 50 }

    @Test
    fun imperativePlay() {
        var agesum = 0
        for (s in singers) {
            val p = computeAge(s)
            if (checkAge(p)) {
                agesum += p.second
            }
        }
        Assertions.assertEquals(
            300,
            agesum
        ) // depending when you are running this test it might fail, now in April 2023 it passes ;)
    }

    @Test
    fun streamsPlay() {
        val agesum: Int = singers
            .map(computeAge)
            .filter(checkAge)
            .map{ obj:Pair<Singer, Int> -> obj.second }
            .reduce{ a: Int, b: Int -> Integer.sum(a, b) }
        Assertions.assertEquals(
            300,
            agesum
        ) // depending when you are running this test it might fail, now in April 2023 it passes ;)
    }

    @Test
    fun reactivePlay() {
        Flux.fromIterable<Singer>(singers) // Flux<Singer>
            .map(computeAge) // Flux <Pair<Singer, Integer>>
            .filter(checkAge) // Flux <Pair<Singer, Integer>>
            .map{ obj -> obj.second } // Flux <Integer>
            .reduce(0,
                { a: Int, b: Int ->
                    Integer.sum(
                        a,
                        b
                    )
                }) // Mono<Integer>
            .subscribe(object : BaseSubscriber<Int>() {
                override fun hookOnNext(agesum: Int) {
                    Assertions.assertEquals(
                        300,
                        agesum
                    ) // depending when you are running this test it might fail, now in April 2023 it passes ;)
                }
            })
    }
}
