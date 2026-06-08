package br.com.mobapps.fisiocontrol.domain.repository

import br.com.mobapps.fisiocontrol.domain.model.Player

interface PlayerRepository {
    suspend fun getPlayers(): Result<List<Player>>
    suspend fun getPlayerById(id: String): Result<Player>
    suspend fun createPlayer(player: Player): Result<Player>
    suspend fun updatePlayer(player: Player): Result<Player>
    suspend fun deactivatePlayer(id: String): Result<Unit>
}
