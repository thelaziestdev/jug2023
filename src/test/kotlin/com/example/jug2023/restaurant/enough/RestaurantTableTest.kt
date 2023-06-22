package com.example.jug2023.restaurant.enough

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RestaurantTableTest {

    @Test
    fun `can open table`() {
        val closedTable = RestaurantTable("1", "Table 1")

        closedTable.open()

        assertTrue(closedTable.isOpen)
    }

    @Test
    fun `cannot open already opened table`() {
        val openTable = RestaurantTable("1", "Table 1", isOpen = true)

        assertThrows(TableOpenException::class.java) {
            openTable.open()
        }
    }

    @Test
    fun `can close payed table`() {
        val payedTable = RestaurantTable("1", "Table 1", isOpen = true, isPayed = true)

        payedTable.close();

        assertFalse(payedTable.isOpen)
    }

    @Test
    fun `cannot close not payed table`() {
        val openTable = RestaurantTable("1", "Table 1", isOpen = true, isPayed = false)

        assertThrows(TableNotPayedException::class.java) {
            openTable.close()
        }
    }

    @Test
    fun `cannot close already closed table`() {
        val closedTable = RestaurantTable("1", "Table 1", isOpen = false)

        assertThrows(TableClosedException::class.java) {
            closedTable.close()
        }
    }

    @Test
    fun `can add products to table`() {
        val product = TableProduct("1", 1, 1.0f)
        val openTable = RestaurantTable("1", "Table 1", isOpen = true, isPayed = false)

        openTable.addProducts(listOf(product))

        assertTrue(openTable.products.contains(product))
    }

    @Test
    fun `cannot add products to closed table`() {
        val product = TableProduct("1", 1, 1.0f)
        val closedTable = RestaurantTable("1", "Table 1", isOpen = false)

        assertThrows(TableClosedException::class.java) {
            closedTable.addProducts(listOf(product))
        }
    }

    @Test
    fun `cannot add products to payed table`() {
        val product = TableProduct("1", 1, 1.0f)
        val payedTable = RestaurantTable("1", "Table 1", isOpen = true, isPayed = true)

        assertThrows(TablePayedException::class.java) {
            payedTable.addProducts(listOf(product))
        }
    }

    @Test
    fun `can release products from table`() {
        val product = TableProduct("1", 1, 1.0f)
        val openTable = RestaurantTable("1", "Table 1", products = mutableListOf(product))

        openTable.fireProduct(product.id)

        assertTrue(openTable.products.filter { it.isReleased }.contains(product))
    }


}
