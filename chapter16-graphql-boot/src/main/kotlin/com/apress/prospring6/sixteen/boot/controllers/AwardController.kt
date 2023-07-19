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
import com.apress.prospring6.sixteen.boot.entities.Singer
import com.apress.prospring6.sixteen.boot.repos.AwardRepo
import com.apress.prospring6.sixteen.boot.repos.SingerRepo
import graphql.schema.DataFetchingEnvironment
import jakarta.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.function.Supplier

/**
 * Created by iuliana on 13/02/2023
 */
@Controller
class AwardController(private val awardRepo: AwardRepo, private val singerRepo: SingerRepo) {
    @QueryMapping
    fun awardsBySinger(@Argument singerId: Long): Iterable<Award> {
        return awardRepo.findAwardsBySinger(singerId)
    }

    @QueryMapping
    fun awards(environment: DataFetchingEnvironment): Iterable<Award> {
        val s = environment.selectionSet
        return if (s.contains("singer")) awardRepo.findAll(fetchSinger()) else awardRepo.findAll()
    }

    @QueryMapping
    fun awardsCount(): Long {
        return awardRepo.count()
    }

    @MutationMapping
    fun newAward(@Argument award: AwardInput): Award {
        val singer: Singer = singerRepo.findById(award.singerId)
            .orElseThrow<IllegalArgumentException>(Supplier<IllegalArgumentException> {
                IllegalArgumentException(
                    "singer with id " + 1 + " does not exists!"
                )
            })
        val newAward = Award()
        newAward.year = award.year
        newAward.category= award.category
        newAward.itemName = award.itemName
        newAward.awardName = award.awardName
        newAward.singer = singer
        return awardRepo.save(newAward)
    }

    //@JvmRecord
    data class AwardInput(
        val year: Int,
        val category: String,
        val itemName: String,
        val awardName: String,
        val singerId: Long
    )

    private fun fetchSinger(): Specification<Award?> {
        return Specification { root: Root<Award?>, query: CriteriaQuery<*>?, builder: CriteriaBuilder? ->
            val f =
                root.fetch<Award, Singer>(
                    "singer",
                    JoinType.LEFT
                )
            val join =
                f as Join<Award, Singer>
            join.on
        }
    }
}
