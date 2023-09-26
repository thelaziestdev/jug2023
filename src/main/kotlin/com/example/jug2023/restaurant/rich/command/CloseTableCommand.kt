package com.example.jug2023.restaurant.rich.command

import com.example.jug2023.restaurant.rich.repository.CloseTableRepository
import com.example.jug2023.restaurant.rich.repository.PayedTableRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

data class CloseTableCommand(private val id: String) {

    @Component
    class CloseTableCommandHandler(
            private val payedTableRepository: PayedTableRepository,
            private val closeTableRepository: CloseTableRepository
    ) {

        @Transactional
        fun handle(command: CloseTableCommand) {
            val payedTable = payedTableRepository.findById(command.id)
            val closedTable = payedTable.close()
            closeTableRepository.save(closedTable)
        }

    }

}
