package ru.aasmc.payrolsystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PayrolSystemApplication

fun main(args: Array<String>) {
    runApplication<PayrolSystemApplication>(*args)
}
