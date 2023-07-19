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
import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

/**
 * Created by iuliana on 03/04/2023
 */
interface SingerService {
    fun findAll(): Flux<Singer>
    fun findById(id: Long): Mono<Singer>
    fun findByFirstNameAndLastName(firstName: String, lastName: String): Mono<Singer>
    fun findByFirstName(firstName: String): Flux<Singer>
    fun findByCriteriaDto(criteria: CriteriaDto): Flux<Singer>
    fun save(singer: Singer): Mono<Singer>
    fun update(id: Long, updateData: Singer): Mono<Singer>
    fun delete(id: Long): Mono<Void>

    class CriteriaDto {
        var fieldName: String? = null
        var fieldValue: String? = null
    }

    class CriteriaValidator : Validator {
        override fun supports(clazz: Class<*>): Boolean {
            return CriteriaDto::class.java.isAssignableFrom(clazz)
        }

        override fun validate(target: Any, errors: Errors) {
            ValidationUtils.rejectIfEmpty(
                errors,
                "fieldName",
                "required",
                arrayOf<Any>("fieldName"),
                "Field Name is required!"
            )
            ValidationUtils.rejectIfEmpty(
                errors,
                "fieldValue",
                "required",
                arrayOf<Any>("fieldValue"),
                "Field Value is required!"
            )
        }
    }

    enum class FieldGroup {
        FIRSTNAME,
        LASTNAME,
        BIRTHDATE;

        companion object {
            fun getField(field: String): FieldGroup {
                return valueOf(field.uppercase(Locale.getDefault()))
            }
        }
    }
}
