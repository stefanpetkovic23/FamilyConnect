package com.example.familyconnectv2.sqlBudget

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BudgetDatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BudgetDB"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(BudgetTable.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${BudgetTable.TABLE_NAME}")
        onCreate(db)
    }

    fun addAmountToBudget(amount: Double): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(BudgetTable.COLUMN_AMOUNT, amount)

        val id = db.insert(BudgetTable.TABLE_NAME, null, values)
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun getBudget(): Budget? {
        val db = this.readableDatabase
        var budget: Budget? = null
        val cursor: Cursor?
        try {
            cursor = db.rawQuery("SELECT * FROM ${BudgetTable.TABLE_NAME}", null)
        } catch (e: Exception) {
            db.execSQL(BudgetTable.CREATE_TABLE)
            return null
        }

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(BudgetTable.COLUMN_ID))
            val amount = cursor.getDouble(cursor.getColumnIndex(BudgetTable.COLUMN_AMOUNT))
            budget = Budget(id, amount)
        }
        cursor.close()
        db.close()
        return budget
    }
}