package br.com.mobapps.fisiocontrol.data.remote.dto

import br.com.mobapps.fisiocontrol.domain.model.Player
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDto(
    val id: String = "",
    @SerialName("created_at") val createdAt: String = "",
    @SerialName("full_name") val fullName: String,
    @SerialName("birth_date") val birthDate: String? = null,
    val position: String? = null,
    val team: String? = null,
    val phone: String? = null,
    @SerialName("photo_url") val photoUrl: String? = null,
    val notes: String? = null,
    @SerialName("is_active") val isActive: Int? = null,
    @SerialName("updated_at") val updatedAt: String? = null
) {
    fun toDomain() = Player(
        id = id,
        fullName = fullName,
        birthDate = birthDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() },
        position = position,
        team = team,
        phone = phone,
        photoUrl = photoUrl,
        notes = notes,
        isActive = (isActive ?: 1) != 0,
        updatedAt = updatedAt ?: createdAt
    )
}

fun Player.toDto() = PlayerDto(
    id = id,
    createdAt = "",
    fullName = fullName,
    birthDate = birthDate?.toString(),
    position = position,
    team = team,
    phone = phone,
    photoUrl = photoUrl,
    notes = notes,
    isActive = if (isActive) 1 else 0,
    updatedAt = updatedAt
)

@Serializable
data class CreatePlayerDto(
    @SerialName("full_name") val fullName: String,
    @SerialName("birth_date") val birthDate: String? = null,
    val position: String? = null,
    val team: String? = null,
    val phone: String? = null,
    @SerialName("photo_url") val photoUrl: String? = null,
    val notes: String? = null,
    @SerialName("is_active") val isActive: Int? = 1,
    @SerialName("updated_at") val updatedAt: String? = null
)

fun Player.toCreateDto() = CreatePlayerDto(
    fullName = fullName,
    birthDate = birthDate?.toString(),
    position = position,
    team = team,
    phone = phone,
    photoUrl = photoUrl,
    notes = notes,
    isActive = if (isActive) 1 else 0,
    updatedAt = updatedAt
)