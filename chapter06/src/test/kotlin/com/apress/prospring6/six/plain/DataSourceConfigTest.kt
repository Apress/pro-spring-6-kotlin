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
package com.apress.prospring6.six.plain

import com.apress.prospring6.six.config.BasicDataSourceCfg
import com.apress.prospring6.six.config.EmbeddedJdbcConfig
import com.apress.prospring6.six.config.SimpleDataSourceCfg
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.sql.SQLException
import javax.sql.DataSource

/**
 * Created by iuliana.cosmina on 05/05/2022
 */
class DataSourceConfigTest {
    //@Disabled("needs MariaDB running, set up container, comment this to run")
    @Test
    @Throws(
        SQLException::class
    )
    fun testSimpleDataSource() {
        val ctx = AnnotationConfigApplicationContext(
            SimpleDataSourceCfg::class.java
        )
        val dataSource = ctx.getBean("dataSource", DataSource::class.java)
        Assertions.assertNotNull(dataSource)
        testDataSource(dataSource)
        ctx.close()
    }

    //@Disabled("needs MariaDB running, set up container, comment this to run")
    @Test
    @Throws(
        SQLException::class
    )
    fun testBasicDataSource() {
        val ctx = AnnotationConfigApplicationContext(
            BasicDataSourceCfg::class.java
        )
        val dataSource = ctx.getBean("dataSource", DataSource::class.java)
        Assertions.assertNotNull(dataSource)
        testDataSource(dataSource)
        ctx.close()
    }

    @Test
    @Throws(SQLException::class)
    fun testEmbeddedDataSource() {
        val ctx = AnnotationConfigApplicationContext(
            EmbeddedJdbcConfig::class.java
        )
        val dataSource = ctx.getBean("dataSource", DataSource::class.java)
        Assertions.assertNotNull(dataSource)
        testDataSource(dataSource)
        ctx.close()
    }

    //@Disabled("needs MariaDB running, set up container, comment this to run")
    @Test
    @Throws(
        SQLException::class
    )
    fun testSpringJdbc() {
        val ctx = AnnotationConfigApplicationContext(
            SpringDatasourceCfg::class.java
        )
        val dataSource = ctx.getBean("dataSource", DataSource::class.java)
        Assertions.assertNotNull(dataSource)
        testDataSource(dataSource)
        val singerDao = ctx.getBean(
            "singerDao",
            SingerDao::class.java
        )
        Assertions.assertEquals("John Mayer", singerDao.findNameById(1L))
        ctx.close()
    }

    @Throws(SQLException::class)
    private fun testDataSource(dataSource: DataSource) {
        try {
            dataSource.connection.use { connection ->
                connection.prepareStatement("SELECT 1").use { statement ->
                    statement.executeQuery().use { resultSet ->
                        while (resultSet.next()) {
                            val mockVal = resultSet.getInt("1")
                            Assertions.assertEquals(1, mockVal)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            LOGGER.debug("Something unexpected happened.", e)
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DataSourceConfigTest::class.java)
    }
}