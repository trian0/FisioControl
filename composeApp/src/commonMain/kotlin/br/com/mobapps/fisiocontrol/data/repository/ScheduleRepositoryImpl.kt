package br.com.mobapps.fisiocontrol.data.repository

import br.com.mobapps.fisiocontrol.cache.FisioDatabase
import br.com.mobapps.fisiocontrol.data.remote.dto.TreatmentScheduleDto
import br.com.mobapps.fisiocontrol.data.remote.dto.toDto
import br.com.mobapps.fisiocontrol.domain.model.ScheduleStatus
import br.com.mobapps.fisiocontrol.domain.model.TreatmentSchedule
import kotlinx.datetime.LocalDate
import br.com.mobapps.fisiocontrol.domain.repository.ScheduleRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class ScheduleRepositoryImpl(
    private val supabase: SupabaseClient,
    private val db: FisioDatabase
) : ScheduleRepository {

    override suspend fun getSchedulesByPlayer(playerId: String): Result<List<TreatmentSchedule>> =
        runCatching {
            val schedules = supabase.from("treatment_schedules")
                .select { filter { eq("player_id", playerId) } }
                .decodeList<TreatmentScheduleDto>()
                .map { it.toDomain() }

            schedules.forEach { cacheSchedule(it) }
            schedules
        }.recoverCatching {
            db.scheduleEntityQueries.selectSchedulesByPlayer(playerId).executeAsList().map { e ->
                TreatmentSchedule(
                    id               = e.id,
                    playerId         = e.player_id,
                    title            = e.title,
                    weeklyPlanning   = e.weekly_planning,
                    startDate        = LocalDate.parse(e.start_date),
                    status           = when (e.status) {
                        "completed" -> ScheduleStatus.COMPLETED
                        "paused"    -> ScheduleStatus.PAUSED
                        else        -> ScheduleStatus.ACTIVE
                    },
                    weeklyAssessment = e.weekly_assessment,
                    sessionsPerWeek  = e.sessions_per_week.toInt(),
                    updatedAt        = e.updated_at
                )
            }
        }

    override suspend fun getScheduleById(id: String): Result<TreatmentSchedule> =
        runCatching {
            supabase.from("treatment_schedules")
                .select { filter { eq("id", id) } }
                .decodeSingle<TreatmentScheduleDto>()
                .toDomain()
                .also { cacheSchedule(it) }
        }.recoverCatching {
            db.scheduleEntityQueries.selectScheduleById(id).executeAsOne().let { e ->
                TreatmentSchedule(
                    id               = e.id,
                    playerId         = e.player_id,
                    title            = e.title,
                    weeklyPlanning   = e.weekly_planning,
                    startDate        = LocalDate.parse(e.start_date),
                    status           = when (e.status) {
                        "completed" -> ScheduleStatus.COMPLETED
                        "paused"    -> ScheduleStatus.PAUSED
                        else        -> ScheduleStatus.ACTIVE
                    },
                    weeklyAssessment = e.weekly_assessment,
                    sessionsPerWeek  = e.sessions_per_week.toInt(),
                    updatedAt        = e.updated_at
                )
            }
        }

    override suspend fun createSchedule(schedule: TreatmentSchedule): Result<TreatmentSchedule> =
        runCatching {
            supabase.from("treatment_schedules")
                .insert(schedule.toDto().copy(id = "")) { select() }
                .decodeSingle<TreatmentScheduleDto>()
                .toDomain()
                .also { cacheSchedule(it) }
        }

    override suspend fun updateSchedule(schedule: TreatmentSchedule): Result<TreatmentSchedule> =
        runCatching {
            supabase.from("treatment_schedules")
                .update(schedule.toDto()) { filter { eq("id", schedule.id) }; select() }
                .decodeSingle<TreatmentScheduleDto>()
                .toDomain()
                .also { cacheSchedule(it) }
        }

    private fun cacheSchedule(s: TreatmentSchedule) {
        db.scheduleEntityQueries.insertSchedule(
            id                = s.id,
            player_id         = s.playerId,
            title             = s.title,
            weekly_planning   = s.weeklyPlanning,
            start_date        = s.startDate.toString(),
            status            = s.status.name.lowercase(),
            weekly_assessment = s.weeklyAssessment,
            sessions_per_week = s.sessionsPerWeek.toLong(),
            updated_at        = s.updatedAt
        )
    }
}
