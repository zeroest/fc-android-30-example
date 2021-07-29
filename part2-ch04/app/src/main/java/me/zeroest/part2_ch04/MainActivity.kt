package me.zeroest.part2_ch04

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val expressionTextView: TextView by lazy {
        findViewById(R.id.expressionTextView)
    }
    private val resultTextView:TextView by lazy {
        findViewById(R.id.resultTextView)
    }

    private var isOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buttonClicked(v: View){
        when (v.id) {
            R.id.zeroButton -> numberButtonClicked("0")
            R.id.oneButton -> numberButtonClicked("1")
            R.id.twoButton -> numberButtonClicked("2")
            R.id.threeButton -> numberButtonClicked("3")
            R.id.fourButton -> numberButtonClicked("4")
            R.id.fiveButton -> numberButtonClicked("5")
            R.id.sixButton -> numberButtonClicked("6")
            R.id.sevenButton -> numberButtonClicked("7")
            R.id.eightButton -> numberButtonClicked("8")
            R.id.nineButton -> numberButtonClicked("9")

            R.id.plusButton -> operatorButtonClicked("+")
            R.id.minusButton -> operatorButtonClicked("-")
            R.id.multiplyButton -> operatorButtonClicked("*")
            R.id.divideButton -> operatorButtonClicked("/")
            R.id.moduloButton -> operatorButtonClicked("%")
        }
    }

    private fun numberButtonClicked(number: String) {
        val expressionText = expressionTextView.text.split(" ")
        if (
            expressionText.isNotEmpty()
            && expressionText.last().length >= 15
        ) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
            return
        } else if (
            number == "0"
            && expressionText.last().isEmpty()
        ) {
            return;
        }

        if (isOperator) {
            expressionTextView.append(" ")
            isOperator = !isOperator
        }

        expressionTextView.append(number)

        //TODO resultTextView 실시간으로 계산 결과를 입력한다.
    }

    private fun operatorButtonClicked(operator: String) {
        if (expressionTextView.text.isEmpty()) {
            return
        }
    }

    fun clearButtonClicked(v: View) {

    }

    fun historyButtonClicked(v: View) {

    }

    fun resultButtonClicked(v: View) {

    }

}