package com.example.colorguess

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.pop_up.*
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ResultActivity : AppCompatActivity() {

    private lateinit var mUserName: String
    private lateinit var mSequence: String
    private lateinit var mColorList: ArrayList<colour>
    private lateinit var checkAnsColorList: ArrayList<colour>
    private lateinit var adapter: colorAdapter
    private lateinit var typeface: Typeface
    private lateinit var typeface1: Typeface
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        setUpActionBar()
        if(intent.hasExtra(Constants.USER_NAME) && intent.hasExtra(Constants.SEQUENCE))
        {
            mUserName= intent.getStringExtra(Constants.USER_NAME).toString()
            mSequence= intent.getStringExtra(Constants.SEQUENCE).toString()
        }
        typeface = Typeface.createFromAsset(assets, "Sweets Smile.ttf")
        tv_arrange.typeface = typeface
        typeface1 = Typeface.createFromAsset(assets, "Maleantes Tres-d.ttf")

        mColorList= ArrayList()
        checkAnsColorList= ArrayList()
        for (color in Constants.getColor()){
            mColorList.add(color)
        }
        checkAnsColorList= mColorList
        setRecycleView()

        btn_reset.typeface= typeface1
        btn_reset.setOnClickListener {
            mColorList.clear()
            for (color in Constants.getColor()){
                mColorList.add(color)
            }
            setRecycleView()
            checkAnsColorList= mColorList
        }

        btn_submit.typeface= typeface1
        btn_submit.setOnClickListener {
            alertDialogForSubmit()
        }

        val itemTouchHelper= ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(rv_colors_list)
    }

    val simpleCallback= object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN
            or ItemTouchHelper.START or ItemTouchHelper.END, 0){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition= viewHolder.adapterPosition
            val toPosition= target.adapterPosition
            Collections.swap(checkAnsColorList, fromPosition, toPosition)
            adapter.notifyItemMoved(fromPosition, toPosition)
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

    }

    @SuppressLint("SetTextI18n")
    private fun alertDialogForSubmit() {
        val builder = AlertDialog.Builder(this@ResultActivity)
        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to submit your response?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss()
            var checkAnsSequence= ""
            for (color in checkAnsColorList){
                checkAnsSequence += color.index.toString()
            }
            if(checkAnsSequence== mSequence){
                Log.e("result", "WON")
                initUI()
            }else{
                Log.e("result", "LOSE")
                cv_rv.visibility= View.GONE
                loseDialog()
            }
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun initUI(){
        val party= Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 2000, TimeUnit.MILLISECONDS).max(2000),
            position = Position.Relative(0.5, 0.3)
        )

        val dialog= Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.pop_up)
        val tv_congratulation = dialog.findViewById<TextView>(R.id.tv_congratulation)
        tv_congratulation.text= "Congratulation $mUserName."
        val tv_booyah = dialog.findViewById<TextView>(R.id.tv_booyah)
        tv_booyah.typeface = typeface
        dialog.window?.setBackgroundDrawableResource(R.drawable.bg)
        dialog.show()

        konfettiView.visibility= View.VISIBLE
        cv_rv.visibility= View.GONE
        konfettiView.start(party)
        Handler().postDelayed({
            val intent= Intent(this@ResultActivity, HistoryActivity::class.java)
            intent.putExtra(Constants.USER_NAME, mUserName)
            intent.putExtra(Constants.RESULT, "WIN")
            startActivity(intent)
            dialog.dismiss()
            finish()
        }, 3500)
    }

    private fun loseDialog()
    {
        val customDialog= Dialog(this)
        customDialog.setContentView(R.layout.dailog_custom)
        customDialog.setCancelable(false)
        val btn_retry= customDialog.findViewById<TextView>(R.id.btn_retry)
        btn_retry.typeface = typeface
        customDialog.findViewById<Button>(R.id.btn_retry).setOnClickListener{
            startActivity(Intent(this@ResultActivity, MainActivity::class.java))
            val databaseHandler: DatabaseHandler= DatabaseHandler(this@ResultActivity)
            date= getDate()
            if( mUserName.isNotEmpty() && date.isNotEmpty())
            {
                val status= databaseHandler.addUser(user(0, mUserName, date, "LOSE"))
                if(status> -1)
                {
                    Log.e("New Record", "$mUserName $date LOSE")
                }
            }
            customDialog.dismiss()
            finish()
        }
        val tv_you_lose= customDialog.findViewById<TextView>(R.id.tv_youLoose)
        tv_you_lose.typeface = typeface
        customDialog.show()
    }

    private fun setRecycleView(){
        rv_colors_list.layoutManager= LinearLayoutManager(this)
        rv_colors_list.setHasFixedSize(true)
        adapter= colorAdapter(this, mColorList)
        rv_colors_list.adapter= adapter
    }

    private fun setUpActionBar()
    {
        setSupportActionBar(toolbar_result_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        toolbar_result_activity.setNavigationOnClickListener{ onBackPressed()}
        konfettiView.visibility= View.GONE
        cv_rv.visibility= View.VISIBLE
    }

    private  fun getDate(): String {
        val c= Calendar.getInstance()
        val dateTime= c.time
        Log.e("Date: ", "" +dateTime)

        val sdf= SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date= sdf.format(dateTime)
        return date
    }
}