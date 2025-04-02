package com.example.bookmanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.facens.ac1biblioteca.R

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "books.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE books (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, author TEXT, category TEXT, isRead INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS books")
        onCreate(db)
    }

    fun insertBook(title: String, author: String, category: String, isRead: Boolean) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("title", title)
        values.put("author", author)
        values.put("category", category)
        values.put("isRead", if (isRead) 1 else 0)
        db.insert("books", null, values)
        db.close()
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val bookList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.bookListView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, bookList)
        listView.adapter = adapter

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val title = findViewById<EditText>(R.id.titleInput).text.toString()
            val author = findViewById<EditText>(R.id.authorInput).text.toString()
            val category = findViewById<Spinner>(R.id.categorySpinner).selectedItem.toString()
            val isRead = findViewById<CheckBox>(R.id.readCheckBox).isChecked

            dbHelper.insertBook(title, author, category, isRead)
            bookList.add("$title - $author ($category)")
            adapter.notifyDataSetChanged()
        }
    }
}
