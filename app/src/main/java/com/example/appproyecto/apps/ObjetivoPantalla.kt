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
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ObjetivosPantalla(navController: NavController) {
    // Variables del formulario
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf(LocalDate.now()) }
    var fechaFin by remember { mutableStateOf(LocalDate.now().plusDays(7)) }
    var completado by remember { mutableStateOf(false) }


    var objetivos by remember { mutableStateOf(mutableListOf<Map<String, Any>>()) }


    var editandoId by remember { mutableStateOf<String?>(null) }

    val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val context = LocalContext.current

    fun abrirDatePicker(fecha: LocalDate, onDateSelected: (LocalDate) -> Unit) {
        val dpd = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            fecha.year,
            fecha.monthValue - 1,
            fecha.dayOfMonth
        )
        dpd.show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(Color(0xFFE3F2FD))
    ) {
        Text(
            if (editandoId == null) "Nuevo Objetivo" else "Editar Objetivo",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFBBDEFB),
                unfocusedContainerColor = Color(0xFFE3F2FD),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFBBDEFB),
                unfocusedContainerColor = Color(0xFFE3F2FD),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = completado, onCheckedChange = { completado = it })
            Text("¿Completado?")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Fecha Inicio: ${formatoFecha.format(fechaInicio)}",
            modifier = Modifier
                .clickable { abrirDatePicker(fechaInicio) { fechaInicio = it } }
                .padding(8.dp),
            color = Color(0xFF1565C0),
            fontWeight = FontWeight.Medium
        )

        Text(
            "Fecha Fin: ${formatoFecha.format(fechaFin)}",
            modifier = Modifier
                .clickable { abrirDatePicker(fechaFin) { fechaFin = it } }
                .padding(8.dp),
            color = Color(0xFF1565C0),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (titulo.isNotBlank()) {
                    if (editandoId == null) {

                        objetivos.add(
                            mapOf(
                                "id" to UUID.randomUUID().toString(),
                                "titulo" to titulo,
                                "descripcion" to descripcion,
                                "fechaInicio" to fechaInicio,
                                "fechaFin" to fechaFin,
                                "completado" to completado
                            )
                        )
                    } else {

                        val index = objetivos.indexOfFirst { it["id"] == editandoId }
                        if (index >= 0) {
                            objetivos[index] = mapOf(
                                "id" to editandoId!!,
                                "titulo" to titulo,
                                "descripcion" to descripcion,
                                "fechaInicio" to fechaInicio,
                                "fechaFin" to fechaFin,
                                "completado" to completado
                            )
                        }
                        editandoId = null
                    }

                    titulo = ""
                    descripcion = ""
                    completado = false
                    fechaInicio = LocalDate.now()
                    fechaFin = LocalDate.now().plusDays(7)
                }
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text(if (editandoId == null) "Guardar" else "Actualizar", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            itemsIndexed(objetivos) { index, obj ->
                val completadoEstado = obj["completado"] as? Boolean ?: false
                val colorFondo = if (completadoEstado) Color(0xFFC8E6C9) else Color(0xFFFFF9C4)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {

                            titulo = obj["titulo"] as? String ?: ""
                            descripcion = obj["descripcion"] as? String ?: ""
                            fechaInicio = obj["fechaInicio"] as? LocalDate ?: LocalDate.now()
                            fechaFin = obj["fechaFin"] as? LocalDate ?: LocalDate.now().plusDays(7)
                            completado = completadoEstado
                            editandoId = obj["id"] as? String
                        },
                    colors = CardDefaults.cardColors(containerColor = colorFondo)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            obj["titulo"] as? String ?: "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            obj["descripcion"] as? String ?: "",
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Inicio: ${formatoFecha.format(obj["fechaInicio"] as? LocalDate ?: LocalDate.now())}")
                        Text("Fin: ${formatoFecha.format(obj["fechaFin"] as? LocalDate ?: LocalDate.now().plusDays(7))}")
                        if (completadoEstado) {
                            Text(
                                "✔ Completado",
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = {
                                objetivos.removeAt(index)
                                if (editandoId == obj["id"]) {
                                    editandoId = null
                                    titulo = ""
                                    descripcion = ""
                                    completado = false
                                    fechaInicio = LocalDate.now()
                                    fechaFin = LocalDate.now().plusDays(7)
                                }
                            }) {
                                Text("Eliminar", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}
