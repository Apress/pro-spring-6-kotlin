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
package com.apress.prospring6.seventeen.boot

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.mvc.WebContentInterceptor
import org.springframework.web.servlet.theme.CookieThemeResolver
import org.springframework.web.servlet.theme.ThemeChangeInterceptor
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Created by iuliana on 28/12/2022
 */
@Configuration
@ComponentScan(basePackages = ["com.apress.prospring6.seventeen.boot"])
open class WebConfig : WebMvcConfigurer {
    @Bean
    open fun messageSource(): MessageSource {
        val messageResource = ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:i18n/global")
            setDefaultEncoding(StandardCharsets.UTF_8.name())
            setUseCodeAsDefaultMessage(true)
            setFallbackToSystemLocale(true)
            // # -1 : never reload, 0 always reload
            //setCacheSeconds(0);
        }
        return messageResource
    }

    @Bean
    open fun localeChangeInterceptor() =
        LocaleChangeInterceptor().apply {
            paramName = "lang"
        }

    @Bean
    open fun themeChangeInterceptor() =
        ThemeChangeInterceptor().apply {
            paramName = "theme"
        }

    @Bean
    open fun localeResolver() =
        CookieLocaleResolver().apply {
            setDefaultLocale(Locale.ENGLISH)
            setCookieMaxAge(3600)
            setCookieName("locale")
        }

    @Bean
    open fun themeResolver() =
        CookieThemeResolver().apply {
            defaultThemeName = "green"
            cookieMaxAge = 3600
            cookieName = "theme"
        }

    @Bean
    open fun webChangeInterceptor() =
        WebContentInterceptor().apply {
            cacheSeconds = 0
            setSupportedMethods("GET", "POST", "PUT", "DELETE")
        }

    override fun addInterceptors(registry: InterceptorRegistry) {
        with(registry) {
            addInterceptor(localeChangeInterceptor()).addPathPatterns("/*")
            addInterceptor(themeChangeInterceptor())
            addInterceptor(webChangeInterceptor())
        }
    }
}
