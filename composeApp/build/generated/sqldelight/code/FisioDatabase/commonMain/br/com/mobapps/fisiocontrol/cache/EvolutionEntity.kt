package br.com.mobapps.fisiocontrol.cache

import kotlin.Long
import kotlin.String

public data class EvolutionEntity(
  public val id: String,
  public val player_id: String,
  public val schedule_id: String?,
  public val session_date: String,
  public val pain_scale: Long?,
  public val physiotherapy_procedures: String?,
  public val objective_note: String?,
  public val created_at: String,
)
