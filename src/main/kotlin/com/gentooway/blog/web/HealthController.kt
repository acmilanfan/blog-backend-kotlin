package com.gentooway.blog.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Health check controller
 */
@RestController
class HealthController {

    @GetMapping("/health")
    fun health(): String {
        return "OK"
    }
}
