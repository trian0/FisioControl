package br.com.mobapps.fisiocontrol.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val name: String,
    val sets: Int = 0,
    val reps: Int = 0,
    val durationSecs: Int = 0,
    val notes: String = ""
)
