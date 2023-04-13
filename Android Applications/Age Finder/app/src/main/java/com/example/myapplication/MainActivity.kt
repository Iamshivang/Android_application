package com.example.myapplication

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var selectedDate: TextView? = null
    private var ans: TextView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dateSelector: Button= findViewById(R.id.DateSelector)
        selectedDate= findViewById(R.id.selectedDate)
        ans= findViewById(R.id.ans)

        dateSelector.setOnClickListener{
            clickDataPicker()
        }
    }

    private fun clickDataPicker()
    {

        val myCalender= Calendar.getInstance()
        val year= myCalender.get(Calendar.YEAR)
        val month= myCalender.get(Calendar.MONTH)
        val day= myCalender.get(Calendar.DAY_OF_MONTH)
        val dpd= DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener{ _, selectedYear, selectedMonth, selectedDay->

                Toast.makeText(this, "Year was $selectedYear and month was ${selectedMonth+1}", Toast.LENGTH_LONG).show()

                val selectedDate1= "$selectedDay/${selectedMonth+1}/$selectedYear"
                selectedDate?.text= selectedDate1

                val sdf= SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val theDate= sdf.parse(selectedDate1)

                theDate?.let {
                    val selectedDateInMinutes= theDate.time / 60000
                    val currentDate= sdf.parse((sdf.format(System.currentTimeMillis())))
                    currentDate?.let {
                        val currentDateInMinutes=  currentDate.time / 60000
                        val differenceInMinutes= currentDateInMinutes- selectedDateInMinutes
                        ans?.text= differenceInMinutes.toString()
                    }

                }

            },
            year,
            month,
            day)

        dpd.datePicker.maxDate= System.currentTimeMillis()- 86400000
        dpd.show()

    }

}
//
//fun main()
//{
//    val myCalender= Calendar.getInstance()
//    val year= myCalender.get(Calendar.YEAR)
//    val month= myCalender.get(Calendar.MONTH)
//    val day= myCalender.get(Calendar.DAY_OF_MONTH)
//    println("$year $month $day")
//    val sdf= SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
//    val currentDate= sdf.parse((sdf.format(System.currentTimeMillis())))
//    println(currentDate)
//    println(currentDate.time)
//    println(currentDate.time/60000)
//    println({currentDate.time/60000}.toString())
//}