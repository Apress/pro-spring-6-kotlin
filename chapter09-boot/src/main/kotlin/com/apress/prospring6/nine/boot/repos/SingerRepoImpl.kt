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
package com.apress.prospring6.nine.boot.repos

import com.apress.prospring6.nine.boot.entities.Singer
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*
import java.util.stream.Stream

/**
 * Created by iuliana.cosmina on 17/07/2022
 */
@Repository
open class SingerRepoImpl : SingerRepo {
    @PersistenceContext
    private val em: EntityManager? = null

    override fun findAll(): Stream<Singer> {
        return em!!.createNamedQuery(
            Singer.FIND_ALL,
            Singer::class.java
        ).resultList.stream()
    }

    override fun findById(id: Long): Singer? {
        return em!!.find(
            Singer::class.java, id
        )
    }

    override fun findByFirstNameAndLastName(fn: String?, ln: String?): Singer? {
        return em!!.createNamedQuery(
            Singer.FIND_BY_FIRST_AND_LAST_NAME,
            Singer::class.java
        )
            .setParameter("fn", fn).setParameter("ln", ln).singleResult
    }

    override fun countAllSingers(): Long {
        return em!!.createNamedQuery(Singer.COUNT_ALL, Long::class.java).singleResult
    }

    override fun save(singer: Singer): Singer {
        return if (singer.id == null) {
            em!!.persist(singer)
            singer
        } else {
            em!!.merge(singer)
        }
    }
}