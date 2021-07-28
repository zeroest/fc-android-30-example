package me.zeroest.part2_ch03

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import java.util.*

class MainActivity : AppCompatActivity() {

    private val firstNumberPicker: NumberPicker by lazy {
        setNumberPicker(R.id.firstNumberPicker)
    }
    private val secondNumberPicker: NumberPicker by lazy {
        setNumberPicker(R.id.secondNumberPicker)
    }
    private val thirdNumberPicker: NumberPicker by lazy {
        setNumberPicker(R.id.thirdNumberPicker)
    }

    private fun setNumberPicker(numberPickerId: Int): NumberPicker {
        return findViewById<NumberPicker>(numberPickerId)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton: Button by lazy {
        findViewById(R.id.openButton)
    }
    private val changePasswordButton: Button by lazy {
        findViewById(R.id.changePasswordButton)
    }

    private var changePasswordMode = false
    private fun flipChangePasswordMode() {
        changePasswordMode = !changePasswordMode
    }

    private fun onChangePasswordMode(){
        flipChangePasswordMode()
        changePasswordButton.setBackgroundColor(Color.RED)
    }
    private fun offChangePasswordMode() {
        flipChangePasswordMode()
        changePasswordButton.setBackgroundColor(Color.BLACK)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNumberPicker()

        initOpenButton()
        initChangePasswordButton()
    }

    private fun initNumberPicker() {
        firstNumberPicker
        secondNumberPicker
        thirdNumberPicker
    }

    private fun initOpenButton() {
        openButton.setOnClickListener {
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            val passwordPreferences = getSharedPreferencePassword()
//            val passwordFromUser = getPasswordFromUser()

            if (!isMatchPassword(getSharedPreferencePassword(), getPasswordFromUser())) {
                // 실패
                return@setOnClickListener
            }

            // 성공
            startActivity(Intent(this, DiaryActivity::class.java))

        }
    }

    private fun initChangePasswordButton() {
        changePasswordButton.setOnClickListener {

            if (changePasswordMode) {
                // 암호 변경 설정
                offChangePasswordMode()

                getSharedPreferences("password", Context.MODE_PRIVATE)
                    .edit (true) {
                        putString("password", getPasswordFromUser())
                    }

            } else {
                // 암호 변경 시작
                // 비밀번호가 맞는지 체크

                if (!isMatchPassword(getSharedPreferencePassword(), getPasswordFromUser())) {
                    // 비밀번호 불일치
                    return@setOnClickListener
                }

                // 비밀번호 일치
                Toast.makeText(this, "변경할 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()

                onChangePasswordMode()

            }

        }
    }

    private fun getSharedPreferencePassword(): String {
        val string = getSharedPreferences("password", Context.MODE_PRIVATE)
            .getString("password", "000")

        return string!!;
    }
    private fun getPasswordFromUser(): String {
        return "${firstNumberPicker.value}${secondNumberPicker.value}${thirdNumberPicker.value}"
    }

    private fun isMatchPassword(passwordPreferences: String, passwordFromUser: String): Boolean {

        val result = passwordPreferences.equals(passwordFromUser)

        if (!result) {
            AlertDialog.Builder(this)
                .setTitle("실패")
                .setMessage("비밀번호가 잘못되었습니다.")
                .setPositiveButton("확인", {dialog, which -> })
                .create()
                .show()
        }

        return result

    }

}