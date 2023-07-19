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

import com.apress.prospring6.six.repo.StoredFunctionV1Test.TestContainersConfig
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.apache.commons.dbcp2.BasicDataSource
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.containers.MariaDBContainer
import javax.sql.DataSource

/**
 * Created by iuliana.cosmina on 09/06/2022
 * This class uses the mariaDB container as a bean, which works if you implement the behaviour to stop it when the test context is destroyed.
 */
@Disabled // because needs preparational work for the Docker environment
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql("classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql") // This works
@SpringJUnitConfig(classes = [TestContainersConfig::class, SingerJdbcRepo::class])
class StoredFunctionV1Test {
    @Autowired
    var singerRepo: SingerRepo? = null

    @Test
    fun testFindAllQuery() {
        val singers = singerRepo!!.findAll()
        Assertions.assertEquals(3, singers.size)
    }

    @Test
    @Disabled
    @Sql("classpath:testcontainers/stored-function.sql")
    fun  // this does not! Testcontainers simply can't support all SQL dialects to 100%.
            testStoredFunction() {
        val firstName = singerRepo!!.findFirstNameById(2L)
        Assertions.assertEquals("Ben", firstName)
    }

    @Configuration
    open class TestContainersConfig {
        var mariaDB: MariaDBContainer<*> = MariaDBContainer("mariadb:latest")

        @PostConstruct
        open fun initialize() {
            mariaDB.start()
        }

        @PreDestroy
        open fun tearDown() {
            mariaDB.stop()
        }

        @Bean
        open fun dataSource(): DataSource? {
            return try {
                val dataSource = BasicDataSource()
                dataSource.driverClassName = mariaDB.driverClassName
                dataSource.url = mariaDB.jdbcUrl
                dataSource.username = mariaDB.username
                dataSource.password = mariaDB.password
                dataSource
            } catch (e: Exception) {
                LOGGER.error("MariaDB TestContainers DataSource bean cannot be created!", e)
                null
            }
        }

        companion object {
            private val LOGGER = LoggerFactory.getLogger(TestContainersConfig::class.java)
        }
    }
}