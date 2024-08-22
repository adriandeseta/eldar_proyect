package com.example.eldar_proyect.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserData(context: Context) : SQLiteOpenHelper(context, "usuarios.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE usuarios(" +
                "idUser INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user TEXT," + "password TEXT)")
    }

    fun userRegister(userData: UserData, user: String, password: String): String {
        val db = userData.writableDatabase
        val values = ContentValues().apply {
            put("user", user)
            put("password", password)
        }
        val users = db.insert("usuarios", null, values)
        return users.toString()
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun showData(userData: UserData) : Cursor{
        val db = userData.readableDatabase
        val consultaSql = "SELECT * FROM usuarios"
        return db.rawQuery(consultaSql, null)
    }
}