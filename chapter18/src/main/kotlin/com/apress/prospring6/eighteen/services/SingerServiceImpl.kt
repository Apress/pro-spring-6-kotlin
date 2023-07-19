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
package com.apress.prospring6.eighteen.services

import com.apress.prospring6.eighteen.entities.Singer
import com.apress.prospring6.eighteen.problem.NotFoundException
import com.apress.prospring6.eighteen.repos.SingerRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by iuliana on 25/12/2022
 */
@Transactional
@Service("singerService")
class SingerServiceImpl(private val singerRepo: SingerRepo) : SingerService {
    override fun findAll(): List<Singer> {
        val singers = singerRepo.findAll()
        if (singers.isEmpty()) {
            throw NotFoundException(Singer::class.java)
        }
        return singers
    }

    override fun findByFirstNameAndLastName(firstName: String, lastName: String): List<Singer> {
        return singerRepo.findByFirstNameAndLastName(firstName, lastName) as List<Singer>
    }

    override fun findById(id: Long): Singer {
        return singerRepo.findById(id).orElseThrow {
            NotFoundException(
                Singer::class.java, id
            )
        }
    }

    override fun save(singer: Singer): Singer {
        return singerRepo.save(singer)
    }

    override fun update(id: Long, singer: Singer): Singer {
        return singerRepo.findById(id)
            .map<Singer>{ s: Singer ->
                s.firstName=singer.firstName
                s.lastName=singer.lastName
                s.birthDate=singer.birthDate
                singerRepo.save<Singer>(s)
            }
            .orElseThrow<NotFoundException> {
                NotFoundException(
                    Singer::class.java, id
                )
            }
    }

    override fun delete(id: Long) {
        val fromDb = singerRepo.findById(id)
        if (fromDb.isEmpty) {
            throw NotFoundException(
                Singer::class.java,
                id
            )
        }
        singerRepo.deleteById(id)
    }
}
