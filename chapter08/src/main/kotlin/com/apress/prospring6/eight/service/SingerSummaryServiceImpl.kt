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
package com.apress.prospring6.eight.service

import com.apress.prospring6.eight.service.SingerSummaryService.Companion.ALL_SINGER_SUMMARY_JPQL_QUERY
import com.apress.prospring6.eight.service.SingerSummaryService.Companion.ALL_SINGER_SUMMARY_RECORD_JPQL_QUERY
import com.apress.prospring6.eight.view.SingerSummary
import com.apress.prospring6.eight.view.SingerSummaryRecord
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Tuple
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Function
import java.util.stream.Stream

/**
 * Created by iuliana.cosmina on 02/07/2022
 */
@Service("singerSummaryService")
@Repository
@Transactional(readOnly = true)
class SingerSummaryServiceImpl : SingerSummaryService {
    @PersistenceContext
    private val em: EntityManager? = null

    override fun findAll(): Stream<SingerSummary> {
        return em!!.createQuery<SingerSummary?>(
            ALL_SINGER_SUMMARY_JPQL_QUERY,
            SingerSummary::class.java
        ).resultList.stream()
    }

    override fun findAllAsRecord(): Stream<SingerSummaryRecord> {
        // old style
        /* return em.createQuery(ALL_SINGER_SUMMARY_RECORD_JPQL_QUERY).getResultList().stream().
                map(obj ->  {
                    Object[] values = (Object[]) obj;
                    return new SingerSummaryRecord((String) values[0],(String) values[1], (String) values[2]);
                });*/
        return em!!.createQuery(ALL_SINGER_SUMMARY_RECORD_JPQL_QUERY, Tuple::class.java).resultList.stream()
            .map{ tuple: Tuple ->
                    SingerSummaryRecord(
                        tuple[0, String::class.java],
                        tuple[1, String::class.java],
                        tuple[2, String::class.java]
                    )
            }
    }
}