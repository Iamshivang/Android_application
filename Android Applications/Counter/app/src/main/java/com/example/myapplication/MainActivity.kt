package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val click= findViewById<Button>(R.id.press)
        val text = findViewById<TextView>(R.id.count)
        var timescilck= 0

        click.setOnClickListener {
            timescilck += 1
            text.text = "Total count : " + timescilck.toString()
            click.text = "Click Me"
            Toast.makeText(this, "You CLICK successfully!", Toast.LENGTH_LONG).show()
        }
    }
}