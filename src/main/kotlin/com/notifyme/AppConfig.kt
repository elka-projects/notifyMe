package com.notifyme

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@EnableAutoConfiguration
@ComponentScan
open class AppConfig
    fun main(args : Array<String>) {
        SpringApplication.run(AppConfig::class.java, *args)
    }
