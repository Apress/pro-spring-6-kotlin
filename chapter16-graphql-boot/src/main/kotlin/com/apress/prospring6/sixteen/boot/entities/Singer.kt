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
package com.apress.prospring6.sixteen.boot.entities

import jakarta.persistence.*
import org.springframework.format.annotation.DateTimeFormat
import java.io.Serial
import java.io.Serializable
import java.time.LocalDate

/**
 * Created by iuliana.cosmina on 4/22/17.
 */
@Entity
@Table(name = "SINGER")
class Singer() : Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	var id: Long? = null

	@Version
	@Column(name = "VERSION")
	var version = 0

	@Column(name = "FIRST_NAME")
	var firstName: String? = null

	@Column(name = "LAST_NAME")
	var lastName: String? = null

	@Column(name = "PSEUDONYM")
	var pseudonym: String? = null

	@Column(name = "GENRE")
	var genre: String? = null

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "BIRTH_DATE")
	var birthDate: LocalDate? = null

	@OneToMany(mappedBy = "singer")
	var awards: Set<Award>? = null

	@ManyToMany
	@JoinTable(
		name = "SINGER_INSTRUMENT",
		joinColumns = [JoinColumn(name = "SINGER_ID")],
		inverseJoinColumns = [JoinColumn(name = "INSTRUMENT_ID")]
	)
	var instruments: Set<Instrument>? = null

	constructor(
		id: Long?,
		version: Int,
		firstName: String?,
		lastName: String?,
		pseudonym: String?,
		genre: String?,
		birthDate: LocalDate?,
		awards: Set<Award>?,
		instruments: Set<Instrument>?
	) : this() {
		this.id = id
		this.version = version
		this.firstName = firstName
		this.lastName = lastName
		this.pseudonym = pseudonym
		this.genre = genre
		this.birthDate = birthDate
		this.awards = awards
		this.instruments = instruments
	}

	companion object {
		@Serial
		private val serialVersionUID = 1L
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Singer

		return id == other.id
	}

	override fun hashCode(): Int {
		return id?.hashCode() ?: 0
	}

}
