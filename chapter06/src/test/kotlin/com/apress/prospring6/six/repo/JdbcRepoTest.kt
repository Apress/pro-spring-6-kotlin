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

import com.apress.prospring6.six.plain.records.Singer
import com.apress.prospring6.six.repo.JdbcRepoTest.EmptyEmbeddedJdbcConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.jdbc.SqlMergeMode
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.util.function.Consumer
import javax.sql.DataSource

/**
 * Created by iuliana.cosmina on 13/05/2022
 */
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql("classpath:h2/drop-schema.sql", "classpath:h2/create-schema.sql")
@SpringJUnitConfig(classes = [EmptyEmbeddedJdbcConfig::class, SingerJdbcRepo::class])
class JdbcRepoTest {
    @Autowired
    var singerRepo: SingerRepo? = null

    @Test
    @DisplayName("should return all singers")
    @Sql(
        value = ["classpath:h2/test-data.sql"],
        config = SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun testFindAllWithMappingSqlQuery() {
        val singers = singerRepo!!.findAll()
        Assertions.assertEquals(3, singers.size)
        singers.forEach(Consumer { singer: Singer ->
            LOGGER.info(
                singer.toString()
            )
        })
    }

    @Test
    @DisplayName("should return Chuck Berry")
    /*@Sql(value = "classpath:h2/add-chuck.sql",
            config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)*/
    /* @SqlGroup({
            @Sql(value = "classpath:h2/add-chuck.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:h2/remove-chuck.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })*/
    @SqlGroup(
        Sql(
            statements = arrayOf("insert into SINGER (first_name, last_name, birth_date) values ('Chuck', 'Berry', '1926-09-18')"),
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
        ),
        Sql(
            statements = arrayOf("delete from  SINGER where first_name = 'Chuck'"),
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
    )
    fun testFindByNameWithMappingSqlQuery() {
        val singers = singerRepo!!.findByFirstName("Chuck")
        Assertions.assertEquals(1, singers.size)
        LOGGER.info("Result: {}", singers[0])
    }

    @Configuration
    open class EmptyEmbeddedJdbcConfig {
        @Bean
        open fun dataSource(): DataSource? {
            return try {
                val dbBuilder = EmbeddedDatabaseBuilder()
                dbBuilder.setType(EmbeddedDatabaseType.H2).setName("musicdb").build()
            } catch (e: Exception) {
                LOGGER.error("Embedded DataSource bean cannot be created!", e)
                null
            }
        }

        companion object {
            private val LOGGER = LoggerFactory.getLogger(EmptyEmbeddedJdbcConfig::class.java)
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RepoBeanTest::class.java)
    }
}