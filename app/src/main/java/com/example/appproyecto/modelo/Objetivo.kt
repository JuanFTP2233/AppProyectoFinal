package com.example.appproyecto.modelo

import java.time.LocalDate

data class Objetivo(
    val idObjetivo: Int,
    val titulo: String,
    val descripcion: String,
    val fechaInicio: LocalDate,
    val fechaFin: LocalDate,
    val completado: Boolean
)
