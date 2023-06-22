package com.example.jug2023.restaurant.enough

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantTableRepository: JpaRepository<RestaurantTable, String>
