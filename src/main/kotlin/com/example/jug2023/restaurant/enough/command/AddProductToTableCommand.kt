package com.example.jug2023.restaurant.enough.command

import com.example.jug2023.restaurant.enough.RestaurantTableRepository
import com.example.jug2023.restaurant.enough.TableProduct
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

data class AddProductToTableCommand(private val tableId: String, private val products: List<TableProduct>) {

    @Component
    class Handler(
            private val restaurantTableRepository: RestaurantTableRepository,
    ) {

        @Transactional
        fun handle(command: AddProductToTableCommand) {
            val table = restaurantTableRepository.findById(command.tableId).orElseThrow()
            table.addProducts(command.products)
            restaurantTableRepository.save(table)
        }

    }

}
