package vn.vistark.locas.core.response_model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("username") val username: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("message") val message: String,
    @SerializedName("code") val code: Int
)