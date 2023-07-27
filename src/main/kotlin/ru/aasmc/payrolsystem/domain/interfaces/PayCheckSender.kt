package ru.aasmc.payrolsystem.domain.interfaces

import ru.aasmc.payrolsystem.domain.vo.PayCheck

interface PayCheckSender {
    fun sendPayCheck(payCheck: PayCheck)
}