package com.example.jug2023.restaurant.rich.command

import com.example.jug2023.restaurant.rich.repository.OpenTableRepository
import com.example.jug2023.restaurant.rich.TableProduct
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

data class AddProductToTableCommand(private val tableId: String, private val products: List<TableProduct>) {

    @Component
    class AddProductToTableCommandHandler(
            private val openTableRepository: OpenTableRepository,
    ) {

        @Transactional
        fun handle(command: AddProductToTableCommand) {
            val openTable = openTableRepository.findById(command.tableId)
            openTable.addProducts(command.products)
            openTableRepository.save(openTable)
        }

    }

}
