package me.zeroest.part3_ch01

import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    // 오버라이드 하라고 경고를 띄운다.
    // 토큰이 갱신될때 호출되는 메서드
    // 따라서 새로운 토큰을 서버에 갱신하는 코드를 여기에 입력하면 된다.
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        Log.i("FirebaseMessagingService", "New Token : $p0")
    }

    // 실제 푸시가 왔을때 호출되는 메서드
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.e("FirebaseMessageingService", "message : ${message.data.toString()}")
    }

}