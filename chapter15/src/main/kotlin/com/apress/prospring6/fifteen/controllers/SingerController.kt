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
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["singer"])
class SingerController(private val singerRepo: SingerRepo) {
    @ResponseStatus(HttpStatus.OK) // @GetMapping(path={"/", ""})
    @RequestMapping(path = ["/", ""], method = [RequestMethod.GET])
    fun all(): List<Singer> {
        return singerRepo.findAll()
    }

    @ResponseStatus(HttpStatus.OK) //@GetMapping(path = "/{id}")
    @RequestMapping(path = ["/{id}"], method = [RequestMethod.GET])
    fun findSingerById(@PathVariable id: Long): Singer? {
        return singerRepo.findById(id).orElse(null)
    }

    @ResponseStatus(HttpStatus.CREATED) //@PostMapping(path="/")
    @RequestMapping(path = ["/"], method = [RequestMethod.POST])
    fun create(@RequestBody singer: Singer): Singer {
        LOGGER.info("Creating singer: {}", singer)
        val saved = singerRepo.save(singer)
        LOGGER.info("Singer created successfully with info: $saved")
        return singer
    }

    @ResponseStatus(HttpStatus.OK) //@PutMapping(value="/{id}")
    @RequestMapping(path = ["/{id}"], method = [RequestMethod.PUT])
    fun update(
        @RequestBody singer: Singer,
        @PathVariable id: Long
    ) { // if we enable validation we cannot provide a Singer with missing fields
        LOGGER.info("Updating singer: {}", singer)
        val fromDb = singerRepo.findById(id).orElseThrow { IllegalArgumentException("Singer does not exist!") }
        fromDb.firstName = singer.firstName
        fromDb.lastName= singer.lastName
        fromDb.birthDate = singer.birthDate
        singerRepo.save(fromDb)
        LOGGER.info("Singer updated successfully with info: $fromDb")
    }

    @ResponseStatus(HttpStatus.NO_CONTENT) //@DeleteMapping(value="/{id}")
    @RequestMapping(path = ["/{id}"], method = [RequestMethod.DELETE])
    fun delete(@PathVariable id: Long) {
        LOGGER.info("Deleting singer with id: {}", id)
        singerRepo.deleteById(id)
        LOGGER.info("Singer deleted successfully")
    }

    companion object {
        val LOGGER = LoggerFactory.getLogger(SingerController::class.java)
    }
}
