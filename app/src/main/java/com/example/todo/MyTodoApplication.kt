package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MyTodoApplication : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
    }
}