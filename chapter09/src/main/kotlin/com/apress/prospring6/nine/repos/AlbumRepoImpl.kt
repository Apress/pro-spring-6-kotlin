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
package com.apress.prospring6.nine.repos

import com.apress.prospring6.nine.entities.Album
import com.apress.prospring6.nine.entities.Singer
import com.apress.prospring6.nine.ex.TitleTooLongException
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.util.stream.Stream

/**
 * Created by iuliana.cosmina on 17/07/2022
 */
@Repository
class AlbumRepoImpl : AlbumRepo {
    @PersistenceContext
    private val em: EntityManager? = null

    @Value("#{jpaProperties.get('hibernate.jdbc.batch_size')}")
    private val batchSize = 0

    override fun findBySinger(singer: Singer): Stream<Album> {
        return em!!.createNamedQuery(
            Album.FIND_ALL,
            Album::class.java
        )
            .setParameter("singer", singer)
            .resultList.stream()
    }

    @Throws(TitleTooLongException::class)
    override fun save(albums: Set<Album>): Set<Album> {
        val savedAlbums: MutableSet<Album> = mutableSetOf()
        var i = 0
        for (a in albums) {
            savedAlbums.add(save(a))
            i++
            if (i % batchSize == 0) {
                // Flush a batch of inserts and release memory.
                em!!.flush()
                em.clear()
            }
        }
        return savedAlbums
    }

    @Throws(TitleTooLongException::class)
    override fun save(album: Album): Album {
        if (50 < album.title!!.length) {
            throw TitleTooLongException("Title " + album.title + " too long!")
        }
        return if (album.id == null) {
            em!!.persist(album)
            album
        } else {
            em!!.merge(album)
        }
    }
}