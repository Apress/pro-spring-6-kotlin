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
package com.apress.prospring6.seven.crud.entities

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.io.Serial
import java.io.Serializable

/**
 * Created by iuliana.cosmina on 4/23/17.
 */
@MappedSuperclass
abstract class AbstractEntity : Serializable {
	/**
	 * Returns the entity identifier. This identifier is unique per entity. It is used by persistence frameworks used in a project,
	 * and although is public, it should not be used by application code.
	 * This identifier is mapped by ORM (Object Relational Mapper) to the database primary key of the Person record to which
	 * the entity instance is mapped.
	 *
	 * @return the unique entity identifier
	 */
	/**
	 * Sets the entity identifier. This identifier is unique per entity.  Is is used by persistence frameworks
	 * and although is public, it should never be set by application code.
	 *
	 * @param id the unique entity identifier
	 */
	@Id // Starting with version 5.x Hibernate will create a new table to maintain Sequence,
	// It now selects the GenerationType.TABLE which uses a database table to generate primary keys.
	// This approach requires a lot of database queries and pessimistic locks to generate unique values.
	//@GeneratedValue(strategy = GenerationType.AUTO) // <= this requires a table called 'hibernate_sequence'
	@GenericGenerator(name = "native_generator", strategy = "native")
	@GeneratedValue(generator = "native_generator")
	@Column(updatable = false)
	var id: Long? = null

	@Version
	@Column(name = "VERSION")
	var version = 0

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as AbstractEntity
		return id == that.id
	}

	override fun hashCode(): Int {
		return if (id != null) id.hashCode() else 0
	}

	companion object {
		@Serial
		private val serialVersionUID = 1L
	}
}