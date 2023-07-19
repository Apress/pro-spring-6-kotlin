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
package com.apress.prospring6.six.rowmapper

import com.apress.prospring6.six.*
import com.apress.prospring6.six.config.BasicDataSourceCfg
import com.apress.prospring6.six.plain.records.Album
import com.apress.prospring6.six.plain.records.Singer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowCallbackHandler
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource
import javax.swing.tree.TreePath


/**
 * Created by iuliana.cosmina on 11/05/2022
 */
interface SingerDao {
    fun findAll(): Set<Singer>
    fun findAllWithAlbums(): Set<Singer>
}

internal class RowMapperDao : SingerDao {
    var namedTemplate: NamedParameterJdbcTemplate? = null

    override fun findAll(): Set<Singer> {
        //return namedTemplate!!.query(
        //    ALL_SELECT, SingerMapper()
        //).toSet()
        return namedTemplate!!.query(
                ALL_SELECT
            ) { rs: ResultSet, rowNum: Int ->
                Singer(
                    rs.getLong("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("birth_date").toLocalDate(),
                    mutableSetOf()
                )
            }.toSet()
    }

    /*class SingerMapper : RowMapper<Singer> {
        override
        fun  mapRow(rs:ResultSet, rowNum:Int):Singer {
            return Singer(rs.getLong("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("birth_date").toLocalDate(),
                    mutableSetOf())
        }
    }*/

    /*override
    fun findAllWithAlbums():Set<Singer> {
        return namedTemplate!!.query(ALL_JOIN_SELECT, SingerWithAlbumsExtractor())!!.toSet()
    }

    class SingerWithAlbumsExtractor : ResultSetExtractor<Set<Singer>> {
        override
        fun extractData(rs:ResultSet):Set<Singer> {
            val map = mutableMapOf<Long, Singer>()
            while (rs.next()) {
                val id = rs.getLong("id")
                var singer = map[id];
                if (singer == null) {
                    singer = Singer(id,
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birth_date").toLocalDate(),
                        mutableSetOf())
                    map[id] = singer
                }

                val albumId = rs.getLong("album_id")
                if (albumId > 0) {
                    val album = Album(albumId,id,rs.getString("title"),
                        rs.getDate("release_date").toLocalDate()
                    );
                    singer.albums.add(album);
                }
            }
            return map.values.toSet()
        }
    }*/

    override fun findAllWithAlbums(): Set<Singer> {
        return namedTemplate!!.query<Set<Singer>>(
            ALL_JOIN_SELECT
        ) { rs: ResultSet ->
            val map: MutableMap<Long, Singer> =
                HashMap()
            var singer: Singer?
            while (rs.next()) {
                val id = rs.getLong("id")
                singer = map[id]
                if (singer == null) {
                    singer = Singer(
                        id, rs.getString("first_name"), rs.getString("last_name"),
                        rs.getDate("birth_date").toLocalDate(),
                        HashSet()
                    )
                    map[id] = singer
                }
                val albumId = rs.getLong("album_id")
                if (albumId > 0) {
                    val album =
                        Album(
                            albumId, id, rs.getString("title"),
                            rs.getDate("release_date").toLocalDate()
                        )
                    singer.albums.add(album)
                }
            }
            map.values.toSet()
        }!!
    }
}

@Import(BasicDataSourceCfg::class)
@Configuration
open class RowMapperCfg {
    @Autowired
    var dataSource: DataSource? = null

    @Bean
    open fun namedTemplate(): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }

    @Bean
    fun singerDao(): SingerDao {
        val dao = RowMapperDao()
        dao.namedTemplate = namedTemplate()
        return dao
    }
}
