package vn.vistark.locas.core.request_model

import com.google.gson.annotations.SerializedName

data class CoodinateRequest(
    @SerializedName("coordinate") var coodinates: Coodinates
)