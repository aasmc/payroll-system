package ru.aasmc.payrolsystem.errors

import java.lang.RuntimeException

class PayrollServiceError(
        val code: Int,
        message: String
): RuntimeException(message)