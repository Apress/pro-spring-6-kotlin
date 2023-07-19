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
package com.apress.prospring6.twenty.boot.problem

import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import java.util.function.Consumer

/**
 * Created by iuliana on 03/04/2023
 */
class MissingValueException : RuntimeException {
	var fieldNames: Map<String, String> = mutableMapOf()
		private set

	constructor(message: String?, fieldNames: Map<String, String>) : super(message) {
		this.fieldNames = fieldNames
	}

	constructor(message: String?, cause: Throwable?, fieldNames: Map<String, String>) : super(message, cause) {
		this.fieldNames = fieldNames
	}

	companion object {
		fun of(errors: List<ObjectError>): MissingValueException {
			val fields: List<String> = ArrayList()
			val fieldNames = HashMap<String, String>()
			errors.forEach(Consumer { err: ObjectError ->
				fieldNames[(err as FieldError).field] = err.getDefaultMessage()!!
			})
			return MissingValueException("Some values are missing!", fieldNames)
		}
	}
}
