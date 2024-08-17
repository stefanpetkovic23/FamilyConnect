package com.example.familyconnectv2.sqlBudget

object CategoryTable {
    const val TABLE_NAME = "category"
    const val COLUMN_ID = "id"
    const val COLUMN_NAME = "name"
    const val COLUMN_BUDGET_ID = "budget_id"

    const val CREATE_TABLE = (
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NAME TEXT NOT NULL," +
                    "$COLUMN_BUDGET_ID INTEGER NOT NULL," +
                    "FOREIGN KEY($COLUMN_BUDGET_ID) REFERENCES ${BudgetTable.TABLE_NAME}(${BudgetTable.COLUMN_ID}) ON DELETE CASCADE)"
            )
}