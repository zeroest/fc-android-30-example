package me.zeroest.part3_ch03

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.edit
import java.util.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // step0 뷰를 초기화해주기
        initOnOffButton()
        initChangeAlarmTimeButton()

        val model = fetchDataFromSharedPreferences();
        renderView(model)
        // step1 데이터 가져오기

        // step2 뷰에 데이터를 그려주기

    }

    private fun initOnOffButton() {
        val onOffButton = findViewById<Button>(R.id.onOffButton)
        onOffButton.setOnClickListener {
            // 데이터를 확인한다.
            val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener

            // 데이터를 저장한다.
            val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not())
            renderView(newModel)

            // 온오프에 따라 작업을 처리한다.
            if (newModel.onOff) {
                // 온 -> 알람을 등록
                val calendar = Calendar.getInstance()
                    .apply {
                        set(Calendar.HOUR_OF_DAY, newModel.hour)
                        set(Calendar.MINUTE, newModel.minute)

                        if (before(Calendar.getInstance())) {
                            add(Calendar.DATE, 1)
                        }
                    }

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val intent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    ALARM_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
/*
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
*/

/*
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
*/

            } else {
                // 오프 -> 알람을 제거
                cancelAlarm()
            }

        }
    }

    private fun initChangeAlarmTimeButton() {
        val changeAlarmButton = findViewById<Button>(R.id.changeAlarmTimeButton)
        changeAlarmButton.setOnClickListener {

            // 현재시간을 가져온다.
            val calendar = Calendar.getInstance()

            // TimePickDialog 띄워 시간설정을 하도록 한다.
            TimePickerDialog(this, { picker, hour, minute ->
                // 시간을 가져와 데이터를 저장한다.
                val model = saveAlarmModel(hour, minute, false)

                // 뷰를 업데이트한다.
                renderView(model)

                // 기존에 있던 알람을 삭제한다.
                cancelAlarm()
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                .show()

        }

    }

    private fun saveAlarmModel(hour: Int, minute: Int, onOff: Boolean): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            hour,
            minute,
            onOff
        )

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString(ALARM_KEY, model.makeDataForDb())
            putBoolean(ONOFF_KEY, model.onOff)
            commit()
        }

        return model
    }

    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "9:30") ?: "9:30"
        val onOffDBValue = sharedPreferences.getBoolean(ONOFF_KEY, false)

        val alarmData = timeDBValue.split(":")

        val alarmModel = AlarmDisplayModel(
            alarmData[0].toInt(),
            alarmData[1].toInt(),
            onOffDBValue
        )

        // 보정
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        if ((pendingIntent == null) and alarmModel.onOff) {
            // 알람은 꺼져 있는데, 데이터는 켜져있는 경우
            alarmModel.onOff = false
        } else if ((pendingIntent != null) and alarmModel.onOff.not()) {
            // 알람이 켜져 있는데, 데이터는 꺼져 있는경우
            pendingIntent.cancel()
        }

        return alarmModel

    }

    private fun renderView(model: AlarmDisplayModel) {
        findViewById<TextView>(R.id.ampmTextView)
            .apply {
                text = model.ampmText
            }
        findViewById<TextView>(R.id.timeTextView)
            .apply {
                text = model.timeText
            }

        findViewById<Button>(R.id.onOffButton)
            .apply {
                text = model.onOffText
                tag = model
            }
    }

    private fun cancelAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        pendingIntent?.cancel()
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private const val ALARM_REQUEST_CODE = 1000
    }

}