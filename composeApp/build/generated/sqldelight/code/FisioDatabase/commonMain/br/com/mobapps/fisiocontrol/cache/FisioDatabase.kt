package br.com.mobapps.fisiocontrol.cache

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import br.com.mobapps.fisiocontrol.cache.composeApp.newInstance
import br.com.mobapps.fisiocontrol.cache.composeApp.schema
import kotlin.Unit

public interface FisioDatabase : Transacter {
  public val evolutionEntityQueries: EvolutionEntityQueries

  public val playerEntityQueries: PlayerEntityQueries

  public val scheduleEntityQueries: ScheduleEntityQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = FisioDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): FisioDatabase =
        FisioDatabase::class.newInstance(driver)
  }
}
