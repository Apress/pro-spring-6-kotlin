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
package com.apress.prospring6.eighteen.boot

import com.apress.prospring6.eighteen.boot.entities.Singer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.function.Executable
import org.springframework.http.*
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.net.URISyntaxException
import java.time.LocalDate
import java.util.*
import java.util.List

/**
 * Created by iuliana on 25/03/2023
 */
@Disabled("Enable this test then run `testCreate()` first, then run the `testPositiveFindById()` to generate Prometheus data.")
class SingerControllerTest {
    private val restTemplate = RestTemplate()
    @RepeatedTest(50)
    fun testFindAll() {
        val singers = restTemplate.getForObject(
            BASE_URL,
            Array<Singer>::class.java
        )!!
        Assertions.assertTrue(singers.size >= 15)
    }

    @RepeatedTest(500)
    @Throws(URISyntaxException::class)
    fun testCreate() {
        val singerNew = Singer()
        singerNew.firstName = UUID.randomUUID().toString().substring(0, 8)
        singerNew.lastName = UUID.randomUUID().toString().substring(0, 8)
        singerNew.birthDate = LocalDate.now()
        val req = RequestEntity(
            singerNew, HttpMethod.POST, URI(
                BASE_URL
            )
        )
        val response = restTemplate.exchange(
            req,
            String::class.java
        )
        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @RepeatedTest(10)
    @Throws(URISyntaxException::class)
    fun testPositiveFindById() {
        val headers = HttpHeaders()
        headers.accept = List.of(MediaType.APPLICATION_JSON)
        for (i in 1..249) {
            val req = RequestEntity<HttpHeaders>(
                headers, HttpMethod.GET, URI(
                    BASE_URL + i
                )
            )
            val response = restTemplate.exchange(
                req,
                Singer::class.java
            )
            Assertions.assertAll("testPositiveFindById",
                Executable {
                    Assertions.assertEquals(
                        HttpStatus.OK,
                        response.statusCode
                    )
                },
                Executable {
                    Assertions.assertTrue(
                        Objects.requireNonNull(
                            response.headers[HttpHeaders.CONTENT_TYPE]
                        ).contains(MediaType.APPLICATION_JSON_VALUE)
                    )
                },
                Executable { Assertions.assertNotNull(response.body) },
                Executable {
                    Assertions.assertEquals(
                        Singer::class.java,
                        response.body.javaClass
                    )
                }
            )
        }
    }

    companion object {
        private const val BASE_URL = "http://localhost:8081/singer/"
    }
}
