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

import com.apress.prospring6.seven.base.config.HibernateConfig
import com.apress.prospring6.seven.base.dao.SingerDao
import com.apress.prospring6.seven.base.entities.Album
import com.apress.prospring6.seven.base.entities.Singer
import jakarta.annotation.PostConstruct
import org.hibernate.cfg.Environment
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.jdbc.SqlMergeMode
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate
import java.util.*
import java.util.function.Consumer

/**
 * Created by iuliana.cosmina on 22/05/2022
 */
@Testcontainers
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql("classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql")
@SpringJUnitConfig(classes = [HibernateTest.TestContainersConfig::class])
class HibernateTest {
    @Autowired
    var singerDao: SingerDao? = null

    @Test
    @DisplayName("should return all singers")
    fun testFindAll() {
        val singers = singerDao!!.findAll()
        Assertions.assertEquals(3, singers.size)
        singers.forEach(Consumer { singer: Singer? ->
            LOGGER.info(
                singer.toString()
            )
        })
    }

    @Test
    @DisplayName("should return singer by id")
    fun testFindById() {
        val singer = singerDao!!.findById(2L)
        Assertions.assertEquals("Ben", singer!!.firstName)
        LOGGER.info(singer.toString())
    }

    @Test
    @DisplayName("should insert a singer with associations")
    @Sql(
        statements = [ // avoid dirtying up the test context
            "delete from ALBUM where SINGER_ID = (select ID from SINGER where FIRST_NAME = 'BB')", "delete from SINGER_INSTRUMENT where SINGER_ID = (select ID from SINGER where FIRST_NAME = 'BB')", "delete from SINGER where FIRST_NAME = 'BB'"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    fun testInsertSinger() {
        val singer = Singer()
        singer.firstName = "BB"
        singer.lastName = "King"
        singer.birthDate = LocalDate.of(1940, 8, 16)
        var album = Album()
        album.title = "My Kind of Blues"
        album.releaseDate = LocalDate.of(1961, 7, 18)
        singer.addAlbum(album)
        album = Album()
        album.title = "A Heart Full of Blues"
        album.releaseDate = LocalDate.of(1962, 3, 20)
        singer.addAlbum(album)
        singerDao!!.save(singer)
        Assertions.assertNotNull(singer.id)
        val singers = singerDao!!.findAllWithAlbum()
        Assertions.assertEquals(4, singers.size)
        listSingersWithAssociations(singers)
    }

    @Configuration
    @Import(HibernateConfig::class)
    open class TestContainersConfig {
        @Autowired
        var hibernateProperties: Properties? = null
        @PostConstruct
        open fun initialize() {
            hibernateProperties!![Environment.FORMAT_SQL] = true
            hibernateProperties!![Environment.USE_SQL_COMMENTS] = true
            hibernateProperties!![Environment.SHOW_SQL] = true
        }
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
        val singer = singerDao!!.findById(5L)
        //making sure such singer exists
        Assertions.assertNotNull(singer)
        //making sure we got expected singer
        Assertions.assertEquals("Simone", singer!!.lastName)
        //retrieve the album
        val album = singer.albums.stream().filter { a: Album -> a.title == "I Put a Spell on You" }
            .findFirst().orElse(null)
        Assertions.assertNotNull(album)
        singer.firstName = "Eunice Kathleen"
        singer.lastName = "Waymon"
        singer.removeAlbum(album)
        val version = singer.version
        singerDao!!.save(singer)
        val nina = singerDao!!.findById(5L)
        Assertions.assertEquals(version + 1, nina!!.version)

        // test the update
        listSingersWithAssociations(singerDao!!.findAllWithAlbum())
    }

    @Test
    @Sql(scripts = ["classpath:testcontainers/add-chuck.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("should delete a singer")
    fun testDelete() {
        val singer = singerDao!!.findById(6L)
        //making sure such singer exists
        Assertions.assertNotNull(singer)
        singerDao!!.delete(singer!!)
        listSingersWithAssociations(singerDao!!.findAllWithAlbum())
    }

    @Test
    fun testNativeQuery() {
        val singer = singerDao!!.findAllDetails("Ben", "Barnes")
        Assertions.assertAll("Native Singer Test",
            Executable {
                Assertions.assertEquals(
                    "Ben",
                    singer!!.firstName
                )
            },
            Executable {
                Assertions.assertEquals(
                    "Barnes",
                    singer!!.lastName
                )
            },
            Executable {
                Assertions.assertEquals(
                    1,
                    singer!!.albums.size
                )
            },
            Executable {
                Assertions.assertEquals(
                    3,
                    singer!!.instruments.size
                )
            }
        )
    }

    @Test
    fun testFindAllNamesByProjection() {
        val singers = singerDao!!.findAllNamesByProjection()
        Assertions.assertEquals(3, singers.size)
        singers.forEach(Consumer { msg: String? ->
            LOGGER.info(
                msg
            )
        })
    }

    @Sql("classpath:testcontainers/stored-function.sql")
    @Test
    fun testFindFirstNameById() {
        val res = singerDao!!.findFirstNameById(1L)
        Assertions.assertEquals("John", res)
    }

    @Disabled("no equivalent SQL syntax supported by Testcontainers")
    @Sql("classpath:testcontainers/stored-procedure.sql")
    @Test
    fun testFindFirstNameByIdUsingProc() {
        val res = singerDao!!.findFirstNameByIdUsingProc(1L)
        Assertions.assertEquals("John", res)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(HibernateTest::class.java)

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

        private fun listSingersWithAssociations(singers: List<Singer>) {
            LOGGER.info(" ---- Listing singers with instruments:")
            for (singer in singers) {
                LOGGER.info(singer.toString())
                for (album in singer.albums) {
                    LOGGER.info("\t" + album.toString())
                }
                for (instrument in singer.instruments) {
                    LOGGER.info("\tInstrument: " + instrument.instrumentId)
                }
            }
        }
    }
}