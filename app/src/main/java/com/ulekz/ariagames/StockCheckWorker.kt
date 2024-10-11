package com.ulekz.ariagames

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class StockCheckWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val dbHelper = UserDatabaseHelper(context)

    override fun doWork(): Result {
        // Verificar el inventario
        val itemsWithLowStock = checkLowStock()

        // Si hay items con bajo stock, enviamos una notificación
        if (itemsWithLowStock.isNotEmpty()) {
            sendNotification("Inventario Bajo", "Los siguientes artículos necesitan reabastecimiento: ${itemsWithLowStock.joinToString(", ")}")
        }

        return Result.success()
    }

    // Verifica el stock en la base de datos
    private fun checkLowStock(): List<String> {
        val db = dbHelper.readableDatabase
        val lowStockItems = mutableListOf<String>()
        val cursor = db.query(
            InventoryContract.InventoryEntry.TABLE_NAME,
            arrayOf(InventoryContract.InventoryEntry.COLUMN_NAME_ITEM, InventoryContract.InventoryEntry.COLUMN_NAME_QUANTITY),
            null, null, null, null, null
        )

        while (cursor.moveToNext()) {
            val item = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_NAME_ITEM))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_NAME_QUANTITY))
            if (quantity <= 2) {  // Umbral de stock bajo
                lowStockItems.add(item)
            }
        }

        cursor.close()
        return lowStockItems
    }

    // Función para enviar notificaciones locales
    private fun sendNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "stock_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Stock Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
