package ru.aasmc.payrolsystem.testutil

import org.testcontainers.containers.PostgreSQLContainer

private const val IMAGE_VERSION = "postgres:15.3"

class PayrollPostgresContainer private constructor(): PostgreSQLContainer<PayrollPostgresContainer>(
        IMAGE_VERSION
) {

    companion object {
        private var container: PayrollPostgresContainer? = null

        fun getInstance(): PayrollPostgresContainer {
            if (container == null) {
                container = PayrollPostgresContainer()
            }
            return container!!
        }
    }

    override fun start() {
        super.start()
        System.setProperty("DB_URL", this.jdbcUrl);
        System.setProperty("DB_USERNAME", this.username);
        System.setProperty("DB_PASSWORD", this.password);
    }

    override fun stop() {
        // do nothing, JVM handles shut down
    }

}