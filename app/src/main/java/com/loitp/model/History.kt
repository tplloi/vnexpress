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
data class History(
    @SerializedName("date")
    @Expose
    var date: String = "",

    @SerializedName("money")
    @Expose
    var money: String = "",

    @SerializedName("phone")
    @Expose
    var phone: String = ""
) : Serializable
