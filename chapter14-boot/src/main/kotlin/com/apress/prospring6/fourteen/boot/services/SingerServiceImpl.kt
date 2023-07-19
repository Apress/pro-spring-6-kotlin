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
package com.apress.prospring6.fourteen.boot.services

import com.apress.prospring6.fourteen.boot.entities.Singer
import com.apress.prospring6.fourteen.boot.problem.InvalidCriteriaException
import com.apress.prospring6.fourteen.boot.problem.NotFoundException
import com.apress.prospring6.fourteen.boot.repos.SingerRepo
import com.apress.prospring6.fourteen.boot.util.CriteriaDto
import com.apress.prospring6.fourteen.boot.util.FieldGroup
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.stream.Collectors
import java.util.stream.StreamSupport

/**
 * Created by iuliana on 25/12/2022
 */
@Transactional
@Service("singerService")
open class SingerServiceImpl(private val singerRepo: SingerRepo) : SingerService {
    @Transactional(readOnly = true)
    override fun findAll(): List<Singer> {
        return singerRepo.findAll(Sort.unsorted())
    }

    override fun delete(id: Long) {
        singerRepo.deleteById(id)
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

    override fun findAllByPage(pageable: Pageable): Page<Singer> {
        return singerRepo.findAll(pageable)
    }

    @Throws(InvalidCriteriaException::class)
    override fun getByCriteriaDto(criteria: CriteriaDto): List<Singer> {
        val fg = FieldGroup.getField(criteria.fieldName!!)
        val result: Iterable<Singer> = when (fg) {
            FieldGroup.FIRSTNAME -> (if (criteria.exactMatch!!) singerRepo.findByFirstName(criteria.fieldValue!!) else singerRepo.findByFirstNameLike(
                criteria.fieldValue!!
            ))

            FieldGroup.LASTNAME -> (if (criteria.exactMatch!!) singerRepo.findByLastName(criteria.fieldValue!!) else singerRepo.findByLastNameLike(
                criteria.fieldValue!!
            ))

            FieldGroup.BIRTHDATE -> {
                val date: LocalDate = try {
                    LocalDate.parse(criteria.fieldValue!!, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                } catch (e: DateTimeParseException) {
                    throw InvalidCriteriaException("fieldValue", "typeMismatch.hiringDate")
                }
                singerRepo.findByBirthDate(date)
            }
        }
        return StreamSupport.stream(result.spliterator(), false).collect(Collectors.toList())
    }
}
