package br.com.mobapps.fisiocontrol.domain.usecase.player

import br.com.mobapps.fisiocontrol.domain.model.Player
import br.com.mobapps.fisiocontrol.domain.repository.PlayerRepository

class GetPlayersUseCase(private val repo: PlayerRepository) {
    suspend operator fun invoke(): Result<List<Player>> = repo.getPlayers()
}
