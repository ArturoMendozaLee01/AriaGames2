package com.ulekz.ariagames

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class InventoryCheckWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Verificar si el permiso de notificaciones está disponible (solo para Android 13 o superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.POST_NOTIFICATIONS)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                return Result.failure()
            }
        }

        // Verificar el inventario y crear la notificación
        checkInventoryAndNotify()
        return Result.success()
    }

    private fun checkInventoryAndNotify() {
        val lowStockItems = getLowStockItems() // Método que obtendría los ítems con bajo stock

        if (lowStockItems.isNotEmpty()) {
            // Crear la notificación
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notification = NotificationCompat.Builder(applicationContext, "inventory_channel")
                .setSmallIcon(R.drawable.ic_launcher) // Aquí debería ir el ícono de la notificación
                .setContentTitle("Inventario Bajo")
                .setContentText("Hay ítems con bajo stock en tu inventario.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            notificationManager.notify(1, notification)
        }
    }

    private fun getLowStockItems(): List<String> {
        // Aquí deberías implementar la lógica que verifica el inventario
        // y retorna una lista de ítems con bajo stock.
        return listOf("Item A", "Item B") // Ejemplo
    }
}
