package com.example.jug2023.restaurant.anemic

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class TableProductAnemic(
        @Id
        val id: String,
        val quantity: Int,
        val price: Float,
        var isReleased: Boolean= false)
