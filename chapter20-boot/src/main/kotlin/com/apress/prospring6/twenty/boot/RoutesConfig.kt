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
package com.apress.prospring6.twenty.boot;

import com.apress.prospring6.twenty.boot.handler.HomeHandler;
import com.apress.prospring6.twenty.boot.handler.SingerHandler;
import com.apress.prospring6.twenty.boot.problem.MissingValueException;
import com.apress.prospring6.twenty.boot.problem.SaveException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.AbstractEnvironment
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import org.springframework.web.reactive.function.server.RouterFunctions.resources;
import org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Created by iuliana on 03/04/2023
 */
@Configuration
open class RoutesConfig {
    @Bean
    open fun staticRouter():RouterFunction<ServerResponse> {
        return resources("/images/**", ClassPathResource("static/images/"))
                .and(resources("/styles/**", ClassPathResource("static/styles/")))
                .and(resources("/js/**", ClassPathResource("static/js/")));
    }

    @Bean
    open fun singerRoutes( homeHandler:HomeHandler,  singerHandler:SingerHandler):RouterFunction<ServerResponse> {
        return route()
                // returns home view template
                .GET("/", homeHandler::view)
                .GET("/home", homeHandler::view)
                .GET("/singers/search", singerHandler::searchView)
                .POST("/singers/go", singerHandler::search)
                // these need to be here, otherwise parameters will not be considered
                .GET("/handler/singer", queryParam("name", {_ -> true}), singerHandler::searchSingers)
                .GET("/handler/singer", RequestPredicates.all()
                        .and(queryParam("fn", {_ -> true}))
                        .and(queryParam("ln", {_ -> true})), singerHandler::searchSinger)
                // requests with parameters always come first
                .GET("/handler/singer", singerHandler.list)
                .POST("/handler/singer", singerHandler::create)
                .GET("/handler/singer/{id}", singerHandler::findById)
                .PUT("/handler/singer/{id}", singerHandler::updateById)
                .DELETE("/handler/singer/{id}", singerHandler.deleteById)
                .filter { request, next ->
                    LOGGER.info("Before handler invocation: {}", request.path())
                    return@filter next.handle(request)
                }
            .build()
    }

    @Bean
    @Order(-2)
    open fun  exceptionHandler():WebExceptionHandler {
        return object : WebExceptionHandler {
            override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
                when(ex) {
                    is SaveException -> {
                        LOGGER.debug("RouterConfig:: handling exception :: ", ex)
                        exchange.response.setStatusCode(HttpStatus.BAD_REQUEST)

                        // marks the response as complete and forbids writing to it
                        return exchange.response.setComplete()
                    }
                    is IllegalArgumentException -> {
                        LOGGER.debug("RouterConfig:: handling exception :: " , ex)
                        exchange.response.setStatusCode(HttpStatus.BAD_REQUEST)

                        // marks the response as complete and forbids writing to it
                        return exchange.response.setComplete()
                    }
                    is MissingValueException -> {
                        exchange.response.setStatusCode(HttpStatus.BAD_REQUEST)
                        exchange.response.headers.add("Content-Type", "application/json")
                        try {
                            val message = JsonMapper().writeValueAsString(ex.fieldNames)
                            val buffer = exchange.response.bufferFactory().wrap(message.toByteArray())
                            return exchange.response.writeWith(Flux.just(buffer))
                        } catch (_:JsonProcessingException) {
                        }
                    }
                }
                return Mono.error(ex)
            }
        }
    }
    companion object {
        val LOGGER = LoggerFactory.getLogger(RoutesConfig::class.java)
    }
}
