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
package com.apress.prospring6.four

import com.apress.prospring6.two.decoupled.MessageProvider
import com.apress.prospring6.two.decoupled.MessageRenderer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.*
import org.springframework.core.env.Environment

object PropertySourcesDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx: ApplicationContext = AnnotationConfigApplicationContext(PropertySourcesCfg::class.java)
        val mr = ctx.getBean(
            "messageRenderer",
            MessageRenderer::class.java
        )
        mr.render()
    }
}

@Configuration
@PropertySource(value = ["classpath:message.properties"])
internal open class PropertySourcesCfg {
    @Autowired
    var env: Environment? = null
    @Bean
    @Lazy
    open fun messageProvider(): MessageProvider =
        ConfigurableMessageProvider(env!!.getProperty("message"))

    @Bean(name = ["messageRenderer"])
    @Scope(value = "prototype")
    @DependsOn(value = ["messageProvider"])
    open fun messageRenderer(): MessageRenderer =
        StandardOutMessageRenderer().apply {
            messageProvider = messageProvider()
        }
}

internal class ConfigurableMessageProvider(
    @param:Value(
        "Configurable message"
    ) override val message: String
) :
    MessageProvider

internal class StandardOutMessageRenderer : MessageRenderer {
    override var messageProvider: MessageProvider? = null

    override fun render() {
        LOGGER.info(messageProvider!!.message)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(StandardOutMessageRenderer::class.java)
    }
}