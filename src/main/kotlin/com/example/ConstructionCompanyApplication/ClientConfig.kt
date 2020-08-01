package com.example.ConstructionCompanyApplication

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


/**
 * @author Greg Turnquist
 */
@Configuration
class ClientConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}