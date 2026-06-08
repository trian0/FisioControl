package br.com.mobapps.fisiocontrol.domain.usecase.evolution

import br.com.mobapps.fisiocontrol.domain.model.DailyEvolution
import br.com.mobapps.fisiocontrol.domain.repository.EvolutionRepository

class RecordEvolutionUseCase(private val repo: EvolutionRepository) {
    suspend operator fun invoke(evolution: DailyEvolution): Result<DailyEvolution> =
        repo.recordEvolution(evolution)
}
