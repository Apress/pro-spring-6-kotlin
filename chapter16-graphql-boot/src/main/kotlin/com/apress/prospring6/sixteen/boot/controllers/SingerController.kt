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
package com.apress.prospring6.sixteen.boot.controllers

import com.apress.prospring6.sixteen.boot.entities.Award
import com.apress.prospring6.sixteen.boot.entities.Instrument
import com.apress.prospring6.sixteen.boot.entities.Singer
import com.apress.prospring6.sixteen.boot.problem.NotFoundException
import com.apress.prospring6.sixteen.boot.repos.SingerRepo
import graphql.schema.DataFetchingEnvironment
import jakarta.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.function.Supplier

/**
 * Created by iuliana on 07/02/2023
 */
@Controller
class SingerController(private val singerRepo: SingerRepo) {
    @QueryMapping
    fun singerById(@Argument id: Long, environment: DataFetchingEnvironment): Singer {
        var spec = byId(id)
        val s = environment.selectionSet
        if (s.contains("awards") && !s.contains("instruments")) spec =
            spec.and(fetchAwards()) else if (s.contains("awards") && s.contains("instruments")) spec =
            spec.and(fetchAwards().and(fetchInstruments())) else if (!s.contains("awards") && s.contains("instruments")) spec =
            spec.and(fetchInstruments())
        return singerRepo.findOne(spec).orElse(null)
    }

    /*
    @QueryMapping
    fun  singers():Iterable<Singer>{
        return singerRepo.findAll()
    }
    */
    @QueryMapping
    fun singers(environment: DataFetchingEnvironment): Iterable<Singer> {
        val s = environment.selectionSet
        return if (s.contains("awards") && !s.contains("instruments"))
            singerRepo.findAll(fetchAwards())
        else if (s.contains("awards") && s.contains("instruments"))
            singerRepo.findAll(fetchAwards().and(fetchInstruments()))
        else if (!s.contains("awards") && s.contains("instruments"))
            singerRepo.findAll(fetchInstruments())
        else singerRepo.findAll()
    }

    @QueryMapping
    fun singersCount(): Long {
        return singerRepo.count()
    }

    @MutationMapping
    fun newSinger(@Argument singer: SingerInput): Singer {
        val date: LocalDate = try {
            LocalDate.parse(singer.birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Bade date format")
        }
        val newSinger = Singer(
            null, 0, singer.firstName, singer.lastName,
            singer.pseudonym, singer.genre, date, null, null
        )
        return singerRepo.save<Singer>(newSinger)
    }

    @MutationMapping
    fun updateSinger(@Argument id: Long, @Argument singer: SingerInput): Singer {
        val fromDb: Singer = singerRepo.findById(id).orElseThrow{
            NotFoundException(
                Singer::class.java, id
            )
        }
        fromDb.firstName=singer.firstName
        fromDb.lastName=singer.lastName
        fromDb.pseudonym=singer.pseudonym
        fromDb.genre=singer.genre
        val date: LocalDate
        try {
            date = LocalDate.parse(singer.birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            fromDb.birthDate=date
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Bade date format")
        }
        return singerRepo.save(fromDb)
    }

    @MutationMapping
    fun deleteSinger(@Argument id: Long): Long {
        singerRepo.findById(id).orElseThrow{
            NotFoundException(
                Singer::class.java, id
            )
        }
        singerRepo.deleteById(id)
        return id
    }

    data class SingerInput(
        val firstName: String,
        val lastName: String,
        val pseudonym: String,
        val genre: String,
        val birthDate: String
    )

    private fun byId(id: Long): Specification<Singer?> {
        return Specification { root: Root<Singer?>, query: CriteriaQuery<*>?, builder: CriteriaBuilder ->
            builder.equal(
                root.get<Any>("id"),
                id
            )
        }
    }

    private fun fetchAwards(): Specification<Singer?> {
        return Specification { root: Root<Singer?>, query: CriteriaQuery<*>?, builder: CriteriaBuilder? ->
            val f =
                root.fetch<Singer, Award>(
                    "awards",
                    JoinType.LEFT
                )
            val join =
                f as Join<Singer, Award>
            join.on
        }
    }

    private fun fetchInstruments(): Specification<Singer?> {
        return Specification { root: Root<Singer?>, query: CriteriaQuery<*>?, builder: CriteriaBuilder? ->
            val f =
                root.fetch<Singer, Instrument>(
                    "instruments",
                    JoinType.LEFT
                )
            val join =
                f as Join<Singer, Instrument>
            join.on
        }
    }
}
