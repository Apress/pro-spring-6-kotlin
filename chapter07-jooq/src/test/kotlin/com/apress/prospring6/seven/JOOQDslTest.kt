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
import com.apress.prospring6.seven.jooq.generated.Tables.*
import com.apress.prospring6.seven.jooq.generated.tables.Singer
import com.apress.prospring6.seven.jooq.generated.tables.SingerInstrument
import com.apress.prospring6.seven.jooq.generated.tables.records.SingerRecord
import com.apress.prospring6.seven.jooq.records.AlbumRecord
import com.apress.prospring6.seven.jooq.records.SingerInstrumentRecord
import com.apress.prospring6.seven.jooq.records.SingerWithAlbums
import com.apress.prospring6.seven.jooq.records.SingerWithInstruments
import org.jooq.*
import org.jooq.Records.mapping
import org.jooq.exception.DataAccessException
import org.jooq.impl.DSL
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
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
import java.time.LocalDate


/**
 * Created by iuliana.cosmina on 19/06/2022
 */
@Testcontainers
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql("classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql")
@SpringJUnitConfig(classes = [BasicDataSourceCfg::class, JOOQConfig::class])
class JOOQDslTest {
    @Autowired
    var dslContext: DSLContext? = null
    @Test
    @DisplayName("should return all singers")
    fun findAll() {
        val singers: Result<SingerRecord> = dslContext!!.selectFrom<SingerRecord>(SINGER).fetch()
        Assertions.assertEquals(3, singers.size)
    }

    @Test
    @DisplayName("should return singer by id")
    fun testFindById() {
        val singerRecord: SingerRecord = dslContext!!.selectFrom<SingerRecord>(SINGER)
            .where(SINGER.ID.eq(2)).fetchOne()!!
        Assertions.assertNotNull(singerRecord)
        assertEquals("Ben", singerRecord.getFirstName())
    }

    @Test
    @DisplayName("should return all singers with instruments")
    fun findAllWithInstruments() {
        val records: Result<Record4<Int, String, String, Result<Record>>> =
            dslContext!!.select<Int, String, String, Result<Record>>(
                SINGER.ID,
                SINGER.FIRST_NAME,
                SINGER.LAST_NAME,
                DSL.multisetAgg(SINGER_INSTRUMENT.instrument().INSTRUMENT_ID).`as`("INSTRUMENTS")
            )
                .from(SINGER)
                .join(SINGER_INSTRUMENT).on(SINGER_INSTRUMENT.SINGER_ID.eq(SINGER.ID))
                .groupBy(SINGER.ID, SINGER.FIRST_NAME, SINGER.LAST_NAME)
                .fetch()
        Assertions.assertEquals(2, records.size)
    }

    @Test
    @DisplayName("should return all singers with instruments as records")
    open fun findAllWithInstrumentsAsRecords() {
        val singerWithInstruments: List<SingerWithInstruments> = dslContext!!.select(
            Singer.SINGER.FIRST_NAME,
            Singer.SINGER.LAST_NAME,
            DSL.multisetAgg<String?>(
                    SingerInstrument.SINGER_INSTRUMENT.instrument().INSTRUMENT_ID)
                .convertFrom<MutableList<SingerInstrumentRecord>>(
                { r : Result<Record1<String?>?> ->
                    r.map(
                        mapping { it -> SingerInstrumentRecord(it!!) }
                    )
                })
        )
            .from(Singer.SINGER)
            .join(SingerInstrument.SINGER_INSTRUMENT)
            .on(SingerInstrument.SINGER_INSTRUMENT.SINGER_ID.eq(Singer.SINGER.ID))
            .groupBy(Singer.SINGER.FIRST_NAME, Singer.SINGER.LAST_NAME)
            .fetch(mapping{ firstName: String, lastName: String, instruments: MutableList<SingerInstrumentRecord> ->
                SingerWithInstruments(
                    firstName,
                    lastName,
                    instruments
                )
            })
        assertEquals(2, singerWithInstruments.size)
    }


