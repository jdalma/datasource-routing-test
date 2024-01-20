package com.example.datasourceroutingtest.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("ADDRESS")
data class Address(
    @Id
    private val id: Int,
    private val streetAddress: String,
    private val city: String
)
