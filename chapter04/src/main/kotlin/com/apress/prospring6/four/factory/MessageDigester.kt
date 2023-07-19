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
package com.apress.prospring6.four.factory

import org.slf4j.LoggerFactory
import java.security.MessageDigest

/**
 * Created by iuliana.cosmina on 26/03/2022
 */
class MessageDigester {
    var digest1: MessageDigest? = null
    var digest2: MessageDigest? = null

    fun digest(msg: String) {
        LOGGER.info("Using digest1")
        digest(msg, digest1)
        LOGGER.info("Using digest2")
        digest(msg, digest2)
    }

    private fun digest(msg: String, digest: MessageDigest?) {
        LOGGER.info("Using algoritm: " + digest!!.algorithm)
        digest.reset()
        val bytes = msg.toByteArray()
        val out = digest.digest(bytes)
        // we are printing the actual byte values
        LOGGER.info("Original Message: {} ", bytes)
        LOGGER.info("Encrypted Message: {} ", out)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MessageDigester::class.java)
    }
}