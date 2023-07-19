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
package com.apress.prospring6.thirteen

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

/**
 * Created by iuliana on 19/11/2022
 */
@RestController
class LetterSenderController(
    private val webClient: RestTemplate,
    @param:Value("#{senderApplication.correspondentAddress}") private val correspondentAddress: String,
    @param:Value("#{senderApplication.sender}") private val sender: String
) {
    @PostMapping(path = ["send"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun sendLetter(@RequestBody letter: Letter) {
        letter.sender = sender
        letter.sentOn = LocalDate.now()
        val request = HttpEntity(letter)
        webClient.exchange(
            "$correspondentAddress/letters", HttpMethod.POST, request,
            Letter::class.java
        )
    }

    @get:GetMapping(path = ["misc"], produces = [MediaType.APPLICATION_JSON_VALUE])
    val miscData: String
        get() {
            val response = webClient.getForObject(
                "https://jsonplaceholder.typicode.com/users",
                String::class.java
            )
            log.info("Random info from non-java application: {} ", response)
            return response!!
        }

    companion object {
        val log = LoggerFactory.getLogger(LetterSenderController::class.java)
    }

}
