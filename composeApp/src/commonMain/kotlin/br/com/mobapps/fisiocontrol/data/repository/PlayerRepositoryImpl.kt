package br.com.mobapps.fisiocontrol.data.repository

import br.com.mobapps.fisiocontrol.cache.FisioDatabase
import br.com.mobapps.fisiocontrol.data.remote.dto.PlayerDto
import br.com.mobapps.fisiocontrol.data.remote.dto.toCreateDto
import br.com.mobapps.fisiocontrol.data.remote.dto.toDto
import br.com.mobapps.fisiocontrol.domain.model.Player
import br.com.mobapps.fisiocontrol.domain.repository.PlayerRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.datetime.LocalDate

class PlayerRepositoryImpl(
    private val supabase: SupabaseClient,
    private val db: FisioDatabase
) : PlayerRepository {

    override suspend fun getPlayers(): Result<List<Player>> = runCatching {
        val players = supabase.from("players")
            .select()
            .decodeList<PlayerDto>()
            .map { it.toDomain() }

        players.forEach { cachePlayer(it) }
        players
    }.recoverCatching {
        db.playerEntityQueries.selectAllActive().executeAsList().map { e ->
            Player(
                id = e.id, fullName = e.full_name,
                birthDate = e.birth_date?.let { runCatching { LocalDate.parse(it) }.getOrNull() },
                position = e.position, team = e.team, phone = e.phone,
                photoUrl = e.photo_url, notes = e.notes,
                isActive = e.is_active == 1L, updatedAt = e.updated_at
            )
        }
    }

    override suspend fun getPlayerById(id: String): Result<Player> = runCatching {
        supabase.from("players")
            .select { filter { eq("id", id) } }
            .decodeSingle<PlayerDto>()
            .toDomain()
    }

    override suspend fun createPlayer(player: Player): Result<Player> = runCatching {
        supabase.from("players")
            .insert(player.toCreateDto()) { select() }
            .decodeSingle<PlayerDto>()
            .toDomain()
            .also { cachePlayer(it) }
    }

    override suspend fun updatePlayer(player: Player): Result<Player> = runCatching {
        supabase.from("players")
            .update(player.toDto()) { filter { eq("id", player.id) }; select() }
            .decodeSingle<PlayerDto>()
            .toDomain()
            .also { cachePlayer(it) }
    }

    override suspend fun deactivatePlayer(id: String): Result<Unit> = runCatching {
        supabase.from("players")
            .update({ set("is_active", false) }) { filter { eq("id", id) } }
        db.playerEntityQueries.deleteById(id)
    }

    private fun cachePlayer(p: Player) {
        db.playerEntityQueries.insertPlayer(
            id         = p.id,
            full_name  = p.fullName,
            birth_date = p.birthDate?.toString(),
            position   = p.position,
            team       = p.team,
            phone      = p.phone,
            photo_url  = p.photoUrl,
            notes      = p.notes,
            is_active  = if (p.isActive) 1L else 0L,
            updated_at = p.updatedAt
        )
    }
}
