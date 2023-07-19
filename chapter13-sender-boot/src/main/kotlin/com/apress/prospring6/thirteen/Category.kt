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
package com.apress.prospring6.thirteen

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.io.IOException
import java.util.*

/**
 * Created by iuliana on 17/11/2022
 */
@JsonSerialize(using = Category.CategorySerializer::class)
@JsonDeserialize(using = Category.CategoryDeserializer::class)
enum class Category(val namex:String) {
    PERSONAL("Personal"),
    FORMAL("Formal"),
    MISC("Miscellaneous");

    class CategorySerializer : JsonSerializer<Category>() {
        @Throws(IOException::class)
        override fun serialize(enumValue: Category, gen: JsonGenerator, serializer: SerializerProvider) {
            gen.writeString(enumValue.namex)
        }
    }

    class CategoryDeserializer : JsonDeserializer<Category>() {
        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(parser: JsonParser, context: DeserializationContext): Category? {
            val jsonValue = parser.text
            return eventOf(jsonValue)
        }
    }

    companion object {
        fun eventOf(value: String): Category? {
            val result = values().firstOrNull { m: Category ->
                m.namex.equals(value, ignoreCase = true)
            }
            return result
        }
    }
}
