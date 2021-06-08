package sharlene.work.tiptime

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import sharlene.work.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat.getCurrencyInstance
import kotlin.math.ceil


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
        val stringInBill = binding.billAmountText.text.toString()

        if (TextUtils.isEmpty(stringInBill) || TextUtils.isEmpty(stringInTextField)) {
            Toast.makeText(this, "some fields are empty", Toast.LENGTH_SHORT).show()
            return
        }

        val cost = stringInTextField.toDouble()
        val bill = stringInBill.toDouble()

        var amount = bill
        var tip: Double
        val tipPercentage = when (binding.tipOption.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }



        if ((bill == 0.0) && (cost == 0.0)) {
            displayTip(0.0, 0.0)
        } else if (cost == 0.0) {
            if (binding.roundUpSwitch.isChecked) {
                amount = ceil(amount)
            }
            displayTip(0.0, amount)
        } else if (bill == 0.0) {
            tip = tipPercentage * cost
            if (binding.roundUpSwitch.isChecked) {
                tip = ceil(tip)
            }
            displayTip(tip, tip)
        }

        tip = tipPercentage * cost
        amount = bill + tip

        if (binding.roundUpSwitch.isChecked) {
            tip = ceil(tip)
            amount = ceil(amount)
        }
        displayTip(tip, amount)
    }

    private fun displayTip(tip: Double, amount: Double?) {
        val formattedTip = getCurrencyInstance().format(tip)

        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)

        val formattedAmount = getCurrencyInstance().format(amount)

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
