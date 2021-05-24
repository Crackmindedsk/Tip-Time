package sharlene.work.tiptime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sharlene.work.tiptime.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calculateButton.setOnClickListener { calculateTip() }
    }
    fun calculateTip(){
        val stringInTextField=binding.costOfService.text.toString()
        val cost=stringInTextField.toDouble()

        val tipPercentage=when(binding.tipOption.checkedRadioButtonId){
            R.id.option_twenty_percent->0.20
            R.id.option_eighteen_percent->0.18
            else->0.15
        }
        var tip=tipPercentage*cost

        val roundUp=binding.roundUpSwitch.isChecked
        if(roundUp){
            tip=kotlin.math.ceil(tip)
        }

        val formattedTip=java.text.NumberFormat.getCurrencyInstance().format(tip)

        binding.tipResult.text=getString(R.string.tip_amount,formattedTip)

    }
}