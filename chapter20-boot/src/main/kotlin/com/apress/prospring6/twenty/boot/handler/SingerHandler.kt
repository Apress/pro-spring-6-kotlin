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
package com.apress.prospring6.twenty.boot.handler

import com.apress.prospring6.twenty.boot.model.Singer
import com.apress.prospring6.twenty.boot.problem.MissingValueException
import com.apress.prospring6.twenty.boot.service.SingerService
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.URI

/**
 * Created by iuliana on 02/04/2023
 */
@Component
class SingerHandler(private val singerService: SingerService) {
    /* 1 */
    var list: HandlerFunction<ServerResponse> = HandlerFunction<ServerResponse> { serverRequest: ServerRequest? ->
        ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(singerService.findAll(), Singer::class.java)
    }

    /* 2 */
    var deleteById: HandlerFunction<ServerResponse> = HandlerFunction<ServerResponse> { serverRequest: ServerRequest ->
        ServerResponse.noContent()
            .build(singerService.delete(serverRequest.pathVariable("id").toLong()))
    }

    /* 3 */
    fun findById(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id = serverRequest.pathVariable("id").toLong()
        return singerService.findById(id)
            .flatMap { singer ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON).bodyValue(singer)
            }
            .switchIfEmpty(ServerResponse.notFound().build())
    }

    /* 4 */
    fun create(serverRequest: ServerRequest): Mono<ServerResponse> {
        val singerMono = serverRequest.bodyToMono(
            Singer::class.java
        )
        return singerMono
            .flatMap<Any>(singerService::save)
            .log()
            .flatMap<ServerResponse> { s: Any ->
                ServerResponse.created(URI.create("/singer/" + (s as Singer).id))
                    .contentType(MediaType.APPLICATION_JSON).bodyValue(s)
            }
        //.switchIfEmpty(status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /* 5 */
    fun updateById(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id = serverRequest.pathVariable("id").toLong()
        return singerService.findById(id)
            .flatMap { fromDb ->
                serverRequest.bodyToMono<Singer>(Singer::class.java)
                    .flatMap { s: Singer? ->
                        ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(
                                singerService.update(id, s as Singer),
                                Singer::class.java
                            )
                    }
            } // we switch to 400 because this is an invalid put request
            .switchIfEmpty(ServerResponse.badRequest().bodyValue("Failure to update singer!"))
        // we can put anything in the ServerResponse including an exception
        //).switchIfEmpty(badRequest().bodyValue(new NotFoundException(String.class, id)));
    }

    /* 6 */
    fun searchSingers(serverRequest: ServerRequest): Mono<ServerResponse> {
        val name = serverRequest.queryParam("name").orElse(null)
        return if (name.isBlank()) {
            // parameter is an empty string
            //return badRequest().bodyValue(new IllegalArgumentException("Missing request parameter 'name'"));
            ServerResponse.badRequest().bodyValue("Missing request parameter 'name'")
        } else ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON).body(singerService.findByFirstName(name), Singer::class.java)
    }

    /* 7 */
    fun searchSinger(serverRequest: ServerRequest): Mono<ServerResponse> {
        val fn = serverRequest.queryParam("fn").orElse(null)
        val ln = serverRequest.queryParam("ln").orElse(null)
        return if (fn == null || ln == null || fn.isBlank() || ln.isBlank()) {
            // one of {fn, ln} (or both) parameter is an empty string
            //return badRequest().bodyValue(new IllegalArgumentException("Missing request parameter, one of {fn, ln}"));
            ServerResponse.badRequest().bodyValue("Missing request parameter, one of {fn, ln}")
        } else singerService.findByFirstNameAndLastName(fn, ln)
            .flatMap { singer ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON).bodyValue(singer)
            }
    }

    fun search(serverRequest: ServerRequest): Mono<ServerResponse> {
        val criteriaMono = serverRequest.bodyToMono(
            SingerService.CriteriaDto::class.java
        )
        return criteriaMono.log()
            .flatMap<SingerService.CriteriaDto?> { criteria: SingerService.CriteriaDto ->
                validate(
                    criteria
                )
            }
            .flatMap { criteria: SingerService.CriteriaDto ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        singerService.findByCriteriaDto(criteria),
                        Singer::class.java
                    )
            }
    }

    private fun validate(criteria: SingerService.CriteriaDto): Mono<SingerService.CriteriaDto?> {
        val validator = SingerService.CriteriaValidator()
        val errors = BeanPropertyBindingResult(criteria, "criteria")
        validator.validate(criteria, errors)
        if (errors.hasErrors()) {
            // throw new ServerWebInputException(errors.toString());
            throw MissingValueException.of(errors.allErrors)
        }
        return Mono.just(criteria)
    }

    fun searchView(request: ServerRequest?): Mono<ServerResponse> {
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .render("singers/search", SingerService.CriteriaDto())
    }
}
