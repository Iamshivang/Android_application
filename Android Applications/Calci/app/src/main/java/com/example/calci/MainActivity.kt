package com.example.calci

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private  var tvInput: TextView?= null
//    private var btnOne: Button?= null
    var lastDot: Boolean= false
    var lastNumeric: Boolean= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvInput= findViewById(R.id.tvInput)
//        btnOne= findViewById(R.id.btnOne)
//        btnOne?.setOnClickListener(){
//            tvInput?.append("1")
//        }
    }

    fun onDigit(view: View)
    {
//        Toast.makeText(this, "Button Clicked", Toast.LENGTH_LONG).show()
        tvInput?.append((view as Button).text)
        lastNumeric= true
//        lastDot= false
    }

    fun onClear(view: View)
    {
        tvInput?.text= ""
        lastDot= false
        lastNumeric= false
    }

    fun onDecimalPoint(view: View)
    {
        if(lastNumeric && !lastDot)
        {
            tvInput?.append(".")
            lastNumeric= false
            lastDot= true
        }
    }

    fun onOperator(view: View){

        if((view as Button).text == "-")
        {
            if(tvInput?.text?.toString() == "")
            {
                lastNumeric= true
            }
        }

        tvInput?.text?.let {
            if(lastNumeric && !isOperatorAdded(it.toString())){
                tvInput?.append((view as Button).text)
                lastNumeric= false
                lastDot= false
            }
        }

    }


    private fun isOperatorAdded(value: String): Boolean
    {
        return if(value.startsWith("-")){
            false
        }else{
            value.contains("/")
                    || value.contains("*")
                    || value.contains("+")
                    || value.contains("-")
        }
//        return (value.contains("/")
//                || value.contains("*")
//                || value.contains("+")
//                || value.contains("-"))
    }

    private fun removeZeroAfterDot(result: String): String{
        var value= result
        if(result.endsWith(".0")) {  // result.contains(".0")
            value = result.substring(0, result.length - 2)
        }
            return value
    }

    fun onEqual(view: View)
    {
        if(lastNumeric)
        {
            var tvValue= tvInput?.text.toString()  // otherwise it will throw sequence of char
            var prefix= ""

            try{              // our application could not crash
                if(tvValue.startsWith("-")){
                    prefix= "-"
                    tvValue= tvValue.substring(1)
                }
                if(tvValue.contains("-")){
                    val splitValue= tvValue.split("-")

                    var one= splitValue[0]
                    var two= splitValue[1]

                    if(prefix.isNotEmpty()){
                        one= prefix + one
                    }
                    tvInput?.text= removeZeroAfterDot((one.toDouble()- two.toDouble()).toString())
                }else if(tvValue.contains("+")){
                    val splitValue= tvValue.split("+")

                    var one= splitValue[0]
                    var two= splitValue[1]

                    if(prefix.isNotEmpty()){
                        one= prefix + one
                    }
                    tvInput?.text= removeZeroAfterDot((one.toDouble()+ two.toDouble()).toString())
                }else if(tvValue.contains("/")){
                    val splitValue= tvValue.split("/")

                    var one= splitValue[0]
                    var two= splitValue[1]

                    if(prefix.isNotEmpty()){
                        one= prefix + one
                    }
                    tvInput?.text= removeZeroAfterDot((one.toDouble()/ two.toDouble()).toString())
                }else if(tvValue.contains("*")){
                    val splitValue= tvValue.split("*")

                    var one= splitValue[0]
                    var two= splitValue[1]

                    if(prefix.isNotEmpty()){
                        one= prefix + one
                    }
                    tvInput?.text= removeZeroAfterDot((one.toDouble()* two.toDouble()).toString())
                }

            }catch (e: ArithmeticException){
                e.printStackTrace()

            }
        }
    }
}