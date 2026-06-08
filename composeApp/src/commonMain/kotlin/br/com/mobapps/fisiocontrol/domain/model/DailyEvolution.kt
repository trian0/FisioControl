package br.com.mobapps.fisiocontrol.domain.model

import kotlinx.datetime.LocalDate

data class DailyEvolution(
    val id: String,
    val playerId: String,
    val scheduleId: String?,
    val sessionDate: LocalDate,
    val painScale: Int?,
    val physiotherapyProcedures: String?,
    val objectiveNote: String?,
    val createdAt: String
)