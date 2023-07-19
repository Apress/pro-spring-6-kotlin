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
package com.apress.prospring6.six.plain

import com.apress.prospring6.six.plain.dao.records.RecordSingerDao
import com.apress.prospring6.six.plain.dao.records.SingerDao
import com.apress.prospring6.six.plain.records.Singer
import org.slf4j.LoggerFactory
import java.time.LocalDate

/**
 * Created by iuliana.cosmina on 03/05/2022
 */
object RecordsJdbcDemo {
    private val LOGGER = LoggerFactory.getLogger(RecordsJdbcDemo::class.java)
    private val singerDao: SingerDao = RecordSingerDao()

    init {
        try {
            Class.forName("org.mariadb.jdbc.Driver")
        } catch (ex: ClassNotFoundException) {
            LOGGER.error("Problem loading DB Driver!", ex)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        LOGGER.info("Listing initial singer data:")
        listAllSingers()
        LOGGER.info("-------------")
        LOGGER.info("Insert a new singer")
        var singer = Singer(null, "Ed", "Sheeran", LocalDate.of(1991, 2, 17), mutableSetOf())
        singer = singerDao.insert(singer)!!
        LOGGER.info("The singer has ID now: " + singer.id)
        LOGGER.info("Listing singer data after new singer created:")
        listAllSingers()
        LOGGER.info("-------------")
        LOGGER.info("Deleting the previous created singer")
        singer.id?.run { singerDao.delete(this) }
        LOGGER.info("Listing singer data after new singer deleted:")
        listAllSingers()
    }

    private fun listAllSingers() {
        val singers = singerDao.findAll()
        for (singer in singers) {
            LOGGER.info(singer.toString())
        }
    }
}