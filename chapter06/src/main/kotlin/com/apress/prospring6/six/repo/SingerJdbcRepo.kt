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

import com.apress.prospring6.six.*
import com.apress.prospring6.six.plain.records.Album
import com.apress.prospring6.six.plain.records.Singer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.util.*
import javax.sql.DataSource
import kotlin.collections.List
import kotlin.collections.MutableMap

/**
 * Created by iuliana.cosmina on 11/05/2022
 */
@Repository("singerRepo")
class SingerJdbcRepo : SingerRepo {
    @set:Autowired
    var dataSource: DataSource? = null
        set(dataSource) {
            field = dataSource ?: throw java.lang.IllegalArgumentException()
            selectAllSingers = SelectAllSingers(dataSource)
            selectSingerByFirstName = SelectSingerByFirstName(dataSource)
            updateSinger = UpdateSinger(dataSource)
            insertSinger = InsertSinger(dataSource)
            insertSingerAlbum = InsertSingerAlbum(dataSource)
            storedFunctionFirstNameById = StoredFunctionFirstNameById(dataSource)
        }
    private var selectAllSingers: SelectAllSingers? = null
    private var selectSingerByFirstName: SelectSingerByFirstName? = null
    private var updateSinger: UpdateSinger? = null
    private var insertSinger: InsertSinger? = null
    private var insertSingerAlbum: InsertSingerAlbum? = null
    private var storedFunctionFirstNameById: StoredFunctionFirstNameById? = null

    override fun findAll(): List<Singer> {
        return selectAllSingers!!.execute().requireNoNulls()
    }

    override fun findByFirstName(firstName: String): List<Singer> {
        return selectSingerByFirstName!!.executeByNamedParam(
            mapOf( "first_name" to firstName)).toList().requireNoNulls()
    }

    override fun findNameById(id: Long): String? {
        return null
    }

    override fun findLastNameById(id: Long): String? {
        return null
    }

    override fun findFirstNameById(id: Long): String? {
        return storedFunctionFirstNameById!!.execute(id)[0]
    }

    override fun findAllWithAlbums(): List<Singer> {
        val jdbcTemplate = JdbcTemplate(dataSource!!)
        val map: MutableMap<Long, Singer> = mutableMapOf()
        jdbcTemplate.query(FIND_SINGER_ALBUM) { rs ->
            while (rs.next()) {
                val singerID = rs.getLong("id")
                val singer = map.computeIfAbsent(singerID) { s: Long? ->
                    Singer(
                        singerID,
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birth_date").toLocalDate(),
                        mutableSetOf()
                    )
                }
                val albumID = rs.getLong("album_id")
                if (albumID > 0) {
                    singer.albums.add(
                            Album(
                                albumID, singerID, rs.getString("title"),
                                rs.getDate("release_date").toLocalDate()
                            )
                        )
                }
            }
        }
        return map.values.toList()
    }

    override fun insert(singer: Singer) {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        insertSinger!!.updateByNamedParam(
            mapOf(
                "first_name" to singer.firstName,
                "last_name" to  singer.lastName,
                "birth_date" to singer.birthDate
            ), keyHolder
        )
        val generatedId = keyHolder.key!!.toLong()
        LOGGER.info("New singer  {} {} inserted with id {}  ", singer.firstName, singer.lastName, generatedId)
    }

    override fun update(singer: Singer) {
        updateSinger!!.updateByNamedParam(
            mapOf(
                "first_name" to singer.firstName,
                "last_name" to singer.lastName,
                "birth_date" to singer.birthDate,
                "id" to singer.id
            )
        )
        LOGGER.info("Existing singer updated with id: " + singer.id)
    }

    override fun delete(singerId: Long) {}

    override fun insertWithAlbum(singer: Singer) {
        val keyHolder = GeneratedKeyHolder()
        insertSinger!!.updateByNamedParam(
            mapOf(
                "first_name" to singer.firstName,
                "last_name" to singer.lastName,
                "birth_date" to singer.birthDate
            ), keyHolder
        )
        val newSingerId = keyHolder.key!!.toLong()
        LOGGER.info("New singer  {} {} inserted with id {}  ", singer.firstName, singer.lastName, newSingerId)
        val albums = singer.albums
        for (album in albums) {
            insertSingerAlbum!!.updateByNamedParam(
                mapOf(
                    "singer_id" to newSingerId,
                    "title" to album.title,
                    "release_date" to album.releaseDate
                )
            )
        }
        insertSingerAlbum!!.flush()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SingerJdbcRepo::class.java)
    }
}