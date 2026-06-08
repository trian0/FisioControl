package br.com.mobapps.fisiocontrol.domain.model

import kotlinx.datetime.LocalDate

data class Player(
    val id: String,
    val fullName: String,
    val birthDate: LocalDate?,
    val position: String?,
    val team: String?,
    val phone: String?,
    val photoUrl: String?,
    val notes: String?,
    val isActive: Boolean,
    val updatedAt: String
)
