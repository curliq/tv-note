package com.free.tvtracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TvtrackerSpringApplication

fun main(args: Array<String>) {
    runApplication<TvtrackerSpringApplication>(*args)
}
