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
package com.apress.prospring6.eight.boot.service

import com.apress.prospring6.eight.boot.entities.Singer
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.PersistenceContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Stream

/**
 * Created by iuliana.cosmina on 02/07/2022
 */
@Service("jpaSingerService")
@Repository
@Transactional
open class SingerServiceImpl : SingerService {
    @PersistenceContext
    private val em: EntityManager? = null

    @Transactional(readOnly = true)
    override fun findAllWithAlbum(): Stream<Singer> {
        return em!!.createNamedQuery(
            Singer.FIND_ALL_WITH_ALBUM,
            Singer::class.java
        ).resultList.stream()
    }

    @Transactional(readOnly = true)
    override fun findAll(): Stream<Singer> {
        return em!!.createNamedQuery(
            Singer.FIND_ALL,
            Singer::class.java
        ).resultList.stream()
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): Singer? {
        val query = em!!.createNamedQuery(
            Singer.FIND_SINGER_BY_ID,
            Singer::class.java
        )
        query.setParameter("id", id)
        return try {
            query.singleResult
        } catch (nre: NoResultException) {
            null
        }
    }

    override fun save(singer: Singer) {
        if (singer.id == null) {
            LOGGER.info("Inserting new singer")
            em!!.persist(singer)
        } else {
            em!!.merge(singer)
            LOGGER.info("Updating existing singer")
        }
    }

    override fun delete(singer: Singer) {
        val mergedContact = em!!.merge(singer)
        em.remove(mergedContact)
        LOGGER.info("Singer with id: " + singer.id + " deleted successfully")
    }

    override fun findAllByNativeQuery(): Stream<Singer> {
        return em!!.createNativeQuery(SingerService.ALL_SINGER_NATIVE_QUERY,
            "singerResult").resultList.stream() as Stream<Singer>
    }

    override fun findFirstNameById(id: Long): String {
        return em!!.createNamedQuery("Singer.getFirstNameById(?)")
            .setParameter(1, id).singleResult.toString()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SingerServiceImpl::class.java)
    }
}