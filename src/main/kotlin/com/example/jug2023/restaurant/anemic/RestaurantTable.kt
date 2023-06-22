package com.example.jug2023.restaurant.anemic

import jakarta.persistence.*

@Entity
class RestaurantTable(
        @Id
        val id: String,
        val name: String,
        var isPayed: Boolean = true,
        var isOpen: Boolean = false,
) {

        @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
        @JoinColumn
        val products: MutableList<TableProduct> = mutableListOf()
}


