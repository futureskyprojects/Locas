package vn.vistark.locas.core.response_model.check

import com.google.gson.annotations.SerializedName

data class CheckResponse(
    @SerializedName("message") var message: String,
    @SerializedName("code") var code: Int
)