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
package com.apress.prospring6.nine.services

import com.apress.prospring6.nine.entities.Album
import com.apress.prospring6.nine.entities.Singer
import com.apress.prospring6.nine.ex.TitleTooLongException
import com.apress.prospring6.nine.repos.AlbumRepo
import com.apress.prospring6.nine.repos.SingerRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Created by iuliana.cosmina on 19/07/2022
 */
@Service
@Transactional
class AllServiceImpl(private val singerRepo: SingerRepo, private val albumRepo: AlbumRepo) :
    AllService {
    @Transactional(readOnly = true)
    override fun findAllWithAlbums(): Stream<Singer> {
        val singers = singerRepo.findAll()
        return singers.peek { s: Singer? ->
            s!!.albums = albumRepo.findBySinger(s)
                .collect(Collectors.toSet())
        }
    }

    @Transactional(readOnly = true)
    override fun findByIdWithAlbums(id: Long): Singer? {
        val singerOpt = singerRepo.findById(id)
        singerOpt?.run {
            albums = albumRepo.findBySinger(this)
                .collect(Collectors.toSet())
        }
        return singerOpt
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun update(singer: Singer) {
        singerRepo.save(singer)
    }

    @Transactional(rollbackFor = [TitleTooLongException::class])
    @Throws(
        TitleTooLongException::class
    )
    override fun saveSingerWithAlbums(s: Singer, albums: Set<Album>) {
        val singer = singerRepo.save(s)
        albums.forEach{ a: Album ->
            a.singer = singer
        }
        albumRepo.save(albums)
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    override fun countSingers(): Long {
        return singerRepo.countAllSingers()
    }
}