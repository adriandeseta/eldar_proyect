package com.example.eldar_proyect.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, "usuarios.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE usuarios(
                idUser INTEGER PRIMARY KEY AUTOINCREMENT,
                user TEXT NOT NULL,
                password TEXT NOT NULL
            )
            """
        )

        db.execSQL(
            """
            CREATE TABLE tarjetas(
                idCard INTEGER PRIMARY KEY AUTOINCREMENT,
                userId INTEGER NOT NULL,
                name TEXT NOT NULL,
                surname TEXT NOT NULL,
                cardNumber TEXT NOT NULL,
                cardType TEXT NOT NULL,
                FOREIGN KEY(userId) REFERENCES usuarios(idUser) ON DELETE CASCADE
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS tarjetas(
                    idCard INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    surname TEXT NOT NULL,
                    cardNumber TEXT NOT NULL,
                    cardType TEXT NOT NULL,
                    FOREIGN KEY(userId) REFERENCES usuarios(idUser) ON DELETE CASCADE
                )
                """
            )
        }
    }

    // Registrar nuevo usuario
    fun userRegister(user: String, password: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("user", user)
            put("password", password)
        }
        return db.insert("usuarios", null, values)
    }

    // Método para agregar una tarjeta
    fun addCard(userId: Int, name: String, surname: String, cardNumber: String, cardType: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("userId", userId)
            put("name", name)
            put("surname", surname)
            put("cardNumber", cardNumber) // Guardar número de tarjeta (mejor si lo encriptas)
            put("cardType", cardType)
        }
        db.insert("tarjetas", null, values)
    }

    // Obtener las tarjetas de un usuario
    fun getCardsByUser(userId: Int): Cursor {
        val db = readableDatabase
        val query = "SELECT * FROM tarjetas WHERE userId = ?"
        return db.rawQuery(query, arrayOf(userId.toString()))
    }

    // Obtener todos los usuarios
    fun getAllUsers(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM usuarios", null)
    }
}
