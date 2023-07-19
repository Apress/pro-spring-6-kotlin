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
package com.apress.prospring6.eighteen.audit

import jakarta.annotation.PostConstruct
import org.hibernate.SessionFactory
import org.hibernate.stat.CollectionStatistics
import org.hibernate.stat.EntityStatistics
import org.hibernate.stat.QueryStatistics
import org.hibernate.stat.Statistics
import org.springframework.jmx.export.annotation.ManagedAttribute
import org.springframework.jmx.export.annotation.ManagedOperation
import org.springframework.jmx.export.annotation.ManagedOperationParameter
import org.springframework.jmx.export.annotation.ManagedResource
import org.springframework.stereotype.Component

/**
 * Created by iuliana.cosmina on 7/9/17.
 * Description: Custom implementation of https://github.com/manuelbernhardt/hibernate-core/blob/master/hibernate-core/src/main/java/org/hibernate/jmx/StatisticsService.java
 * that is not part of Hibernate 6.
 */
@Component
@ManagedResource(description = "JMX managed resource", objectName = "jmxDemo:name=ProSpring6SingerApp-hibernate")
class CustomHibernateStatistics(private val sessionFactory: SessionFactory) {
	private var stats: Statistics? = null
	@PostConstruct
	private fun init() {
		stats = sessionFactory.statistics
	}

	@ManagedOperation(description = "Get statistics for entity name")
	@ManagedOperationParameter(name = "entityName", description = "Full class name for the entity")
	fun getEntityStatistics(entityName: String): EntityStatistics {
		return stats!!.getEntityStatistics(entityName)
	}

	@ManagedOperation(description = "Get statistics for role")
	@ManagedOperationParameter(name = "role", description = "Role name")
	fun getCollectionStatistics(role: String): CollectionStatistics {
		return stats!!.getCollectionStatistics(role)
	}

	@ManagedOperation(description = "Get statistics for query")
	@ManagedOperationParameter(name = "hql", description = "Query name")
	fun getQueryStatistics(hql: String): QueryStatistics {
		return stats!!.getQueryStatistics(hql)
	}

	@get:ManagedAttribute
	val entityDeleteCount: Long
		get() = stats!!.entityDeleteCount

	@get:ManagedAttribute
	val entityInsertCount: Long
		get() = stats!!.entityInsertCount

	@get:ManagedAttribute
	val entityLoadCount: Long
		get() = stats!!.entityLoadCount

	@get:ManagedAttribute
	val entityFetchCount: Long
		get() = stats!!.entityFetchCount

	@get:ManagedAttribute
	val entityUpdateCount: Long
		get() = stats!!.entityUpdateCount

	@get:ManagedAttribute
	val queryExecutionCount: Long
		get() = stats!!.queryExecutionCount

	@get:ManagedAttribute
	val queryCacheHitCount: Long
		get() = stats!!.queryCacheHitCount

	@get:ManagedAttribute
	val queryExecutionMaxTime: Long
		get() = stats!!.queryExecutionMaxTime

	@get:ManagedAttribute
	val queryCacheMissCount: Long
		get() = stats!!.queryCacheMissCount

	@get:ManagedAttribute
	val queryCachePutCount: Long
		get() = stats!!.queryCachePutCount

	@get:ManagedAttribute
	val flushCount: Long
		get() = stats!!.flushCount

	@get:ManagedAttribute
	val connectCount: Long
		get() = stats!!.connectCount

	@get:ManagedAttribute
	val secondLevelCacheHitCount: Long
		get() = stats!!.secondLevelCacheHitCount

	@get:ManagedAttribute
	val secondLevelCacheMissCount: Long
		get() = stats!!.secondLevelCacheMissCount

	@get:ManagedAttribute
	val secondLevelCachePutCount: Long
		get() = stats!!.secondLevelCachePutCount

	@get:ManagedAttribute
	val sessionCloseCount: Long
		get() = stats!!.sessionCloseCount

	@get:ManagedAttribute
	val sessionOpenCount: Long
		get() = stats!!.sessionOpenCount

	@get:ManagedAttribute
	val collectionLoadCount: Long
		get() = stats!!.collectionLoadCount

	@get:ManagedAttribute
	val collectionFetchCount: Long
		get() = stats!!.collectionFetchCount

	@get:ManagedAttribute
	val collectionUpdateCount: Long
		get() = stats!!.collectionUpdateCount

	@get:ManagedAttribute
	val collectionRemoveCount: Long
		get() = stats!!.collectionRemoveCount

	@get:ManagedAttribute
	val collectionRecreateCount: Long
		get() = stats!!.collectionRecreateCount

	@get:ManagedAttribute
	val startTime: Long
		get() = stats!!.startTime

	@get:ManagedAttribute
	val isStatisticsEnabled: Boolean
		get() = stats!!.isStatisticsEnabled

	@get:ManagedAttribute
	val entityNames: Array<String>
		get() = stats!!.entityNames

	@get:ManagedAttribute
	val queries: Array<String>
		get() = stats!!.queries

	@get:ManagedAttribute
	val successfulTransactionCount: Long
		get() = stats!!.successfulTransactionCount

	@get:ManagedAttribute
	val transactionCount: Long
		get() = stats!!.transactionCount

	@get:ManagedAttribute
	val prepareStatementCount: Long
		get() = stats!!.prepareStatementCount

	@get:ManagedAttribute
	val queryExecutionMaxTimeQueryString: String
		get() = stats!!.queryExecutionMaxTimeQueryString
}