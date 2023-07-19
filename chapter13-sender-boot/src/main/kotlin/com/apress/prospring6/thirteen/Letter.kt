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

import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import java.io.Serial
import java.io.Serializable
import java.time.LocalDate

/**
 * Created by iuliana on 17/11/2022
 */
@Entity
class Letter() : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @NotEmpty var title: String? = null
    var sender: String? = null
    var sentOn: LocalDate? = null

    @Enumerated(EnumType.STRING)
    var category = Category.MISC

    @NotEmpty var content: String? = null

    constructor(id: Long?, title: String?, sender: String?, sentOn: LocalDate?, category: Category, content: String?) : this() {
        this.id = id
        this.title = title
        this.sender = sender
        this.sentOn = sentOn
        this.category = category
        this.content = content
    }


    override fun hashCode() = content.hashCode() + 31*(
            category.hashCode() + 31*(
                    sentOn.hashCode() + 31*(
                            sender.hashCode() + 31*(
                                    title.hashCode() + 31*id.hashCode()))))

    override fun equals(other: Any?) =
        (other is Letter)
                && (other.id == id)
                && (other.title == title)
                && (other.sender == sender)
                && (other.sentOn == sentOn)
                && (other.category == category)
                && (other.content == content)

    companion object {
        @Serial
        private val serialVersionUID = 1L
    }
}
