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
package com.apress.prospring6.three.pickle

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.util.*

/**
 * Created by iuliana.cosmina on 13/03/2022
 */
object PickleAutowiringDemo {
    private val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(PickleAutowiringDemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext(AutowiringCfg::class.java)
        val target = ctx.getBean(TrickyTarget::class.java)
        logger.info("target: Created target? {}", target != null)
        logger.info("target: Injected bar? {}", target.bar != null)
        logger.info("target: Injected fooOne? {}", if (target.fooOne != null) target.fooOne.toString() else "")
        logger.info("target: Injected fooTwo? {}", if (target.fooTwo != null) target.fooTwo.toString() else "")
    }
}

internal class TrickyTarget {
    var fooOne: Foo? = null
        @Autowired  @Qualifier("fooImplOne")set(value) {
            // comment @Qualifier annotation to cause NoUniqueBeanDefinitionException being thrown at runtime
            field = value
            logger.info(" --> Property fooOne set")
        }
    var fooTwo: Foo? = null
        @Autowired @Qualifier("fooImplTwo")
        set(value) {
            // comment @Qualifier annotation to cause NoUniqueBeanDefinitionException being thrown at runtime
            // and make sure for @Primary in FooImpl to be commented as well
            field = value
            logger.info(" --> Property fooTwo set")
        }
    var bar: Bar? = null
        @Autowired set(value) {
            logger.info(" --> Property bar set")
            field = value
        }

    constructor() {
        logger.info(" --> TrickyTarget() called")
    }

    constructor(foo: Foo?) {
        fooOne = foo
        logger.info(" --> TrickyTarget(Foo) called")
    }

    constructor(foo: Foo?, bar: Bar?) {
        fooOne = foo
        this.bar = bar
        logger.info(" --> TrickyTarget(Foo, Bar) called")
    }

    companion object {
        private val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(TrickyTarget::class.java)
    }
}

internal interface Foo { // empty interface, used as a marker interface
}

internal class FooImplOne : Foo {
    var id = "one:" + UUID.randomUUID().toString().replace("-", "").substring(0, 8)
    override fun toString(): String {
        return "${super.toString()}, id=$id"
    }
}

internal class FooImplTwo : Foo {
    var id = "two:" + UUID.randomUUID().toString().replace("-", "").substring(0, 8)
    override fun toString(): String {
        return "${super.toString()}, id=$id"
    }
}

internal class Bar

@Configuration
@ComponentScan
internal open class AutowiringCfg {
    @Bean //@Primary
    open fun fooImplOne(): Foo {
        return FooImplOne()
    }

    @Bean
    open fun fooImplTwo(): Foo {
        return FooImplTwo()
    }

    @Bean
    open fun bar(): Bar {
        return Bar()
    }

    @Bean
    open fun trickyTarget(): TrickyTarget {
        return TrickyTarget()
    }
}