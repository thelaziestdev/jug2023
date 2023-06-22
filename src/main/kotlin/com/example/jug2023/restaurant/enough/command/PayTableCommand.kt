package com.example.jug2023.restaurant.enough.command

import com.example.jug2023.restaurant.enough.RestaurantTableRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

class PayTableCommand(val id: String) {
    @Component
    class Handler(
            private val restaurantTableRepository: RestaurantTableRepository
    ) {

        @Transactional
        fun handle(command: PayTableCommand) {
            val table = restaurantTableRepository.findById(command.id).orElseThrow()
            table.pay()
            restaurantTableRepository.save(table)
        }
    }
}
