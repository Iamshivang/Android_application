package com.example.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minuteworkout.databinding.ActivityExerciseBinding
import com.example.a7minuteworkout.databinding.DialogCustomBackConformationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityExerciseBinding?= null

    private var restTimer: CountDownTimer?= null
    private var restProgress= 0
    private val restTimerDuration: Long= 10
    private val exerciseTimerDuration: Long= 30

    private var exerciseTimer: CountDownTimer?= null
    private var exerciseProgress= 0

    private var exerciseList: ArrayList<ExerciseModel>?= null
    private var currentExercisePosition: Int= -1

    private var tts: TextToSpeech?= null
    private var player: MediaPlayer?= null

    private var exerciseAdapter: ExerciseStatusAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)
        if(supportActionBar!= null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener{
            customDialogBackButton()
        }

        exerciseList= Constants.defaultExerciseList()

        tts= TextToSpeech(this, this)

        setRestView()
        setupExerciseStatusRecycleView()
    }

    override fun onBackPressed() {
        customDialogBackButton()
//        super.onBackPressed()
    }

    private fun customDialogBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConformationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.tvNO.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setupExerciseStatusRecycleView()
    {
        binding?.rvExerciseStaus?.layoutManager= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter= ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStaus?.adapter= exerciseAdapter
    }

    private fun setExerciseView()
    {
        binding?.flRestView?.visibility= View.INVISIBLE
        binding?.tvTitle?.visibility= View.INVISIBLE
        binding?.tvUpComingLavel?.visibility= View.INVISIBLE
        binding?.tvUpComingExerciseName?.visibility= View.INVISIBLE
        binding?.tvExerciseName?.visibility= View.VISIBLE
        binding?.flExreciseView?.visibility= View.VISIBLE
        binding?.ivImage?.visibility= View.VISIBLE

        if (exerciseTimer!= null)
        {
            exerciseTimer?.cancel()
            exerciseProgress= 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text= exerciseList!![currentExercisePosition].getName()

        setExerciseRestProgressBar()
    }

    private fun setRestView()
    {

        try {
            val soundURI= Uri.parse("android.resource://com.example.a7minuteworkout/" + R.raw.groovehit)
            player= MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping= false
            player?.start()
        }catch (e: Exception){
            e.printStackTrace()
        }

        binding?.flRestView?.visibility= View.VISIBLE
        binding?.tvTitle?.visibility= View.VISIBLE
        binding?.tvUpComingLavel?.visibility= View.VISIBLE
        binding?.tvUpComingExerciseName?.visibility= View.VISIBLE
        binding?.tvExerciseName?.visibility= View.INVISIBLE
        binding?.flExreciseView?.visibility= View.INVISIBLE
        binding?.ivImage?.visibility= View.INVISIBLE

        if (restTimer!= null)
        {
            restTimer?.cancel()
            restProgress= 0
        }

        binding?.tvUpComingExerciseName?.text= exerciseList!![currentExercisePosition+ 1].getName()
        setRestProgressBar()
    }

    private fun setRestProgressBar()
    {
        binding?.progressBar?.progress= restProgress

        restTimer= object: CountDownTimer(restTimerDuration* 1000, 1000)
        {
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress= 10- restProgress
                binding?.tvTimer?.text= (10-restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setExerciseView()
            }

        }.start()
    }

    private fun setExerciseRestProgressBar()
    {
        binding?.progressBarExercise?.progress= exerciseProgress

        exerciseTimer= object: CountDownTimer(exerciseTimerDuration* 1000, 1000)
        {
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress= 30- exerciseProgress
                binding?.tvTimerExercise?.text= (30-exerciseProgress).toString()
            }

            override fun onFinish() {


                if(currentExercisePosition+1< exerciseList!!.size)
                {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setRestView()
                }
                else
                {
                    finish()
                    val intent= Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }

        }.start()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        if (restTimer!= null)
        {
            restTimer?.cancel()
            restProgress= 0
        }
        if (exerciseTimer!= null)
        {
            exerciseTimer?.cancel()
            exerciseProgress= 0
        }

        // if activity is closed then shut down the Bol Bachhan
        if(tts!= null)
        {
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player!= null)
        {
            player!!.stop()
        }
        binding= null
    }

    override fun onInit(status: Int) {
        if(status== TextToSpeech.SUCCESS)
        {
            val result= tts?.setLanguage(Locale.UK)
        }
        else
        {
            if(status== TextToSpeech.LANG_MISSING_DATA || status== TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS", "The language specified is not supported")
            }
            else{
                Log.e("TTS", "Initialization failed!")
            }
        }
    }

    private fun speakOut(text: String)
    {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}