package br.com.mobapps.fisiocontrol.domain.repository

import br.com.mobapps.fisiocontrol.domain.model.TreatmentSchedule

interface ScheduleRepository {
    suspend fun getSchedulesByPlayer(playerId: String): Result<List<TreatmentSchedule>>
    suspend fun getScheduleById(id: String): Result<TreatmentSchedule>
    suspend fun createSchedule(schedule: TreatmentSchedule): Result<TreatmentSchedule>
    suspend fun updateSchedule(schedule: TreatmentSchedule): Result<TreatmentSchedule>
}
