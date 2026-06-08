package br.com.mobapps.fisiocontrol.domain.usecase.schedule

import br.com.mobapps.fisiocontrol.domain.model.TreatmentSchedule
import br.com.mobapps.fisiocontrol.domain.repository.ScheduleRepository

class SaveScheduleUseCase(private val repo: ScheduleRepository) {
    suspend operator fun invoke(schedule: TreatmentSchedule): Result<TreatmentSchedule> =
        if (schedule.id.isEmpty()) repo.createSchedule(schedule) else repo.updateSchedule(schedule)
}
