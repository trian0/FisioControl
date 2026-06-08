package br.com.mobapps.fisiocontrol.cache

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class EvolutionEntityQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectEvolutionsByPlayer(player_id: String, mapper: (
    id: String,
    player_id: String,
    schedule_id: String?,
    session_date: String,
    pain_scale: Long?,
    physiotherapy_procedures: String?,
    objective_note: String?,
    created_at: String,
  ) -> T): Query<T> = SelectEvolutionsByPlayerQuery(player_id) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getString(3)!!,
      cursor.getLong(4),
      cursor.getString(5),
      cursor.getString(6),
      cursor.getString(7)!!
    )
  }

  public fun selectEvolutionsByPlayer(player_id: String): Query<EvolutionEntity> =
      selectEvolutionsByPlayer(player_id) { id, player_id_, schedule_id, session_date, pain_scale,
      physiotherapy_procedures, objective_note, created_at ->
    EvolutionEntity(
      id,
      player_id_,
      schedule_id,
      session_date,
      pain_scale,
      physiotherapy_procedures,
      objective_note,
      created_at
    )
  }

  public fun insertEvolution(
    id: String,
    player_id: String,
    schedule_id: String?,
    session_date: String,
    pain_scale: Long?,
    physiotherapy_procedures: String?,
    objective_note: String?,
    created_at: String,
  ) {
    driver.execute(-262_019_890, """
        |INSERT OR REPLACE INTO EvolutionEntity
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 8) {
          bindString(0, id)
          bindString(1, player_id)
          bindString(2, schedule_id)
          bindString(3, session_date)
          bindLong(4, pain_scale)
          bindString(5, physiotherapy_procedures)
          bindString(6, objective_note)
          bindString(7, created_at)
        }
    notifyQueries(-262_019_890) { emit ->
      emit("EvolutionEntity")
    }
  }

  private inner class SelectEvolutionsByPlayerQuery<out T : Any>(
    public val player_id: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("EvolutionEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("EvolutionEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-687_463_904,
        """SELECT EvolutionEntity.id, EvolutionEntity.player_id, EvolutionEntity.schedule_id, EvolutionEntity.session_date, EvolutionEntity.pain_scale, EvolutionEntity.physiotherapy_procedures, EvolutionEntity.objective_note, EvolutionEntity.created_at FROM EvolutionEntity WHERE player_id = ? ORDER BY session_date DESC""",
        mapper, 1) {
      bindString(0, player_id)
    }

    override fun toString(): String = "EvolutionEntity.sq:selectEvolutionsByPlayer"
  }
}
