package me.zeroest.part3_ch04.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.zeroest.part3_ch04.model.History

@Dao
interface HistoryDao {

    @Query("select * from history")
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("delete from history where keyword == :keyword")
    fun delete(keyword: String)

}