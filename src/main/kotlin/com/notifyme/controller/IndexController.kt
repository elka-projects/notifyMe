package com.notifyme.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable

@RestController
open class IndexController {

    @GetMapping("/")
    fun helloWorld() = helloUser("World")

    @GetMapping("/{user}")
    fun helloUser(@PathVariable("user") user: String) = "Hello $user!"

    @GetMapping("/show/message")
    fun showMessage(): String {
        return "notifyMe"
    }

}