package com.example.ConstructionCompanyApplication

interface APIConfiguration {
    companion object{
        val API_BASE_URL = "http://localhost:8080"
        val JSON_CONTENT_TYPE = "application/json+hal"
    }
}