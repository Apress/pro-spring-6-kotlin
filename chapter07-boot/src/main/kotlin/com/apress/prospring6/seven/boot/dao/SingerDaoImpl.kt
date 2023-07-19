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
package com.apress.prospring6.seven.boot.dao

import com.apress.prospring6.seven.boot.entities.Album
import com.apress.prospring6.seven.boot.entities.Instrument
import com.apress.prospring6.seven.boot.entities.Singer
import jakarta.persistence.Tuple
import org.hibernate.SessionFactory
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Connection
import java.sql.Date
import java.sql.Types
import java.util.concurrent.atomic.AtomicReference
import java.util.stream.Collectors

/**
 * Created by iuliana.cosmina on 22/05/2022
 */
@Transactional
@Repository("singerDao")
class SingerDaoImpl(private val sessionFactory: SessionFactory) : SingerDao {
    @Transactional(readOnly = true)
    override fun findAll(): List<Singer> {
        return sessionFactory.currentSession.createQuery(
            "from Singer s",
            Singer::class.java
        ).list()
    }

    @Transactional(readOnly = true)
    override fun findAllDetails(firstName: String?, lastName: String?): Singer {
        val results = sessionFactory.currentSession
            .createNativeQuery<Tuple>(
                ALL_SELECT,
                Tuple::class.java
            )
            .setParameter("firstName", firstName)
            .setParameter("lastName", lastName)
            .list()
        val singer = Singer()
        for (item in results) {
            if (singer.firstName == null && singer.lastName == null) {
                singer.firstName = item["FIRST_NAME"] as String
                singer.lastName = item["LAST_NAME"] as String
            }
            val album = Album()
            album.title = item["TITLE"] as String
            album.releaseDate = (item["RELEASE_DATE"] as Date).toLocalDate()
            singer.addAlbum(album)
            val instrument = Instrument()
            instrument.instrumentId = item["INSTRUMENT_ID"] as String
            singer.instruments.add(instrument)
        }
        return singer
    }

    @Transactional(readOnly = true)
    override fun findAllWithAlbum(): List<Singer> {
        return sessionFactory.currentSession.createNamedQuery(
            "Singer.findAllWithAlbum",
            Singer::class.java
        ).list() // this uses the NamedQuery
        //return sessionFactory.getCurrentSession().createQuery("from Singer s").list(); // this causes the LazyInitialization exception
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): Singer? {
        return sessionFactory.currentSession.createNamedQuery(
            "Singer.findById",
            Singer::class.java
        ).setParameter("id", id).uniqueResult()
    }

    @Transactional
    override fun save(singer: Singer) {
        val session = sessionFactory.currentSession
        if (singer.id == null) {
            session.persist(singer)
        } else {
            session.merge(singer)
        }
        LOGGER.info("Singer saved with id: " + singer.id)
    }

    @Transactional
    override fun delete(singer: Singer) {
        sessionFactory.currentSession.remove(singer)
        LOGGER.info("Singer deleted with id: " + singer.id)
    }

    @Transactional(readOnly = true)
    override fun findAllNamesByProjection(): Set<String> {
        val projResult = sessionFactory.currentSession
            .createQuery("select s.firstName as fn, s.lastName as ln from Singer s", Tuple::class.java)
            .resultList
        return projResult.stream().map { tuple: Tuple ->
            tuple.get(
                "fn",
                String::class.java
            ) + " " + tuple.get("ln", String::class.java)
        }.collect(Collectors.toSet())
    }

    @Transactional(readOnly = true)
    override fun findFirstNameById(id: Long): String? {
        val firstNameResult = AtomicReference<String>()
        sessionFactory.currentSession.doWork { connection: Connection ->
            connection.prepareCall(
                "{ ? = call getfirstnamebyid(?) }"
            ).use { function ->
                function.registerOutParameter(1, Types.VARCHAR)
                function.setLong(2, id)
                function.execute()
                firstNameResult.set(function.getString(1))
            }
        }
        return firstNameResult.get()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SingerDaoImpl::class.java)
        private const val ALL_SELECT: String = """
            select distinct s.FIRST_NAME, s.LAST_NAME, a.TITLE, a.RELEASE_DATE, i.INSTRUMENT_ID
            from SINGER s
            inner join ALBUM a on s.id = a.SINGER_ID
            inner join SINGER_INSTRUMENT si on s.ID = si.SINGER_ID
            inner join INSTRUMENT i on si.INSTRUMENT_ID = i.INSTRUMENT_ID
            where s.FIRST_NAME = :firstName and s.LAST_NAME= :lastName
            """
    }
}