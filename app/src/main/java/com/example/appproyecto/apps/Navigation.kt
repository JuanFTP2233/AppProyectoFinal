package com.example.appproyecto.apps

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginPantalla(navController) }
        composable("registro") { RegistroPantalla(navController) }
        composable("menu") { MenuPantalla(navController, username = String()) }
        composable("anotacion") { AnotacionesPersonalPantalla(navController) }
        composable("nota") { NotasRapidasPantalla(navController) }
        composable("objetivo") { ObjetivosPantalla(navController) }
        composable("progreso") { ProgresoObjetivosPantalla(navController) }
        composable("resumen") { ResumenProgresoPantalla(navController) }
    }
}