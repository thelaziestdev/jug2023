package com.example.jug2023.restaurant.rich.command

import com.example.jug2023.restaurant.rich.TableProduct
import com.example.jug2023.restaurant.rich.persistence.RestaurantTableEntity
import com.example.jug2023.restaurant.rich.persistence.RestaurantTableEntityRepository
import com.example.jug2023.restaurant.rich.persistence.TableProductEntity
import com.github.ydespreaux.testcontainers.mysql.MySQLContainer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class TableCommandsTest @Autowired constructor(
        private val tableRepository: RestaurantTableEntityRepository,
        private val openTableCommandHandler: OpenTableCommand.Handler,
        private val closeTableCommandHandler: CloseTableCommand.Handler,
        private val addProductToTableCommandHandler: AddProductToTableCommand.Handler,
        private val payTableCommandHandler: PayTableCommand.Handler,
        private val releaseProductsCommandHandler: ReleaseProductsCommand.Handler,
) {

    @BeforeEach
    fun setup() {
        tableRepository.deleteAll()
        tableRepository.flush()
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
    fun `can open table`() {
        val table = RestaurantTableEntity("1", "RestaurantTableEntity one")
        tableRepository.save(table)

        openTableCommandHandler.handle(OpenTableCommand("1"))

        val tableAfterOpen = tableRepository.findById("1").get()
        assertTrue(tableAfterOpen.isOpen)
    }

    @Test
    fun `can close table`() {
        val table = RestaurantTableEntity("1", "RestaurantTableEntity one", isOpen = true)
        tableRepository.save(table)

        closeTableCommandHandler.handle(CloseTableCommand("1"))

        val tableAfterClose = tableRepository.findById("1").get()
        assertFalse(tableAfterClose.isOpen)
    }

    @Test
    fun `can add product to table`() {
        val table = RestaurantTableEntity("1", "RestaurantTableEntity one", isOpen = true)
        tableRepository.save(table)

        addProductToTableCommandHandler.handle(AddProductToTableCommand("1", listOf(TableProduct("1", 1, 1f))))

        val tableAfterAddProduct = tableRepository.findById("1").get()
        assertEquals(1, tableAfterAddProduct.products.size)
    }

    @Test
    fun `can release products from table`() {
        val table = RestaurantTableEntity("1", "RestaurantTableEntity one", isOpen = true)
        table.products.add(TableProductEntity("1", 1, 1f))
        tableRepository.save(table)

        releaseProductsCommandHandler.handle(ReleaseProductsCommand("1", listOf("1")))

        val tableAfterReleaseProducts = tableRepository.findById("1").get()
        assertEquals(1, tableAfterReleaseProducts.products.filter { it.isReleased }.size)
    }

    @Test
    fun `can pay table`() {
        val table = RestaurantTableEntity("1", "RestaurantTableEntity one", isOpen = true)
        table.products.add(TableProductEntity("1", 1, 1f))
        tableRepository.save(table)

        payTableCommandHandler.handle(PayTableCommand("1"))

        val tableAfterPay = tableRepository.findById("1").get()
        assertTrue(tableAfterPay.isPayed)
    }

}
