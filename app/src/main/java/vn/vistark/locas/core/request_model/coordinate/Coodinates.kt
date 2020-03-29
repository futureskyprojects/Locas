package vn.vistark.locas.core.request_model.coordinate

import com.google.gson.annotations.SerializedName

data class Coodinates(
    @SerializedName("lat") var lat: Double,
    @SerializedName("lng") var lng: Double
)