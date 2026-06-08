package br.com.mobapps.fisiocontrol.cache

import kotlin.Long
import kotlin.String

public data class ScheduleEntity(
  public val id: String,
  public val player_id: String,
  public val title: String,
  public val weekly_planning: String?,
  public val start_date: String,
  public val status: String,
  public val weekly_assessment: String?,
  public val sessions_per_week: Long,
  public val updated_at: String,
)
