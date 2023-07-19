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
package com.apress.prospring6.six.config

import org.apache.commons.dbcp2.BasicDataSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import javax.sql.DataSource

/**
 * Created by iuliana.cosmina on 05/05/2022
 */
@Configuration
@PropertySource("classpath:db/jdbc.properties")
open class BasicDataSourceCfg {
    @Value("\${jdbc.driverClassName}")
    private val driverClassName: String? = null

    @Value("\${jdbc.url}")
    private val url: String? = null

    @Value("\${jdbc.username}")
    private val username: String? = null

    @Value("\${jdbc.password}")
    private val password: String? = null

    @Bean(destroyMethod = "close")
    open fun dataSource(): DataSource? {
        return try {
            val dataSource = BasicDataSource()
            dataSource.driverClassName = driverClassName
            dataSource.url = url
            dataSource.username = username
            dataSource.password = password
            dataSource
        } catch (e: Exception) {
            LOGGER.error("DBCP DataSource bean cannot be created!", e)
            null
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BasicDataSourceCfg::class.java)
    }
}