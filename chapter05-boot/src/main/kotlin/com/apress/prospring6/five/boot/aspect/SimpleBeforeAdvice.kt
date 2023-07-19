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
package com.apress.prospring6.five.boot.aspect

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Created by iuliana.cosmina on 18/04/2022
 */
@Component
@org.aspectj.lang.annotation.Aspect
class SimpleBeforeAdvice {
    @org.aspectj.lang.annotation.Pointcut("execution(* com.apress.prospring6.five..sing*(com.apress.prospring6.five.boot.aspect.Guitar))  && args(value)")
    fun singExecution(value: Guitar?) {
    }

    @get:org.aspectj.lang.annotation.Pointcut("bean(john*)")
    val isJohn: Unit
        get() {}

    @org.aspectj.lang.annotation.Before(value = "singExecution(guitar) && isJohn()", argNames = "joinPoint,guitar")
    fun simpleBeforeAdvice(joinPoint: org.aspectj.lang.JoinPoint, guitar: Guitar) {
        if (guitar.brand == "Gibson") {
            val signature: org.aspectj.lang.reflect.MethodSignature =
                joinPoint.getSignature() as org.aspectj.lang.reflect.MethodSignature
            LOGGER.info(
                " > Executing: {} from {} with {}",
                signature.getName(),
                signature.getDeclaringTypeName(),
                guitar.brand
            )
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SimpleBeforeAdvice::class.java)
    }
}