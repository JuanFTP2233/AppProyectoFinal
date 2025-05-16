package com.example.appproyecto.apps

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotasRapidasPantalla(navController: NavController) {
    val context = LocalContext.current
    var nota by remember { mutableStateOf("") }
    var esRecordatorio by remember { mutableStateOf(false) }
    var notas by remember { mutableStateOf(listOf<Map<String, Any>>()) }

    // Lanzador de permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> }
    )

    // Crear canal y pedir permiso si es necesario
    LaunchedEffect(Unit) {
        crearCanalDeNotificacion(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9))
            .padding(20.dp)
    ) {
        Text(
            "Notas Rápidas",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = Color(0xFF33691E)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = nota,
            onValueChange = { nota = it },
            placeholder = { Text("Escribe tu nota aquí") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE6EE9C),
                unfocusedContainerColor = Color(0xFFDCEDC8),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = esRecordatorio,
                onCheckedChange = { esRecordatorio = it },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF558B2F))
            )
            Text("Marcar como recordatorio", color = Color(0xFF33691E))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (nota.isNotBlank()) {
                    val nuevaNota = mapOf(
                        "id" to UUID.randomUUID().toString(),
                        "contenido" to nota,
                        "fechaCreacion" to LocalDate.now(),
                        "esRecordatorio" to esRecordatorio
                    )
                    notas = notas + nuevaNota
                    if (esRecordatorio) {
                        enviarNotificacion(context, nota)
                    }
                    nota = ""
                    esRecordatorio = false
                }
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF558B2F))
        ) {
            Text("Guardar", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(notas) { notaItem ->
                val fondo = if (notaItem["esRecordatorio"] == true) Color(0xFFFFF9C4) else Color(0xFFDCEDC8)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = fondo)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = notaItem["contenido"] as String,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Fecha: ${(notaItem["fechaCreacion"] as LocalDate).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                        if (notaItem["esRecordatorio"] == true) {
                            Text(
                                text = "🔔 Recordatorio",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBF360C)
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = {
                                notas = notas.filterNot { it["id"] == notaItem["id"] }
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

fun crearCanalDeNotificacion(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val canal = NotificationChannel(
            "canal_rapidas",
            "Notas Rápidas",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Canal para notas rápidas marcadas como recordatorio"
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(canal)
    }
}

fun enviarNotificacion(context: Context, contenido: String) {
    val builder = NotificationCompat.Builder(context, "canal_rapidas")
        .setSmallIcon(android.R.drawable.ic_popup_reminder)
        .setContentTitle("🔔 Nota marcada como recordatorio")
        .setContentText(contenido)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        try {
            notify(System.currentTimeMillis().toInt(), builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
