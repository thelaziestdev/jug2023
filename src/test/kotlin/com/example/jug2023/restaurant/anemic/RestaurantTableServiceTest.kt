package com.example.jug2023.restaurant.anemic

import com.github.ydespreaux.testcontainers.mysql.MySQLContainer
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest
@Testcontainers
class RestaurantTableServiceTest @Autowired constructor(
        private val tableService: TableService,
        private val tableRepository: TableRepository) {

    @BeforeEach
    fun setup() {
        tableRepository.deleteAll()
    }

    companion object {

        @Container
        @JvmStatic
        private val MY_SQL_CONTAINER = MySQLContainer("5.7")
                .withDatabaseName("integration-tests-db")
                .withUsername("sa")
                .withPassword("sa")
        @DynamicPropertySource
        @JvmStatic
        fun props(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl)
            registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername)
            registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword)
        }
    }


    @Test
    @Transactional
    fun `can open table`() {
        val table = RestaurantTableAnemic("1", "RestaurantTableEntity one")
        tableRepository.save(table)

        tableService.openTable("1")

        val tableAfterOpen = tableRepository.findById("1").get()
        assertTrue(tableAfterOpen.isOpen)
    }

    @Test
    @Transactional
    fun `cannot open already opened table`() {
        val table = RestaurantTableAnemic("1", "RestaurantTableEntity one", isOpen = true)
        tableRepository.save(table)

        assertThrows<TableNotAvailableException> { tableService.openTable("1") }
    }

    @Test
    @Transactional
    fun `can close table`() {
        val table = RestaurantTableAnemic("1", "RestaurantTableEntity one", isOpen = true)
        tableRepository.save(table)

        tableService.closeTable("1")

        val tableAfterClose = tableRepository.findById("1").get()
        assertTrue(tableAfterClose.isOpen.not())
        assertTrue(tableAfterClose.products.isEmpty())
    }

    @Test
    @Transactional
    fun `can add products to table`() {
        val table = RestaurantTableAnemic("1", "RestaurantTableEntity one")
        tableRepository.save(table)

        tableService.openTable("1")
        tableService.addProducts("1", listOf(TableProductAnemic("1", 1, 1.0f)))

        val tableAfterAddProducts = tableRepository.findById("1").get()
        assertTrue(tableAfterAddProducts.products.isNotEmpty())
    }

    @Test
    @Transactional
    fun `cannot add products to not open table`() {
        val table = RestaurantTableAnemic("1", "RestaurantTableEntity one")
        tableRepository.save(table)

        assertThrows<TableNotOpenException> { tableService.addProducts("1", listOf(TableProductAnemic("1", 1, 1.0f))) }
    }

    @Test
    @Transactional
    fun `can release products from table`() {
        val table = RestaurantTableAnemic("1", "RestaurantTableEntity one")
        tableRepository.save(table)

        tableService.openTable("1")
        tableService.addProducts("1", listOf(TableProductAnemic("1", 1, 1.0f)))
        tableService.releaseProducts("1", listOf("1"))

        val tableAfterReleaseProducts = tableRepository.findById("1").get()
        assertTrue(tableAfterReleaseProducts.products.first().isReleased)
    }

    @Test
    @Transactional
    fun `can pay table`() {
        val table = RestaurantTableAnemic("1", "RestaurantTableEntity one")
        tableRepository.save(table)

        tableService.openTable("1")
        tableService.addProducts("1", listOf(TableProductAnemic("1", 1, 1.0f)))
        tableService.releaseProducts("1", listOf("1"))
        tableService.payTable("1")

        val tableAfterPay = tableRepository.findById("1").get()
        assertTrue(tableAfterPay.isPayed)
    }

    @Test
    @Transactional
    fun `cannot pay for not released products`() {
        val table = RestaurantTableAnemic("1", "RestaurantTableEntity one")
        tableRepository.save(table)

        tableService.openTable("1")
        tableService.addProducts("1", listOf(TableProductAnemic("1", 1, 1.0f)))

        assertThrows<ProductNotReleasedException> { tableService.payTable("1") }
    }

}
