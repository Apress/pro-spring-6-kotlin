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
package com.apress.prospring6.eleven.validator

import com.apress.prospring6.eleven.domain.Address
import com.apress.prospring6.eleven.domain.BloggerWithAddress
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator

/**
 * Created by iuliana on 28/08/2022
 */
@Component("bloggerWithAddressValidator")
class BloggerWithAddressValidator(addressValidator: Validator) :
    Validator {
    private val addressValidator: Validator

    init {
        require(addressValidator.supports(Address::class.java)) {
            "The supplied [Validator] must " +
                    "support the validation of [Address] instances."
        }
        this.addressValidator = addressValidator
    }

    override fun supports(clazz: Class<*>): Boolean {
        return BloggerWithAddress::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "address.required")
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalSite", "personalSite.required")
        val b = target as BloggerWithAddress
        if (StringUtils.isEmpty(b.firstName) && StringUtils.isEmpty(b.lastName)) {
            errors.rejectValue("firstName", "firstNameOrLastName.required")
            errors.rejectValue("lastName", "firstNameOrLastName.required")
        }
        try {
            errors.pushNestedPath("address")
            ValidationUtils.invokeValidator(addressValidator, b.address, errors)
        } finally {
            errors.popNestedPath()
        }
    }
}