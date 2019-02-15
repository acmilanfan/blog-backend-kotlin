package com.gentooway.blog.web

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Health check controller
 */
@CrossOrigin
@RestController
class HealthController {

    @GetMapping("/health")
    fun health(): String {
        return "OK"
    }
}
