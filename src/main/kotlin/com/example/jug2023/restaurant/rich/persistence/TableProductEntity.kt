package com.example.jug2023.restaurant.rich.persistence

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class TableProductEntity(
        @Id
        val id: String,
        val quantity: Int,
        val price: Float,
        var isReleased: Boolean= false)
