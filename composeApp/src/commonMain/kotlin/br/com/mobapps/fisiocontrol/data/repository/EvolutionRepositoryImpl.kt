package br.com.mobapps.fisiocontrol.data.repository

import br.com.mobapps.fisiocontrol.cache.FisioDatabase
import br.com.mobapps.fisiocontrol.data.remote.dto.DailyEvolutionDto
import br.com.mobapps.fisiocontrol.data.remote.dto.toDto
import br.com.mobapps.fisiocontrol.domain.model.DailyEvolution
import br.com.mobapps.fisiocontrol.domain.repository.EvolutionRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.datetime.LocalDate

class EvolutionRepositoryImpl(
    private val supabase: SupabaseClient,
    private val db: FisioDatabase
) : EvolutionRepository {

    override suspend fun getEvolutionsByPlayer(playerId: String): Result<List<DailyEvolution>> =
        runCatching {
            val evolutions = supabase.from("daily_evolutions")
                .select { filter { eq("player_id", playerId) } }
                .decodeList<DailyEvolutionDto>()
                .map { it.toDomain() }

            evolutions.forEach { cacheEvolution(it) }
            evolutions
        }.recoverCatching {
            db.evolutionEntityQueries.selectEvolutionsByPlayer(playerId).executeAsList().map { e ->
                DailyEvolution(
                    id = e.id, playerId = e.player_id, scheduleId = e.schedule_id,
                    sessionDate = LocalDate.parse(e.session_date), painScale = e.pain_scale?.toInt(),
                    physiotherapyProcedures = e.physiotherapy_procedures, objectiveNote = e.objective_note,
                    createdAt = e.created_at
                )
            }
        }

    override suspend fun recordEvolution(evolution: DailyEvolution): Result<DailyEvolution> =
        runCatching {
            supabase.from("daily_evolutions")
                .insert(evolution.toDto().copy(id = "")) { select() }
                .decodeSingle<DailyEvolutionDto>()
                .toDomain()
                .also { cacheEvolution(it) }
        }

    private fun cacheEvolution(e: DailyEvolution) {
        db.evolutionEntityQueries.insertEvolution(
            id              = e.id,
            player_id       = e.playerId,
            schedule_id     = e.scheduleId,
            session_date    = e.sessionDate.toString(),
            pain_scale      = e.painScale?.toLong(),
            physiotherapy_procedures = e.physiotherapyProcedures,
            objective_note  = e.objectiveNote,
            created_at      = e.createdAt
        )
    }
}
