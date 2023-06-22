package com.example.jug2023.restaurant.rich.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantTableEntityRepository: JpaRepository<RestaurantTableEntity, String>
