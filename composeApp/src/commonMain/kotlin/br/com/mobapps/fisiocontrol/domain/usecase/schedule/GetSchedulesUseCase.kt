package br.com.mobapps.fisiocontrol.domain.usecase.schedule

import br.com.mobapps.fisiocontrol.domain.model.TreatmentSchedule
import br.com.mobapps.fisiocontrol.domain.repository.ScheduleRepository

class GetSchedulesUseCase(private val repo: ScheduleRepository) {
    suspend operator fun invoke(playerId: String): Result<List<TreatmentSchedule>> =
        repo.getSchedulesByPlayer(playerId)
}
