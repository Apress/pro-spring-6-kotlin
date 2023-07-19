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
package com.apress.prospring6.eight.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup
import javax.sql.DataSource

/**
 * Created by iuliana.cosmina on 02/07/2022
 * Equivalent with
 * {@snippet :
 * * <beans ...>
 * *     <jee:jndi-lookup id="prospring6Emf" * jndi-name="persistence/prospring6PersistenceUnit"></jee:jndi-lookup>
 * * </beans>
 * * }
 * Just a code sample showing multiple versions of configuring this. Won't work without an Apache Tomcat or other server actually defining the JNDI DataSource.
 */
@Configuration
open class JndiDataSourceCfg {
    @Bean
    open fun dataSource(): DataSource {
        val dsLookup = JndiDataSourceLookup()
        dsLookup.isResourceRef = true
        return dsLookup.getDataSource("persistence/prospring6PersistenceUnit")
    } /* @Bean
    public JndiObjectFactoryBean jndiFactory() throws NamingException {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setResourceRef(true);
        jndiObjectFactoryBean.setJndiName("persistence/prospring6PersistenceUnit");
        jndiObjectFactoryBean.setProxyInterface(DataSource.class);
        jndiObjectFactoryBean.afterPropertiesSet();
        return jndiObjectFactoryBean;
    }*/
    /* @Bean
    public DataSource jndiFactory() throws NamingException {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setResourceRef(true);
        jndiObjectFactoryBean.setJndiName("persistence/prospring6PersistenceUnit");
        jndiObjectFactoryBean.setProxyInterface(DataSource.class);
        jndiObjectFactoryBean.afterPropertiesSet();
        return (DataSource) jndiObjectFactoryBean.getObject();
    }*/
}