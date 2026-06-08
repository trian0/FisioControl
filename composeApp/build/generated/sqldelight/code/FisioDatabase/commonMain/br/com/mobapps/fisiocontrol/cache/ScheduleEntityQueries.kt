package br.com.mobapps.fisiocontrol.cache

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class ScheduleEntityQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectSchedulesByPlayer(player_id: String, mapper: (
    id: String,
    player_id: String,
    title: String,
    weekly_planning: String?,
    start_date: String,
    status: String,
    weekly_assessment: String?,
    sessions_per_week: Long,
    updated_at: String,
  ) -> T): Query<T> = SelectSchedulesByPlayerQuery(player_id) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3),
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6),
      cursor.getLong(7)!!,
      cursor.getString(8)!!
    )
  }

  public fun selectSchedulesByPlayer(player_id: String): Query<ScheduleEntity> =
      selectSchedulesByPlayer(player_id) { id, player_id_, title, weekly_planning, start_date,
      status, weekly_assessment, sessions_per_week, updated_at ->
    ScheduleEntity(
      id,
      player_id_,
      title,
      weekly_planning,
      start_date,
      status,
      weekly_assessment,
      sessions_per_week,
      updated_at
    )
  }

  public fun <T : Any> selectScheduleById(id: String, mapper: (
    id: String,
    player_id: String,
    title: String,
    weekly_planning: String?,
    start_date: String,
    status: String,
    weekly_assessment: String?,
    sessions_per_week: Long,
    updated_at: String,
  ) -> T): Query<T> = SelectScheduleByIdQuery(id) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3),
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6),
      cursor.getLong(7)!!,
      cursor.getString(8)!!
    )
  }

  public fun selectScheduleById(id: String): Query<ScheduleEntity> = selectScheduleById(id) { id_,
      player_id, title, weekly_planning, start_date, status, weekly_assessment, sessions_per_week,
      updated_at ->
    ScheduleEntity(
      id_,
      player_id,
      title,
      weekly_planning,
      start_date,
      status,
      weekly_assessment,
      sessions_per_week,
      updated_at
    )
  }

  public fun insertSchedule(
    id: String,
    player_id: String,
    title: String,
    weekly_planning: String?,
    start_date: String,
    status: String,
    weekly_assessment: String?,
    sessions_per_week: Long,
    updated_at: String,
  ) {
    driver.execute(1_532_271_600, """
        |INSERT OR REPLACE INTO ScheduleEntity
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 9) {
          bindString(0, id)
          bindString(1, player_id)
          bindString(2, title)
          bindString(3, weekly_planning)
          bindString(4, start_date)
          bindString(5, status)
          bindString(6, weekly_assessment)
          bindLong(7, sessions_per_week)
          bindString(8, updated_at)
        }
    notifyQueries(1_532_271_600) { emit ->
      emit("ScheduleEntity")
    }
  }

  private inner class SelectSchedulesByPlayerQuery<out T : Any>(
    public val player_id: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("ScheduleEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("ScheduleEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_473_693_144,
        """SELECT ScheduleEntity.id, ScheduleEntity.player_id, ScheduleEntity.title, ScheduleEntity.weekly_planning, ScheduleEntity.start_date, ScheduleEntity.status, ScheduleEntity.weekly_assessment, ScheduleEntity.sessions_per_week, ScheduleEntity.updated_at FROM ScheduleEntity WHERE player_id = ? ORDER BY start_date DESC""",
        mapper, 1) {
      bindString(0, player_id)
    }

    override fun toString(): String = "ScheduleEntity.sq:selectSchedulesByPlayer"
  }

  private inner class SelectScheduleByIdQuery<out T : Any>(
    public val id: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("ScheduleEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("ScheduleEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_750_918_747,
        """SELECT ScheduleEntity.id, ScheduleEntity.player_id, ScheduleEntity.title, ScheduleEntity.weekly_planning, ScheduleEntity.start_date, ScheduleEntity.status, ScheduleEntity.weekly_assessment, ScheduleEntity.sessions_per_week, ScheduleEntity.updated_at FROM ScheduleEntity WHERE id = ? LIMIT 1""",
        mapper, 1) {
      bindString(0, id)
    }

    override fun toString(): String = "ScheduleEntity.sq:selectScheduleById"
  }
}
