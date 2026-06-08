package br.com.mobapps.fisiocontrol.domain.usecase.schedule

import br.com.mobapps.fisiocontrol.domain.model.TreatmentSchedule
import br.com.mobapps.fisiocontrol.domain.repository.ScheduleRepository

class GetScheduleByIdUseCase(private val repo: ScheduleRepository) {
    suspend operator fun invoke(id: String): Result<TreatmentSchedule> = repo.getScheduleById(id)
}
