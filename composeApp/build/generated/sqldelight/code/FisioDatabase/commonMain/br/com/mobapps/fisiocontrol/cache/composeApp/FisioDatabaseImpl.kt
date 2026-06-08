package br.com.mobapps.fisiocontrol.cache.composeApp

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import br.com.mobapps.fisiocontrol.cache.EvolutionEntityQueries
import br.com.mobapps.fisiocontrol.cache.FisioDatabase
import br.com.mobapps.fisiocontrol.cache.PlayerEntityQueries
import br.com.mobapps.fisiocontrol.cache.ScheduleEntityQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<FisioDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = FisioDatabaseImpl.Schema

internal fun KClass<FisioDatabase>.newInstance(driver: SqlDriver): FisioDatabase =
    FisioDatabaseImpl(driver)

private class FisioDatabaseImpl(
  driver: SqlDriver,
) : TransacterImpl(driver), FisioDatabase {
  override val evolutionEntityQueries: EvolutionEntityQueries = EvolutionEntityQueries(driver)

  override val playerEntityQueries: PlayerEntityQueries = PlayerEntityQueries(driver)

  override val scheduleEntityQueries: ScheduleEntityQueries = ScheduleEntityQueries(driver)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE EvolutionEntity (
          |    id               TEXT NOT NULL PRIMARY KEY,
          |    player_id        TEXT NOT NULL,
          |    schedule_id      TEXT,
          |    session_date     TEXT NOT NULL,
          |    pain_scale       INTEGER,
          |    physiotherapy_procedures TEXT,
          |    objective_note   TEXT,
          |    created_at       TEXT NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE PlayerEntity (
          |    id           TEXT NOT NULL PRIMARY KEY,
          |    full_name    TEXT NOT NULL,
          |    birth_date   TEXT,
          |    position     TEXT,
          |    team         TEXT,
          |    phone        TEXT,
          |    photo_url    TEXT,
          |    notes        TEXT,
          |    is_active    INTEGER NOT NULL DEFAULT 1,
          |    updated_at   TEXT NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE ScheduleEntity (
          |    id                 TEXT NOT NULL PRIMARY KEY,
          |    player_id          TEXT NOT NULL,
          |    title              TEXT NOT NULL,
          |    weekly_planning    TEXT,
          |    start_date         TEXT NOT NULL,
          |    status             TEXT NOT NULL,
          |    weekly_assessment  TEXT,
          |    sessions_per_week  INTEGER NOT NULL,
          |    updated_at         TEXT NOT NULL
          |)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> = QueryResult.Unit
  }
}
