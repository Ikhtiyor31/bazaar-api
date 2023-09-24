package com.strawberry.bazaarapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
@SpringBootApplication
@EnableAsync
class BazaarApiApplication

fun main(args: Array<String>) {
    runApplication<BazaarApiApplication>(*args)
}

