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
package com.apress.prospring6.fifteen.controllers

import com.apress.prospring6.fifteen.entities.Singer
import com.apress.prospring6.fifteen.repos.SingerRepo
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Created by iuliana on 22/01/2023
 */
@RestController
@RequestMapping(path = ["singer2"])
class Singer2Controller(private val singerRepo: SingerRepo) {
    @GetMapping(path = ["/", ""])
    fun all(): ResponseEntity<List<Singer>> {
        val singers = singerRepo.findAll()
        return if (singers.isEmpty()) {
            ResponseEntity.notFound().build()
        } else ResponseEntity.ok().body(singers)
    }

    @GetMapping(path = ["/{id}"])
    fun findSingerById(@PathVariable id: Long): ResponseEntity<Singer> {
        val fromDb: Optional<Singer> = singerRepo.findById(id)
        return fromDb
            .map { s: Singer ->
                ResponseEntity.ok().body(s)
            }
            .orElseGet {
                ResponseEntity.notFound()
                    .build()
            }
    }

    @PostMapping(path = ["/"])
    fun create(@RequestBody @Valid singer: Singer): ResponseEntity<Singer> {
        LOGGER.info("Creating singer: {}", singer)
        return try {
            val saved = singerRepo.save(singer)
            LOGGER.info("Singer created successfully with info: {}", saved)
            ResponseEntity<Singer>(saved, HttpStatus.CREATED)
        } catch (dive: DataIntegrityViolationException) {
            LOGGER.debug("Could not create singer.", dive)
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping(value = ["/{id}"])
    fun update(@RequestBody @Valid singer: Singer, @PathVariable id: Long): ResponseEntity<Any> {
        LOGGER.info("Updating singer: {}", singer)
        val fromDb: Optional<Singer> = singerRepo.findById(id)
        return fromDb
            .map<ResponseEntity<Any>> { s: Singer ->
                s.firstName = singer.firstName
                s.lastName = singer.lastName
                s.birthDate = singer.birthDate
                try {
                    singerRepo.save(s)
                    return@map ResponseEntity.ok().build<Any>()
                } catch (dive: DataIntegrityViolationException) {
                    Companion.LOGGER.debug("Could not update singer.", dive)
                    return@map ResponseEntity.badRequest().build<Any>()
                }
            }
            .orElseGet {
                ResponseEntity.notFound().build()
            }
    }

    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        LOGGER.info("Deleting singer with id: {}", id)
        val fromDb: Optional<Singer> = singerRepo.findById(id)
        return fromDb
            .map<ResponseEntity<Any>> { s: Singer? ->
                singerRepo.deleteById(id)
                ResponseEntity.noContent().build()
            }
            .orElseGet {
                ResponseEntity.notFound().build()
            }
    }

    companion object {
        val LOGGER = LoggerFactory.getLogger(Singer2Controller::class.java)
    }
}
