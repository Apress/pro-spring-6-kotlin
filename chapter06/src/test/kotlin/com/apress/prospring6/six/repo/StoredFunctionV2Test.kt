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
package com.apress.prospring6.six.repo

import com.apress.prospring6.six.config.BasicDataSourceCfg
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * Created by iuliana.cosmina on 09/06/2022
 * This class uses the mariaDB container as a static object whose lifecycle is managed by JUnit Jupiter - because of the `Testcontainers`
 */
@Disabled // because needs preparational work for the Docker environment
@Testcontainers
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql("classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql") // This works
@SpringJUnitConfig(classes = [BasicDataSourceCfg::class, SingerJdbcRepo::class])
class StoredFunctionV2Test {
    @Autowired
    var singerRepo: SingerRepo? = null

    @Test
    fun testFindAllQuery() {
        val singers = singerRepo!!.findAll()
        Assertions.assertEquals(3, singers.size)
    }

    @Test //@Sql({ "classpath:testcontainers/original-stored-function.sql" }) // this does not! Testcontainers simply can't support all SQL dialects to 100%.
    @Sql("classpath:testcontainers/stored-function.sql")
    fun  // different SQL syntax
            testStoredFunction() {
        val firstName = singerRepo!!.findFirstNameById(2L)
        Assertions.assertEquals("Ben", firstName)
    }

    companion object {
        @Container
        var mariaDB: MariaDBContainer<*> = MariaDBContainer("mariadb:latest")

        @DynamicPropertySource // this does the magic
        fun setUp(registry: DynamicPropertyRegistry) {
            with(registry) {
                add("jdbc.driverClassName") { mariaDB.driverClassName }
                add("jdbc.url") { mariaDB.jdbcUrl }
                add("jdbc.username") { mariaDB.username }
                add("jdbc.password") { mariaDB.password }
            }
        }
    }
}