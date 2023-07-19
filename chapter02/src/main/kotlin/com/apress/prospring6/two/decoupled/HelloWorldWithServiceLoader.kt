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
package com.apress.prospring6.two.decoupled

import java.util.*

/**
 * Created by iuliana.cosmina on 08/02/2022
 */
object HelloWorldWithServiceLoader {
    @JvmStatic
    fun main(args: Array<String>) {
        val slr: ServiceLoader<MessageRenderer> = ServiceLoader.load(MessageRenderer::class.java)
        val slp: ServiceLoader<MessageProvider> = ServiceLoader.load(MessageProvider::class.java)
        val mr: MessageRenderer = slr.findFirst().orElseThrow {
            IllegalArgumentException(
                "Service of type 'MessageRenderer' was not found!"
            )
        }
        val mp: MessageProvider = slp.findFirst().orElseThrow {
            IllegalArgumentException(
                "Service of type 'MessageProvider' was not found!"
            )
        }
        mr.messageProvider = mp
        mr.render()
    }
}