package com.loitp.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by ©Loitp93 on 2/4/2021.
 * VinHMS
 * www.muathu@gmail.com
 */
@Keep
@Entity(tableName = "NewsFeed")
data class NewsFeed(
        @ColumnInfo(name = "title")
        @SerializedName("title")
        @Expose
        var title: String = "",

        @PrimaryKey
        @ColumnInfo(name = "link")
        @SerializedName("link")
        @Expose
        var link: String = "",

        @ColumnInfo(name = "image")
        @SerializedName("image")
        @Expose
        var image: String = "",

        @ColumnInfo(name = "publishDate")
        @SerializedName("publishDate")
        @Expose
        var publishDate: String = "",

        @ColumnInfo(name = "description")
        @SerializedName("description")
        @Expose
        var description: String = "",

        @ColumnInfo(name = "feedType")
        @SerializedName("feedType")
        @Expose
        var feedType: String = ""
) : Serializable
