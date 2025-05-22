package com.example.appproyecto.apps

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appproyecto.modelo.Objetivo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProgresoObjetivosPantalla(
    navController: NavController,
    objetivos: List<Objetivo> = emptyList(),
    progresoManual: Map<Int, Int> = emptyMap()
) {
    val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun calcularDiasTotales(objetivo: Objetivo): Int {
        return ChronoUnit.DAYS.between(objetivo.fechaInicio, objetivo.fechaFin).toInt() + 1
    }

    fun obtenerProgresoManual(objetivoId: Int): Int {
        return progresoManual[objetivoId] ?: 0
    }

    fun calcularProgresoFinal(objetivo: Objetivo): Float {
        val progresoManualDias = obtenerProgresoManual(objetivo.idObjetivo)
        val diasTotales = calcularDiasTotales(objetivo)
        return (progresoManualDias.toFloat() / diasTotales).coerceIn(0f, 1f)
    }

    // Estadísticas generales
    val totalObjetivos = objetivos.size
    val objetivosCompletados = objetivos.count { it.completado }
    val objetivosEnProgreso = objetivos.count { !it.completado }
    val progresoPromedio = if (objetivos.isNotEmpty()) {
        objetivos.map { calcularProgresoFinal(it) }.average().toFloat()
    } else 0f

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

        // Título principal
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
                    text = "📊 Progreso de Objetivos",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Visualiza tu progreso y estadísticas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjetas de estadísticas generales
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Total de objetivos
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "📋",
                        fontSize = 24.sp
                    )
                    Text(
                        "$totalObjetivos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                    Text(
                        "Total",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Objetivos completados
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "✅",
                        fontSize = 24.sp
                    )
                    Text(
                        "$objetivosCompletados",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                    Text(
                        "Completados",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Objetivos en progreso
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "⏳",
                        fontSize = 24.sp
                    )
                    Text(
                        "$objetivosEnProgreso",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF9800)
                    )
                    Text(
                        "En Progreso",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Progreso promedio
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "🎯 Progreso Promedio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                    Text(
                        "${(progresoPromedio * 100).toInt()}%",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { progresoPromedio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF1565C0),
                    trackColor = Color(0xFFE0E0E0)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Lista de objetivos
        if (objetivos.isNotEmpty()) {
            Text(
                "📋 Detalle de Objetivos",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1565C0)
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(objetivos) { objetivo ->
                    val diasTotales = calcularDiasTotales(objetivo)
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
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colorFondo),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, colorBorde)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Título y estado
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        objetivo.titulo,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color(0xFF1565C0)
                                    )
                                    if (objetivo.descripcion.isNotBlank()) {
                                        Text(
                                            objetivo.descripcion,
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }

                                // Badge de estado
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

                            // Fechas
                            Row(
                                modifier = Modifier.fillMaxWidth(),
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

                            Spacer(modifier = Modifier.height(12.dp))

                            // Progreso
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "📊 Progreso",
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

                                // Información adicional
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "⏱️ Días restantes: ${if (diasRestantes >= 0) diasRestantes else "Vencido"}",
                                        fontSize = 10.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Text(
                                        "📈 Duración: $diasTotales días",
                                        fontSize = 10.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Mensaje cuando no hay objetivos
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "📋",
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No hay objetivos registrados",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Crea tu primer objetivo para ver el progreso aquí",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}


