package com.notifyme.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
open class IndexController

    @RequestMapping("/")
    @ResponseBody
    fun showIndex(): String {
        return "dare nie?"
    }