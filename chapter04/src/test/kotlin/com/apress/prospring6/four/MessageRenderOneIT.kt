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

import com.apress.prospring6.four.impl.provider.ProviderConfig
import com.apress.prospring6.four.impl.renderer.RendererConfig
import com.apress.prospring6.two.decoupled.MessageProvider
import com.apress.prospring6.two.decoupled.MessageRenderer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * Created by iuliana.cosmina on 31/03/2022
 */
class MessageRenderOneIT {
    @Test
    fun testConfig() {
        val ctx = AnnotationConfigApplicationContext(
            RendererConfig::class.java,
            ProviderConfig::class.java
        )
        val messageProvider = ctx.getBean(
            MessageProvider::class.java
        )
        val messageRenderer = ctx.getBean(
            MessageRenderer::class.java
        )
        Assertions.assertAll("messageTest",
            Executable { Assertions.assertNotNull(messageRenderer) },
            Executable { Assertions.assertNotNull(messageProvider) },
            Executable {
                Assertions.assertEquals(
                    messageProvider,
                    messageRenderer.messageProvider
                )
            }
        )
        messageRenderer.render()
    }
}