package com.example.jug2023.restaurant.rich.command

import com.example.jug2023.restaurant.rich.repository.OpenTableRepository
import com.example.jug2023.restaurant.rich.repository.PayedTableRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

class PayTableCommand(val id: String) {
    @Component
    class Handler(
            private val openTableRepository: OpenTableRepository,
            private val payedTableRepository: PayedTableRepository
    ) {

        @Transactional
        fun handle(command: PayTableCommand) {
            val openTable = openTableRepository.findById(command.id)
            val payedTable = openTable.pay()
            payedTableRepository.save(payedTable)
        }
    }
}
