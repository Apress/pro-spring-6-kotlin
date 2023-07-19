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
import com.apress.prospring6.six.repo.StoredFunctionV3Test.TestContainersConfig
import jakarta.annotation.PostConstruct
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.ext.ScriptUtils
import org.testcontainers.jdbc.JdbcDatabaseDelegate
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.shaded.com.google.common.io.Resources
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.script.ScriptException

/**
 * Created by iuliana.cosmina on 09/06/2022
 * version without the @Sql annotations
 */
@Disabled // because needs preparational work for the Docker environment
@Testcontainers
@SpringJUnitConfig(classes = [TestContainersConfig::class, SingerJdbcRepo::class])
class StoredFunctionV3Test {
    @Autowired
    var singerRepo: SingerRepo? = null

    @Test
    fun testFindAllQuery() {
        val singers = singerRepo!!.findAll()
        Assertions.assertEquals(3, singers.size)
    }

    @Test
    fun testStoredFunction() {
        val firstName = singerRepo!!.findFirstNameById(2L)
        Assertions.assertEquals("Ben", firstName)
    }

    @Configuration
    @Import(
        BasicDataSourceCfg::class
    )
    open class TestContainersConfig {
        @PostConstruct
        @Throws(ScriptException::class, IOException::class)
        open fun initialize() {
            val script1 =
                Resources.toString(Resources.getResource("testcontainers/create-schema.sql"), StandardCharsets.UTF_8)
            val script2 = Resources.toString(
                Resources.getResource("testcontainers/original-stored-function.sql"),
                StandardCharsets.UTF_8
            )
            mariaDB.start()
            ScriptUtils.executeDatabaseScript(
                JdbcDatabaseDelegate(mariaDB, ""),
                "schema.sql",
                script1,
                false,
                false,
                ScriptUtils.DEFAULT_COMMENT_PREFIX,
                ScriptUtils.DEFAULT_STATEMENT_SEPARATOR,
                "$$",
                "$$$"
            )
            ScriptUtils.executeDatabaseScript(
                JdbcDatabaseDelegate(mariaDB, ""),
                "schema.sql",
                script2,
                false,
                false,
                ScriptUtils.DEFAULT_COMMENT_PREFIX,
                ScriptUtils.DEFAULT_STATEMENT_SEPARATOR,
                "$$",
                "$$$"
            )
        }
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