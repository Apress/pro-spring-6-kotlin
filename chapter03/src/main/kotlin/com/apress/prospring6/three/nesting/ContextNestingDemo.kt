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
package com.apress.prospring6.three.nesting

import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * Created by iuliana.cosmina on 06/03/2022
 */
object ContextNestingDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val parentCtx = AnnotationConfigApplicationContext()
        parentCtx.register(ParentConfig::class.java)
        parentCtx.refresh()
        val childCtx = AnnotationConfigApplicationContext()
        childCtx.register(ChildConfig::class.java)
        childCtx.parent = parentCtx
        childCtx.refresh()
        val song1: Song = childCtx.getBean("song1") as Song
        val song2: Song = childCtx.getBean("song2") as Song
        val song3: Song = childCtx.getBean("song3") as Song
        println("from parent ctx: " + song1.title)
        println("from parent ctx: " + song2.title)
        println("from child ctx: " + song3.title)
    }
}