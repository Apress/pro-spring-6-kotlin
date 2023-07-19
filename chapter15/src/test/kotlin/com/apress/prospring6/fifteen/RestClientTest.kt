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
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpRequest
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.util.*

/**
 * Created by iuliana on 17/01/2023
 */
@Disabled("Start Apache Tomcat and deploy the app first, then comment this line, then run each test manually and notice the result!")
class RestClientTest {
    val restTemplate = RestTemplate()

    @Test
    fun testFindAll() {
        LOGGER.info("--> Testing retrieve all singers")
        val singers = restTemplate.getForObject(
            URI_SINGER_ROOT,
            Array<Singer>::class.java
        )
        Assertions.assertTrue(singers!!.isNotEmpty())
        Arrays.stream(singers).forEach { s: Singer ->
            LOGGER.info(
                s.toString()
            )
        }
    }

    @Test
    fun testFindAllWithExecute() {
        LOGGER.info("--> Testing retrieve all singers")
        val singers = restTemplate.execute(
            URI_SINGER_ROOT, HttpMethod.GET,
            { request: ClientHttpRequest? ->
                LOGGER.debug(
                    "Request submitted ..."
                )
            },
            { response: ClientHttpResponse ->
                Assertions.assertEquals(
                    HttpStatus.OK,
                    response.statusCode
                )
                String(response.body.readAllBytes())
            }
        )
        LOGGER.info("Response: {}", singers)
    }

    @Test
    fun testFindById() {
        LOGGER.info("--> Testing retrieve a singer by id : 1")
        val singer = restTemplate.getForObject(
            URI_SINGER_WITH_ID,
            Singer::class.java, 1
        )
        Assertions.assertNotNull(singer)
        LOGGER.info(singer!!.toString())
    }

    @Test
    fun testCreate() {
        LOGGER.info("--> Testing create singer")
        var singerNew = Singer().apply {
            firstName = "TEST"
            lastName = "Singer"
            birthDate = LocalDate.now()
        }
        singerNew = restTemplate.postForObject(
            URI_SINGER_ROOT, singerNew,
            Singer::class.java
        )!!
        LOGGER.info("Singer created successfully: $singerNew")
    }

    @Test
    fun testCreateWithExchange() {
        LOGGER.info("--> Testing create singer")
        val singerNew = Singer().apply {
            firstName = "TEST2"
            lastName = "Singer2"
            birthDate = LocalDate.now()
        }
        val request = HttpEntity(singerNew)
        val created = restTemplate.exchange(
            URI_SINGER_ROOT, HttpMethod.POST, request,
            Singer::class.java
        )
        Assertions.assertEquals(HttpStatus.CREATED, created.statusCode)
        val singerCreated = created.body
        Assertions.assertNotNull(singerCreated)
        LOGGER.info("Singer created successfully: $singerCreated")
    }

    @Test
    fun testDelete() {
        LOGGER.info("--> Testing delete singer by id : 57") // TODO check your database and select an ID from there
        val initialCount = restTemplate.getForObject(
            URI_SINGER_ROOT,
            Array<Singer>::class.java
        )!!.size
        restTemplate.delete(URI_SINGER_WITH_ID, 57)
        val afterDeleteCount = restTemplate.getForObject(
            URI_SINGER_ROOT,
            Array<Singer>::class.java
        )!!.size
        Assertions.assertEquals(initialCount - afterDeleteCount, 1)
    }

    @Test
    fun testUpdate() {
        LOGGER.info("--> Testing update singer by id : 1")
        val singer = restTemplate.getForObject(
            URI_SINGER_WITH_ID,
            Singer::class.java, 1
        )!!
        singer.firstName = "John"
        restTemplate.put(URI_SINGER_WITH_ID, singer, 1)
        LOGGER.info("Singer update successfully: $singer")
    }

    companion object {
        private const val URI_SINGER_ROOT = "http://localhost:8080/chapter15-1.0-SNAPSHOT/singer/"
        private const val URI_SINGER_WITH_ID = "http://localhost:8080/chapter15-1.0-SNAPSHOT/singer/{id}"
        val LOGGER = LoggerFactory.getLogger(RestClientTest::class.java)
    }
}
