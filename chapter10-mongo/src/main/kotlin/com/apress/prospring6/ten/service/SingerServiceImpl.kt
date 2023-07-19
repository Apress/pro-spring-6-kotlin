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

import com.apress.prospring6.ten.document.Singer
import com.apress.prospring6.ten.repos.SingerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Stream
import java.util.stream.StreamSupport

@Transactional
@Service
open class SingerServiceImpl(private val singerRepository: SingerRepository) : SingerService {
    override fun findAll(): Stream<Singer> {
        return StreamSupport.stream<Singer>(singerRepository.findAll().spliterator(),
            false)
    }

    override fun findByFirstName(firstName: String): Stream<Singer> {
        return StreamSupport.stream(singerRepository.findByFirstName(firstName).spliterator(), false)
    }

    override fun saveAll(singers: Iterable<Singer>) {
        singerRepository.saveAll(singers)
    }

    override fun findByFullName(positional: Boolean, firstName: String, lastName: String): Singer {
        val singers = if (positional) singerRepository.findByPositionedParams(
            firstName,
            lastName
        ) else singerRepository.findByNamedParams(firstName, lastName)
        return singers.iterator().next()
    }
}