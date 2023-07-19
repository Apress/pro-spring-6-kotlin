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
package com.apress.prospring6.three

internal interface ManagedComponent {
    fun performLookup(container: Container?)
}

internal interface Container {
    fun getDependency(key: String?): Any?
}

internal class DefaultContainer : Container {
    override fun getDependency(key: String?): Any {
        return when(key) {
            "provider" -> HelloWorldMessageProvider()
            else -> throw RuntimeException("Unknown dependency: $key")
        }
    }
}

object CDLDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val container: Container = DefaultContainer()
        val renderer: MessageRenderer = StandardOutMessageRenderer()
        renderer.performLookup(container)
        renderer.render()
    }
}

internal interface MessageProvider {
    val message: String?
}

internal class HelloWorldMessageProvider : MessageProvider {
    init {
        println(" --> HelloWorldMessageProvider: constructor called")
    }

    override val message: String?
        get() = "Hello World!"
}

internal interface MessageRenderer : ManagedComponent {
    fun render()
}

internal class StandardOutMessageRenderer : MessageRenderer {
    private var messageProvider: MessageProvider? = null

    init {
        println(" --> StandardOutMessageRenderer: constructor called")
    }

    override fun performLookup(container: Container?) {
        messageProvider = container!!.getDependency("provider") as MessageProvider?
    }

    override fun toString(): String {
        return messageProvider.toString()
    }

    override fun render() {
        val msg = messageProvider?:
            throw RuntimeException(
                "You must set the property messageProvider of class:"
                        + StandardOutMessageRenderer::class.java.name
            )
        println(msg.message)
    }
}