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
package com.apress.prospring6.six.boot

import com.apress.prospring6.six.boot.records.Singer
import com.apress.prospring6.six.boot.repo.SingerRepo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

/**
 * Created by iuliana.cosmina on 14/05/2022
 */
@ActiveProfiles("testcontainers")
/*@Sql(value = "classpath:testcontainers/create-schema.sql",
        config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)*/
@SpringBootTest(classes = [Chapter6Application::class])
class Chapter6ApplicationV2Test {
    @Autowired
    var singerRepo: SingerRepo? = null

    @Test
    @DisplayName("should return all singers")
    fun testFindAllWithJdbcTemplate() {
        val singers: List<Singer> = singerRepo!!.findAll()
            .toList()
        Assertions.assertEquals(3, singers.size)
        singers.forEach{ singer: Singer ->
            LOGGER.info(
                singer.toString()
            )
        }
    }

    @Test
    @DisplayName("find singer by name")
    @Sql("classpath:testcontainers/stored-function.sql")
    fun testStoredFunction() {
        val firstName = singerRepo!!.findFirstNameById(2L)
        Assertions.assertEquals("Ben", firstName)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Chapter6ApplicationV2Test::class.java)
    }
}