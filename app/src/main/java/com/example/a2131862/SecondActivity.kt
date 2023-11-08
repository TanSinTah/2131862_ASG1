package com.example.a2131862

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)//Replace 'activity_second' with your layout XML file name

        val textViewSecond = findViewById<TextView>(R.id.textViewSecond)
        textViewSecond.text = "This is the Second Activity"

    }
}