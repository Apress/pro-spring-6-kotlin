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
package com.apress.prospring6.six.plain.dao.pojos

import com.apress.prospring6.six.*
import com.apress.prospring6.six.plain.pojos.Singer
import org.apache.commons.lang3.NotImplementedException
import org.slf4j.LoggerFactory
import java.sql.Date
import java.sql.SQLException
import java.sql.Statement

/**
 * Created by iuliana.cosmina on 03/05/2022
 */
class PlainSingerDao : SingerDao {
    override fun findAll(): Set<Singer> {
        val result: MutableSet<Singer> = HashSet()
        try {
            connection.use { connection ->
                connection!!.prepareStatement(ALL_SELECT).use { statement ->
                    statement.executeQuery().use { resultSet ->
                        while (resultSet.next()) {
                            val singer = Singer().apply {
                                id = resultSet.getLong("id")
                                firstName = resultSet.getString("first_name")
                                lastName = resultSet.getString("last_name")
                                birthDate = resultSet.getDate("birth_date").toLocalDate()
                            }
                            result.add(singer)
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            LOGGER.error("Problem when executing SELECT!", ex)
        }
        return result
    }

    override fun findByFirstName(firstName: String): Set<Singer> {
        throw NotImplementedException("findByFirstName")
    }

    override fun findNameById(id: Long): String {
        throw NotImplementedException("findNameById")
    }

    override fun findLastNameById(id: Long): String {
        throw NotImplementedException("findLastNameById")
    }

    override fun findFirstNameById(id: Long): String {
        throw NotImplementedException("findFirstNameById")
    }

    override fun insert(singer: Singer): Singer? {
        try {
            connection.use { connection ->
                val statement =
                    connection!!.prepareStatement(SIMPLE_INSERT, Statement.RETURN_GENERATED_KEYS)
                statement.setString(1, singer.firstName)
                statement.setString(2, singer.lastName)
                statement.setDate(3, Date.valueOf(singer.birthDate))
                statement.execute()
                val generatedKeys = statement.generatedKeys
                if (generatedKeys.next()) {
                    singer.id = generatedKeys.getLong(1)
                }
                return singer
            }
        } catch (ex: SQLException) {
            LOGGER.error("Problem executing INSERT", ex)
        }
        return null
    }

    override fun update(singer: Singer) {
        throw NotImplementedException("update")
    }

    override fun delete(singerId: Long) {
        try {
            connection.use { connection ->
                connection!!.prepareStatement(SIMPLE_DELETE).use { statement ->
                    statement.setLong(1, singerId!!)
                    statement.execute()
                }
            }
        } catch (ex: SQLException) {
            LOGGER.error("Problem executing DELETE", ex)
        }
    }

    override fun findAllWithAlbums(): Set<Singer> {
        throw NotImplementedException("findAllWithAlbums")
    }

    override fun insertWithAlbum(singer: Singer) {
        throw NotImplementedException("insertWithAlbum")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(PlainSingerDao::class.java)
    }
}