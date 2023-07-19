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
package com.apress.prospring6.eleven.formatter.factory

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.Formatter
import org.springframework.format.support.FormattingConversionServiceFactoryBean
import org.springframework.stereotype.Service
import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by iuliana on 26/08/2022
 */
@Service("conversionService")
class ApplicationConversionServiceFactoryBean : FormattingConversionServiceFactoryBean() {
    private var dateTimeFormatter: DateTimeFormatter? = null

    @set:Autowired(required = false)
    var datePattern = DEFAULT_DATE_PATTERN

    private val formatters: MutableSet<Formatter<*>> = mutableSetOf()

    @PostConstruct
    fun init() {
        dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern)
        formatters.add(getDateTimeFormatter())
        setFormatters(formatters)
    }

    private fun getDateTimeFormatter(): Formatter<LocalDate> {
        return object : Formatter<LocalDate> {
            @Throws(ParseException::class)
            override fun parse(source: String, locale: Locale): LocalDate {
                LOGGER.info("Parsing date string: $source")
                return LocalDate.parse(source, dateTimeFormatter)
            }

            override fun print(source: LocalDate, locale: Locale): String {
                LOGGER.info("Formatting datetime: $source")
                return source.format(dateTimeFormatter)
            }
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(
            ApplicationConversionServiceFactoryBean::class.java
        )
        private const val DEFAULT_DATE_PATTERN = "yyyy-MM-dd"
    }
}