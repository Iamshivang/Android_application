package com.example.colorguess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_result.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryActivity : AppCompatActivity() {

    private lateinit var mUserName: String
    private lateinit var mResult: String
    private lateinit var date: String
    private lateinit var mHistory: ArrayList<user>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setUpActionBar()
        if(intent.hasExtra(Constants.USER_NAME) && intent.hasExtra(Constants.RESULT))
        {
            mUserName= intent.getStringExtra(Constants.USER_NAME).toString()
            mResult= intent.getStringExtra(Constants.RESULT).toString()
            addRecord()
        }

        setupListOfDataInTheRecycleView()
    }

    private  fun addRecord() {
        val databaseHandler: DatabaseHandler= DatabaseHandler(this@HistoryActivity)
        date= getDate()

        if( mUserName.isNotEmpty() && mResult.isNotEmpty())
        {
            val status= databaseHandler.addUser(user(0, mUserName, date, mResult))
            if(status> -1)
            {
                Log.e("New Record", "$mUserName $date $mResult")
            }
            setupListOfDataInTheRecycleView()
        }
    }

    private  fun getDate(): String {

        val c= Calendar.getInstance()
        val dateTime= c.time
        Log.e("Date: ", "" +dateTime)

        val sdf= SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date= sdf.format(dateTime)
        return date
    }

    private  fun setupListOfDataInTheRecycleView()
    {
        val databaseHandler: DatabaseHandler= DatabaseHandler(this@HistoryActivity)
        mHistory= ArrayList()
        mHistory= databaseHandler.viewUser()

        if(mHistory.size> 0)
        {
            tvNoRecordsAvailable.visibility= View.GONE
            rvItemsList.visibility= View.VISIBLE
            rvItemsList.layoutManager= LinearLayoutManager(this@HistoryActivity)
            val itemAdapter= ItemAdapter(this, mHistory)
            rvItemsList.adapter= itemAdapter
        }else{
            tvNoRecordsAvailable.visibility= View.VISIBLE
            rvItemsList.visibility= View.GONE
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_history_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        toolbar_history_activity.setNavigationOnClickListener{ onBackPressed()}
    }
}