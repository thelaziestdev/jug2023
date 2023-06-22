package com.example.jug2023.restaurant.rich.command

import com.example.jug2023.restaurant.rich.repository.CloseTableRepository
import com.example.jug2023.restaurant.rich.repository.OpenTableRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

data class OpenTableCommand(private val id: String) {

    @Component
    class Handler(
            private val openTableRepository: OpenTableRepository,
            private val closeTableRepository: CloseTableRepository
    ) {
        @Transactional
        fun handle(command: OpenTableCommand) {
            val closedTable = closeTableRepository.findById(command.id)
            val openTable = closedTable.open()
            openTableRepository.save(openTable)
        }
    }

}
