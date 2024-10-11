package com.ulekz.ariagames

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

object UserContract {
    object UserEntry : BaseColumns {
        const val TABLE_NAME = "users"
        const val COLUMN_NAME_USERNAME = "username"
        const val COLUMN_NAME_EMAIL = "email"
        const val COLUMN_NAME_PASSWORD = "password"
    }

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${UserEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${UserEntry.COLUMN_NAME_USERNAME} TEXT," +
                "${UserEntry.COLUMN_NAME_EMAIL} TEXT," +
                "${UserEntry.COLUMN_NAME_PASSWORD} TEXT)"

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${UserEntry.TABLE_NAME}"
}

object InventoryContract {
    object InventoryEntry : BaseColumns {
        const val TABLE_NAME = "inventory"
        const val COLUMN_NAME_ITEM = "item"
        const val COLUMN_NAME_CATEGORY = "category"
        const val COLUMN_NAME_QUANTITY = "quantity"
    }

    const val SQL_CREATE_INVENTORY_ENTRIES =
        "CREATE TABLE ${InventoryEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${InventoryEntry.COLUMN_NAME_ITEM} TEXT," +
                "${InventoryEntry.COLUMN_NAME_CATEGORY} TEXT," +
                "${InventoryEntry.COLUMN_NAME_QUANTITY} INTEGER)"

    const val SQL_DELETE_INVENTORY_ENTRIES = "DROP TABLE IF EXISTS ${InventoryEntry.TABLE_NAME}"
}

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(UserContract.SQL_CREATE_ENTRIES)
        db.execSQL(InventoryContract.SQL_CREATE_INVENTORY_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UserContract.SQL_DELETE_ENTRIES)
        db.execSQL(InventoryContract.SQL_DELETE_INVENTORY_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "UserDatabase.db"
    }

    fun addUser(username: String, email: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(UserContract.UserEntry.COLUMN_NAME_USERNAME, username)
            put(UserContract.UserEntry.COLUMN_NAME_EMAIL, email)
            put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, password)
        }
        val newRowId = db.insert(UserContract.UserEntry.TABLE_NAME, null, values)
        return newRowId != -1L
    }

    fun validateUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val projection = arrayOf(BaseColumns._ID)
        val selection = "${UserContract.UserEntry.COLUMN_NAME_EMAIL} = ? AND ${UserContract.UserEntry.COLUMN_NAME_PASSWORD} = ?"
        val selectionArgs = arrayOf(email, password)

        val cursor = db.query(
            UserContract.UserEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    // Actualizamos esta función para manejar tanto actualizaciones como inserciones
    fun updateInventory(item: String, category: String, quantity: Int): Boolean {
        val db = writableDatabase
        val cursor = db.query(
            InventoryContract.InventoryEntry.TABLE_NAME,
            arrayOf(InventoryContract.InventoryEntry.COLUMN_NAME_ITEM),
            "${InventoryContract.InventoryEntry.COLUMN_NAME_ITEM} = ? AND ${InventoryContract.InventoryEntry.COLUMN_NAME_CATEGORY} = ?",
            arrayOf(item, category),
            null, null, null
        )

        val values = ContentValues().apply {
            put(InventoryContract.InventoryEntry.COLUMN_NAME_ITEM, item)
            put(InventoryContract.InventoryEntry.COLUMN_NAME_CATEGORY, category)
            put(InventoryContract.InventoryEntry.COLUMN_NAME_QUANTITY, quantity)
        }

        return if (cursor.moveToFirst()) {
            // Si el ítem ya existe, lo actualizamos
            val updatedRows = db.update(
                InventoryContract.InventoryEntry.TABLE_NAME,
                values,
                "${InventoryContract.InventoryEntry.COLUMN_NAME_ITEM} = ? AND ${InventoryContract.InventoryEntry.COLUMN_NAME_CATEGORY} = ?",
                arrayOf(item, category)
            )
            cursor.close()
            Log.d("InventoryUpdate", "Updated $item in $category with quantity $quantity, rows affected: $updatedRows")
            updatedRows > 0
        } else {
            // Si no existe, lo insertamos
            val newRowId = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values)
            cursor.close()
            Log.d("InventoryUpdate", "Inserted $item in $category with quantity $quantity, newRowId: $newRowId")
            newRowId != -1L
        }
    }


    fun getItemQuantity(item: String, category: String): Int {
        val db = readableDatabase
        val projection = arrayOf(InventoryContract.InventoryEntry.COLUMN_NAME_QUANTITY)
        val selection = "${InventoryContract.InventoryEntry.COLUMN_NAME_ITEM} = ? AND ${InventoryContract.InventoryEntry.COLUMN_NAME_CATEGORY} = ?"
        val selectionArgs = arrayOf(item, category) // Asegúrate de que la categoría se pase correctamente

        val cursor = db.query(
            InventoryContract.InventoryEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var quantity = 0
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_NAME_QUANTITY))
        }

        cursor.close()
        return quantity
    }
}
