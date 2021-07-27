package me.zeroest.prat2_ch02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            firstNumberTextView,
            secondNumberTextView,
            thirdNumberTextView,
            fourthNumberTextView,
            fifthNumberTextView,
            sixthNumberTextView,
        )
    }
    private var didRun = false
    private val pickNumberSet = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNumberPicker()
        initRunButton()
        initAddButton()
        initClearButton()
    }

    private fun initNumberPicker() {
        numberPicker.minValue = 1
        numberPicker.maxValue = 46
    }
    private fun initRunButton() {
        runButton.setOnClickListener {

            val list = MakeRandomNumber.getRandomNumber(pickNumberSet)

            list.forEachIndexed{index, number ->
                val textView = numberTextViewList[index]
                showNumber(textView, number)
            }

            pickNumberSet.clear()

            didRun = true

        }
    }

    private fun initAddButton() {
        addButton.setOnClickListener {

            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            showNumber(textView, numberPicker.value)

            pickNumberSet.add(numberPicker.value)

        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {

            pickNumberSet.clear()

            numberTextViewList.forEach{
                it.isVisible = false
            }

            didRun = false

        }
    }

    private fun showNumber(textView: TextView, number: Int){
        textView.text = number.toString()
        textView.isVisible = true
        val drawableId: Int by lazy {
            when (number) {
                in 1..10 -> R.drawable.circle_yellow
                in 11..20 -> R.drawable.circle_blue
                in 21..30 -> R.drawable.circle_red
                in 31..40 -> R.drawable.circle_gray
                else -> R.drawable.circle_green
            }
        }
        textView.background = ContextCompat.getDrawable(this, drawableId)
    }

}