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

import org.hibernate.cfg.Environment
import org.hibernate.envers.configuration.EnversSettings
import org.hibernate.envers.strategy.internal.ValidityAuditStrategy
import org.hibernate.jpa.HibernatePersistenceProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.sql.DataSource

/**
 * Created by iuliana.cosmina on 06/08/2022
 */
@Import(BasicDataSourceCfg::class)
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = ["com.apress.prospring6.ten"])
@EnableJpaRepositories("com.apress.prospring6.ten.repos")
open class EnversConfig {
    @Autowired
    var dataSource: DataSource? = null

    @Bean
    open fun transactionManager(): PlatformTransactionManager {
        return JpaTransactionManager().apply {
            entityManagerFactory = entityManagerFactory().getObject()
        }
    }

    @Bean
    open fun jpaVendorAdapter(): JpaVendorAdapter {
        return HibernateJpaVendorAdapter()
    }

    @Bean
    open fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        return LocalContainerEntityManagerFactoryBean().apply {
            setPersistenceProviderClass(HibernatePersistenceProvider::class.java)
            setPackagesToScan("com.apress.prospring6.ten.entities")
            dataSource = this@EnversConfig.dataSource!!
            setJpaProperties(jpaProperties())
            jpaVendorAdapter = jpaVendorAdapter()
        }
    }

    @Bean
    open fun jpaProperties(): Properties {
        val jpaProps = Properties()
        jpaProps[Environment.HBM2DDL_AUTO] = "none"
        jpaProps[Environment.FORMAT_SQL] = false
        jpaProps[Environment.STATEMENT_BATCH_SIZE] = 30
        jpaProps[Environment.USE_SQL_COMMENTS] = false
        jpaProps[Environment.SHOW_SQL] = false

        //Properties for Hibernate Envers
        jpaProps[EnversSettings.AUDIT_TABLE_SUFFIX] = "_H"
        jpaProps[EnversSettings.REVISION_FIELD_NAME] = "AUDIT_REVISION"
        jpaProps[EnversSettings.REVISION_TYPE_FIELD_NAME] = "ACTION_TYPE"
        jpaProps[EnversSettings.AUDIT_STRATEGY] = ValidityAuditStrategy::class.java.name
        jpaProps[EnversSettings.AUDIT_STRATEGY_VALIDITY_END_REV_FIELD_NAME] = "AUDIT_REVISION_END"
        jpaProps[EnversSettings.AUDIT_STRATEGY_VALIDITY_STORE_REVEND_TIMESTAMP] = "true"
        jpaProps[EnversSettings.AUDIT_STRATEGY_VALIDITY_REVEND_TIMESTAMP_FIELD_NAME] = "AUDIT_REVISION_END_TS"
        return jpaProps
    }
}