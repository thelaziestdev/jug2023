package com.example.jug2023.restaurant.enough

import jakarta.persistence.*

@Entity
class RestaurantTable(
        @Id
        val id: String,
        val name: String,
        var isPayed: Boolean = true,
        var isOpen: Boolean = false,
        @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
        @JoinColumn
        val products: MutableList<TableProduct> = mutableListOf()
) {

    fun addProducts(products: List<TableProduct>) {
        if (isOpen.not()) {
            throw TableClosedException()
        }
        if (isPayed) {
            throw TablePayedException()
        }
        this.products.addAll(products)
    }

    fun fireProduct(productId: String) {
        products.find { it.id == productId }?.let {
            it.isReleased = true
        }
    }

    fun pay() {
        if (isPayed) {
            throw TablePayedException()
        }
        if (products.any { it.isReleased.not() }) {
            throw ProductNotFiredException()
        }
        isPayed = true
    }

    fun close() {
        if (!isOpen) {
            throw TableClosedException()
        }
        if (!isPayed) {
            throw TableNotPayedException()
        }
        isOpen = false
        products.clear()
    }

    fun open() {
        if (isOpen) {
            throw TableOpenException()
        }
        isOpen = true
        isPayed = false
    }

}

class TableOpenException : RuntimeException("Table is already open")
class TableClosedException : RuntimeException("Table is already closed")
class TablePayedException : RuntimeException("Table is already payed")
class TableNotPayedException : RuntimeException("Table is not payed")
class ProductNotFiredException : RuntimeException("Product is not fired to the kitchen")



