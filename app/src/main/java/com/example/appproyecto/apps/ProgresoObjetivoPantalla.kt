package com.example.appproyecto.apps

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProgresoObjetivosPantalla(navController: NavController) {
    val metaDias = 10
    val hoy = LocalDate.now()

    var objetivos by remember {
        mutableStateOf(
            mutableListOf(
                mutableMapOf(
                    "id" to "1",
                    "titulo" to "Leer un libro",
                    "seguimientos" to mutableListOf<LocalDate>()
                ),
                mutableMapOf(
                    "id" to "2",
                    "titulo" to "Hacer ejercicio",
                    "seguimientos" to mutableListOf<LocalDate>()
                )
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF0F0F0))
    ) {
        Text("Seguimiento de Objetivos", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(objetivos) { objetivo ->
                val titulo = objetivo["titulo"] as String
                val id = objetivo["id"] as String
                val seguimientos = objetivo["seguimientos"] as MutableList<LocalDate>
                val progreso = (seguimientos.size.toFloat() / metaDias).coerceAtMost(1f)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = progreso,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF4CAF50)
                        )
                        Text("${(progreso * 100).toInt()}% completado")

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = {
                            if (!seguimientos.contains(hoy)) {
                                seguimientos.add(hoy)
                            }
                        }) {
                            Text("✔ Marcar seguimiento de hoy")
                        }
                    }
                }
            }
        }
    }
}




