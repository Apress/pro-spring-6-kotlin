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
package com.apress.prospring6.three.collectioninject

import com.apress.prospring6.three.nesting.Song
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * Created by iuliana.cosmina on 06/03/2022
 */
object CollectionInjectionDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(CollectionConfig::class.java, CollectingBean::class.java)
        ctx.refresh()
        val collectingBean = ctx.getBean(CollectingBean::class.java)
        collectingBean.printCollections()
    }
}

@Component
internal class CollectingBean {
    @Autowired
    @Qualifier("list")
    var songListResource:List<Song>? = null

    @Autowired
    var songList:List<Song>? = null

    @Autowired
    var songSet: Set<Song>? = null

    @Autowired
    @Qualifier("set")
    var songSetResource:Set<Song>? = null

    @Autowired
    var songMap: Map<String, Song>? = null

    @Autowired
    @Qualifier("map")
    var songMapResource:Map<String, Song>? = null

    @Autowired
    var props: Properties? = null
    fun printCollections() {
        println("-- list injected using @Autowired -- ")
        songList!!.forEach{ s: Song -> println(s.title) }
        println("-- list injected using @Resource / @Autowired @Qualifier(\"list\") / @Inject @Named(\"list\") -- ")
        songListResource!!.forEach{ s: Song -> println(s.title) }
        println("-- set injected using @Autowired -- -- ")
        songSet!!.forEach{ s: Song -> println(s.title) }
        println("-- set injected using @Resource / @Autowired @Qualifier(\"set\") / @Inject @Named(\"set\") -- ")
        songSetResource!!.forEach{ s: Song -> println(s.title) }
        println("-- map injected using  @Autowired -- ")
        songMap!!.forEach{ k: String, v: Song -> println(k + ": " + v.title) }
        println("-- map injected using @Resource / @Autowired @Qualifier(\"map\") / @Inject @Named(\"map\")-- ")
        songMapResource!!.forEach{ (k: String, v: Song) -> println(k + ": " + v.title) }
        println("-- props injected with @Autowired -- ")
        props!!.forEach { k: Any, v: Any -> println("$k: $v") }
    }
}

@Configuration
internal open class CollectionConfig {
    @Bean
    open fun list(): List<Song> {
        return listOf(
            Song("Not the end"),
            Song("Rise Up")
        )
    }

    @Bean
    open fun set(): Set<Song> {
        return setOf(
            Song("Ordinary Day"),
            Song("Birds Fly")
        )
    }

    @Bean
    open fun map(): Map<String, Song> {
        return mapOf(
            "John Mayer" to Song("Gravity"),
            "Ben Barnes" to Song("11:11")
        )
    }

    @Bean
    open fun props(): Properties {
        val props = Properties()
        props["said.she"] = "Never Mine"
        props["said.he"] = "Cold and jaded"
        return props
    }

    @Bean
    open fun song1(): Song {
        return Song("Here's to hoping")
    }

    @Bean
    open fun song2(): Song {
        return Song("Wishing the best for you")
    }
}