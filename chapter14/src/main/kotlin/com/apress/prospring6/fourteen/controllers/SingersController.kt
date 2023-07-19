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
package com.apress.prospring6.fourteen.controllers

import com.apress.prospring6.fourteen.controllers.OneSingerController.Companion.setPhoto
import com.apress.prospring6.fourteen.entities.Singer
import com.apress.prospring6.fourteen.problem.InvalidCriteriaException
import com.apress.prospring6.fourteen.services.SingerService
import com.apress.prospring6.fourteen.util.CriteriaDto
import com.apress.prospring6.fourteen.util.SingerForm
import com.apress.prospring6.fourteen.util.UrlUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.context.MessageSource
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.io.IOException
import java.util.*

/**
 * Created by iuliana on 27/12/2022
 */
@Controller
@RequestMapping("/singers")
class SingersController(private val singerService: SingerService, private val messageSource: MessageSource) {
    @GetMapping //@RequestMapping(method = RequestMethod.GET)
    fun list(uiModel: Model): String {
        var singers: List<Singer> = singerService.findAll().toMutableList()
        singers = singers.sortedBy ( Singer::id )
        uiModel.addAttribute("singers", singers)
        return "singers/list"
    }

    // --------------- create  -------------------
    @GetMapping(value = ["/create"])
    fun showCreateForm(uiModel: Model): String {
        uiModel.addAttribute("singerForm", SingerForm())
        return "singers/create"
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Throws(
        IOException::class
    )
    fun create(
        singerForm: @Valid SingerForm, bindingResult: BindingResult, uiModel: Model,
        httpServletRequest: HttpServletRequest,
        locale: Locale
    ): String {
        return if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", messageSource.getMessage("singer.save.fail", arrayOf(), locale))
            uiModel.addAttribute("singerForm", singerForm)
            "singers/create"
        } else {
            uiModel.asMap().clear()
            val s = Singer().apply {
                firstName = singerForm.firstName
                lastName = singerForm.lastName
                birthDate = singerForm.birthDate
            }
            // Process file  upload
            if (!singerForm.file!!.isEmpty) {
                setPhoto(s, singerForm.file!!)
            }
            val created = singerService.save(s)
            "redirect:/singer/" + UrlUtil.encodeUrlPathSegment(
                created.id.toString(),
                httpServletRequest
            )
        }
    }

    // --------------- search  -------------------
    @GetMapping(value = ["/search"])
    fun showSearchform(criteria: CriteriaDto?): String {
        return "singers/search"
    }

    @GetMapping(value = ["/go"])
    fun processSubmit(
        @ModelAttribute("criteriaDto") @Valid criteria: CriteriaDto,
        result: BindingResult, model: Model, locale: Locale
    ): String {
        return if (result.hasErrors()) {
            "singers/search"
        } else try {
            val singers: List<Singer> = singerService.getByCriteriaDto(criteria)
            if (singers.isEmpty()) {
                result.addError(
                    FieldError(
                        "criteriaDto",
                        "noResults",
                        messageSource.getMessage("NotEmpty.criteriaDto.noResults", null, locale)
                    )
                )
                "singers/search"
            } else if (singers.size == 1) {
                "redirect:/singer/" + singers[0].id
            } else {
                model.addAttribute("singers", singers)
                "singers/list"
            }
        } catch (ice: InvalidCriteriaException) {
            result.addError(
                FieldError(
                    "criteriaDto", ice.fieldName,
                    messageSource.getMessage(ice.messageKey, null, locale)
                )
            )
            "singers/search"
        }
    }
}
