package com.example.a7minuteworkout

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.a7minuteworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    private var binding: ActivityFinishBinding?= null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)


//        binding?.toolbarFinishActivity?.setTitleTextColor(R.color.light_blue)
        setSupportActionBar(binding?.toolbarFinishActivity)
        if(supportActionBar!= null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarFinishActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

        binding?.btnFinish?.setOnClickListener{
            finish()
        }

        val dao= (application as WorkOutApp).db.historyDoa()
        addDateToDatabase(dao)
    }

    private  fun addDateToDatabase(historyDao: HistoryDao)
    {

        val c= Calendar.getInstance()
        val dateTime= c.time
        Log.e("Date: ", "" +dateTime)

        val sdf= SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date= sdf.format(dateTime)
        Log.e("Formatted Date: ", "" +date)

        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))
            Log.e("Date: ", "Added")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding= null
    }
}