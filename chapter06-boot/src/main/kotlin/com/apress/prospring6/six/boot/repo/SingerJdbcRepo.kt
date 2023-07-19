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
package com.apress.prospring6.six.boot.repo

import com.apress.prospring6.six.boot.records.Singer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SqlParameter
import org.springframework.jdbc.`object`.SqlFunction
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Types
import java.util.*
import java.util.stream.Stream
import javax.sql.DataSource

/**
 * Created by iuliana.cosmina on 11/05/2022
 */
@Repository("singerRepo")
open class SingerJdbcRepo : SingerRepo {
    private var jdbcTemplate: JdbcTemplate? = null
    private var storedFunctionFirstNameById: StoredFunctionFirstNameById? = null

    @Autowired
    open fun setJdbcTemplate(jdbcTemplate: JdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate
        storedFunctionFirstNameById = StoredFunctionFirstNameById(jdbcTemplate.dataSource)
    }

    override fun findAll(): Stream<Singer> {
        return jdbcTemplate!!.queryForStream(
            ALL_SELECT
        ) { rs: ResultSet, rowNum: Int ->
            Singer(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDate("birth_date"),
                mutableListOf()
            )
        }
    }

    override fun findFirstNameById(id: Long): String? {
        return storedFunctionFirstNameById!!.execute(id)[0]
    }

    internal class StoredFunctionFirstNameById(dataSource: DataSource?) :
        SqlFunction<String?>(dataSource!!, SQL_CALL) {
        init {
            declareParameter(SqlParameter(Types.INTEGER))
            compile()
        }

        companion object {
            private const val SQL_CALL = "select getfirstnamebyid(?)"
        }
    }

    companion object {
        const val ALL_SELECT = "select * from SINGER"
    }
}