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
package com.apress.prospring6.three.methodinject

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Lookup
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

/**
 * Created by iuliana.cosmina on 07/03/2022
 */
object MethodInjectionDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext(LookupConfig::class.java)
        val abstractLockOpener = ctx.getBean("abstractLockOpener", LockOpener::class.java)
        val standardLockOpener = ctx.getBean("standardLockOpener", LockOpener::class.java)
        displayInfo("abstractLockOpener", abstractLockOpener)
        displayInfo("standardLockOpener", standardLockOpener)
    }

    fun displayInfo(beanName: String, lockOpener: LockOpener) {
        val keyHelperOne = lockOpener.createKeyOpener()
        val keyHelperTwo = lockOpener.createKeyOpener()
        println("[" + beanName + "]: KeyHelper Instances the Same?  " + (keyHelperOne === keyHelperTwo))
        val stopWatch = StopWatch()
        stopWatch.start("lookupDemo")
        for (x in 0 until 100000) {
            val keyHelper = lockOpener.createKeyOpener()
            keyHelper!!.open()
        }
        stopWatch.stop()
        println("100000 gets took " + stopWatch.totalTimeMillis + " ms")
    }
}

@Configuration
@ComponentScan
internal open class LookupConfig

interface LockOpener {
    fun createKeyOpener(): KeyHelper?
    fun openLock()
}

@Component("standardLockOpener")
internal class StandardLockOpener : LockOpener {
    var keyOpener: KeyHelper? = null

    override fun createKeyOpener(): KeyHelper? {
        return keyOpener
    }

    @Autowired
    @Qualifier("keyHelper")
    fun setKeyHelper(keyHelper: KeyHelper?) {
        keyOpener = keyHelper
    }

    override fun openLock() {
        keyOpener!!.open()
    }
}

@Component("abstractLockOpener")
internal abstract class AbstractLockOpener : LockOpener {
    @Lookup("keyHelper")
    abstract override fun createKeyOpener(): KeyHelper?

    override fun openLock() {
        createKeyOpener()!!.open()
    }
}

@Component("keyHelper")
@Scope("prototype")
class KeyHelper {
    fun open() {}
}