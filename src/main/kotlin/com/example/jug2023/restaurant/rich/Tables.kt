package com.example.jug2023.restaurant.rich


abstract class Table(
        val id: String,
        val name: String)

class OpenTable(
        id: String,
        name: String,
        val products: MutableList<TableProduct> = mutableListOf(),
        val firedProducts: MutableList<TableProduct> = mutableListOf()
) : Table(id, name) {
    fun addProducts(products: List<TableProduct>) {
        this.products.addAll(products)
    }
    fun fireProduct(id: String) {
        val product = products.first { it.id == id }
        products.remove(product)
        firedProducts.add(product)
    }

    fun pay() = PayedTable(id, name, firedProducts)

}

class PayedTable(
        id: String,
        name: String,
        val products: MutableList<TableProduct>
) : Table(id, name) {

    fun close() = ClosedTable(id, name)

}

class ClosedTable(
        id: String,
        name: String,
) : Table(id, name) {

    fun open() = OpenTable(id, name)

}

data class TableProduct(
        val id: String,
        val quantity: Int,
        val price: Float)
