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
package com.apress.prospring6.nine

import com.apress.prospring6.nine.boot.Chapter9Application
import com.apress.prospring6.nine.boot.entities.Album
import com.apress.prospring6.nine.boot.ex.TitleTooLongException
import com.apress.prospring6.nine.boot.services.AllService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.jdbc.SqlMergeMode
import java.time.LocalDate

/**
 * Created by iuliana.cosmina on 28/07/2022
 */
@ActiveProfiles("test")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql("classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql")
@SpringBootTest(classes = [Chapter9Application::class])
open class Chapter9ApplicationTest {
    @Autowired
    var service: AllService? = null

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
    @DisplayName("should perform a rollback because DataIntegrityViolationException")
    open fun testRollbackRuntimeUpdate() {
        val singer = service!!.findByIdWithAlbums(5L)
        Assertions.assertNotNull(singer)
        singer!!.firstName = "Eunice Kathleen"
        singer.lastName = "Waymon"
        val album = Album()
        album.title = "Little Girl Blue"
        album.releaseDate = LocalDate.of(1959, 2, 20)
        album.singer = singer
        val albums = mutableSetOf(album)
        Assertions.assertThrows(
            DataIntegrityViolationException::class.java,
            { service!!.saveSingerWithAlbums(singer, albums) },
            "PersistenceException not thrown!"
        )
        val nina = service!!.findByIdWithAlbums(5L)!!
        Assertions.assertAll("nina was not updated",
            Executable { Assertions.assertNotNull(nina) },
            Executable {
                Assertions.assertNotEquals(
                    "Eunice Kathleen",
                    nina.firstName
                )
            },
            Executable {
                Assertions.assertNotEquals(
                    "Waymon",
                    nina.lastName
                )
            }
        )
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
    @DisplayName("should perform a rollback because TitleTooLongException")
    open fun testRollbackCheckedUpdate() {
        val singer = service!!.findByIdWithAlbums(5L)
        Assertions.assertNotNull(singer)
        singer!!.firstName = "Eunice Kathleen"
        singer.lastName = "Waymon"
        val album = Album()
        album.title = """ 
            Sit there and count your fingers
            What can you do?
            Old girl you're through
            Sit there, count your little fingers
            Unhappy little girl blue
            """
        album.releaseDate = LocalDate.of(1959, 2, 20)
        album.singer = singer
        val albums = mutableSetOf(album)
        Assertions.assertThrows(
            TitleTooLongException::class.java,
            { service!!.saveSingerWithAlbums(singer, albums) },
            "TitleTooLongException not thrown!"
        )
        val nina = service!!.findByIdWithAlbums(5L)!!
        Assertions.assertAll("nina was not updated",
            Executable { Assertions.assertNotNull(nina) },
            Executable {
                Assertions.assertNotEquals(
                    "Eunice Kathleen",
                    nina.firstName
                )
            },
            Executable {
                Assertions.assertNotEquals(
                    "Waymon",
                    nina.lastName
                )
            }
        )
    }
}