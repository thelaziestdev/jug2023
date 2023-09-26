package com.example.jug2023.restaurant.rich.command

import com.example.jug2023.restaurant.rich.repository.OpenTableRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

data class ReleaseProductsCommand(private val tableId: String, private val productsIds: List<String>) {

    @Component
    class ReleaseProductsCommandHandler(
            private val openTableRepository: OpenTableRepository,
    ) {

        @Transactional
        fun handle(command: ReleaseProductsCommand) {
            val openTable = openTableRepository.findById(command.tableId)
            command.productsIds.forEach(openTable::fireProduct)
            openTableRepository.save(openTable)
        }
    }
}
