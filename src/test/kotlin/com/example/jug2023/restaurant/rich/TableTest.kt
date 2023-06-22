package com.example.jug2023.restaurant.rich

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TableTest {

    @Test
    fun `can open table`() {
        val closedTable = ClosedTable("1", "Table 1")

        closedTable.open()
    }

    @Test
    fun `can close payed table`() {
        val payedTable = PayedTable("1", "Table 1", mutableListOf(TableProduct("1", 1, 1.0f)))

        payedTable.close();
    }

    @Test
    fun `can add products to table`() {
        val product = TableProduct("1", 1, 1.0f)
        val openTable = OpenTable("1", "Table 1")

        openTable.addProducts(listOf(product))

        assertTrue(openTable.products.contains(product))
    }

    @Test
    fun `can release products from table`() {
        val product = TableProduct("1", 1, 1.0f)
        val openTable = OpenTable("1", "Table 1", mutableListOf(product))

        openTable.fireProduct(product.id)

        assertTrue(openTable.products.isEmpty())
        assertTrue(openTable.firedProducts.contains(product))
    }

    @Test
    fun `can pay table`() {
        val product = TableProduct("1", 1, 1.0f)
        val openTable = OpenTable("1", "Table 1", mutableListOf(product))

        openTable.fireProduct(product.id)
        val payedTable = openTable.pay()

        assertTrue(payedTable.products.contains(product))
    }

}
