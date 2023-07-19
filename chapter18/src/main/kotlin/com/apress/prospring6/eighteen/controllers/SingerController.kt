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
package com.apress.prospring6.eighteen.controllers

import com.apress.prospring6.eighteen.entities.Singer
import com.apress.prospring6.eighteen.services.SingerService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * Created by iuliana on 22/01/2023
 */
@RestController
@RequestMapping(path = ["singer"])
class SingerController(private val singerService: SingerService) {
    @GetMapping(path = ["/", ""])
    fun all(): List<Singer> {
        return singerService.findAll()
    }

    @GetMapping(path = ["/{id}"])
    fun findSingerById(@PathVariable id: Long): Singer {
        return singerService.findById(id)!!
    }

    @PostMapping(path = ["/"])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid singer: Singer): Singer {
        LOGGER.info("Creating singer: $singer")
        return singerService.save(singer)
    }

    @PutMapping(value = ["/{id}"])
    fun update(@RequestBody @Valid singer: Singer, @PathVariable id: Long) {
        LOGGER.info("Updating singer: $singer")
        singerService.update(id, singer)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable id: Long) {
        Companion.LOGGER.info("Deleting singer with id: {}", id)
        singerService.delete(id)
    }

    companion object {
        val LOGGER = LoggerFactory.getLogger(SingerController::class.java)
    }
}
