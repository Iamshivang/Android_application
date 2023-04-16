package com.example.colorguess

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var typeface1: Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpActionBar()
        typeface1 = Typeface.createFromAsset(assets, "Maleantes Tres-d.ttf")
        iv_guess_color.typeface= typeface1
        iv_guess_color1.typeface= typeface1

        btn_Play.setOnClickListener{

            val name: String= et_Name.text.toString().trim { it <= ' '}

            if(name.isEmpty()){
                Toast.makeText(this,
                    "Please enter your name!", Toast.LENGTH_LONG).show()
            }else{
                val intent= Intent(this, ColorActivity:: class.java)
                intent.putExtra(Constants.USER_NAME, et_Name.text.toString())
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.action_refresh ->{
                startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setUpActionBar()
    {
        setSupportActionBar(toolbar_main_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        toolbar_main_activity.setNavigationOnClickListener{ onBackPressed()}
    }

}