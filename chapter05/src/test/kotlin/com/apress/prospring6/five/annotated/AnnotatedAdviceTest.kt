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
package com.apress.prospring6.five.annotated

import com.apress.prospring6.five.advice.*
import com.apress.prospring6.five.common.Guitar
import com.apress.prospring6.five.common.RejectedInstrumentException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.util.*

/**
 * Created by iuliana.cosmina on 18/04/2022
 */
class AnnotatedAdviceTest {
    @Test
    fun testBeforeAdviceV1() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, BeforeAdviceV1::class.java)
        ctx.refresh()
        Assertions.assertTrue(listOf(*ctx.beanDefinitionNames).contains("beforeAdviceV1"))
        val documentarist = ctx.getBean(
            "documentarist",
            NewDocumentarist::class.java
        )
        documentarist.execute()
        ctx.close()
    }

    @Test
    fun testBeforeAdviceV2() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, BeforeAdviceV2::class.java)
        ctx.refresh()
        Assertions.assertTrue(Arrays.asList(*ctx.beanDefinitionNames).contains("beforeAdviceV2"))
        val documentarist = ctx.getBean(
            "documentarist",
            NewDocumentarist::class.java
        )
        documentarist.execute()
        ctx.close()
    }

    @Test
    fun testBeforeAdviceV3() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, BeforeAdviceV3::class.java)
        ctx.refresh()
        Assertions.assertTrue(Arrays.asList(*ctx.beanDefinitionNames).contains("beforeAdviceV3"))
        val documentarist = ctx.getBean(
            "documentarist",
            NewDocumentarist::class.java
        )
        documentarist.execute()
        ctx.close()
    }

    @Test
    fun testBeforeAdviceV4() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, BeforeAdviceV4::class.java)
        ctx.refresh()
        Assertions.assertTrue(Arrays.asList(*ctx.beanDefinitionNames).contains("beforeAdviceV4"))
        val documentarist = ctx.getBean(
            "documentarist",
            NewDocumentarist::class.java
        )
        documentarist.execute()
        ctx.close()
    }

    @Test
    fun testAroundAdviceV1() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, AroundAdviceV1::class.java)
        ctx.refresh()
        Assertions.assertTrue(Arrays.asList(*ctx.beanDefinitionNames).contains("aroundAdviceV1"))
        val documentarist = ctx.getBean(
            "documentarist",
            NewDocumentarist::class.java
        )
        documentarist.execute()
        ctx.close()
    }

    @Test
    fun testAroundAdviceV2() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, CommandingDocumentarist::class.java, AroundAdviceV2::class.java)
        ctx.refresh()
        Assertions.assertTrue(Arrays.asList(*ctx.beanDefinitionNames).contains("aroundAdviceV2"))
        val documentarist = ctx.getBean(
            "commandingDocumentarist",
            CommandingDocumentarist::class.java
        )
        documentarist.execute()
        ctx.close()
    }

    @Test
    fun testAfterAdviceV1() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, AfterAdviceV1::class.java)
        ctx.refresh()
        Assertions.assertTrue(Arrays.asList(*ctx.beanDefinitionNames).contains("afterAdviceV1"))
        val guitar = Guitar()
        val guitarist = ctx.getBean("agustin", PretentiosGuitarist::class.java)
        guitarist.sing(guitar)
        LOGGER.info("-------------------")
        guitar.brand = "Musicman"
        Assertions.assertThrows(
            IllegalArgumentException::class.java,
            { guitarist.sing(guitar) }, "Unacceptable guitar!"
        )
        ctx.close()
    }

    @Test
    fun testAfterReturningAdviceV1() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, AfterReturningAdviceV1::class.java)
        ctx.refresh()
        Assertions.assertTrue(Arrays.asList(*ctx.beanDefinitionNames).contains("afterReturningAdviceV1"))
        val guitar = Guitar()
        val guitarist = ctx.getBean("agustin", PretentiosGuitarist::class.java)
        guitarist.sing(guitar)
        LOGGER.info("-------------------")
        guitar.brand = "Musicman"
        Assertions.assertThrows(
            IllegalArgumentException::class.java,
            { guitarist.sing(guitar) }, "Unacceptable guitar!"
        )
        ctx.close()
    }

    @Test
    fun testAfterThrowingAdviceV1() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, AfterThrowingAdviceV1::class.java)
        ctx.refresh()
        Assertions.assertTrue(Arrays.asList(*ctx.beanDefinitionNames).contains("afterThrowingAdviceV1"))
        val guitar = Guitar()
        val guitarist = ctx.getBean("agustin", PretentiosGuitarist::class.java)
        guitarist.sing(guitar)
        LOGGER.info("-------------------")
        guitar.brand = "Musicman"
        Assertions.assertThrows(
            IllegalArgumentException::class.java,
            { guitarist.sing(guitar) }, "Unacceptable guitar!"
        )
        ctx.close()
    }

    @Test
    fun testAfterThrowingAdviceV2() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, AfterThrowingAdviceV2::class.java)
        ctx.refresh()
        Assertions.assertTrue(Arrays.asList(*ctx.beanDefinitionNames).contains("afterThrowingAdviceV2"))
        val guitar = Guitar()
        val guitarist = ctx.getBean("agustin", PretentiosGuitarist::class.java)
        guitarist.sing(guitar)
        LOGGER.info("-------------------")
        guitar.brand = "Musicman"
        Assertions.assertThrows(
            RejectedInstrumentException::class.java,
            { guitarist.sing(guitar) }, "Unacceptable guitar!"
        )
        ctx.close()
    }

    @Test
    fun testAfterThrowingAdviceV5() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, BeforeAdviceV5::class.java)
        ctx.refresh()
        Assertions.assertTrue(Arrays.asList(*ctx.beanDefinitionNames).contains("beforeAdviceV5"))
        val johnMayer = ctx.getBean(
            "johnMayer",
            GrammyGuitarist::class.java
        )
        johnMayer.sing(Guitar())
        val pretentiousGuitarist = ctx.getBean(
            "agustin",
            PretentiosGuitarist::class.java
        )
        pretentiousGuitarist.sing(Guitar())
        ctx.close()
    }

    @Test
    fun testAfterThrowingAdviceV6() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, BeforeAdviceV6::class.java)
        ctx.refresh()
        Assertions.assertTrue(Arrays.asList(*ctx.beanDefinitionNames).contains("beforeAdviceV6"))
        val johnMayer = ctx.getBean(
            "johnMayer",
            GrammyGuitarist::class.java
        )
        johnMayer.sing(Guitar())
        val pretentiousGuitarist = ctx.getBean(
            "agustin",
            PretentiosGuitarist::class.java
        )
        pretentiousGuitarist.sing(Guitar())
        ctx.close()
    }

    @Test
    fun testAfterThrowingAdviceV7() {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(AspectJAopConfig::class.java, BeforeAdviceV7::class.java)
        ctx.refresh()
        Assertions.assertTrue(Arrays.asList(*ctx.beanDefinitionNames).contains("beforeAdviceV7"))
        val johnMayer = ctx.getBean(
            "johnMayer",
            GrammyGuitarist::class.java
        )
        johnMayer.sing(Guitar())
        val pretentiousGuitarist = ctx.getBean(
            "agustin",
            PretentiosGuitarist::class.java
        )
        pretentiousGuitarist.sing(Guitar())
        val gretsch = Guitar()
        gretsch.brand = "Gretsch"
        johnMayer.sing(gretsch)
        pretentiousGuitarist.sing(gretsch)
        ctx.close()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AnnotatedAdviceTest::class.java)
    }
}