package com.example.jug2023.restaurant.rich.persistence

import jakarta.persistence.*

@Entity
class RestaurantTableEntityRich(
        @Id
        val id: String,
        val name: String,
        var isPayed: Boolean = true,
        var isOpen: Boolean = false,
) {

        @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
        @JoinColumn
        val products: MutableList<TableProductEntityRich> = mutableListOf()

}

