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
package com.apress.prospring6.eight

import com.apress.prospring6.eight.config.JpaConfig
import com.apress.prospring6.eight.entities.Album
import com.apress.prospring6.eight.entities.Instrument
import com.apress.prospring6.eight.entities.Singer
import com.apress.prospring6.eight.service.SingerService
import com.apress.prospring6.eight.service.SingerSummaryService
import com.apress.prospring6.eight.view.SingerSummary
import jakarta.annotation.PostConstruct
import org.hibernate.cfg.Environment
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
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

/**
 * Created by iuliana.cosmina on 03/07/2022
 */
@Testcontainers
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql("classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql")
@SpringJUnitConfig(classes = [SingerServiceTest.TestContainersConfig::class])
class SingerServiceTest {
    @Autowired
    @Qualifier("jpaSingerService")
    var singerService: SingerService? = null

    @Autowired
    var singerSummaryService: SingerSummaryService? = null

    @Test
    @DisplayName("should return all singers")
    fun testFindAll() {
        val singers: List<Singer> = singerService!!.findAll()
            .peek{ singer: Singer ->
                    LOGGER.info(
                        singer.toString()
                    )
                }.toList()
        Assertions.assertEquals(3, singers.size)
    }

    @Test
    @DisplayName("should return all singers with albums")
    fun testFindAllWithAlbum() {
        val singers: List<Singer> = singerService!!.findAllWithAlbum()
            .peek{ s: Singer ->
                    LOGGER.info(s.toString())
                s.albums.forEach{ a: Album ->
                    LOGGER.info(
                        "\tAlbum:$a"
                    )
                }
                s.instruments.forEach{ i: Instrument ->
                    LOGGER.info(
                        "\tInstrument: " + i.instrumentId
                    )
                }
            }.toList()
        Assertions.assertEquals(3, singers.size)
    }

    @Test
    @DisplayName("should return all singers and their most recent album as records")
    fun testFindAllWithAlbumAsRecords() {
        val singers = singerSummaryService!!.findAllAsRecord()
            .peek{ s ->
                LOGGER.info(
                    s.toString()
                )
            }.toList()
        Assertions.assertEquals(2, singers.size)
    }

    @Test
    @DisplayName("should return all singers and their most recent album as POJOs")
    fun testFindAllAsPojos() {
        val singers: List<SingerSummary> = singerSummaryService!!.findAll()
            .peek{ s: SingerSummary ->
                LOGGER.info(
                    s.toString()
                )
            }.toList()
        Assertions.assertEquals(2, singers.size)
    }

    @Test
    @DisplayName("should insert a singer with associations")
    @Sql(
        statements = [ // avoid dirtying up the test context
            "delete from ALBUM where SINGER_ID = (select ID from SINGER where FIRST_NAME = 'BB')", "delete from SINGER_INSTRUMENT where SINGER_ID = (select ID from SINGER where FIRST_NAME = 'BB')", "delete from SINGER where FIRST_NAME = 'BB'"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    fun testInsert() {
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
        singerService!!.save(singer)
        Assertions.assertNotNull(singer.id)
        val singers: List<Singer> = singerService!!.findAllWithAlbum()
            .peek { s: Singer ->
                    LOGGER.info(s.toString())
                    s.albums.forEach{ a: Album ->
                        LOGGER.info(
                            "\tAlbum:$a"
                        )
                    }
                    s.instruments.forEach{ i: Instrument ->
                        LOGGER.info(
                            "\tInstrument: " + i.instrumentId
                        )
                    }
                }.toList()
        Assertions.assertEquals(4, singers.size)
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
        val singer = singerService!!.findById(5L)!!
        //making sure such singer exists
        Assertions.assertNotNull(singer)
        //making sure we got expected singer
        Assertions.assertEquals("Simone", singer.lastName)
        //retrieve the album
        val album = singer.albums.stream().filter { a: Album -> a.title == "I Put a Spell on You" }
            .findFirst().orElse(null)
        Assertions.assertNotNull(album)
        singer.firstName = "Eunice Kathleen"
        singer.lastName = "Waymon"
        singer.removeAlbum(album)
        val version = singer.version
        singerService!!.save(singer)
        val nina = singerService!!.findById(5L)!!
        Assertions.assertAll("nina was updated",
            Executable { Assertions.assertNotNull(nina) },
            Executable {
                Assertions.assertEquals(
                    version + 1,
                    nina.version
                )
            }
        )
    }

    @Test
    fun testUpdateAlbumSet() {
        val singer = singerService!!.findById(1L)!!
        //making sure such singer exists
        Assertions.assertNotNull(singer)
        //making sure we got expected record
        Assertions.assertEquals("Mayer", singer.lastName)
        //retrieve the album
        val album = singer.albums.stream().filter { a: Album -> a.title == "Battle Studies" }
            .findAny().orElse(null)
        singer.firstName = "John Clayton"
        singer.removeAlbum(album)
        singerService!!.save(singer)
        val singers: List<Singer> = singerService!!.findAllWithAlbum()
            .peek{ s: Singer ->
                    LOGGER.info(s.toString())
                    s.albums.forEach{ a: Album ->
                        LOGGER.info(
                            "\tAlbum:$a"
                        )
                    }
                    s.instruments.forEach{ i: Instrument ->
                        LOGGER.info(
                            "\tInstrument: " + i.instrumentId
                        )
                    }
                }.toList()
        Assertions.assertEquals(3, singers.size)
    }

    @Test
    @Sql(scripts = ["classpath:testcontainers/add-chuck.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("should delete a singer")
    fun testDelete() {
        val singer = singerService!!.findById(6L)!!
        //making sure such singer exists
        Assertions.assertNotNull(singer)
        singerService!!.delete(singer)
        val deleted = singerService!!.findById(6L)
        Assertions.assertTrue(deleted == null)
    }

    @Sql("classpath:testcontainers/stored-function.sql")
    @Test
    fun testFindFirstNameById() {
        val res = singerService!!.findFirstNameById(1L)
        Assertions.assertEquals("John", res)
    }

    @Configuration
    @Import(JpaConfig::class)
    open class TestContainersConfig {
        @Autowired
        var jpaProperties: Properties? = null
        @PostConstruct
        open fun initialize() {
            jpaProperties!![Environment.FORMAT_SQL] = true
            jpaProperties!![Environment.USE_SQL_COMMENTS] = true
            jpaProperties!![Environment.SHOW_SQL] = true
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SingerServiceTest::class.java)

        @Container
        var mariaDB: MariaDBContainer<*> = MariaDBContainer("mariadb:latest")

        @DynamicPropertySource // this does the magic
        @JvmStatic
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