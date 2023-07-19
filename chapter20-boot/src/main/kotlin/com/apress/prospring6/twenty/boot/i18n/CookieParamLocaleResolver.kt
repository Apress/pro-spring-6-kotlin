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
package com.apress.prospring6.twenty.boot.i18n

import org.springframework.context.i18n.LocaleContext
import org.springframework.context.i18n.SimpleLocaleContext
import org.springframework.http.ResponseCookie
import org.springframework.util.CollectionUtils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.i18n.LocaleContextResolver
import java.time.Duration
import java.util.*

/**
 * Created by Iuliana Cosmina on 22/07/2020
 * Description: an implementation of LocaleContextResolver that stores the desired locale in cookie with a lifespan of five minutes.
 */
class CookieParamLocaleResolver(private val languageParameterName: String) : LocaleContextResolver {
	override fun resolveLocaleContext(exchange: ServerWebExchange): LocaleContext {
		var defaultLocale: Locale? = getLocaleFromCookie(exchange)
		val referLang = exchange.request.queryParams[languageParameterName]
		if (!CollectionUtils.isEmpty(referLang)) {
			val lang = referLang!![0]
			defaultLocale = Locale.forLanguageTag(lang)
			setLocaleToCookie(lang, exchange)
		}
		return SimpleLocaleContext(defaultLocale)
	}

	private fun setLocaleToCookie(languageValue: String, exchange: ServerWebExchange) {
		val cookies = exchange.request.cookies
		val langCookie = cookies.getFirst(LOCALE_REQUEST_ATTRIBUTE_NAME)
		if (langCookie == null || languageValue != langCookie.value) {
			val cookie =
				ResponseCookie.from(LOCALE_REQUEST_ATTRIBUTE_NAME, languageValue).maxAge(Duration.ofMinutes(5)).build()
			exchange.response.addCookie(cookie)
		}
	}

	private fun getLocaleFromCookie(exchange: ServerWebExchange): Locale {
		val cookies = exchange.request.cookies
		val langCookie = cookies.getFirst(LOCALE_REQUEST_ATTRIBUTE_NAME)
		return if (langCookie != null) Locale.forLanguageTag(langCookie.value) else Locale.getDefault()
	}

	override fun setLocaleContext(exchange: ServerWebExchange, localeContext: LocaleContext?) {
		throw UnsupportedOperationException("Not Supported")
	}

	companion object {
		const val LOCALE_REQUEST_ATTRIBUTE_NAME = "Bookstore.Cookie.LOCALE"
	}
}
