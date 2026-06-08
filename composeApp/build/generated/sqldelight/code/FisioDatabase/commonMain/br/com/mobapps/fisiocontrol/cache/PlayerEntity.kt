package br.com.mobapps.fisiocontrol.cache

import kotlin.Long
import kotlin.String

public data class PlayerEntity(
  public val id: String,
  public val full_name: String,
  public val birth_date: String?,
  public val position: String?,
  public val team: String?,
  public val phone: String?,
  public val photo_url: String?,
  public val notes: String?,
  public val is_active: Long,
  public val updated_at: String,
)
