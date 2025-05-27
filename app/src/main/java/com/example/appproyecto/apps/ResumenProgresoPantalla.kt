package com.example.appproyecto.apps

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appproyecto.modelo.Objetivo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResumenProgresoPantalla(navController: NavController) {
    // Aquí conectarás tus datos reales
    val totalObjetivos = 5
    val objetivosCompletados = 2
    val objetivosVencidos = 1
    val objetivosUrgentes = 1

    val totalNotasRapidas = 8
    val recordatorios = 3

    val totalAnotaciones = 4

    val porcentajeCompletado = if (totalObjetivos > 0) (objetivosCompletados * 100) / totalObjetivos else 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF3E5F5),
                        Color(0xFFE8F5E8),
                        Color(0xFFFFF3E0)
                    )
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header principal
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📊 Resumen de Progreso",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = Color(0xFF1565C0)
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tu productividad en un vistazo",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progreso general circular
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(120.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { porcentajeCompletado / 100f },
                            modifier = Modifier.size(120.dp),
                            color = when {
                                porcentajeCompletado >= 80 -> Color(0xFF4CAF50)
                                porcentajeCompletado >= 50 -> Color(0xFFFF9800)
                                else -> Color(0xFFE57373)
                            },
                            strokeWidth = 12.dp,
                            trackColor = Color(0xFFE0E0E0)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$porcentajeCompletado%",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1565C0)
                            )
                            Text(
                                text = "Completado",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        item {
            // Tarjetas de estadísticas principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EstadisticaCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Objetivos",
                    valor = totalObjetivos.toString(),
                    icono = Icons.Default.Favorite,
                    color = Color(0xFF1565C0),
                    subtitulo = "$objetivosCompletados completados"
                )

                EstadisticaCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Notas Rápidas",
                    valor = totalNotasRapidas.toString(),
                    icono = Icons.Default.Home,
                    color = Color(0xFF558B2F),
                    subtitulo = "$recordatorios recordatorios"
                )

                EstadisticaCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Anotaciones",
                    valor = totalAnotaciones.toString(),
                    icono = Icons.Default.Menu,
                    color = Color(0xFF8B4513),
                    subtitulo = "personales"
                )
            }
        }

        item {
            // Detalle de objetivos
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "🎯 Estado de Objetivos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    EstadoObjetivo(
                        titulo = "Completados",
                        cantidad = objetivosCompletados,
                        total = totalObjetivos,
                        color = Color(0xFF4CAF50),
                        icono = "✅"
                    )

                    EstadoObjetivo(
                        titulo = "Urgentes",
                        cantidad = objetivosUrgentes,
                        total = totalObjetivos,
                        color = Color(0xFFFF9800),
                        icono = "⏰"
                    )

                    EstadoObjetivo(
                        titulo = "Vencidos",
                        cantidad = objetivosVencidos,
                        total = totalObjetivos,
                        color = Color(0xFFE57373),
                        icono = "⚠️"
                    )
                }
            }
        }

        item {
            // Actividad reciente
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "📈 Resumen de Actividad",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ActividadItem(
                        icono = Icons.Default.AddCircle,
                        titulo = "Objetivos Activos",
                        descripcion = "${totalObjetivos - objetivosCompletados} en progreso",
                        color = Color(0xFF1565C0)
                    )

                    ActividadItem(
                        icono = Icons.Default.Notifications,
                        titulo = "Recordatorios Pendientes",
                        descripcion = "$recordatorios notas marcadas",
                        color = Color(0xFFFF9800)
                    )

                    ActividadItem(
                        icono = Icons.Default.Create,
                        titulo = "Última Anotación",
                        descripcion = if (totalAnotaciones > 0) "Guardada hoy" else "Sin anotaciones",
                        color = Color(0xFF8B4513)
                    )
                }
            }
        }

        item {
            // Motivación
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        porcentajeCompletado >= 80 -> Color(0xFFE8F5E8)
                        porcentajeCompletado >= 50 -> Color(0xFFFFF3E0)
                        else -> Color(0xFFFFEBEE)
                    }
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when {
                            porcentajeCompletado >= 80 -> "🎉 ¡Excelente trabajo!"
                            porcentajeCompletado >= 50 -> "💪 ¡Vas por buen camino!"
                            objetivosCompletados > 0 -> "🌟 ¡Sigue así!"
                            else -> "🚀 ¡Es hora de empezar!"
                        },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            porcentajeCompletado >= 80 -> Color(0xFF2E7D32)
                            porcentajeCompletado >= 50 -> Color(0xFFE65100)
                            else -> Color(0xFFC62828)
                        },
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = when {
                            porcentajeCompletado >= 80 -> "Has completado la mayoría de tus objetivos. ¡Increíble productividad!"
                            porcentajeCompletado >= 50 -> "Estás avanzando bien. Un poco más de esfuerzo y llegarás a la meta."
                            objetivosCompletados > 0 -> "Tienes algunos logros. ¡Continúa con ese momentum!"
                            else -> "Aún no has completado objetivos. ¡Tu viaje productivo está por comenzar!"
                        },
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun EstadisticaCard(
    modifier: Modifier = Modifier,
    titulo: String,
    valor: String,
    icono: ImageVector,
    color: Color,
    subtitulo: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = valor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Text(
                text = titulo,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Text(
                text = subtitulo,
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EstadoObjetivo(
    titulo: String,
    cantidad: Int,
    total: Int,
    color: Color,
    icono: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icono,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 12.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            val porcentaje = if (total > 0) (cantidad * 100) / total else 0
            Text(
                text = "$cantidad de $total ($porcentaje%)",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        LinearProgressIndicator(
            progress = { if (total > 0) cantidad.toFloat() / total else 0f },
            modifier = Modifier
                .width(80.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color(0xFFE0E0E0)
        )
    }
}

@Composable
fun ActividadItem(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Text(
                text = descripcion,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}