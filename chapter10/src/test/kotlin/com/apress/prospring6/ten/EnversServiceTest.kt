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
package com.apress.prospring6.ten

import com.apress.prospring6.ten.config.AuditCfg
import com.apress.prospring6.ten.config.EnversConfig
import com.apress.prospring6.ten.entities.SingerAudit
import com.apress.prospring6.ten.service.SingerAuditService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate

/**
 * Created by iuliana.cosmina on 06/08/2022
 */
@Testcontainers
@Sql("classpath:testcontainers/audit/drop-schema.sql", "classpath:testcontainers/audit/create-schema.sql")
@SpringJUnitConfig(classes = [EnversConfig::class, AuditCfg::class])
class EnversServiceTest : TestContainersBase() {
    @Autowired
    var auditService: SingerAuditService? = null

    @BeforeEach
    fun setUp() {
        val singer = SingerAudit()
        singer.firstName = "BB"
        singer.lastName = "King"
        singer.birthDate = LocalDate.of(1940, 8, 16)
        auditService!!.save(singer)
    }

    @Test
    fun testFindAuditByRevision() {
        // update to create new version
        val singer = auditService!!.findAll().findFirst().orElse(null)
        Assertions.assertNotNull(singer)
        singer.firstName = "Riley B."
        auditService!!.save(singer)
        val oldSinger = auditService!!.findAuditByRevision(singer.id!!, 1)
        Assertions.assertEquals("BB", oldSinger!!.firstName)
        LOGGER.info(">> old singer: {} ", oldSinger)
        val newSinger = auditService!!.findAuditByRevision(singer.id!!, 2)
        Assertions.assertEquals("Riley B.", newSinger!!.firstName)
        LOGGER.info(">> updated singer: {} ", newSinger)
    }

    @Test
    fun testFindAuditAfterDeletion() {
        // delete record
        val singer = auditService!!.findAll().findFirst().orElse(null)!!
        auditService!!.delete(singer.id!!)

        // extract from audit
        val deletedSinger = auditService!!.findAuditByRevision(singer.id!!, 1)
        Assertions.assertEquals("BB", deletedSinger!!.firstName)
        LOGGER.info(">> deleted singer: {} ", deletedSinger)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EnversServiceTest::class.java)
    }
}