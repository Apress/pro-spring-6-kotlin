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
package com.apress.prospring6.seventeen.boot.controllers

import com.apress.prospring6.seventeen.boot.entities.Singer
import com.apress.prospring6.seventeen.boot.services.SingerService
import jakarta.validation.Valid
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

/**
 * Created by iuliana on 26/12/2022
 */
@Controller
@RequestMapping("/singer/{id}")
open class OneSingerController(private val singerService: SingerService, private val messageSource: MessageSource) {
    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping
    open fun showSingerData(@PathVariable("id") id: Long, uiModel: Model): String {
        val singer: Singer = singerService.findById(id)!!
        uiModel.addAttribute("singer", singer)
        return "singers/show"
    }

    //@RequestMapping(path = "/edit", method = RequestMethod.GET)
    @GetMapping(path = ["/edit"])
    open fun showEditForm(@PathVariable("id") id: Long, uiModel: Model): String {
        val singer: Singer = singerService.findById(id)!!
        uiModel.addAttribute("singer", singer)
        return "singers/edit"
    }

    @GetMapping(path = ["/upload"])
    open fun showPhotoUploadForm(@PathVariable("id") id: Long, uiModel: Model): String {
        val singer: Singer = singerService.findById(id)!!
        uiModel.addAttribute("singer", singer)
        return "singers/upload"
    }

    @PutMapping
    open fun updateSingerInfo(
        @Valid singer: Singer,
        bindingResult: BindingResult,
        uiModel: Model,
        locale: Locale
    ): String {
        return if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", messageSource.getMessage("singer.save.fail", arrayOf(), locale))
            uiModel.addAttribute("singer", singer)
            "singers/edit"
        } else {
            uiModel.asMap().clear()
            val fromDb = singerService.findById(singer.id!!)!!
            with(fromDb){
                firstName=singer.firstName
                lastName=singer.lastName
                birthDate=singer.birthDate
            }
            singerService.save(fromDb)
            "redirect:/singer/" + singer.id
        }
    }

    @GetMapping("/photo") //@RequestMapping(value = "/photo", method = RequestMethod.GET)
    @ResponseBody
    open fun downloadPhoto(@PathVariable("id") id: Long): ByteArray? {
        val singer: Singer = singerService.findById(id)!!
        if (singer.photo != null) {
            LOGGER.info("Downloading photo for id: {} with size: {}", singer.id, singer.photo!!.size)
            return singer.photo
        }
        return null
    }

    @PostMapping(path = ["/photo"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Throws(
        IOException::class
    )
    open fun handleFileUpload(
        @RequestParam(value = "file", required = false) file: MultipartFile,
        @PathVariable("id") id: Long
    ): String {
        val fromDb = singerService.findById(id)!!

        // Process file  upload
        if (!file.isEmpty) {
            setPhoto(fromDb, file)
            singerService.save(fromDb)
        }
        return "redirect:/singer/$id"
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    open fun deleteSinger(@PathVariable("id") id: Long): String {
        singerService.findById(id)
        singerService.delete(id)
        return "redirect:/singers/list"
    }

    companion object {
        @Throws(IOException::class)
        fun setPhoto(singer: Singer, file: MultipartFile) {
            val inputStream = file.inputStream
            val fileContent = IOUtils.toByteArray(inputStream)
            singer.photo=fileContent
        }

        private val LOGGER = LoggerFactory.getLogger(OneSingerController::class.java)
    }
}
