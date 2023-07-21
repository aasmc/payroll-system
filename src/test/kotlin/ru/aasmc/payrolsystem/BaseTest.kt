package ru.aasmc.payrolsystem

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import ru.aasmc.payrolsystem.repository.*
import ru.aasmc.payrolsystem.testutil.PayrollPostgresContainer

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BaseTest{

    @Autowired
    lateinit var webTestClient: WebTestClient
    @Autowired
    lateinit var affiliationRepo: AffiliationRepository
    @Autowired
    lateinit var employeeRepo: EmployeeRepository
    @Autowired
    lateinit var paymentTypeRepo: PaymentTypeRepository
    @Autowired
    lateinit var salesReceiptRepo: SalesReceiptRepository
    @Autowired
    lateinit var serviceChargeRepo: ServiceChargeRepository
    @Autowired
    lateinit var timeCardRepo: TimeCardRepository

    companion object {

        @JvmStatic
        @Container
        val container: PostgreSQLContainer<PayrollPostgresContainer> = PayrollPostgresContainer.getInstance()

    }

    @BeforeEach
    fun clear() {
        serviceChargeRepo.deleteAll()
        employeeRepo.deleteAll()
        timeCardRepo.deleteAll()
        salesReceiptRepo.deleteAll()
        affiliationRepo.deleteAll()
        paymentTypeRepo.deleteAll()
    }
}