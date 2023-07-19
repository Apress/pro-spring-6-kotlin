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
package com.apress.prospring6.eight.boot.entities

import jakarta.persistence.*
import java.io.Serial
import java.time.LocalDate
import java.util.*

/**
 * Created by iuliana.cosmina on 4/21/17.
 */
@Entity
@Table(name = "ALBUM")
class Album : AbstractEntity() {
	@get:Column
	var title: String? = null

	@get:Column(name = "RELEASE_DATE")
	var releaseDate: LocalDate? = null
	private var singer: Singer? = null
	@ManyToOne
	@JoinColumn(name = "SINGER_ID")
	fun getSinger(): Singer? {
		return singer
	}

	fun setSinger(singer: Singer?) {
		this.singer = singer
	}

	override fun equals(o: Any?): Boolean {
		if (this === o) return true
		if (o == null || javaClass != o.javaClass) return false
		val album = o as Album
		return if (id != null) {
			id == o.id
		} else title == album.title && releaseDate == album.releaseDate
	}

	override fun hashCode(): Int {
		return Objects.hash(title, releaseDate)
	}

	override fun toString(): String {
		return "Album - Id: " + id + ", Singer id: " + singer!!.id + ", Title: " + title + ", Release Date: " + releaseDate
	}

	companion object {
		@Serial
		private val serialVersionUID = 3L
	}
}