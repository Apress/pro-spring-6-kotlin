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
package com.apress.prospring6.eleven.domain

import com.apress.prospring6.eleven.validator.CheckCountrySinger
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * Created by iuliana on 28/08/2022
 */
@CheckCountrySinger
class Singer {
    @NotNull @Size(min = 2, max = 60)
    var firstName: String? = null
    var lastName: String? = null
    @NotNull
    var genre: Genre? = null
    @NotNull
    var gender: Gender? = null
    val isCountrySinger: Boolean
        get() = genre == Genre.COUNTRY

    override fun toString(): String {
        return "Singer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", genre=" + genre +
                ", gender=" + gender +
                '}'
    }

    enum class Genre(private val code: String) {
        POP("P"), JAZZ("J"), BLUES("B"), COUNTRY("C");

        override fun toString() = this.code
    }

    enum class Gender(private val code: String) {
        MALE("M"), FEMALE("F"), UNSPECIFIED("U");

        override fun toString() = this.code
    }
}