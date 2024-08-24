package com.example.eldar_proyect.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserData(context: Context) : SQLiteOpenHelper(context, "usuarios.db", null, 2) { // Cambiar versión a 2 para manejar la actualización

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla de usuarios
        db.execSQL("CREATE TABLE usuarios(" +
                "idUser INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user TEXT," +
                "password TEXT)")

        // Crear tabla de tarjetas
        db.execSQL("CREATE TABLE tarjetas(" +
                "idCard INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "name TEXT," +
                "surname TEXT," +
                "cardNumber TEXT," +
                "cardType TEXT," +
                "FOREIGN KEY(userId) REFERENCES usuarios(idUser))")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Manejar la actualización de la base de datos
        if (oldVersion < 2) {
            // Crear tabla de tarjetas en la nueva versión
            db.execSQL("CREATE TABLE tarjetas(" +
                    "idCard INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userId INTEGER," +
                    "name TEXT," +
                    "surname TEXT," +
                    "cardNumber TEXT," +
                    "cardType TEXT," +
                    "FOREIGN KEY(userId) REFERENCES usuarios(idUser))")
        }
    }

    fun userRegister(user: String, password: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("user", user)
            put("password", password)
        }
        return db.insert("usuarios", null, values)
    }

    fun addCard(userId: Int, name: String, surname: String, cardNumber: String, cardType: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("userId", userId)
            put("name", name)
            put("surname", surname)
            put("cardNumber", cardNumber)
            put("cardType", cardType)
        }
        db.insert("tarjetas", null, values)
    }

    fun getAllUsers(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM usuarios", null)
    }

    fun getCardsByUser(userId: Int): Cursor {
        val db = readableDatabase
        val query = "SELECT * FROM tarjetas WHERE userId = ?"
        return db.rawQuery(query, arrayOf(userId.toString()))
    }

    fun getAllCards(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM tarjetas", null)
    }
}
