package sharlene.work.tiptime

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import sharlene.work.tiptime.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calculateButton.setOnClickListener {
            calculateTip()
        }
        binding.costOfServiceText.setOnKeyListener { view, keycode, _ ->
            handleKeyEvent(
                view,
                keycode
            )
        }

    }

    private fun calculateTip() {
        val stringInTextField = binding.costOfServiceText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()

        val stringInBill = binding.billAmountText.text.toString()
        val bill = stringInBill.toDoubleOrNull()
        var amount=bill
        var tip:Double
        val tipPercentage = when (binding.tipOption.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }



        if((bill==0.0 || bill==null) && (cost==0.0 ||cost==null)){
            displayTip(0.0,0.0)
            return
        }else if (cost == null || cost == 0.0) {
            if (binding.roundUpSwitch.isChecked) {
                amount = amount?.let { kotlin.math.ceil(it) }
            }
            displayTip(0.0,amount)
            return
        }else if(bill == null || bill == 0.0) {
            tip = tipPercentage * cost
            if (binding.roundUpSwitch.isChecked) {
                tip = kotlin.math.ceil(tip)
            }
            displayTip(tip,tip)
            return
        }

        tip = tipPercentage * cost
        amount = bill + tip

        if (binding.roundUpSwitch.isChecked) {
            tip = kotlin.math.ceil(tip)
            amount = kotlin.math.ceil(amount)
        }
        displayTip(tip,amount)



    }

    private fun displayTip(tip: Double, amount: Double?) {
        val formattedTip = java.text.NumberFormat.getCurrencyInstance().format(tip)

        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
        val formattedAmount = java.text.NumberFormat.getCurrencyInstance().format(amount)

        binding.totalAmount.text = getString(R.string.total_amount, formattedAmount)
    }

    private fun handleKeyEvent(view: View, keycode: Int): Boolean {
        if (keycode == KeyEvent.KEYCODE_ENTER) {
            //hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}
