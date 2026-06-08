package br.com.mobapps.fisiocontrol.domain.repository

import br.com.mobapps.fisiocontrol.domain.model.DailyEvolution

interface EvolutionRepository {
    suspend fun getEvolutionsByPlayer(playerId: String): Result<List<DailyEvolution>>
    suspend fun recordEvolution(evolution: DailyEvolution): Result<DailyEvolution>
}
