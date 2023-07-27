package ru.aasmc.payrolsystem.domain.common

import java.io.Serializable

interface PersistableObject<ID: Serializable> {

    fun getId(): ID

}