    @Test
    @DisplayName("should return all singers with albums")
    fun findAllWithAlbums() {
        val records: Result<Record3<String, String, String>> =
            dslContext!!.select<String, String, String>(SINGER.FIRST_NAME, SINGER.LAST_NAME, ALBUM.TITLE)
                .from(SINGER)
                .join(ALBUM).on(ALBUM.SINGER_ID.eq(SINGER.ID))
                .fetch()
        assertEquals(3, records.size)
    }

    @Test
    @DisplayName("should return all singers with albums as records")
    fun findAllWithAlbumsAsRecords() {
        val singerWithAlbums: List<SingerWithAlbums> =
            dslContext!!.select(
                SINGER.FIRST_NAME,
                SINGER.LAST_NAME,
                SINGER.BIRTH_DATE,
                DSL.multisetAgg<String?,LocalDate?>(ALBUM.TITLE, ALBUM.RELEASE_DATE)
                    .convertFrom<MutableList<AlbumRecord>>(
                        { r  ->
                          r.map(
                            mapping { title:String, release:LocalDate ->
                                AlbumRecord(title, release)
                            }
                          )
                        }
                    )
            )
                .from(SINGER)
                .innerJoin(ALBUM).on(ALBUM.SINGER_ID.eq(SINGER.ID))
                .groupBy(SINGER.FIRST_NAME, SINGER.LAST_NAME, SINGER.BIRTH_DATE)
                .fetch(mapping{ firstName:String, lastName:String, birthDate:LocalDate, albums:MutableList<AlbumRecord> ->
                    SingerWithAlbums(firstName, lastName, birthDate, albums)
                })
        assertEquals(2, singerWithAlbums.size)
    }

    @Test
    @DisplayName("should insert a singer")
    @Sql(
        statements = ["delete from SINGER where FIRST_NAME = 'BB'"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    fun testInsertSinger() {
        val insertedRows = dslContext!!.insertInto<SingerRecord>(SINGER)
            .columns<String, String, LocalDate>(SINGER.FIRST_NAME, SINGER.LAST_NAME, SINGER.BIRTH_DATE)
            .values("BB", "King", LocalDate.of(1940, 8, 16))
            .execute()
        Assertions.assertEquals(1, insertedRows)
    }

    @Test
    @DisplayName("should insert a singer using SingerRecord")
    @Sql(
        statements = ["delete from SINGER where FIRST_NAME = 'BB'"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    fun testInsertSingerRecord() {
        val singerRecord: SingerRecord = dslContext!!.newRecord(SINGER)
        singerRecord.setFirstName("BB")
        singerRecord.setLastName("King")
        singerRecord.setBirthDate(LocalDate.of(1940, 8, 16))
        val insertedRows: Int = singerRecord.store()
        Assertions.assertEquals(1, insertedRows)
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
        val updatedRows: Int = dslContext!!.update<SingerRecord>(SINGER)
            .set<String>(SINGER.FIRST_NAME, "Eunice Kathleen")
            .set<String>(SINGER.LAST_NAME, "Waymon")
            .where(SINGER.ID.eq(5))
            .execute()
        Assertions.assertEquals(1, updatedRows)
    }

    @Test
    @Sql(scripts = ["classpath:testcontainers/add-chuck.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("should delete a singer")
    fun testDelete() {
        Assertions.assertThrows<DataAccessException>(
            DataAccessException::class.java
        ) {
            dslContext!!.deleteFrom<SingerRecord>(SINGER)
                .where(SINGER.ID.eq(6))
                .execute()
        }

        // in case deletion works
/*        int deletedRows =   dslContext.deleteFrom(SINGER)
                .where(SINGER.ID.eq(6))
                .execute();
        assertEquals(1, deletedRows);*/
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JOOQDslTest::class.java)

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