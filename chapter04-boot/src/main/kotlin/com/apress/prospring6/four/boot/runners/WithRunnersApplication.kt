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
package com.apress.prospring6.four.boot.runners

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by iuliana.cosmina on 03/04/2022
 */
@org.springframework.core.annotation.Order(2)
@org.springframework.stereotype.Component("messageRenderer")
internal class StandardOutMessageRenderer : com.apress.prospring6.two.decoupled.MessageRenderer,
    CommandLineRunner {
    override var messageProvider: com.apress.prospring6.two.decoupled.MessageProvider? = null
        @Autowired set(value) {
            field = value
        }

    override fun render() {
        logger.info(messageProvider!!.message)
    }

    @Throws(java.lang.Exception::class)
    override fun run(vararg args: String?) {
        render()
    }

    companion object {
        private val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(StandardOutMessageRenderer::class.java)
    }
}

@org.springframework.core.annotation.Order(1)
@org.springframework.stereotype.Component
internal class ConfigurableMessageProvider(
    @param:org.springframework.beans.factory.annotation.Value(
        "Configurable message"
    ) override var message: String
) :
    com.apress.prospring6.two.decoupled.MessageProvider, CommandLineRunner {

    @Throws(java.lang.Exception::class)
    override fun run(vararg args: String) {
        if (args.size >= 1) {
            message = args[0]
        }
    }
}

@SpringBootApplication
open class WithRunnersApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(arrayOf(WithRunnersApplication::class.java), args)
        }
    }
}