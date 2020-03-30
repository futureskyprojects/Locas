package vn.vistark.locas.core.request_model.place_in_range

import com.google.gson.annotations.SerializedName
import vn.vistark.locas.core.request_model.coordinate.Coodinates

data class PlaceInRangeRequest(
    @SerializedName("coordinate") var coordinate: Coodinates,
    @SerializedName("distance") var distance: Float
)