package br.com.mobapps.fisiocontrol.data.remote.dto

import br.com.mobapps.fisiocontrol.domain.model.ScheduleStatus
import br.com.mobapps.fisiocontrol.domain.model.TreatmentSchedule
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TreatmentScheduleDto(
    val id: String = "",
    @SerialName("player_id") val playerId: String,
    val title: String,
    @SerialName("weekly_planning") val weeklyPlanning: String? = null,
    @SerialName("start_date") val startDate: String,
    val status: String = "active",
    @SerialName("weekly_assessment") val weeklyAssessment: String? = null,
    @SerialName("sessions_per_week") val sessionsPerWeek: Int = 3,
    @SerialName("updated_at") val updatedAt: String = ""
) {
    fun toDomain() = TreatmentSchedule(
        id              = id,
        playerId        = playerId,
        title           = title,
        weeklyPlanning  = weeklyPlanning,
        startDate       = LocalDate.parse(startDate),
        status          = when (status) {
            "completed" -> ScheduleStatus.COMPLETED
            "paused"    -> ScheduleStatus.PAUSED
            else        -> ScheduleStatus.ACTIVE
        },
        weeklyAssessment = weeklyAssessment,
        sessionsPerWeek  = sessionsPerWeek,
        updatedAt        = updatedAt
    )
}

fun TreatmentSchedule.toDto() = TreatmentScheduleDto(
    id               = id,
    playerId         = playerId,
    title            = title,
    weeklyPlanning   = weeklyPlanning,
    startDate        = startDate.toString(),
    status           = when (status) {
        ScheduleStatus.COMPLETED -> "completed"
        ScheduleStatus.PAUSED    -> "paused"
        else                     -> "active"
    },
    weeklyAssessment = weeklyAssessment,
    sessionsPerWeek  = sessionsPerWeek,
    updatedAt        = updatedAt
)