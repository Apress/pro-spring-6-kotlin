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
package com.apress.prospring6.five.manual

import org.slf4j.LoggerFactory
import org.springframework.aop.MethodBeforeAdvice
import org.springframework.aop.framework.ProxyFactory
import org.springframework.lang.Nullable
import java.lang.reflect.Method

/**
 * Created by iuliana.cosmina on 09/04/2022
 * Example that allows users to authenticate with any password, and it also allows only a single, hard-coded user access to the secured methods. However, it
 * does illustrate how easy it is to use AOP to implement a crosscutting concern such as security.
 * This is a demonstration for before advice.
 */
object SecureMethodAdviceDemo {
    private val LOGGER = LoggerFactory.getLogger(SecureMethodAdviceDemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        val mgr = SimpleSecurityManager()
        LOGGER.info("---- Successful access to message of SecureBean ---- ")
        val bean = secureBean
        mgr.login("John", "pwd")
        bean.writeSecureMessage()
        mgr.logout()
        try {
            LOGGER.info("---- Prohibited access to message of SecureBean ---- ")
            mgr.login("invalid user", "pwd")
            bean.writeSecureMessage()
        } catch (ex: SecurityException) {
            LOGGER.error("Exception Caught: {}", ex.message)
        } finally {
            mgr.logout()
        }
        try {
            LOGGER.info("----  No user logged in. Prohibited access to message of SecureBean ---- ")
            bean.writeSecureMessage()
        } catch (ex: SecurityException) {
            LOGGER.error("Exception Caught: {}", ex.message)
        }
    }

    private val secureBean: SecureBean
        get() {
            val target = SecureBean()
            val advice = SecurityAdvice()
            val factory =
                ProxyFactory()
            factory.setTarget(target)
            factory.addAdvice(advice)
            return factory.proxy as SecureBean
        }
}

internal class SecureBean {
    fun writeSecureMessage() {
        LOGGER.debug("""
                Every time I learn something new, 
                it pushes some old stuff out of my brain
                """)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SecureBean::class.java)
    }
}

internal class SecurityAdvice : MethodBeforeAdvice {
    private val securityManager: SimpleSecurityManager

    init {
        securityManager = SimpleSecurityManager()
    }

    @Throws(Throwable::class)
    override fun before(method: Method, args: Array<out Any>, @Nullable target: Any?) {
        val user = securityManager.loggedOnUser
        if (user == null) {
            LOGGER.debug("No user authenticated")
            throw SecurityException("You must login before attempting to invoke the method: " + method.name)
        } else if ("John" == user.userName) {
            LOGGER.debug("Logged in user is John - OKAY!")
        } else {
            LOGGER.debug("Logged in user is {} NOT GOOD :(", user.userName)
            throw SecurityException("User " + user.userName + " is not allowed access to method " + method.name)
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SecurityAdvice::class.java)
    }
}

internal class UserInfo(val userName: String, val password: String)

/**
 * The application uses the SecurityManager class to authenticate a user and, later, to retrieve the details
 * of the currently authenticated user.
 */
internal class SimpleSecurityManager {
    fun login(userName: String, password: String) {
        threadLocal.set(UserInfo(userName, password))
    }

    fun logout() {
        threadLocal.set(null)
    }

    val loggedOnUser: UserInfo?
        get() = threadLocal.get()

    companion object {
        private val threadLocal = ThreadLocal<UserInfo?>()
    }
}