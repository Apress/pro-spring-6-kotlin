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
package com.apress.prospring6.seventeen.controllers.boot

import io.restassured.RestAssured
import io.restassured.authentication.FormAuthConfig
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus

/**
 * Created by iuliana on 12/03/2023
 */
@Disabled("Start the Chapter17Application application and then enable this test")
class Chapter17ApplicationTest {
    @BeforeEach
    fun setUp() {
        RestAssured.port = 8081
        RestAssured.baseURI = "http://localhost"
    }

    @Test
    fun johnShouldNotSeeTheDeleteButton() {
        val cfg = FormAuthConfig("/auth", "user", "pass")
            .withLoggingEnabled()
        val responseStr = RestAssured.given()
            .contentType(ContentType.URLENC)
            .auth().form("john", "doe", cfg)
            .`when`()["/singer/1"]
            .then()
            .assertThat().statusCode(HttpStatus.OK.value())
            .extract().body().asString()
        Assertions.assertAll(
            Executable {
                Assertions.assertTrue(
                    responseStr.contains(
                        "<div class=\"card-header\">Singer Details</div>"
                    )
                )
            },
            Executable {
                Assertions.assertTrue(
                    responseStr.contains(
                        "<td>Mayer</td>"
                    )
                )
            },
            Executable {
                Assertions.assertFalse(
                    responseStr.contains(
                        "Delete"
                    )
                )
            }
        )
    }

    @Test
    fun johnShouldNotBeAllowedToDeleteSinger() {
        val cfg = FormAuthConfig("/auth", "user", "pass")
            .withLoggingEnabled()
        val responseStr = RestAssured.given()
            .contentType(ContentType.URLENC)
            .auth().form("john", "doe", cfg)
            .`when`().delete("/singer/1")
            .then()
            .assertThat().statusCode(HttpStatus.FORBIDDEN.value())
            .extract().body().asString()
    }

    @Test
    fun adminShouldSeeTheDeleteButton() {
        val cfg = FormAuthConfig("/auth", "user", "pass")
            .withLoggingEnabled()
        val responseStr = RestAssured.given()
            .contentType(ContentType.URLENC)
            .auth().form("admin", "admin", cfg)
            .`when`()["/singer/1"]
            .then()
            .assertThat().statusCode(HttpStatus.OK.value())
            .extract().body().asString()
        Assertions.assertAll(
            Executable {
                Assertions.assertTrue(
                    responseStr.contains(
                        "<div class=\"card-header\">Singer Details</div>"
                    )
                )
            },
            Executable {
                Assertions.assertTrue(
                    responseStr.contains(
                        "<td>Mayer</td>"
                    )
                )
            },
            Executable {
                Assertions.assertTrue(
                    responseStr.contains(
                        "Delete"
                    )
                )
            }
        )
    }
}
