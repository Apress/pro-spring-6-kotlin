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
package com.apress.prospring6.twenty.boot.service

import com.apress.prospring6.twenty.boot.model.Singer
import com.apress.prospring6.twenty.boot.problem.SaveException
import com.apress.prospring6.twenty.boot.repo.SingerRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.function.Function

/**
 * Created by iuliana on 03/04/2023
 */
@Transactional
@Service
open class SingerServiceImpl : SingerService {
    private val singerRepo: SingerRepo? = null

    override fun findAll(): Flux<Singer> {
        return singerRepo!!.findAll()
    }

    override fun findByCriteriaDto(criteria: SingerService.CriteriaDto): Flux<Singer> {
        val fieldName = SingerService.FieldGroup.getField(criteria.fieldName!!.uppercase())
        return when (fieldName) {
            SingerService.FieldGroup.FIRSTNAME -> if ("*" == criteria.fieldValue) singerRepo!!.findAll() else singerRepo!!.findByFirstName(
                criteria.fieldValue!!
            )

            SingerService.FieldGroup.LASTNAME -> if ("*" == criteria.fieldValue) singerRepo!!.findAll() else singerRepo!!.findByLastName(
                criteria.fieldValue!!
            )

            SingerService.FieldGroup.BIRTHDATE -> if ("*" == criteria.fieldValue) singerRepo!!.findAll() else singerRepo!!.findByBirthDate(
                LocalDate.parse(criteria.fieldValue!!, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            )
        }
    }

    override fun findByFirstNameAndLastName(firstName: String, lastName: String): Mono<Singer> {
        return singerRepo!!.findByFirstNameAndLastName(firstName, lastName)
    }

    override fun findById(id: Long): Mono<Singer> {
        return singerRepo!!.findById(id)
    }

    override fun findByFirstName(firstName: String): Flux<Singer> {
        return singerRepo!!.findByFirstName(firstName)
    }

    override fun save(singer: Singer): Mono<Singer> {
        return singerRepo!!.save(singer)
            .onErrorMap { error: Throwable ->
                SaveException(
                    "Could Not Save Singer $singer",
                    error
                )
            }
    }

    override fun update(id: Long, updateData: Singer): Mono<Singer> {
        return singerRepo!!.findById(id)
            .flatMap<Singer>{ original: Singer ->
                original.firstName = updateData.firstName
                original.lastName = updateData.lastName
                original.birthDate = updateData.birthDate
                singerRepo.save(original)
                    .onErrorMap { error: Throwable? ->
                        SaveException(
                            "Could Not Update Singer $updateData",
                            error
                        )
                    }
            }
    }

    override fun delete(id: Long): Mono<Void> {
        return singerRepo!!.deleteById(id)
    }
}
