package br.com.mobapps.fisiocontrol.domain.usecase.player

import br.com.mobapps.fisiocontrol.domain.model.Player
import br.com.mobapps.fisiocontrol.domain.repository.PlayerRepository

class SavePlayerUseCase(private val repo: PlayerRepository) {
    suspend operator fun invoke(player: Player): Result<Player> =
        if (player.id.isEmpty()) repo.createPlayer(player) else repo.updatePlayer(player)
}
