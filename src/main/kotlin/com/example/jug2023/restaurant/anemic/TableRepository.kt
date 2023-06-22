package com.example.jug2023.restaurant.anemic

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TableRepository: JpaRepository<RestaurantTable, String>
