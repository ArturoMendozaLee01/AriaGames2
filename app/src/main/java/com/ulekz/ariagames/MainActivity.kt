package com.ulekz.ariagames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.work.*
import com.ulekz.ariagames.ui.theme.AriaGamesTheme
import java.util.concurrent.TimeUnit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.WorkManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de notificaciones automáticas
        setupNotificationWorker()

        val dbHelper = UserDatabaseHelper(this)

        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            val navController = rememberNavController()

            AriaGamesTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLogin = { email, password ->
                                    if (dbHelper.validateUser(email, password)) {
                                        navController.navigate("dashboard")
                                    }
                                },
                                onRegisterClick = { navController.navigate("register") },
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { isDarkMode = it }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                onRegister = { username, email, password ->
                                    dbHelper.addUser(username, email, password)
                                    navController.navigate("login")
                                },
                                onBackToLoginClick = { navController.navigate("login") },
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { isDarkMode = it }
                            )
                        }

                        composable("dashboard") {
                            DashboardScreen(
                                onAddToInventory = { navController.navigate("category") },
                                onRemoveFromInventory = { navController.navigate("removeInventory") },
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { isDarkMode = it },
                                onLogout = { navController.navigate("login") },
                                onConsultarTipoDeCambio = { navController.navigate("tipoDeCambio") } // Nueva función de navegación
                            )
                        }

                        composable("category") {
                            CategoryScreen(
                                onCategorySelected = { category ->
                                    navController.navigate("subcategory/$category")
                                },
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { isDarkMode = it }
                            )
                        }

                        composable("subcategory/{category}") { backStackEntry ->
                            val category = backStackEntry.arguments?.getString("category") ?: ""
                            SubcategoryScreen(
                                category = category,
                                onSubcategorySelected = { subcategory, quantity ->
                                    dbHelper.updateInventory(subcategory, category, quantity)
                                    navController.navigate("dashboard")
                                },
                                dbHelper = dbHelper,
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { isDarkMode = it },
                                navController = navController
                            )
                        }

                        composable("removeInventory") {
                            RemoveFromInventoryScreen(
                                dbHelper = dbHelper,
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { isDarkMode = it },
                                navController = navController
                            )
                        }

                        // Falta Aquí actualizamos el llamado a InventoryScreen
                        composable("inventoryScreen") {
                            InventoryScreen(
                                dbHelper = dbHelper,
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { isDarkMode = it }
                            )
                        }

                        // Pantalla para consultar el tipo de cambio
                        composable("tipoDeCambio") {
                            TipoDeCambioScreen(
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { isDarkMode = it }
                            )
                        }
                    }
                }
            }
        }
    }

    // Configuración del Worker para notificaciones periódicas
    private fun setupNotificationWorker() {
        val workRequest = PeriodicWorkRequestBuilder<InventoryCheckWorker>(
            1, TimeUnit.HOURS // Configurado para que verifique el inventario cada 1 hora
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "InventoryCheckWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
