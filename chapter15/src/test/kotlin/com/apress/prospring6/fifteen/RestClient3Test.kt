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
package com.apress.prospring6.fifteen

import com.apress.prospring6.fifteen.entities.Singer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.net.URISyntaxException
import java.time.LocalDate
import java.util.*
import java.util.List

/**
 * Created by iuliana on 22/01/2023
 */
@Disabled("Start Apache Tomcat and deploy the app first, then comment this line, then run each test manually and notice the result!")
class RestClient3Test {
    var restTemplate = RestTemplate()

    @Test
    @Throws(URISyntaxException::class)
    fun testPositiveFindById() {
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        val req = RequestEntity<HttpHeaders>(
            headers, HttpMethod.GET, URI(
                URL_GET_ALL_SINGERS + 1
            )
        )
        LOGGER.info("--> Testing retrieve a singer by id : 1")
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
                    ).contains(MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @Test
    @Throws(URISyntaxException::class)
    fun testNegativeFindById() {
        LOGGER.info("--> Testing retrieve a singer by id : 99")
        val req = RequestEntity<HttpHeaders>(
            HttpMethod.GET, URI(
                URL_GET_ALL_SINGERS + 99
            )
        )
        Assertions.assertThrowsExactly(
            HttpClientErrorException.NotFound::class.java
        ) {
            restTemplate.exchange(
                req,
                HttpStatus::class.java
            )
        }
    }

    @Test
    @Throws(URISyntaxException::class)
    fun testNegativeCreate() {
        LOGGER.info("--> Testing create singer")
        val singerNew = Singer()
        singerNew.firstName = "Ben"
        singerNew.lastName = "Barnes"
        singerNew.birthDate = LocalDate.now()
        val req = RequestEntity(
            singerNew, HttpMethod.POST, URI(
                URL_CREATE_SINGER
            )
        )
        Assertions.assertThrowsExactly(
            HttpClientErrorException.BadRequest::class.java
        ) {
            restTemplate.exchange(
                req,
                HttpStatus::class.java
            )
        }
    }

    @Test
    @Throws(URISyntaxException::class)
    fun testNegativeUpdate() {
        Companion.LOGGER.info("--> Testing update singer")
        val singerNew = Singer()
        singerNew.firstName = "Mimi"
        singerNew.birthDate = LocalDate.now()
        val req = RequestEntity(
            singerNew, HttpMethod.PUT, URI(
                URL_CREATE_SINGER + 7
            )
        )
        Assertions.assertThrowsExactly(
            HttpClientErrorException.BadRequest::class.java
        ) {
            restTemplate.exchange(
                req,
                HttpStatus::class.java
            )
        }
    }

    companion object {
        private const val URL_GET_ALL_SINGERS = "http://localhost:8080/ch15/singer3/"
        private const val URL_CREATE_SINGER = "http://localhost:8080/ch15/singer3/"
        val LOGGER = LoggerFactory.getLogger(RestClientTest::class.java)
    }
}
