package com.example.jug2023.restaurant.enough

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class TableProduct(
        @Id
        val id: String,
        val quantity: Int,
        val price: Float,
        var isReleased: Boolean= false)
