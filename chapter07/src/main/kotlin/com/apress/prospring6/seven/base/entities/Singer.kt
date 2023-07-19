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
package com.apress.prospring6.seven.base.entities

import jakarta.persistence.*
import java.io.Serial
import java.time.LocalDate

/**
 * Created by iuliana.cosmina on 4/22/17.
 */
@Entity
@Table(name = "SINGER")
@NamedQueries(
	NamedQuery(
		name = "Singer.findById",
		query = """
      				select distinct s from Singer s 
					left join fetch s.albums a 
					left join fetch s.instruments i 
					where s.id = :id
					"""
	),
	NamedQuery(
		name = "Singer.findAllWithAlbum",
		query = """
      				select distinct s from Singer s 
					left join fetch s.albums a 
					left join fetch s.instruments i 
					"""
	)
)
@NamedStoredProcedureQuery(
	name = "getFirstNameByIdProc",
	procedureName = "getFirstNameByIdProc",
	parameters = [StoredProcedureParameter(
		name = "in_id",
		type = Long::class,
		mode = ParameterMode.IN
	), StoredProcedureParameter(name = "fn_res", type = String::class, mode = ParameterMode.OUT)]
)
class Singer : AbstractEntity() {
	@get:Column(name = "FIRST_NAME")
	var firstName: String? = null

	@get:Column(name = "LAST_NAME")
	var lastName: String? = null

	@get:Column(name = "BIRTH_DATE")
	var birthDate: LocalDate? = null

	@get:OneToMany(
		mappedBy = "singer",
		cascade = [CascadeType.ALL],
		orphanRemoval = true
	)
	var albums: MutableSet<Album> = HashSet()

	@get:JoinTable(
		name = "SINGER_INSTRUMENT",
		joinColumns = [JoinColumn(name = "SINGER_ID")],
		inverseJoinColumns = [JoinColumn(name = "INSTRUMENT_ID")]
	)
	@get:ManyToMany
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

	override fun toString(): String {
		return ("Singer - Id: " + id + ", First name: " + firstName
				+ ", Last name: " + lastName + ", Birthday: " + birthDate)
	}

	companion object {
		private val serialVersionUID = 2L
	}
}