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
package com.apress.prospring6.seven.base.config

import org.hibernate.cfg.Environment
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.orm.hibernate5.HibernateTransactionManager
import org.springframework.orm.hibernate5.LocalSessionFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.sql.DataSource

/**
 * Created by iuliana.cosmina on 22/05/2022
 */
@Import(BasicDataSourceCfg::class)
@Configuration
@ComponentScan(basePackages = ["com.apress.prospring6.seven.base"])
@EnableTransactionManagement
open class HibernateConfig {
    @Autowired
    var dataSource: DataSource? = null

    @Bean
    open fun hibernateProperties(): Properties {
        val hibernateProp = Properties()
        hibernateProp[Environment.HBM2DDL_AUTO] = "none"
        hibernateProp[Environment.FORMAT_SQL] = false
        hibernateProp[Environment.USE_SQL_COMMENTS] = false
        hibernateProp[Environment.SHOW_SQL] = false
        hibernateProp[Environment.MAX_FETCH_DEPTH] = 3
        hibernateProp[Environment.STATEMENT_BATCH_SIZE] = 10
        hibernateProp[Environment.STATEMENT_FETCH_SIZE] = 50
        return hibernateProp
    }

    @Bean
    open fun sessionFactory(): LocalSessionFactoryBean {
        val sessionFactory = LocalSessionFactoryBean()
        sessionFactory.setDataSource(dataSource!!)
        sessionFactory.setPackagesToScan("com.apress.prospring6.seven.base.entities")
        sessionFactory.hibernateProperties = hibernateProperties()
        return sessionFactory
    }

    @Bean
    open fun transactionManager(): PlatformTransactionManager {
        val transactionManager = HibernateTransactionManager()
        transactionManager.sessionFactory = sessionFactory().getObject()
        return transactionManager
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(HibernateConfig::class.java)
    }
}