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
package com.apress.prospring6.three.autowiring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.*
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by iuliana.cosmina on 11/03/2022
 */
object AutowiringDemo {
    private val LOGGER: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(AutowiringDemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext(AutowiringCfg::class.java)
        val target = ctx.getBean(Target::class.java)
        LOGGER.info("target: Created target? {}", target != null)
        LOGGER.info("target: Injected bar? {}", target.bar != null)
        LOGGER.info("target: Injected fooOne? {}", if (target.fooOne != null) target.fooOne!!.id else "")
        LOGGER.info("target: Injected fooTwo? {}", if (target.fooTwo != null) target.fooTwo!!.id else "")
        val anotherTarget = ctx.getBean(AnotherTarget::class.java)
        LOGGER.info("anotherTarget: Created anotherTarget? {}", anotherTarget != null)
        LOGGER.info("anotherTarget: Injected bar? {}", anotherTarget.bar != null)
        LOGGER.info(
            "anotherTarget: Injected fooOne? {}",
            if (anotherTarget.fooOne != null) anotherTarget.fooOne!!.id else ""
        )
        LOGGER.info(
            "anotherTarget: Injected fooTwo? {}",
            if (anotherTarget.fooTwo != null) anotherTarget.fooTwo!!.id else ""
        )
        val fieldTarget = ctx.getBean(FieldTarget::class.java)
        LOGGER.info("fieldTarget: Created fieldTarget? {}", fieldTarget != null)
        LOGGER.info("fieldTarget: Injected bar? {}", fieldTarget.bar != null)
        LOGGER.info("fieldTarget: Injected fooOne? {}", if (fieldTarget.fooOne != null) fieldTarget.fooOne!!.id else "")
        LOGGER.info("fieldTarget: Injected fooTwo? {}", if (fieldTarget.fooTwo != null) fieldTarget.fooTwo!!.id else "")
    }
}

@Configuration
@ComponentScan
internal open class AutowiringCfg {
    @Bean
    open fun anotherFoo(): Foo {
        return Foo()
    }
}

@Component
@Lazy
internal class Target {
    var fooOne: Foo?
    var fooTwo: Foo? = null
    var bar: Bar? = null

    init {
        logger.info(" --> Target.init() called");
    }

    @Autowired
    constructor(@Qualifier("foo") foo: Foo?) {
        fooOne = foo
        logger.info(" --> Target(Foo) called")
    }

    //@Autowired
    constructor(@Qualifier("foo") foo: Foo?, bar: Bar?) {
        fooOne = foo
        this.bar = bar
        logger.info(" --> Target(Foo, Bar) called")
    }

    companion object {
        private val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(Target::class.java)
    }
}

@Component
@Lazy
internal class AnotherTarget {
    var fooOne: Foo? = null
        @Autowired set(@Qualifier("foo") value) {
            logger.info(" --> AnotherTarget#setFooOne(Foo) called")
            field = value
        }

    var fooTwo: Foo? = null
        @Autowired set(@Qualifier("anotherFoo") value) {
            logger.info(" --> AnotherTarget#setFooTwo(Foo) called")
            field = value
        }

    var bar: Bar? = null
        @Autowired set(value) {
            logger.info(" --> AnotherTarget#setBar(Bar) called")
            field = value
        }

    companion object {
        private val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(AnotherTarget::class.java)
    }
}

@Component
@Lazy
internal class FieldTarget {
    @Autowired
    @Qualifier("foo")
    var fooOne: Foo? = null

    @Autowired
    @Qualifier("anotherFoo")
    var fooTwo: Foo? = null

    @Autowired
    var bar: Bar? = null
}

@Component
internal class Foo {
    var id = UUID.randomUUID().toString()
        .replace("-", "").substring(0, 8)
}

@Component
internal class Bar