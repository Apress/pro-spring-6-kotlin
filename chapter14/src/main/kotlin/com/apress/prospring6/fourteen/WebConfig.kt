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
package com.apress.prospring6.fourteen

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.http.HttpStatus
import org.springframework.ui.context.support.ResourceBundleThemeSource
import org.springframework.validation.Validator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.multipart.support.StandardServletMultipartResolver
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.config.annotation.*
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.mvc.WebContentInterceptor
import org.springframework.web.servlet.theme.CookieThemeResolver
import org.springframework.web.servlet.theme.ThemeChangeInterceptor
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.spring6.view.ThymeleafViewResolver
import org.thymeleaf.templatemode.TemplateMode
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.*


/**
 * Created by iuliana on 25/12/2022
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = ["com.apress.prospring6.fourteen"])
open class WebConfig : WebMvcConfigurer, ApplicationContextAware {
    private var applicationContext: ApplicationContext? = null

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    @Bean(name = [DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME])
    open fun multipartResolver() = StandardServletMultipartResolver()

    @Bean
    open fun templateResolver() =
        SpringResourceTemplateResolver().apply {
            setApplicationContext(applicationContext!!)
            prefix = "/WEB-INF/views/"
            suffix = ".html"
            templateMode = TemplateMode.HTML
            isCacheable = false
        }
    @Bean
    @Description("Thymeleaf Template Engine")
    open fun templateEngine() = SpringTemplateEngine().apply {
            addDialect(Java8TimeDialect())
            setTemplateResolver(templateResolver())
            setTemplateEngineMessageSource(messageSource())
            enableSpringELCompiler = true
        }

    @Bean
    @Description("Thymeleaf View Resolver")
    open fun viewResolver() = ThymeleafViewResolver().apply {
            templateEngine = templateEngine()
            order = 1
        }

    @Bean
    open fun themeSource() = ResourceBundleThemeSource()

    @Bean
    open fun validator(): Validator = LocalValidatorFactoryBean().apply {
        setValidationMessageSource(messageSource())
    }

    override fun getValidator() = validator()

    //Declare our static resources. I added cache to the java config but it?s not required.
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        super.addResourceHandlers(registry)
        registry.addResourceHandler("/images/**", "/styles/**")
            .addResourceLocations("/images/", "/styles/")
    }

    /*override fun configureDefaultServletHandling(configurer:DefaultServletHandlerConfigurer) {
        configurer.enable();
    }*/

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addRedirectViewController("/", "/home")
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        with(registry){
            addInterceptor(localeChangeInterceptor()).addPathPatterns("/*")
            addInterceptor(themeChangeInterceptor())
            addInterceptor(webChangeInterceptor())
        }
    }

    @Bean
    open fun messageSource(): MessageSource
        = ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:i18n/global")
            setDefaultEncoding(StandardCharsets.UTF_8.name())
            setUseCodeAsDefaultMessage(true)
            setFallbackToSystemLocale(true)
            // # -1 : never reload, 0 always reload
            //setCacheSeconds(0);
        }

    @Bean
    open fun localeChangeInterceptor() = LocaleChangeInterceptor().apply {
            paramName = "lang"
        }

    @Bean
    open fun themeChangeInterceptor() = ThemeChangeInterceptor().apply {
            paramName = "theme"
        }

    @Bean
    open fun localeResolver() = CookieLocaleResolver("locale").apply {
            setDefaultLocale(Locale.ENGLISH)
            setCookieMaxAge(Duration.ofHours(1))
        }

    @Bean
    open fun themeResolver() = CookieThemeResolver().apply {
            defaultThemeName = "green"
            cookieMaxAge = 3600
            cookieName = "theme"
        }

    @Bean
    open fun webChangeInterceptor() = WebContentInterceptor().apply {
            cacheSeconds = 0
            setSupportedMethods("GET", "POST", "PUT", "DELETE")
        }
}
