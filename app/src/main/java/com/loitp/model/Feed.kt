package com.loitp.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by ©Loitp93 on 2/4/2021.
 * VinHMS
 * www.muathu@gmail.com
 */
@Keep
data class Feed(
        @SerializedName("title")
        @Expose
        var title: String = "",

        @SerializedName("url")
        @Expose
        var url: String = ""
) : Serializable
