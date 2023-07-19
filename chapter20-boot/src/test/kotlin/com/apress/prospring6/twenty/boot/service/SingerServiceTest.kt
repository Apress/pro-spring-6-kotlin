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

import com.apress.prospring6.twenty.boot.ReactiveDbConfigTests
import com.apress.prospring6.twenty.boot.model.Singer
import com.apress.prospring6.twenty.boot.problem.SaveException
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.test.StepVerifier.FirstStep
import java.time.LocalDate

/**
 * Created by iuliana on 16/04/2023
 */
@DataR2dbcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Import(
    SingerServiceImpl::class
)
class SingerServiceTest : ReactiveDbConfigTests() {
    @Autowired
    var singerService: SingerService? = null

    @Order(1)
    @Test
    fun serviceExists() {
        Assertions.assertNotNull(singerService)
    }

    @Order(2)
    @Test
    fun testFindAll() {
        singerService!!.findAll()
            .log()
            .`as`<FirstStep<Singer>> { publisher: Flux<Singer> ->
                StepVerifier.create(
                    publisher
                )
            }
            .expectNextCount(4)
            .verifyComplete()
    }

    @Order(3)
    @Test
    fun testFindById() {
        singerService!!.findById(1L)
            .log()
            .`as`<FirstStep<Singer>> { publisher: Mono<Singer> ->
                StepVerifier.create(
                    publisher
                )
            }
            .expectNextMatches { s: Singer -> "John" == s.firstName && "Mayer" == s.lastName }
            .verifyComplete()
    }

    @Order(4)
    @Test
    fun testFindByFirstNameAndLastName() {
        singerService!!.findByFirstNameAndLastName("John", "Mayer")
            .log()
            .`as`<FirstStep<Singer>> { publisher: Mono<Singer> ->
                StepVerifier.create(
                    publisher
                )
            }
            .expectNext(
                Singer().apply {
                    id=1L
                    firstName= "John"
                    lastName="Mayer"
                    birthDate = LocalDate.of(1977, 10, 16)
                }
            )
            .verifyComplete()
    }

    @Order(5)
    @Test
    fun testFindByFistNameAndLastNameNoResult() {
        singerService!!.findByFirstNameAndLastName("Gigi", "Pedala")
            .log()
            .`as`<FirstStep<Singer>> { publisher: Mono<Singer> ->
                StepVerifier.create(
                    publisher
                )
            }
            .expectNextCount(0)
            .verifyComplete()
    }

    @Order(6)
    @Test
    fun findByCriteriaDto() {
        val criteria = SingerService.CriteriaDto()
        criteria.fieldName = SingerService.FieldGroup.BIRTHDATE.name
        criteria.fieldValue = "1977-10-16"
        singerService!!.findByCriteriaDto(criteria)
            .log()
            .`as`<FirstStep<Singer>> { publisher: Flux<Singer> ->
                StepVerifier.create(
                    publisher
                )
            }
            .expectNext(
                Singer().apply {
                    id = 1L
                    firstName = "John"
                    lastName = "Mayer"
                    birthDate = LocalDate.of(1977, 10, 16)
                }
            )
            .verifyComplete()
    }

    @Order(7)
    @Test
    fun testCreateSinger() {
        singerService!!.save(
            Singer().apply {
                firstName = "Test"
                lastName = "Test"
                birthDate = LocalDate.now()
            }
        )
            .log()
            .`as`<FirstStep<Singer>> { publisher: Mono<Singer> ->
                StepVerifier.create(
                    publisher
                )
            }
            .assertNext { s: Singer ->
                Assertions.assertNotNull(
                    s.id
                )
            }
            .verifyComplete()
    }

    @Order(8)
    @Test
    fun testNoCreateSinger() {
        singerService!!.save(
            Singer().apply {
                firstName = "John"
                lastName = "Mayer"
                birthDate = LocalDate.now()
            }
        )
            .log()
            .`as`<FirstStep<Singer>> { publisher: Mono<Singer> ->
                StepVerifier.create(
                    publisher
                )
            }
            .verifyError(SaveException::class.java)
    }

    @Order(9)
    @Test
    fun testUpdateSinger() {
        singerService!!.update(
            4L,
                Singer().apply {
                    firstName = "Erik Patrick"
                    lastName = "Clapton"
                    birthDate = LocalDate.now()
                }
            )
            .log()
            .`as`<FirstStep<Singer>> { publisher: Mono<Singer> ->
                StepVerifier.create(
                    publisher
                )
            }
            .expectNext(
                Singer().apply {
                    id = 4L
                    firstName = "Erik Patrick"
                    lastName = "Clapton"
                    birthDate = LocalDate.now()
                }
            )
            .verifyComplete()
    }

    @Order(10)
    @Test
    fun testUpdateSingerWithDuplicateData() {
        singerService!!.update(
            4L,
            Singer().apply {
                firstName = "John"
                lastName = "Mayer"
                birthDate = LocalDate.now()
            }
        )
            .log()
            .`as`<FirstStep<Singer>> { publisher: Mono<Singer> ->
                StepVerifier.create(
                    publisher
                )
            }
            .verifyError(SaveException::class.java)
    }

    @Order(11)
    @Test // negative test, lastName is null which is not allowed
    fun testFailedCreateSinger() {
        singerService!!.update(
            4L,
            Singer().apply {
                firstName = "Test"
                birthDate = LocalDate.now()
            }
        )
            .log()
            .`as`<FirstStep<Singer>> { publisher: Mono<Singer> ->
                StepVerifier.create(
                    publisher
                )
            }
            .verifyError(SaveException::class.java)
    }

    @Order(12)
    @Test
    fun testDeleteSinger() {
        singerService!!.delete(4L)
            .log()
            .`as`<FirstStep<Void>> { publisher: Mono<Void> ->
                StepVerifier.create(
                    publisher
                )
            }
            .expectNextCount(0)
            .verifyComplete()
    }
}
