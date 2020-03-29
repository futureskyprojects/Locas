import com.google.gson.annotations.SerializedName

data class FavoritePlaces(
    @SerializedName("ma_yt") val ma_yt: Int,
    @SerializedName("ma_nd") val ma_nd: Int,
    @SerializedName("ngay_tao") val ngay_tao: String,
    @SerializedName("ngay_sua") val ngay_sua: String,
    @SerializedName("ma_dd") val ma_dd: Int,
    @SerializedName("ten_dd") val ten_dd: String,
    @SerializedName("mo_ta") val mo_ta: String,
    @SerializedName("toa_do") val toa_do: String,
    @SerializedName("logo") val logo: String,
    @SerializedName("gio_mo_cua") val gio_mo_cua: String,
    @SerializedName("gio_dong_cua") val gio_dong_cua: String,
    @SerializedName("luoc_su_hinh_thanh") val luoc_su_hinh_thanh: String,
    @SerializedName("nguoi_tao") val nguoi_tao: Int,
    @SerializedName("ma_xap") val ma_xap: Int,
    @SerializedName("hinh_anh") val hinh_anh: String,
    @SerializedName("ma_dm") val ma_dm: Int,
    @SerializedName("trang_thai") val trang_thai: Int,
    @SerializedName("dia_chi") val dia_chi: String
)