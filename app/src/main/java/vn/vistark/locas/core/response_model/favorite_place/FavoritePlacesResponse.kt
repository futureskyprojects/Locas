import com.google.gson.annotations.SerializedName

data class FavoritePlacesResponse(
    @SerializedName("places") val places: List<FavoritePlaces>,
    @SerializedName("message") val message: String,
    @SerializedName("code") val code: Int
)