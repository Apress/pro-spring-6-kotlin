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
package com.apress.prospring6.eleven

import com.apress.prospring6.eleven.domain.Address
import com.apress.prospring6.eleven.domain.Blogger
import com.apress.prospring6.eleven.domain.BloggerWithAddress
import com.apress.prospring6.eleven.formatter.FormattingServiceCfg
import com.apress.prospring6.eleven.validator.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.ObjectError
import org.springframework.validation.ValidationUtils
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDate
import java.util.function.Consumer

/**
 * Created by iuliana on 28/08/2022
 */
class SpringValidatorTest {
    @Test
    @Throws(MalformedURLException::class)
    fun testSimpleBloggerValidator() {
        AnnotationConfigApplicationContext(
            AppConfig::class.java,
            FormattingServiceCfg::class.java,
            SimpleBloggerValidator::class.java
        ).use { ctx ->
            val blogger = Blogger("", "Pedala", LocalDate.of(2000, 1, 1), URL("https://none.co.uk"))
            val blogger2 =
                Blogger(null, "Pedala", LocalDate.of(2000, 1, 1), URL("https://none.co.uk"))
            val bloggerValidator =
                ctx.getBean(SimpleBloggerValidator::class.java)
            val result =
                BeanPropertyBindingResult(blogger, "blogger")
            ValidationUtils.invokeValidator(bloggerValidator, blogger, result)
            val errors = result.allErrors
            Assertions.assertEquals(1, errors.size)
            errors.forEach(Consumer { e: ObjectError ->
                LOGGER.info(
                    "Object '{}' failed validation. Error code: {}",
                    e.objectName,
                    e.code
                )
            })
            // ----------------'null' passes the validation---------------------
            val result2 =
                BeanPropertyBindingResult(blogger2, "blogger2")
            ValidationUtils.invokeValidator(bloggerValidator, blogger2, result)
            val errors2 = result2.allErrors
            Assertions.assertEquals(0, errors2.size)
        }
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testBloggerValidator() {
        AnnotationConfigApplicationContext(
            AppConfig::class.java,
            FormattingServiceCfg::class.java,
            BloggerValidator::class.java
        ).use { ctx ->
            val blogger = Blogger(null, "Pedala", LocalDate.of(2000, 1, 1), URL("https://none.co.uk"))
            val bloggerValidator = ctx.getBean(BloggerValidator::class.java)
            val result =
                BeanPropertyBindingResult(blogger, "blogger")
            ValidationUtils.invokeValidator(bloggerValidator, blogger, result)
            val errors = result.allErrors
            Assertions.assertEquals(1, errors.size)
            errors.forEach(Consumer { e: ObjectError ->
                LOGGER.info(
                    "Error Code: {}",
                    e.code
                )
            })
        }
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testComplexBloggerValidator() {
        AnnotationConfigApplicationContext(
            AppConfig::class.java,
            FormattingServiceCfg::class.java,
            ComplexBloggerValidator::class.java
        ).use { ctx ->
            val blogger = Blogger(null, null, LocalDate.of(1973, 1, 1), URL("https://none.co.uk"))
            val bloggerValidator =
                ctx.getBean(ComplexBloggerValidator::class.java)
            val result =
                BeanPropertyBindingResult(blogger, "blogger")
            ValidationUtils.invokeValidator(bloggerValidator, blogger, result)
            val errors = result.allErrors
            Assertions.assertEquals(3, errors.size)
            errors.forEach(Consumer { e: ObjectError ->
                LOGGER.info(
                    "Error Code: {}",
                    e.code
                )
            })
        }
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testBloggerWithAddressValidator() {
        AnnotationConfigApplicationContext(
            AppConfig::class.java,
            FormattingServiceCfg::class.java,
            AddressValidator::class.java,
            BloggerWithAddressValidator::class.java
        ).use { ctx ->
            val address =
                Address("221B", "UK")
            val blogger =
                BloggerWithAddress(null, "Mazzie", LocalDate.of(1973, 1, 1), null, address)
            val bloggerValidator = ctx.getBean(
                BloggerWithAddressValidator::class.java
            )
            val result =
                BeanPropertyBindingResult(blogger, "blogger")
            ValidationUtils.invokeValidator(bloggerValidator, blogger, result)
            val errors = result.allErrors
            Assertions.assertEquals(2, errors.size)
            errors.forEach(Consumer { e: ObjectError ->
                LOGGER.info(
                    "Error Code: {}",
                    e.code
                )
            })
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SpringValidatorTest::class.java)
    }
}