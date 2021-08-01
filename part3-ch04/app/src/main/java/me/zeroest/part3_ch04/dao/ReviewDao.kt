package me.zeroest.part3_ch04.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.zeroest.part3_ch04.model.Review

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)

    @Query("select * from review where id == :bookId")
    fun findByBookId(bookId: Int): Review

}