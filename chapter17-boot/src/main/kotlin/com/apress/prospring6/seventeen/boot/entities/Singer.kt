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
package com.apress.prospring6.seventeen.boot.entities

import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.io.Serial
import java.time.LocalDate
import java.util.*

/**
 * Created by iuliana.cosmina on 4/22/17.
 */
@Entity
@Table(name = "SINGER")
class Singer : AbstractEntity() {
	@Column(name = "FIRST_NAME")
	@NotEmpty @Size(min = 2, max = 30)var firstName: String? = null

	@Column(name = "LAST_NAME")
	@NotEmpty @Size(min = 2, max = 30) var lastName: String? = null

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "BIRTH_DATE")
	var birthDate: LocalDate? = null

	@OneToMany(mappedBy = "singer")
	var albums: MutableSet<Album> = mutableSetOf()

	@Basic(fetch = FetchType.LAZY)
	@Lob
	@Column(name = "PHOTO")
	var photo: ByteArray? = null

	@ManyToMany
	@JoinTable(
		name = "SINGER_INSTRUMENT",
		joinColumns = [JoinColumn(name = "SINGER_ID")],
		inverseJoinColumns = [JoinColumn(name = "INSTRUMENT_ID")]
	)
	var instruments: MutableSet<Instrument> = mutableSetOf()

	fun addAlbum(album: Album): Boolean {
		album.singer = this
		return albums.add(album)
	}

	fun removeAlbum(album: Album) {
		albums.remove(album)
	}

	fun addInstrument(instrument: Instrument): Boolean {
		return instruments.add(instrument)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val singer = other as Singer
		return if (id != null) {
			id == other.id
		} else firstName == singer.firstName && lastName == singer.lastName
	}

	override fun hashCode(): Int {
		return Objects.hash(firstName, lastName)
	}

	override fun toString(): String {
		return ("Singer - Id: " + id + ", First name: " + firstName
				+ ", Last name: " + lastName + ", Birthday: " + birthDate)
	}

	companion object {
		@Serial
		private val serialVersionUID = 2L
	}
}
