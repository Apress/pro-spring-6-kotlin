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
package com.apress.prospring6.nineteen.boot.stomp

import jakarta.annotation.PostConstruct
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Controller
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Controller
open class StockController(
    private val taskScheduler: TaskScheduler,
    private val simpMessagingTemplate: SimpMessagingTemplate
) {
    private val stocks: MutableList<Stock> = mutableListOf()
    private val random = Random(System.currentTimeMillis())

    @MessageMapping("/addStock")
    @Throws(Exception::class)
    open fun addStock(stock: Stock) {
        stocks.add(stock)
        broadcastUpdatedPrices()
    }

    private fun broadcastUpdatedPrices() {
        for (stock in stocks) {
            stock.price = stock.price + updatedStockPrice * stock.price
            stock.date = LocalDateTime.now()
        }
        simpMessagingTemplate.convertAndSend("/topic/price", stocks)
    }

    private val updatedStockPrice: Double
        get() {
            var priceChange = random.nextDouble() * 5.0
            if (random.nextInt(2) == 1) {
                priceChange = -priceChange
            }
            return priceChange / 100.0
        }

    @PostConstruct
    open fun broadcastTimePeriodically() {
        with(stocks){
            add(Stock("VMW", 1.00))
            add(Stock("EMC", 1.00))
            add(Stock("GOOG", 1.00))
            add(Stock("IBM", 1.00))
        }
        taskScheduler.scheduleAtFixedRate({ broadcastUpdatedPrices() }, Duration.ofSeconds(2))
    }
}
