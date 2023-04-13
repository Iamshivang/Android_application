package com.example.a7minuteworkout

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minuteworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding?= null
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

//        binding?.toolbarHistoryActivity?.setTitleTextColor(R.color.light_blue)
        setSupportActionBar(binding?.toolbarHistoryActivity)
        if(supportActionBar!= null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title= "HISTORY"
        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

        val dao= (application as WorkOutApp).db.historyDoa()
        getAllCompleteDates(dao)
    }

    private fun getAllCompleteDates(historyDao: HistoryDao)
    {
        lifecycleScope.launch{
            historyDao.fetchAllDates().collect{ allCompleteDatesList->
                if(allCompleteDatesList.isNotEmpty())
                {
                    binding?.tvHistory?.visibility= View.VISIBLE
                    binding?.rvHistory?.visibility= View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility= View.INVISIBLE

                    binding?.rvHistory?.layoutManager= LinearLayoutManager(this@HistoryActivity)

                    val dates= ArrayList<String>()
                    for(date in allCompleteDatesList)
                    {
                        dates.add(date.date)
                    }

                    val historyAdapter= HistoryAdapter(dates)
                    binding?.rvHistory?.adapter= historyAdapter
                }
                else{
                    binding?.tvHistory?.visibility= View.GONE
                    binding?.rvHistory?.visibility= View.GONE
                    binding?.tvNoDataAvailable?.visibility= View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding= null
    }
}