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
package com.apress.prospring6.twelve.spring.config

import com.apress.prospring6.twelve.spring.entities.Car
import com.apress.prospring6.twelve.spring.repos.CarRepository
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate

/**
 * Created by iuliana on 29/10/2022
 */
@Service
class DBInitializer(private val carRepository: CarRepository) {
    @PostConstruct
    fun initDB() {
        LOGGER.info("Starting database initialization...")
        var car = Car().apply {
            licensePlate = "GRAVITY-0405"
            manufacturer = "Ford"
            manufactureDate = LocalDate.of(2006, 9, 12)
        }
        carRepository.save(car)
        car = Car().apply {
            licensePlate = "CLARITY-0432"
            manufacturer = "Toyota"
            manufactureDate = LocalDate.of(2003, 9, 9)
        }
        carRepository.save(car)
        car = Car().apply {
            licensePlate = "ROSIE-0402"
            manufacturer = "Toyota"
            manufactureDate = LocalDate.of(2017, 4, 16)
        }
        carRepository.save(car)
        car = Car().apply {
            licensePlate = "HUGO-0442"
            manufacturer = "Peugeot"
            manufactureDate = LocalDate.of(2014, 6, 1)
        }
        carRepository.save(car)
        car = Car().apply {
            licensePlate = "NESSIE-0842"
            manufacturer = "Ford"
            manufactureDate = LocalDate.of(2004, 8, 17)
        }
        carRepository.save(car)
        car = Car().apply {
            licensePlate = "CALEDONIA-1983"
            manufacturer = "Ford"
            manufactureDate = LocalDate.of(2001, 10, 2)
        }
        carRepository.save(car)
        LOGGER.info("Database initialization finished.")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DBInitializer::class.java)
    }
}
