package com.example.appproyecto.apps

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProgresoObjetivosPantalla(navController: NavController) {
    // Simulación lista de objetivos con id, título y porcentaje completado
    var objetivos by remember {
        mutableStateOf(
            mutableListOf(
                mutableMapOf(
                    "id" to "1",
                    "titulo" to "Aprender Kotlin",
                    "progreso" to 0.5f, // 50%
                    "seguimientos" to mutableListOf<Map<String, Any>>() // lista de seguimientos
                ),
                mutableMapOf(
                    "id" to "2",
                    "titulo" to "Hacer ejercicio diario",
                    "progreso" to 0.3f, // 30%
                    "seguimientos" to mutableListOf<Map<String, Any>>()
                )
            )
        )
    }

    // Estados para diálogo agregar seguimiento
    var mostrarDialogo by remember { mutableStateOf(false) }
    var seguimientoObjetivoId by remember { mutableStateOf<String?>(null) }
    var progresoDiario by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }
    var fechaSeguimiento by remember { mutableStateOf(LocalDate.now()) }

    val context = LocalContext.current
    val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun abrirDatePicker(onDateSelected: (LocalDate) -> Unit) {
        val dpd = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            fechaSeguimiento.year,
            fechaSeguimiento.monthValue - 1,
            fechaSeguimiento.dayOfMonth
        )
        dpd.show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFE3F2FD))
    ) {
        Text(
            "Progreso de objetivos",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            itemsIndexed(objetivos) { index, objetivo ->
                val titulo = objetivo["titulo"] as? String ?: ""
                val progreso = objetivo["progreso"] as? Float ?: 0f

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = progreso,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp),
                            color = Color(0xFF1976D2),
                            trackColor = Color(0xFFBBDEFB)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "${(progreso * 100).toInt()}% completado",
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(onClick = {
                            seguimientoObjetivoId = objetivo["id"] as String
                            mostrarDialogo = true
                            // Reset inputs
                            progresoDiario = ""
                            comentario = ""
                            fechaSeguimiento = LocalDate.now()
                        }) {
                            Text("Agregar seguimiento")
                        }
                    }
                }
            }
        }

        if (mostrarDialogo && seguimientoObjetivoId != null) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                title = { Text("Agregar seguimiento") },
                text = {
                    Column {
                        Text("Fecha: ${formatoFecha.format(fechaSeguimiento)}",
                            modifier = Modifier
                                .clickable { abrirDatePicker { fechaSeguimiento = it } }
                                .padding(vertical = 8.dp),
                            color = Color(0xFF1976D2)
                        )

                        OutlinedTextField(
                            value = progresoDiario,
                            onValueChange = { progresoDiario = it },
                            label = { Text("Progreso diario (0 a 100)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = comentario,
                            onValueChange = { comentario = it },
                            label = { Text("Comentario") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val progNum = progresoDiario.toFloatOrNull()
                        if (progNum != null && progNum in 0f..100f) {
                            // Agregar seguimiento a la lista del objetivo
                            val index = objetivos.indexOfFirst { it["id"] == seguimientoObjetivoId }
                            if (index >= 0) {
                                val listaSeguimientos =
                                    objetivos[index]["seguimientos"] as MutableList<Map<String, Any>>

                                listaSeguimientos.add(
                                    mapOf(
                                        "fecha" to fechaSeguimiento,
                                        "progresoDiario" to progNum,
                                        "comentario" to comentario
                                    )
                                )

                                val totalProg = listaSeguimientos.sumOf { it["progresoDiario"] as Float }
                                val nuevoProgreso = (totalProg / (listaSeguimientos.size * 100)).toFloat()
                                objetivos[index]["progreso"] = nuevoProgreso.coerceIn(0f, 1f)
                            }
                            mostrarDialogo = false
                        }
                    }) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
