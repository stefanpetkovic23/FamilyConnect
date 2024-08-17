package com.example.familyconnectv2.sqlBudget

object BudgetTable {
    const val TABLE_NAME = "budget"
    const val COLUMN_ID = "id"
    const val COLUMN_AMOUNT = "amount"

    const val CREATE_TABLE = (
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_AMOUNT REAL NOT NULL)"
            )
}