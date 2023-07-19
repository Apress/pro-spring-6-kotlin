/*
Freeware License, some rights reserved

Copyright (c) 2019 Iuliana Cosmina

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
package com.apress.prospring6.fourteen

import jakarta.servlet.Filter
import jakarta.servlet.MultipartConfigElement
import jakarta.servlet.ServletRegistration
import org.springframework.web.filter.CharacterEncodingFilter
import org.springframework.web.filter.HiddenHttpMethodFilter
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer
import java.nio.charset.StandardCharsets

/**
 * @author Iuliana Cosmina
 * @since 1.0
 */
class WebInitializer : AbstractAnnotationConfigDispatcherServletInitializer() {
    override fun getRootConfigClasses(): Array<Class<*>> {
        return arrayOf(BasicDataSourceCfg::class.java, TransactionCfg::class.java)
    }

    override fun getServletConfigClasses(): Array<Class<*>> {
        return arrayOf(WebConfig::class.java)
    }

    override fun getServletMappings(): Array<String> {
        return arrayOf("/")
    }

    override fun getServletFilters(): Array<Filter> {
        val cef = CharacterEncodingFilter().apply {
            encoding = StandardCharsets.UTF_8.name()
            setForceEncoding(true)
        }
        return arrayOf(HiddenHttpMethodFilter(), cef)
    }

    public override fun customizeRegistration(registration: ServletRegistration.Dynamic) {
        registration.setInitParameter("throwExceptionIfNoHandlerFound", "true")
        registration.setMultipartConfig(multipartConfigElement) // <=> <multipart-config>
        super.customizeRegistration(registration)
    }

    private val multipartConfigElement: MultipartConfigElement
        get() = MultipartConfigElement(null, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD)

    companion object {
        private const val MAX_FILE_SIZE: Long = 5000000

        // Beyond that size spring will throw exception.
        private const val MAX_REQUEST_SIZE: Long = 5000000

        // Size threshold after which files will be written to disk
        private const val FILE_SIZE_THRESHOLD = 0
    }
}
