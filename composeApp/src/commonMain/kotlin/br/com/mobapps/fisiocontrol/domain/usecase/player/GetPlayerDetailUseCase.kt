package br.com.mobapps.fisiocontrol.domain.usecase.player

import br.com.mobapps.fisiocontrol.domain.model.Player
import br.com.mobapps.fisiocontrol.domain.repository.PlayerRepository

class GetPlayerDetailUseCase(private val repo: PlayerRepository) {
    suspend operator fun invoke(id: String): Result<Player> = repo.getPlayerById(id)
}
