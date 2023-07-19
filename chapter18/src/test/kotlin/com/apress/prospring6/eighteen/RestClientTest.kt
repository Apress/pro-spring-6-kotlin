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
package com.apress.prospring6.eighteen

import com.apress.prospring6.eighteen.entities.Singer
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

/**
 * Created by iuliana on 17/01/2023
 */
//@Disabled("Start Apache Tomcat and deploy the app first, then comment this line, then run each test manually and notice the result!")
class RestClientTest {
    var restTemplate = RestTemplate()
    @RepeatedTest(10)
    @Test
    fun testCreate() {
        LOGGER.info("--> Testing create singer")
        var singerNew = Singer().apply {
            firstName="TEST" + System.currentTimeMillis()
            lastName="Singer" + System.currentTimeMillis()
            birthDate=LocalDate.now()
        }
        singerNew = restTemplate.postForObject(
            URI_SINGER_ROOT, singerNew,
            Singer::class.java
        )!!
        LOGGER.info("Singer created successfully: $singerNew")
    }

    @Test
    fun testDelete() {
        LOGGER.info("--> Deleting singers with id > 15")
        for (i in 16..99) {
            try {
                restTemplate.delete(URI_SINGER_WITH_ID, i)
            } catch (e: Exception) {
                // no need for this
            }
        }
    }

    companion object {
        private const val URI_SINGER_ROOT = "http://localhost:8080/chapter18-1.0-SNAPSHOT/singer/"
        private const val URI_SINGER_WITH_ID = "http://localhost:8080/chapter18-1.0-SNAPSHOT/singer/{id}"
        val LOGGER = LoggerFactory.getLogger(RestClientTest::class.java)
    }
}
