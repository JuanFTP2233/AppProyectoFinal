package com.example.appproyecto.modelo

import java.time.LocalDate

data class ProgresoObjetivo(
    val idProgreso: Int,
    val fecha: LocalDate,
    val progresoDiario: String,
    val comentario: String
)
