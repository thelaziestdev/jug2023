package com.example.jug2023.restaurant.rich.repository

import com.example.jug2023.restaurant.anemic.TableNotFoundException
import com.example.jug2023.restaurant.rich.ClosedTable
import com.example.jug2023.restaurant.rich.OpenTable
import com.example.jug2023.restaurant.rich.PayedTable
import com.example.jug2023.restaurant.rich.TableProduct
import com.example.jug2023.restaurant.rich.persistence.RestaurantTableEntityRich
import com.example.jug2023.restaurant.rich.persistence.RestaurantTableEntityRepository
import com.example.jug2023.restaurant.rich.persistence.TableProductEntityRich
import org.springframework.stereotype.Component

@Component
class OpenTableRepository(
    private val restaurantTableEntityRepository: RestaurantTableEntityRepository,
) {

    fun findById(id: String): OpenTable {
        val table = restaurantTableEntityRepository.findById(id).orElseThrow { TableNotFoundException(id) }
        if (table.isOpen) {
            return OpenTable(
                id = table.id,
                name = table.name,
                products = table.products.filter { it.isReleased.not() }.map { TableProduct(it.id, it.quantity, it.price) }.toMutableList(),
                firedProducts = table.products.filter { it.isReleased }.map { TableProduct(it.id, it.quantity, it.price) }.toMutableList()
            )
        } else {
            throw TableNotFoundException(id)
        }

    }

    fun save(table: OpenTable) {
        restaurantTableEntityRepository.save(table.toEntity())
    }

    fun OpenTable.toEntity(): RestaurantTableEntityRich {
        val entity = RestaurantTableEntityRich(id, name, isOpen = true)
        entity.products.addAll(products.map { TableProductEntityRich(it.id, it.quantity, it.price) })
        entity.products.addAll(firedProducts.map { TableProductEntityRich(it.id, it.quantity, it.price, true) })
        return entity
    }

}

@Component
class CloseTableRepository(
    private val restaurantTableEntityRepository: RestaurantTableEntityRepository,
) {

    fun findById(id: String): ClosedTable {
        val table = restaurantTableEntityRepository.findById(id).orElseThrow { TableNotFoundException(id) }
        if (!table.isOpen) {
            return ClosedTable(table.id, table.name)
        } else {
            throw TableNotFoundException(id)
        }

    }

    fun save(table: ClosedTable) {
        restaurantTableEntityRepository.save(table.toEntity())
    }

    fun ClosedTable.toEntity(): RestaurantTableEntityRich {
        return RestaurantTableEntityRich(id, name, isOpen = false, isPayed = false)
    }

}

@Component
class PayedTableRepository(
    private val restaurantTableEntityRepository: RestaurantTableEntityRepository,
) {

    fun findById(id: String): PayedTable {
        val table = restaurantTableEntityRepository.findById(id).orElseThrow { TableNotFoundException(id) }
        if (table.isPayed) {
            return PayedTable(
                table.id,
                table.name,
                table.products.map { TableProduct(it.id, it.quantity, it.price) }.toMutableList()
            )
        } else {
            throw TableNotFoundException(id)
        }

    }

    fun save(table: PayedTable) {
        restaurantTableEntityRepository.save(table.toEntity())
    }

    fun PayedTable.toEntity(): RestaurantTableEntityRich {
        val entity = RestaurantTableEntityRich(id, name, isOpen = true, isPayed = true)
        entity.products.addAll(products.map { TableProductEntityRich(it.id, it.quantity, it.price) })
        return entity
    }

}
