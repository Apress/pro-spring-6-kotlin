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
package com.apress.prospring6.five.advice

import com.apress.prospring6.five.common.Guitar
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Created by iuliana.cosmina on 20/04/2022
 */
@Component
@Aspect
class AroundAdviceV2 {
    @Pointcut("execution(* com.apress.prospring6.five..sing*(com.apress.prospring6.five.common.Guitar))  && args(value)")
    fun singExecution(value: Guitar?) {
    }

    @Around(value = "singExecution(guitar)", argNames = "pjp,guitar")
    @Throws(Throwable::class)
    fun simpleAroundAdvice(pjp: ProceedingJoinPoint, guitar: Guitar): Any {
        val signature = pjp.signature as MethodSignature
        LOGGER.info(
            " > Before Executing: {} from {} with argument {}",
            signature.name,
            signature.declaringTypeName,
            guitar.brand
        )
        val retVal = pjp.proceed()
        LOGGER.info(
            " > After Executing: {} from {} with argument {}",
            signature.name,
            signature.declaringTypeName,
            guitar.brand
        )
        return retVal
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AroundAdviceV2::class.java)
    }
}