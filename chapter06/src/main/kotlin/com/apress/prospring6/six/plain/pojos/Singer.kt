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
package com.apress.prospring6.six.plain.pojos

import java.io.Serializable
import java.time.LocalDate

/**
 * Created by iuliana.cosmina on 03/05/2022
 */
class Singer() : Serializable {
    var id: Long? = null
    var firstName: String? = null
    var lastName: String? = null
    var birthDate: LocalDate? = null
    var albums: MutableSet<Album>? = null

    constructor(id: Long?, firstName: String?, lastName: String?, birthDate: LocalDate?, albums: MutableSet<Album>?) : this() {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.birthDate = birthDate
        this.albums = albums
    }

    fun addAlbum(album: Album): Boolean {
        if (albums == null) {
            albums = mutableSetOf(album)
            return true
        } else {
            if (albums!!.contains(album)) {
                return false
            }
        }
        albums!!.add(album)
        return true
    }

    override fun toString(): String {
        return "Singer[id=" + id +
                ",firstName=" + firstName +
                ",lastName=" + lastName +
                ",birthDate=" + birthDate +
                "]"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}