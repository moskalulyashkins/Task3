package com.example.task3

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var billInput: EditText
    private lateinit var peopleInput: EditText
    private lateinit var tipSeekBar: SeekBar
    private lateinit var tipPercentText: TextView
    private lateinit var tipResult: TextView
    private lateinit var totalResult: TextView
    private lateinit var perPersonResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        billInput = findViewById(R.id.billInput)
        peopleInput = findViewById(R.id.peopleInput)
        tipSeekBar = findViewById(R.id.tipSeekBar)
        tipPercentText = findViewById(R.id.tipPercentText)
        tipResult = findViewById(R.id.tipResult)
        totalResult = findViewById(R.id.totalResult)
        perPersonResult = findViewById(R.id.perPersonResult)

        // 1. При движении SeekBar — пересчитываем
        tipSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tipPercentText.text = "$progress%"
                calculateTip()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 2. При изменении суммы счёта — пересчитываем
        billInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateTip()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 3. При изменении количества человек — пересчитываем
        peopleInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateTip()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        calculateTip()
    }

    private fun calculateTip() {
        // "Если сумма пустая — считайте как 0."
        val billString = billInput.text.toString()
        val bill = if (billString.isEmpty()) 0.0 else billString.toDoubleOrNull() ?: 0.0

        val percent = tipSeekBar.progress.toDouble()

        // Количество человек (если пустое, ставим 1)
        val peopleString = peopleInput.text.toString()
        val people = if (peopleString.isEmpty()) 1 else peopleString.toIntOrNull() ?: 1

        // Формулы по заданию:
        // чаевые = сумма × процент / 100. Итого = сумма + чаевые. На человека = итого / количество человек.
        val tip = bill * percent / 100
        val total = bill + tip
        val perPerson = if (people > 0) total / people else 0.0

        // "Выводите числа с двумя знаками после запятой"
        tipResult.text = "Чаевые: ${String.format("%.2f", tip)}₽"
        totalResult.text = "Итого: ${String.format("%.2f", total)}₽"
        perPersonResult.text = "На человека: ${String.format("%.2f", perPerson)}₽"
    }
}