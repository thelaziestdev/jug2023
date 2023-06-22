package com.example.jug2023.restaurant.enough.command

import com.example.jug2023.restaurant.enough.RestaurantTableRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

data class ReleaseProductsCommand(private val tableId: String, private val productsIds: List<String>) {

    @Component
    class Handler(
            private val restaurantTableRepository: RestaurantTableRepository,
    ) {

        @Transactional
        fun handle(command: ReleaseProductsCommand) {
            val table = restaurantTableRepository.findById(command.tableId).orElseThrow()
            command.productsIds.forEach(table::fireProduct)
            restaurantTableRepository.save(table)
        }
    }
}
