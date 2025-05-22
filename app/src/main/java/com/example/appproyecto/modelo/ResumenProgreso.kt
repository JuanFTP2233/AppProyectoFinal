package com.example.appproyecto.modelo

import java.time.LocalDate

data class ResumenProgreso(
    val idResumen: Int,
    val fecha: LocalDate,
    val objetivosCompletados: Int,
    val totalObjetivos: Int,
    val comentarioResumen: String
)
