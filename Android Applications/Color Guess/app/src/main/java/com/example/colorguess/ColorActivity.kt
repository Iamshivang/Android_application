package com.example.colorguess

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import kotlinx.android.synthetic.main.activity_color.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ColorActivity : AppCompatActivity() {

    lateinit var mUserName: String
    var mSequence: String= ""
    private var doubleBackToExitPressedOnce= false

    private lateinit var mColorList: ArrayList<colour>
    private var restTimer: CountDownTimer?= null
    private  var restProgress= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color)

        if(intent.hasExtra(Constants.USER_NAME))
        {
            mUserName= intent.getStringExtra(Constants.USER_NAME).toString()
        }
        val typeface: Typeface = Typeface.createFromAsset(assets, "Sweets Smile.ttf")
        tv_memorize.typeface = typeface

        mColorList= ArrayList()
        for (color in Constants.getColor()){
            mColorList.add(color)
        }
        mColorList.shuffle()

        Log.e("Color List", mColorList.toString())

        setRestProgressBar()
    }

    private fun setRestProgressBar(){
        flRestView.visibility= View.VISIBLE
        card_view.visibility= View.GONE

        if (restTimer!= null)
        {
            restTimer?.cancel()
            restProgress= 0
        }
        setProgressBar()
    }

    private fun setProgressBar()
    {
        progressBar.progress= restProgress

        restTimer= object: CountDownTimer(5000, 1000)
        {
            override fun onTick(p0: Long) {
                restProgress++
                progressBar.progress= 5- restProgress
                tvTimer.text= (5-restProgress).toString()
            }

            override fun onFinish() {
                showColor()
            }

        }.start()
    }

    private fun showColor(){
        flRestView.visibility= View.GONE
        card_view.visibility= View.VISIBLE

        CoroutineScope(Dispatchers.Main).launch {
            for (color in mColorList) {
                card_view.setBackgroundColor(color.name.toColorInt())
                delay(1000)
                mSequence += color.index.toString()
            }
            Log.e(mSequence, mSequence)
            val intent= Intent(this@ColorActivity, ResultActivity::class.java)
            intent.putExtra(Constants.USER_NAME, mUserName)
            intent.putExtra(Constants.SEQUENCE, mSequence)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {

        alertDialogForBack()
    }

    private fun alertDialogForBack() {
        val builder = AlertDialog.Builder(this@ColorActivity)
        builder.setMessage("Did you really want to QUIT?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss()
            super.onBackPressed()
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (restTimer!= null)
        {
            restTimer?.cancel()
            restProgress= 0
        }
    }

}