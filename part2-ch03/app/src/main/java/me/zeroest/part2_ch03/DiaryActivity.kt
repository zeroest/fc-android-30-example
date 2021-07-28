package me.zeroest.part2_ch03

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity: AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private val diaryEditText: EditText by lazy {
        findViewById(R.id.diaryEditText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        initDiary()
    }

    private fun initDiary(){

        diaryEditText.setText(getDetailPreferences()!!.getString("detail", ""))

        val runnable = Runnable {
            getDetailPreferences()!!
                .edit {
                    putString("detail", diaryEditText.text.toString())
                }

            Log.d("DiaryActivity", "SAVE :: ${diaryEditText.text}")
        }

        diaryEditText.addTextChangedListener {
            Log.d("DiaryActivity", "TextChanged :: $it")
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500)
        }

    }

    private fun getDetailPreferences(): SharedPreferences? {
        return getSharedPreferences("diary", Context.MODE_PRIVATE)
    }

}