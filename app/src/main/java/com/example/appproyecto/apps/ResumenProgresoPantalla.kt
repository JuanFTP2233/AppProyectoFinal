package com.example.appproyecto.apps

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun ResumenProgresoPantalla(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Resumen del día", style = MaterialTheme.typography.titleLarge)
        Text("Aquí se mostrarán las tareas, notas y objetivos completados hoy.")
    }
}
