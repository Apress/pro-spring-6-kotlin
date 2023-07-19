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
package com.apress.prospring6.ten.boot

import com.apress.prospring6.ten.boot.document.Singer
import com.apress.prospring6.ten.boot.service.SingerService
import org.junit.jupiter.api.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class
)
class Mongo10ApplicationTest {
    @Autowired
    var singerService: SingerService? = null

    @Order(1)
    @Test
    fun testSaveThree() {
        singerService!!.saveAll(
            listOf(
                Singer("John", "Mayer", LocalDate.of(1977, 10, 16)),
                Singer("Ben", "Barnes", LocalDate.of(1980, 8, 20)),
                Singer("John", "Butler", LocalDate.of(1975, 4, 1))
            )
        )
    }

    @Order(2)
    @Test
    fun testFindAll() {
        val singers = singerService!!.findAll().peek { s: Singer ->
            LOGGER.info(
                s.toString()
            )
        }.toList()
        Assertions.assertEquals(3, singers.size)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Mongo10ApplicationTest::class.java)

        @Container
        var mongoDBContainer = MongoDBContainer("mongo:5.0.14")

        @DynamicPropertySource
        @JvmStatic
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri", { mongoDBContainer.getReplicaSetUrl("testdb") })
        }
    }
}