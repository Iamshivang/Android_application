package com.example.a7minuteworkout

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import com.example.a7minuteworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val  METRIC_UNITS_VIEW= "METRIC_UNITS_VIEW"
        private const val  US_UNITS_VIEW= "US_UNITS_VIEW"
    }

    private var currentVisibleView: String= "METRIC_UNITS_VIEW"
    private var binding: ActivityBmiBinding?= null
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

//        binding?.toolbarBMIActivity?.setTitleTextColor(R.color.light_blue)
        setSupportActionBar(binding?.toolbarBMIActivity)
        if(supportActionBar!= null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title= "Calculate BMI"
        }

        binding?.toolbarBMIActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener{_, checkedId: Int ->
            if(checkedId== R.id.rbMetricUnits)
            {
                makeVisibleMetricUnitsView()
            }
            else
            {
                makeVisibleUsUnitsView()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener{
            calculateUnits()
        }
    }

    private  fun makeVisibleMetricUnitsView()
    {
        currentVisibleView= "METRIC_UNITS_VIEW"
        binding?.tilMetricUnitHeight?.visibility= View.VISIBLE
        binding?.tilMetricUnitWeight?.visibility= View.VISIBLE
        binding?.tilUsMetricUnitWeight?.visibility= View.GONE
        binding?.tilMetricUsUnitHeightFeet?.visibility= View.GONE
        binding?.tilMetricUsUnitHeightInch?.visibility= View.GONE

        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()

        binding?.llDiplayBMIResult?.visibility= View.INVISIBLE
    }

    private  fun makeVisibleUsUnitsView()
    {
        currentVisibleView= "US_UNITS_VIEW"
        binding?.tilMetricUnitHeight?.visibility= View.INVISIBLE
        binding?.tilMetricUnitWeight?.visibility= View.INVISIBLE
        binding?.tilUsMetricUnitWeight?.visibility= View.VISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility= View.VISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility= View.VISIBLE

        binding?.etUsMetricUnitHeightFeet?.text!!.clear()
        binding?.etUsMetricUnitHeightInch?.text!!.clear()
        binding?.etUsMetricUnitWeight?.text!!.clear()

        binding?.llDiplayBMIResult?.visibility= View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float){

        val bmiLabel: String
        val bmiDescription: String

        if(bmi.compareTo(15f)<= 0)
        {
            bmiLabel= "Very severely Underweight"
            bmiDescription= "Oops, You really needs to take better care of yourself! Eat more!"
        }
        else if((bmi.compareTo(15f)> 0)&& (bmi.compareTo(16f)<= 0))
        {
            bmiLabel= "Severely Underweight"
            bmiDescription= "Oops, You needs to take better care of yourself! Eat more"
        }
        else if((bmi.compareTo(16f)> 0)&& (bmi.compareTo(18.5f)<= 0))
        {
            bmiLabel= "Underweight"
            bmiDescription= "Oops, You needs to take better care of yourself! Eat more"
        }
        else if((bmi.compareTo(18.5f)> 0)&& (bmi.compareTo(25f)<= 0))
        {
            bmiLabel= "Normal"
            bmiDescription= "Congregations!, You are in good in shape"
        }
        else if((java.lang.Float.compare(bmi, 25f)> 0)&& (java.lang.Float.compare(bmi, 30f)<= 0))
        {
            bmiLabel= "Overweight"
            bmiDescription= "Oops, You needs to take better care of yourself! Workout! may be"
        }
        else if((bmi.compareTo(30f)> 0)&& (bmi.compareTo(35f)<= 0))
        {
            bmiLabel= "Obese Class (Moderately obese)"
            bmiDescription= "Oops, You really needs to take better care of yourself! Workout Daily!"
        }
        else if((bmi.compareTo(35f)> 0)&& (bmi.compareTo(40f)<= 0))
        {
            bmiLabel= "Obese Class (Severely obese)"
            bmiDescription= "OMG, You are in very dangerous condition, Act Now!"
        }
        else
        {
            bmiLabel= "Obese Class (Very Severely obese)"
            bmiDescription= "OMG, You are in most dangerous condition, Act Now!"
        }

        val bmiValue= BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.llDiplayBMIResult?.visibility= View.VISIBLE
        binding?.tvBMIValue?.text= bmiValue
        binding?.tvBMIType?.text= bmiLabel
        binding?.tvBMIDescription?.text= bmiDescription
    }

    private fun validateMetricUnits(): Boolean
    {
        var isValid= true

        if(binding?.etMetricUnitWeight?.text.toString().isEmpty())
        {
            isValid= false
        }
        if (binding?.etMetricUnitHeight?.text.toString().isEmpty())
        {
            isValid= false
        }

        return isValid
    }

    private fun validateUsUnits(): Boolean
    {
        var isValid= true

        if(binding?.etUsMetricUnitWeight?.text.toString().isEmpty())
        {
            isValid= false
        }
        if (binding?.etUsMetricUnitHeightFeet?.text.toString().isEmpty())
        {
            isValid= false
        }
        if (binding?.etUsMetricUnitHeightInch?.text.toString().isEmpty())
        {
            isValid= false
        }

        return isValid
    }

    private fun calculateUnits()
    {
        if(currentVisibleView== METRIC_UNITS_VIEW)
        {
            if(validateMetricUnits())
            {
                val weightValue= binding?.etMetricUnitWeight?.text.toString().toFloat()/ 100
                val heightValue= binding?.etMetricUnitWeight?.text.toString().toFloat()/ 100
                val bmi= weightValue/(heightValue* heightValue)
                displayBMIResult(bmi)
                binding?.etMetricUnitWeight?.text!!.clear()
                binding?.etMetricUnitHeight?.text!!.clear()
            }else
            {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            if(validateUsUnits())
            {
                val usUnitHeightValueFeet: Float= binding?.etUsMetricUnitHeightFeet?.text.toString().toFloat()
                val usUnitHeightValueInch: Float= binding?.etUsMetricUnitHeightInch?.text.toString().toFloat()
                val usUnitWeightValue: Float= binding?.etUsMetricUnitWeight?.text.toString().toFloat()
                val heightValue= usUnitHeightValueInch+ (usUnitHeightValueFeet* 12)
                val bmi= 703* (usUnitWeightValue/ (heightValue* heightValue))
                displayBMIResult(bmi)
                binding?.etUsMetricUnitWeight?.text!!.clear()
                binding?.etUsMetricUnitHeightFeet?.text!!.clear()
                binding?.etUsMetricUnitHeightInch?.text!!.clear()
            }else
            {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding= null
    }
}