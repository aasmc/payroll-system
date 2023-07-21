package ru.aasmc.payrolsystem.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.aasmc.payrolsystem.model.TimeCard

interface TimeCardRepository: JpaRepository<TimeCard, Long> {
}