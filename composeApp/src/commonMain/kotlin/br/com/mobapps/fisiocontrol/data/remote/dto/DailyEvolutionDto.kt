package br.com.mobapps.fisiocontrol.data.remote.dto

import br.com.mobapps.fisiocontrol.domain.model.DailyEvolution
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyEvolutionDto(
    val id: String = "",
    @SerialName("player_id") val playerId: String,
    @SerialName("schedule_id") val scheduleId: String? = null,
    @SerialName("session_date") val sessionDate: String,
    @SerialName("pain_scale") val painScale: Int? = null,
    @SerialName("physiotherapy_procedures") val physiotherapyProcedures: String? = null,
    @SerialName("objective_note") val objectiveNote: String? = null,
    @SerialName("created_at") val createdAt: String = ""
) {
    fun toDomain() = DailyEvolution(
        id = id,
        playerId = playerId,
        scheduleId = scheduleId,
        sessionDate = LocalDate.parse(sessionDate),
        painScale = painScale,
        physiotherapyProcedures = physiotherapyProcedures,
        objectiveNote = objectiveNote,
        createdAt = createdAt
    )
}

fun DailyEvolution.toDto() = DailyEvolutionDto(
    id = id,
    playerId = playerId,
    scheduleId = scheduleId,
    sessionDate = sessionDate.toString(),
    painScale = painScale,
    physiotherapyProcedures = physiotherapyProcedures,
    objectiveNote = objectiveNote,
    createdAt = createdAt
)
