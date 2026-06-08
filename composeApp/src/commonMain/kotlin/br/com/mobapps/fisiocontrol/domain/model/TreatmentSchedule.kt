package br.com.mobapps.fisiocontrol.domain.model

import kotlinx.datetime.LocalDate

data class TreatmentSchedule(
    val id: String,
    val playerId: String,
    val title: String,
    val weeklyPlanning: String?,
    val startDate: LocalDate,
    val status: ScheduleStatus,
    val weeklyAssessment: String?,
    val sessionsPerWeek: Int,
    val updatedAt: String
)

enum class ScheduleStatus(val label: String) {
    ACTIVE("Ativo"),
    COMPLETED("Concluído"),
    PAUSED("Pausado")
}