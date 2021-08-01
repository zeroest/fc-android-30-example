package me.zeroest.part3_ch04

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import me.zeroest.part3_ch04.adapter.BookAdapter
import me.zeroest.part3_ch04.adapter.HistoryAdapter
import me.zeroest.part3_ch04.api.BookService
import me.zeroest.part3_ch04.databinding.ActivityMainBinding
import me.zeroest.part3_ch04.model.BestSellerDto
import me.zeroest.part3_ch04.model.History
import me.zeroest.part3_ch04.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor




class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var bookService: BookService

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()
        initHistoryRecyclerView()

        db = getDatabase(this)
//        initDb()

        initBookService()
        getBestSeller()
        initSearchEditText()

    }

/*
    private fun initDb() {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()
    }
*/

    private fun initBookRecyclerView() {

        adapter = BookAdapter(itemClickListener = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel", it)
            startActivity(intent)
        })

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter

    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickedListener = {
            deleteSearchKeyword(it)
        })

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
    }


    private fun initBookService() {

        val interceptor = HttpLoggingInterceptor()
        interceptor.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.interparkUrl))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)

    }


    private fun getBestSeller() {

        bookService.getBestSeller(getString(R.string.interparkApiKey), CATEGORY_ID)
            .enqueue(object : Callback<BestSellerDto> {
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        Log.d(MAIN_TAG, it.toString())

                        adapter.submitList(it.books)

                        it.books.forEach{ book ->
                            Log.d(MAIN_TAG, book.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.e(MAIN_TAG, t.toString())
                }
            })

    }

    private fun initSearchEditText() {
        binding.searchEditText
            .setOnKeyListener { view, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                    search(binding.searchEditText.text.toString())
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

        binding.searchEditText
            .setOnTouchListener{ view, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    showHistoryView()
                }
                return@setOnTouchListener false
            }
    }

    private fun search(keyword: String) {
        hideHistoryView()

        if (keyword.isBlank()) {
            getBestSeller()
            return
        }

        bookService.getBooksByName(getString(R.string.interparkApiKey), keyword)
            .enqueue(object : Callback<SearchBookDto> {
                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {
                    saveSearchKeyword(keyword);
                    if (response.isSuccessful.not()) {
                        return;
                    }

                    response.body()?.let {
                        Log.d(MAIN_TAG, it.toString())

                        adapter.submitList(it.books)

                        it.books.forEach{ book ->
                            Log.d(MAIN_TAG, book.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    Log.e(MAIN_TAG, t.toString())
                }

            })

    }


    private fun showHistoryView(){
        Thread {
            val keywords = db.historyDao().getAll().reversed()
            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()
    }

    private fun hideHistoryView(){
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            // View 갱신
            showHistoryView()
        }.start()
    }

    companion object {
        private const val MAIN_TAG = "MainActivity"

        private const val CATEGORY_ID = "100"
    }
}