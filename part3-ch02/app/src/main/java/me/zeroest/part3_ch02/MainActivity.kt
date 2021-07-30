package me.zeroest.part3_ch02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {
    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initData()
//        initViews()
    }

    private fun initViews() {
        viewPager.setPageTransformer { page, position ->
            when {
                position.absoluteValue > 1.0F -> {
                    page.alpha = 0F
                }

                position.absoluteValue == 0F -> {
                    page.alpha = 1F
                }

                else -> {
                    page.alpha = 1 - 2 * position.absoluteValue
                }

            }
        }
    }

    private fun initData() {
        // 배포버전은 12시간 단위로 재배포가 가능하게 하자

        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )

        // 실무에서 fetch를 못했을때 기본값으로 설정하는 부분
//        remoteConfig.setDefaultsAsync()

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                progressBar.visibility = View.GONE
                if (it.isSuccessful) {
                    val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                    val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")

                    displayQuotesPager(quotes, isNameRevealed)
//                    viewPager.adapter = QuotesPagerAdapter(quotes)
                }
            }
    }

    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json);
        var jsonList = emptyList<JSONObject>()

        for (idx in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(idx)
            jsonObject ?.let {
                jsonList = jsonList + it
            }
        }

        return jsonList.map {
            Quote(it.getString("quote"), it.getString("name"))
        }
    }

    private fun displayQuotesPager(quotes: List<Quote>, isNameRevealed: Boolean) {
        val adapter = QuotesPagerAdapter(quotes, isNameRevealed)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(adapter.itemCount/2, false)
    }

/*
    private fun initViews() {
        viewPager.adapter = QuotesPagerAdapter(
            listOf(
                Quote(
                    "나는 생각한다. 고로 나는 존재한다.",
                    "데카르"
                ),
                Quote(
                    "그래서 내일 뭐해",
                    "신나래"
                ),
            )
        )
    }
*/
}