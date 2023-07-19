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
package com.apress.prospring6.ten.service

import com.apress.prospring6.ten.entities.SingerAudit
import com.apress.prospring6.ten.repos.SingerAuditRepository
import org.hibernate.envers.AuditReaderFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Stream
import java.util.stream.StreamSupport

/**
 * Created by iuliana.cosmina on 04/08/2022
 */
@Service("singerAuditService")
@Transactional
class SingerAuditServiceImpl(private val singerAuditRepository: SingerAuditRepository) : SingerAuditService {
    @Transactional(readOnly = true)
    override fun findAll(): Stream<SingerAudit> {
        return StreamSupport.stream(singerAuditRepository.findAll().spliterator(),
            false)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): SingerAudit? {
        return singerAuditRepository.findById(id).orElse(null)
    }

    override fun save(singer: SingerAudit): SingerAudit {
        return singerAuditRepository.saveAndFlush(singer)
    }

    override fun delete(id: Long) {
        singerAuditRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun findAuditByRevision(id: Long, revision: Int): SingerAudit? {
        //var auditReader = AuditReaderFactory.get(entityManager);
        //return auditReader.find(SingerAudit.class, id, revision);
        return singerAuditRepository.findAuditByIdAndRevision(id, revision)
    }
}