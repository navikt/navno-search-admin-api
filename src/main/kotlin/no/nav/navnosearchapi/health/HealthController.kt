package no.nav.navnosearchapi.health

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/internal")
class HealthController {
    @GetMapping("/isAlive")
    fun isAlive(): String {
        return "Hello world"
    }

    @GetMapping("/isReady")
    fun isReady(): String {
        return "Hello world"
    }
}