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
package com.apress.prospring6.ten.service

import com.apress.prospring6.ten.entities.Album
import com.apress.prospring6.ten.entities.Singer
import com.apress.prospring6.ten.repos.AlbumRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.stream.Stream
import java.util.stream.StreamSupport

/**
 * Created by iuliana.cosmina on 02/08/2022
 */
@Service
@Transactional(readOnly = true)
class AlbumServiceImpl(private val albumRepository: AlbumRepository) :
    AlbumService {
    override fun findBySinger(singer: Singer): Stream<Album> {
        return StreamSupport.stream(albumRepository.findBySinger(singer).spliterator(),
            false)
    }

    override fun findWithReleaseDateGreaterThan(rd: LocalDate): Stream<Album> {
        return StreamSupport.stream(
            albumRepository.findWithReleaseDateGreaterThan(rd).spliterator(),
            false
        )
    }

    override fun findByTitle(title: String): Stream<Album> {
        return StreamSupport.stream(albumRepository.findByTitle(title).spliterator(),
            false)
    }
}