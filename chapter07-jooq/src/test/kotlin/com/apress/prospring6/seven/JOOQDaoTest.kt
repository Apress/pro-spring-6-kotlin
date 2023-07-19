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
package com.apress.prospring6.seven

import com.apress.prospring6.seven.base.config.BasicDataSourceCfg
import com.apress.prospring6.seven.jooq.config.JOOQConfig
import com.apress.prospring6.seven.jooq.generated.tables.daos.SingerDao
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.jdbc.SqlMergeMode
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * Created by iuliana.cosmina on 19/06/2022
 */
@Testcontainers
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql("classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql")
@SpringJUnitConfig(classes = [BasicDataSourceCfg::class, JOOQConfig::class])
class JOOQDaoTest {
    @Autowired
    var dslContext: DSLContext? = null

    @Test
    @DisplayName("should return all singers")
    fun findAll() {
        val dao = SingerDao(
            dslContext!!.configuration()
        )
        val singers = dao.findAll()
        Assertions.assertEquals(3, singers.size)
    }

    @Test
    @DisplayName("should return singer by id")
    fun testFindById() {
        val dao = SingerDao(
            dslContext!!.configuration()
        )
        val singer = dao.findById(2)
        Assertions.assertNotNull(singer)
        Assertions.assertEquals("Ben", singer!!.firstName)
    }

    @Test
    @SqlGroup(
        Sql(
            scripts = ["classpath:testcontainers/add-nina.sql"],
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
        ),
        Sql(
            scripts = ["classpath:testcontainers/remove-nina.sql"],
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
    )
    @DisplayName("should update a singer")
    fun testUpdate() {
        val dao = SingerDao(
            dslContext!!.configuration()
        )
        val nina = dao.findById(5)
        Assertions.assertNotNull(nina)
        nina!!.firstName = "Eunice Kathleen"
        nina.lastName = "Waymon"
        dao.update(nina)
        val updatedNina = dao.findById(5)
        Assertions.assertNotNull(updatedNina)
        Assertions.assertEquals("Eunice Kathleen", updatedNina!!.firstName)
        Assertions.assertEquals("Waymon", updatedNina.lastName)
    }

    companion object {
        @Container
        var mariaDB: MariaDBContainer<*> = MariaDBContainer("mariadb:latest")

        @DynamicPropertySource // this does the magic
        @JvmStatic
        fun setUp(registry: DynamicPropertyRegistry) {
            registry.add("jdbc.driverClassName") { mariaDB.driverClassName }
            registry.add("jdbc.url") { mariaDB.jdbcUrl }
            registry.add("jdbc.username") { mariaDB.username }
            registry.add("jdbc.password") { mariaDB.password }
        }
    }
}