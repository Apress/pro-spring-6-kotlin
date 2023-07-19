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

import com.apress.prospring6.seven.base.dao.SingerDao
import com.apress.prospring6.seven.base.entities.Album
import com.apress.prospring6.seven.base.entities.Singer
import com.apress.prospring6.seven.config.HibernateTestConfig
import org.junit.jupiter.api.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Commit
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.time.LocalDate
import java.util.function.Consumer

/**
 * Created by iuliana.cosmina on 16/06/2022
 */
@SpringJUnitConfig(classes = [HibernateTestConfig::class])
@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class
)
class H2HibernateTest {
    @Autowired
    var singerDao: SingerDao? = null
    @Commit
    @Test
    @Order(1)
    @DisplayName("01. should insert a singer with albums")
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
        val created = singerDao!!.save(singer)
        Assertions.assertNotNull(created.id)
    }

    @Test
    @Order(2)
    @DisplayName("02. should return all singers")
    fun testFindAll() {
        val singers = singerDao!!.findAll()
        Assertions.assertEquals(1, singers.size)
        singers.forEach(Consumer { singer: Singer? ->
            LOGGER.info(
                singer.toString()
            )
        })
    }

    @Test
    @Order(3)
    @DisplayName("03. should update a singer")
    fun testUpdate() {
        val singer = singerDao!!.findAll()[0]

        //making sure such singer exists
        Assertions.assertNotNull(singer)
        singer.firstName = "Riley B. "
        val version = singer.version
        singerDao!!.save(singer)
        val bb = singerDao!!.findById(singer.id)
        Assertions.assertEquals(version + 1, bb!!.version)
    }

    @Test
    @Order(4)
    @DisplayName("04. should delete a singer")
    fun testDelete() {
        val singer = singerDao!!.findAll()[0]
        //making sure such singer exists
        Assertions.assertNotNull(singer)
        singerDao!!.delete(singer)
        Assertions.assertEquals(0, singerDao!!.findAll().size)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(HibernateTest::class.java)
    }
}