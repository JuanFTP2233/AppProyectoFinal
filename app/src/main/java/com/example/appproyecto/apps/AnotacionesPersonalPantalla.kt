package com.example.appproyecto.apps

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnotacionesPersonalPantalla(navController: NavController) {
    var textoTitulo by remember { mutableStateOf("") }
    var textoContenido by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var editandoNotaId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Anotaciones",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        TextField(
            value = textoTitulo,
            onValueChange = { textoTitulo = it },
            placeholder = { Text("Título") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFFF8DC),
                unfocusedContainerColor = Color(0xFFFFF8DC),
                cursorColor = Color(0xFF8B4513)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        TextField(
            value = textoContenido,
            onValueChange = { textoContenido = it },
            placeholder = { Text("Contenido de la nota") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFFF8DC),
                unfocusedContainerColor = Color(0xFFFFF8DC),
                cursorColor = Color(0xFF8B4513)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (textoTitulo.isNotBlank() && textoContenido.isNotBlank()) {
                        if (editandoNotaId != null) {
                            notas = notas.map {
                                if (it["id"] == editandoNotaId) {
                                    it.toMutableMap().apply {
                                        put("titulo", textoTitulo)
                                        put("contenido", textoContenido)
                                    }
                                } else it
                            }
                            editandoNotaId = null
                        } else {
                            val nuevaNota = mapOf(
                                "id" to UUID.randomUUID().toString(),
                                "titulo" to textoTitulo,
                                "contenido" to textoContenido,
                                "fechaHora" to LocalDateTime.now()
                            )
                            notas = notas + nuevaNota
                        }
                        textoTitulo = ""
                        textoContenido = ""
                    }
                }
            ) {
                Text(if (editandoNotaId != null) "Actualizar" else "Guardar")
            }

            if (editandoNotaId != null) {
                Button(
                    onClick = {
                        textoTitulo = ""
                        textoContenido = ""
                        editandoNotaId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancelar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(notas) { nota ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            textoTitulo = nota["titulo"] as String
                            textoContenido = nota["contenido"] as String
                            editandoNotaId = nota["id"] as String
                        },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8DC)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = nota["titulo"] as String,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF8B4513)
                        )
                        Text(
                            text = nota["contenido"] as String,
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        val fechaHora = nota["fechaHora"] as LocalDateTime
                        Text(
                            text = "Guardado: ${
                                fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                            }",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = {
                                notas = notas.filterNot { it["id"] == nota["id"] }
                                if (editandoNotaId == nota["id"]) {
                                    textoTitulo = ""
                                    textoContenido = ""
                                    editandoNotaId = null
                                }
                            }) {
                                Text("Borrar", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}
