package com.example.appproyecto.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

// Colores personalizados estilo cuaderno
val fondoColor = Color(0xFFFDF6E3)
val lineaColor = Color(0xFFB3CDE0)
val bordeColor = Color(0xFFDDDDDD)
val primaryColor = Color(0xFFB26D00)
val secondaryColor = Color(0xFF8C5600)

@Composable
fun RegistroPantalla(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoColor)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Crear Cuenta",
                fontSize = 28.sp,
                color = primaryColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    errorMessage = ""
                },
                label = { Text("Nombre", color = secondaryColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(10.dp)),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = lineaColor,
                    focusedBorderColor = primaryColor,
                    cursorColor = primaryColor
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    errorMessage = ""
                },
                label = { Text("Correo", color = secondaryColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(10.dp)),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = lineaColor,
                    focusedBorderColor = primaryColor,
                    cursorColor = primaryColor
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = contraseña,
                onValueChange = {
                    contraseña = it
                    errorMessage = ""
                },
                label = { Text("Contraseña", color = secondaryColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(10.dp)),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = lineaColor,
                    focusedBorderColor = primaryColor,
                    cursorColor = primaryColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    when {
                        nombre.isBlank() || correo.isBlank() || contraseña.isBlank() -> {
                            errorMessage = "Por favor, completa todos los campos."
                        }

                        else -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                val registrado = registrarUsuarioEnBD(nombre, correo, contraseña)
                                withContext(Dispatchers.Main) {
                                    if (registrado) {
                                        navController.navigate("login") {
                                            popUpTo("registro") { inclusive = true }
                                        }
                                    } else {
                                        errorMessage = "El correo ya está registrado."
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                )
            ) {
                Text("Registrarse", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.navigate("login") }) {
                Text(
                    "¿Ya tienes cuenta? Inicia sesión aquí",
                    color = secondaryColor,
                    fontSize = 14.sp
                )
            }
        }
    }
}

fun registrarUsuarioEnBD(nombre: String, correo: String, contraseña: String): Boolean {
    return true
}
