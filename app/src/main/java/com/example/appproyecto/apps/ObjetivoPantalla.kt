package com.example.appproyecto.apps

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appproyecto.modelo.Objetivo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ObjetivosPantalla(navController: NavController) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf(LocalDate.now()) }
    var fechaFin by remember { mutableStateOf(LocalDate.now().plusDays(7)) }
    var completado by remember { mutableStateOf(false) }

    var objetivos by remember { mutableStateOf<List<Objetivo>>(emptyList()) }
    var editandoId by remember { mutableStateOf<Int?>(null) }

    val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formatoFechaCompleta = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", Locale("es", "ES"))
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

    fun guardarObjetivo() {
        if (titulo.isNotBlank()) {
            val nuevoId = if (objetivos.isEmpty()) 1 else objetivos.maxOf { it.idObjetivo } + 1
            val nuevoObjetivo = Objetivo(
                idObjetivo = editandoId ?: nuevoId,
                titulo = titulo,
                descripcion = descripcion,
                fechaInicio = fechaInicio,
                fechaFin = fechaFin,
                completado = completado
            )

            objetivos = if (editandoId == null) {
                objetivos + nuevoObjetivo
            } else {
                objetivos.map {
                    if (it.idObjetivo == editandoId) nuevoObjetivo else it
                }
            }

            editandoId = null
            titulo = ""
            descripcion = ""
            completado = false
            fechaInicio = LocalDate.now()
            fechaFin = LocalDate.now().plusDays(7)
        }
    }

    fun eliminarObjetivo(id: Int) {
        objetivos = objetivos.filter { it.idObjetivo != id }
        if (editandoId == id) {
            editandoId = null
            titulo = ""
            descripcion = ""
            completado = false
            fechaInicio = LocalDate.now()
            fechaFin = LocalDate.now().plusDays(7)
        }
    }

    var progresoManual by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }

    fun calcularDiasTotales(objetivo: Objetivo): Int {
        return ChronoUnit.DAYS.between(objetivo.fechaInicio, objetivo.fechaFin).toInt() + 1
    }

    fun calcularPorcentajePorDia(objetivo: Objetivo): Float {
        val diasTotales = calcularDiasTotales(objetivo)
        return if (diasTotales > 0) 100f / diasTotales else 100f
    }

    fun obtenerProgresoManual(objetivoId: Int): Int {
        return progresoManual[objetivoId] ?: 0
    }

    fun calcularProgresoFinal(objetivo: Objetivo): Float {
        val progresoManualDias = obtenerProgresoManual(objetivo.idObjetivo)
        val diasTotales = calcularDiasTotales(objetivo)
        return (progresoManualDias.toFloat() / diasTotales).coerceIn(0f, 1f)
    }

    fun avanzarProgreso(objetivoId: Int, objetivo: Objetivo) {
        val diasTotales = calcularDiasTotales(objetivo)
        val progresoActual = obtenerProgresoManual(objetivoId)
        if (progresoActual < diasTotales) {
            progresoManual = progresoManual + (objetivoId to progresoActual + 1)
        }
    }

    fun retrocederProgreso(objetivoId: Int) {
        val progresoActual = obtenerProgresoManual(objetivoId)
        if (progresoActual > 0) {
            progresoManual = progresoManual + (objetivoId to progresoActual - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFFF3E5F5)
                    )
                )
            )
            .padding(16.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (editandoId == null) " Nuevo Objetivo" else " Editar Objetivo",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Define tus metas y alcanza el éxito",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título del objetivo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1565C0),
                        focusedLabelColor = Color(0xFF1565C0)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1565C0),
                        focusedLabelColor = Color(0xFF1565C0)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F5F5))
                        .padding(12.dp)
                ) {
                    Checkbox(
                        checked = completado,
                        onCheckedChange = { completado = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50))
                    )
                    Text(
                        "Marcar como completado",
                        modifier = Modifier.padding(start = 8.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { abrirDatePicker(fechaInicio) { fechaInicio = it } },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "📅 Fecha Inicio",
                                fontSize = 12.sp,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                formatoFecha.format(fechaInicio),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { abrirDatePicker(fechaFin) { fechaFin = it } },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "🎯 Fecha Límite",
                                fontSize = 12.sp,
                                color = Color(0xFFC62828),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                formatoFecha.format(fechaFin),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { guardarObjetivo() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1565C0)
                    )
                ) {
                    Text(
                        if (editandoId == null) "💾 Guardar Objetivo" else "✅ Actualizar Objetivo",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (objetivos.isNotEmpty()) {
            Text(
                "Mis Objetivos (${objetivos.size})",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1565C0)
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(objetivos) { _, objetivo ->
                val diasTotales = calcularDiasTotales(objetivo)
                val porcentajePorDia = calcularPorcentajePorDia(objetivo)
                val progresoActual = calcularProgresoFinal(objetivo)
                val diasProgreso = obtenerProgresoManual(objetivo.idObjetivo)
                val diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), objetivo.fechaFin).toInt()

                val colorFondo = when {
                    objetivo.completado -> Color(0xFFE8F5E8)
                    diasRestantes < 0 -> Color(0xFFFFEBEE)
                    diasRestantes <= 3 -> Color(0xFFFFF3E0)
                    else -> Color.White
                }

                val colorBorde = when {
                    objetivo.completado -> Color(0xFF4CAF50)
                    diasRestantes < 0 -> Color(0xFFE57373)
                    diasRestantes <= 3 -> Color(0xFFFF9800)
                    else -> Color(0xFF1565C0)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            titulo = objetivo.titulo
                            descripcion = objetivo.descripcion
                            fechaInicio = objetivo.fechaInicio
                            fechaFin = objetivo.fechaFin
                            completado = objetivo.completado
                            editandoId = objetivo.idObjetivo
                        },
                    colors = CardDefaults.cardColors(containerColor = colorFondo),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, colorBorde)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    objetivo.titulo,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF1565C0)
                                )
                                if (objetivo.descripcion.isNotBlank()) {
                                    Text(
                                        objetivo.descripcion,
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }


                            if (objetivo.completado) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
                                ) {
                                    Text(
                                        "✓ Completado",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            } else if (diasRestantes < 0) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE57373))
                                ) {
                                    Text(
                                        "⚠ Vencido",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            } else if (diasRestantes <= 3) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800))
                                ) {
                                    Text(
                                        "⏰ Urgente",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        "📅 Inicio",
                                        fontSize = 10.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        formatoFecha.format(objetivo.fechaInicio),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "🎯 Límite",
                                        fontSize = 10.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        formatoFecha.format(objetivo.fechaFin),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Progreso Manual",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1565C0)
                                )
                                Text(
                                    "${(progresoActual * 100).toInt()}% ($diasProgreso/$diasTotales días)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (objetivo.completado) Color(0xFF4CAF50) else Color(0xFF1565C0)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            LinearProgressIndicator(
                                progress = { progresoActual },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = when {
                                    objetivo.completado -> Color(0xFF4CAF50)
                                    progresoActual >= 1f -> Color(0xFF4CAF50)
                                    diasRestantes < 0 -> Color(0xFFE57373)
                                    diasRestantes <= 3 -> Color(0xFFFF9800)
                                    else -> Color(0xFF1565C0)
                                },
                                trackColor = Color(0xFFE0E0E0)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Control manual: ${String.format("%.1f", porcentajePorDia)}% por día",
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )

                                Row {
                                    FilledTonalButton(
                                        onClick = { retrocederProgreso(objetivo.idObjetivo) },
                                        enabled = diasProgreso > 0,
                                        modifier = Modifier.size(32.dp),
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = Color(0xFFFFEBEE)
                                        )
                                    ) {
                                        Text(
                                            "−",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFE57373)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    FilledTonalButton(
                                        onClick = { avanzarProgreso(objetivo.idObjetivo, objetivo) },
                                        enabled = diasProgreso < diasTotales,
                                        modifier = Modifier.size(32.dp),
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = Color(0xFFE8F5E8)
                                        )
                                    ) {
                                        Text(
                                            "+",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4CAF50)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                when {
                                    progresoActual >= 1f -> "🎉 ¡Objetivo completado manualmente!"
                                    diasProgreso == 0 -> "▶️ Comienza tu progreso"
                                    diasProgreso == diasTotales - 1 -> "🔥 ¡Casi terminado!"
                                    else -> "💪 Progreso: $diasProgreso de $diasTotales días completados"
                                },
                                fontSize = 11.sp,
                                color = when {
                                    progresoActual >= 1f -> Color(0xFF4CAF50)
                                    diasProgreso > diasTotales * 0.7 -> Color(0xFF4CAF50)
                                    diasProgreso > diasTotales * 0.4 -> Color(0xFFFF9800)
                                    else -> Color.Gray
                                },
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))


                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = {
                                    titulo = objetivo.titulo
                                    descripcion = objetivo.descripcion
                                    fechaInicio = objetivo.fechaInicio
                                    fechaFin = objetivo.fechaFin
                                    completado = objetivo.completado
                                    editandoId = objetivo.idObjetivo
                                }
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = Color(0xFF1565C0),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Editar", color = Color(0xFF1565C0))
                            }

                            TextButton(
                                onClick = { eliminarObjetivo(objetivo.idObjetivo) }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color(0xFFE57373),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Eliminar", color = Color(0xFFE57373))
                            }
                        }
                    }
                }
            }
        }
    }
}