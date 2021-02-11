package com.loitp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.core.base.BaseDao
import com.loitp.model.NewsFeed

@Dao
interface AppDao : BaseDao<NewsFeed> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListNewsFeed(list: List<NewsFeed>)

    @Query("SELECT * FROM NewsFeed WHERE feedType=:feedType LIMIT :limitNumber OFFSET :offsetNumber")
    fun getListNewsFeed(feedType: String?, limitNumber: Int, offsetNumber: Int): List<NewsFeed>

    @Query("DELETE FROM NewsFeed")
    suspend fun deleteAll()

//    @Query("SELECT * FROM Comic WHERE url=:url")
//    fun find(url: String): Comic?
}
