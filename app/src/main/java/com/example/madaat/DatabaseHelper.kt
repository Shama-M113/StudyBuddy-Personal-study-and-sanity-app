package com.example.madaat

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "UserDB", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT,
                email TEXT,
                password TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
        val createTasksTable = """
        CREATE TABLE tasks (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            email TEXT,  -- To link task to user
            task TEXT,
            date TEXT,
            isCompleted INTEGER DEFAULT 0
        )
    """.trimIndent()
        db.execSQL(createTasksTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS tasks") // <-- ADD THIS LINE
        onCreate(db)
    }

    fun insertUser(username: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("username", username)
            put("email", email)
            put("password", password)
        }
        val result = db.insert("users", null, values)
        return result != -1L
    }

    fun checkLogin(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", arrayOf(email, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    fun getAllUsers(): List<String> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users", null)
        val users = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            do {
                val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                users.add("Username: $username, Email: $email")
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }
    fun insertTask(email: String, task: String, date: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("email", email)
            put("task", task)
            put("date", date)
            put("isCompleted", 0)
        }
        val result = db.insert("tasks", null, values)
        return result != -1L
    }
    fun getTasks(email: String, date: String): List<TaskModel> {
        val taskList = mutableListOf<TaskModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks WHERE email = ? AND date = ?", arrayOf(email, date))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val taskText = cursor.getString(cursor.getColumnIndexOrThrow("task"))
                val taskDate = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val isDone = cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted")) // column name matches DB

                taskList.add(TaskModel(id, email, taskText, taskDate, isDone))
            } while (cursor.moveToNext())
        }
        Log.d("DB_DEBUG", "Fetching tasks for $email on $date. Found ${taskList.size} tasks.")

        cursor.close()
        return taskList
    }

    fun updateTaskDone(taskId: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("isCompleted", 1)
        }
        db.update("tasks", values, "id = ?", arrayOf(taskId.toString()))
    }
    fun deleteTask(taskId: Int) {
        val db = this.writableDatabase
        db.delete("tasks", "id = ?", arrayOf(taskId.toString()))
    }


}
