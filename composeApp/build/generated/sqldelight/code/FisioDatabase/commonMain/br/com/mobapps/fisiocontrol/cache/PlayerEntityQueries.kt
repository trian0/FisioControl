package br.com.mobapps.fisiocontrol.cache

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class PlayerEntityQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectAllActive(mapper: (
    id: String,
    full_name: String,
    birth_date: String?,
    position: String?,
    team: String?,
    phone: String?,
    photo_url: String?,
    notes: String?,
    is_active: Long,
    updated_at: String,
  ) -> T): Query<T> = Query(1_846_965_217, arrayOf("PlayerEntity"), driver, "PlayerEntity.sq",
      "selectAllActive",
      "SELECT PlayerEntity.id, PlayerEntity.full_name, PlayerEntity.birth_date, PlayerEntity.position, PlayerEntity.team, PlayerEntity.phone, PlayerEntity.photo_url, PlayerEntity.notes, PlayerEntity.is_active, PlayerEntity.updated_at FROM PlayerEntity WHERE is_active = 1 ORDER BY full_name ASC") {
      cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getString(3),
      cursor.getString(4),
      cursor.getString(5),
      cursor.getString(6),
      cursor.getString(7),
      cursor.getLong(8)!!,
      cursor.getString(9)!!
    )
  }

  public fun selectAllActive(): Query<PlayerEntity> = selectAllActive { id, full_name, birth_date,
      position, team, phone, photo_url, notes, is_active, updated_at ->
    PlayerEntity(
      id,
      full_name,
      birth_date,
      position,
      team,
      phone,
      photo_url,
      notes,
      is_active,
      updated_at
    )
  }

  public fun <T : Any> selectById(id: String, mapper: (
    id: String,
    full_name: String,
    birth_date: String?,
    position: String?,
    team: String?,
    phone: String?,
    photo_url: String?,
    notes: String?,
    is_active: Long,
    updated_at: String,
  ) -> T): Query<T> = SelectByIdQuery(id) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getString(3),
      cursor.getString(4),
      cursor.getString(5),
      cursor.getString(6),
      cursor.getString(7),
      cursor.getLong(8)!!,
      cursor.getString(9)!!
    )
  }

  public fun selectById(id: String): Query<PlayerEntity> = selectById(id) { id_, full_name,
      birth_date, position, team, phone, photo_url, notes, is_active, updated_at ->
    PlayerEntity(
      id_,
      full_name,
      birth_date,
      position,
      team,
      phone,
      photo_url,
      notes,
      is_active,
      updated_at
    )
  }

  public fun insertPlayer(
    id: String,
    full_name: String,
    birth_date: String?,
    position: String?,
    team: String?,
    phone: String?,
    photo_url: String?,
    notes: String?,
    is_active: Long,
    updated_at: String,
  ) {
    driver.execute(148_191_556, """
        |INSERT OR REPLACE INTO PlayerEntity
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 10) {
          bindString(0, id)
          bindString(1, full_name)
          bindString(2, birth_date)
          bindString(3, position)
          bindString(4, team)
          bindString(5, phone)
          bindString(6, photo_url)
          bindString(7, notes)
          bindLong(8, is_active)
          bindString(9, updated_at)
        }
    notifyQueries(148_191_556) { emit ->
      emit("PlayerEntity")
    }
  }

  public fun deleteById(id: String) {
    driver.execute(136_720_615, """DELETE FROM PlayerEntity WHERE id = ?""", 1) {
          bindString(0, id)
        }
    notifyQueries(136_720_615) { emit ->
      emit("PlayerEntity")
    }
  }

  private inner class SelectByIdQuery<out T : Any>(
    public val id: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("PlayerEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("PlayerEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_011_142_584,
        """SELECT PlayerEntity.id, PlayerEntity.full_name, PlayerEntity.birth_date, PlayerEntity.position, PlayerEntity.team, PlayerEntity.phone, PlayerEntity.photo_url, PlayerEntity.notes, PlayerEntity.is_active, PlayerEntity.updated_at FROM PlayerEntity WHERE id = ?""",
        mapper, 1) {
      bindString(0, id)
    }

    override fun toString(): String = "PlayerEntity.sq:selectById"
  }
}
