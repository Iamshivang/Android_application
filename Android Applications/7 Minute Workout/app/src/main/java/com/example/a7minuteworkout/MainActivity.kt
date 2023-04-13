package com.example.a7minuteworkout

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.example.a7minuteworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding?= null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

//        val fLStartButton: FrameLayout= findViewById(R.id.flStart)
        binding?.flStart?.setOnClickListener{
            Toast.makeText(this@MainActivity, "Let's go Buddy",Toast.LENGTH_SHORT).show()
            val intent= Intent(this@MainActivity, ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding?.flBMI?.setOnClickListener{
            val intent= Intent(this@MainActivity, BMIActivity::class.java)
            startActivity(intent)
        }

        binding?.flHistory?.setOnClickListener{
            val intent= Intent(this@MainActivity, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        binding= null
    }
}