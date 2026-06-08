package br.com.mobapps.fisiocontrol.domain.usecase.evolution

import br.com.mobapps.fisiocontrol.domain.model.DailyEvolution
import br.com.mobapps.fisiocontrol.domain.repository.EvolutionRepository

class GetEvolutionsUseCase(private val repo: EvolutionRepository) {
    suspend operator fun invoke(playerId: String): Result<List<DailyEvolution>> =
        repo.getEvolutionsByPlayer(playerId)
}
