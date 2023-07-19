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
package com.apress.prospring6.fifteen.entities

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.io.Serial
import java.time.LocalDate

/**
 * Created by iuliana.cosmina on 4/22/17.
 */
@Entity
@Table(name = "SINGER")
class Singer {
	@JsonIgnore // do not serialize
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	var id: Long? = null

	@JsonIgnore // do not serialize
	@Version
	@Column(name = "VERSION")
	var version = 0

	@Column(name = "FIRST_NAME")
	var firstName: @NotEmpty @Size(min = 2, max = 30) String? = null

	@Column(name = "LAST_NAME")
	var lastName: @NotEmpty @Size(min = 2, max = 30) String? = null

	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "BIRTH_DATE")
	var birthDate: LocalDate? = null

	companion object {
		@Serial
		private val serialVersionUID = 2L
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Singer

		if (id != other.id) return false
		if (firstName != other.firstName) return false
		if (lastName != other.lastName) return false
		return birthDate == other.birthDate
	}

	override fun hashCode(): Int {
		var result = id?.hashCode() ?: 0
		result = 31 * result + (firstName?.hashCode() ?: 0)
		result = 31 * result + (lastName?.hashCode() ?: 0)
		result = 31 * result + (birthDate?.hashCode() ?: 0)
		return result
	}

	override fun toString(): String {
		return "Singer(id=$id, version=$version, firstName=$firstName, lastName=$lastName, birthDate=$birthDate)"
	}
}
