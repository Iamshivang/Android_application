package com.example.colorguess

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION= 1
        private const val DATABASE_NAME= "EmployeeDatabase"
        private const val TABLE_RESULT= "UsersTable"

        private const val KEY_ID= "id"
        private const val KEY_NAME= "name"
        private const val KEY_DATE= "date"
        private const val KEY_RESULT= "result"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE= ("CREATE TABLE $TABLE_RESULT($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_DATE TEXT, $KEY_RESULT TEXT)")
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_RESULT")
        onCreate(db)
    }

    fun addUser(emp: user): Long
    {
        val db= this.writableDatabase

        val contentValues= ContentValues()

        contentValues.put(KEY_NAME, emp.name)
        contentValues.put(KEY_DATE, emp.date)
        contentValues.put(KEY_RESULT, emp.result)

        val success= db.insert(TABLE_RESULT, null, contentValues)
        db.close()
        return success
    }

    fun viewUser(): ArrayList<user>
    {
        val empList: ArrayList<user> = ArrayList()
        val selectQuery= "SELECT * FROM $TABLE_RESULT"
        val db= this.readableDatabase
        var cursor: Cursor?= null

        try {
            cursor= db.rawQuery(selectQuery, null)
        }catch (e: SQLException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var date: String
        var result: String

        if(cursor.moveToFirst())
        {
            do {
                id= cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                name= cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME))
                date= cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE))
                result= cursor.getString(cursor.getColumnIndexOrThrow(KEY_RESULT))

                val emp= user(id, name, date, result)
                empList.add(emp)
            }while (cursor.moveToNext())
        }
        cursor.close()
        empList.reverse()
        return empList
    }
}