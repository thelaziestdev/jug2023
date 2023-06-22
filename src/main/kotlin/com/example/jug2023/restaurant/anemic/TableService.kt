package com.example.jug2023.restaurant.anemic

import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
class TableService(private val tableRepository: TableRepository) {

    @Transactional
    fun openTable(tableId: String) {
        val table = tableRepository.findById(tableId).orElseThrow { TableNotFoundException(tableId) }
        if(!table.isOpen) {
            table.isOpen = true
        } else {
            throw TableNotAvailableException(tableId)
        }
        tableRepository.save(table)
    }

    @Transactional
    fun addProducts(tableId: String, products: List<TableProduct>) {
            val table = tableRepository.findById(tableId).orElseThrow { TableNotFoundException(tableId) }
            if(table.isOpen) {
                table.products.addAll(products)
            } else {
                throw TableNotOpenException(tableId)
            }
            tableRepository.save(table)
    }

    @Transactional
    fun releaseProducts(tableId: String, productsIds: List<String>) {
        val table = tableRepository.findById(tableId).orElseThrow { TableNotFoundException(tableId) }
        productsIds.forEach { productId ->
            table.products.find { it.id == productId }?.isReleased = true
        }
        tableRepository.save(table)
    }

    @Transactional
    fun payTable(tableId: String) {
        val table = tableRepository.findById(tableId).orElseThrow { TableNotFoundException(tableId) }
        table.products.forEach { if(it.isReleased.not()) throw ProductNotReleasedException(it.id) }
        table.isPayed = true
        tableRepository.save(table)
    }

    @Transactional
    fun closeTable(tableId: String) {
        val table = tableRepository.findById(tableId).orElseThrow { TableNotFoundException(tableId) }
        table.isOpen = false
        table.products.clear()
        tableRepository.save(table)
    }

}

class ProductNotReleasedException(id: String) : RuntimeException("Product with id $id was not released!")
class TableNotOpenException(tableId: String) : RuntimeException("RestaurantTableEntity with id $tableId is not open!")
class TableNotAvailableException(tableId: String) : RuntimeException("RestaurantTableEntity with id $tableId is not available!")
class TableNotFoundException(tableId: String): RuntimeException("RestaurantTableEntity with id $tableId was not found!")
