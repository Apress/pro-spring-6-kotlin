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
package com.apress.prospring6.ten.config

import com.mongodb.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@ComponentScan(basePackages = ["com.apress.prospring6.ten.service"])
@EnableMongoRepositories(basePackages = ["com.apress.prospring6.ten.repos"])
@Configuration
@PropertySource("classpath:mongo.properties")
// We can't use that, because for simplicity the docker mongo was not
// started as a replica set:
//@EnableTransactionManagement
open class MongoCfg : AbstractMongoClientConfiguration() {
    @Value("\${mongo.url}")
    private val url: String? = null

    @Value("\${mongo.db}")
    private val database: String? = null

    @Value("\${mongo.user}")
    private val user: String? = null

    @Value("\${mongo.password}")
    private val password: String? = null

    @Value("\${mongo.authSource}")
    private val authSource: String? = null

    override fun getMappingBasePackages() = mutableSetOf("com.apress.prospring6.ten.document")

    @Bean
    open fun transactionManager(dbFactory: MongoDatabaseFactory?): MongoTransactionManager {
        return MongoTransactionManager(dbFactory!!)
    }

    override fun getDatabaseName(): String = database!!

    override fun autoIndexCreation(): Boolean {
        return true
    }

    override fun mongoClientSettings(): MongoClientSettings {
        val builder = MongoClientSettings.builder()
        builder.applyConnectionString(ConnectionString(url!!))
            .credential(
                MongoCredential.createScramSha1Credential(
                    user!!,
                    authSource!!,
                    password!!.toCharArray()))
        this.configureClientSettings(builder)
        return builder.build()
    }
}