package com.example.appproyecto.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MenuPantalla(navController: NavController, username: String) {
    // Colores personalizados
    val fondoColor = Color(0xFFFDF6E3)
    val lineaColor = Color(0xFFB3CDE0)
    val bordeColor = Color(0xFFDDDDDD)
    val primaryColor = Color(0xFFB26D00)
    val secondaryColor = Color(0xFF8C5600)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoColor)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Bienvenido, $username",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                textAlign = TextAlign.Center
            )

            Divider(color = lineaColor, thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))

            BotonMenu(texto = "Anotaciones", onClick = { navController.navigate("anotacion") }, primaryColor)
            BotonMenu(texto = "Notas rápidas", onClick = { navController.navigate("nota") }, primaryColor)
            BotonMenu(texto = "Objetivos", onClick = { navController.navigate("objetivo") }, primaryColor)
            BotonMenu(texto = "Progreso de objetivos", onClick = { navController.navigate("progreso") }, primaryColor)
            BotonMenu(texto = "Resumen del día", onClick = { navController.navigate("resumen") }, primaryColor)
        }
    }
}

@Composable
fun BotonMenu(texto: String, onClick: () -> Unit, colorFondo: Color) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorFondo),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Text(
            text = texto,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
