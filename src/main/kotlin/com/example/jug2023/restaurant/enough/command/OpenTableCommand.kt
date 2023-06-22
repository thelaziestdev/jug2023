package com.example.jug2023.restaurant.enough.command

import com.example.jug2023.restaurant.enough.RestaurantTableRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

data class OpenTableCommand(private val id: String) {

    @Component
    class Handler(
            private val restaurantTableRepository: RestaurantTableRepository,
    ) {
        @Transactional
        fun handle(command: OpenTableCommand) {
            val table = restaurantTableRepository.findById(command.id).orElseThrow()
            table.open()
            restaurantTableRepository.save(table)
        }
    }

}
