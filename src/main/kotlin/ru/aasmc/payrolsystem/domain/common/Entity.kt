package ru.aasmc.payrolsystem.domain.common

import java.io.Serializable

interface Entity<T, ID: Serializable>: PersistableObject<ID> {
}