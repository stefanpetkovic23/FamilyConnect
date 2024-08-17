package com.example.familyconnectv2.sqlBudget

object CategoryItemTable {
    const val TABLE_NAME = "category_item"
    const val COLUMN_ID = "id"
    const val COLUMN_CATEGORY_ID = "category_id"
    const val COLUMN_NAME = "name"
    const val COLUMN_AMOUNT = "amount"

    const val CREATE_TABLE = (
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_CATEGORY_ID INTEGER NOT NULL," +
                    "$COLUMN_NAME TEXT NOT NULL," +
                    "$COLUMN_AMOUNT REAL NOT NULL," +
                    "FOREIGN KEY($COLUMN_CATEGORY_ID) REFERENCES ${CategoryTable.TABLE_NAME}(${CategoryTable.COLUMN_ID}) ON DELETE CASCADE)"
            )
}