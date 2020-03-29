import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class PlacesFromWard(
    @SerializedName("ma_dd") val ma_dd: Int,
    @SerializedName("ten_dd") val ten_dd: String,
    @SerializedName("mo_ta") val mo_ta: String,
    @SerializedName("toa_do") val toa_do: String,
    @SerializedName("logo") val logo: String,
    @SerializedName("gio_mo_cua") val gio_mo_cua: String,
    @SerializedName("gio_dong_cua") val gio_dong_cua: String,
    @SerializedName("luoc_su_hinh_thanh") val luoc_su_hinh_thanh: String,
    @SerializedName("ngay_tao") val ngay_tao: String,
    @SerializedName("ngay_sua") val ngay_sua: String,
    @SerializedName("nguoi_tao") val nguoi_tao: Int,
    @SerializedName("ma_xap") val ma_xap: Int,
    @SerializedName("hinh_anh") val hinh_anh: String,
    @SerializedName("ma_dm") val ma_dm: Int,
    @SerializedName("trang_thai") val trang_thai: Int,
    @SerializedName("dia_chi") val dia_chi: String,
    @SerializedName("rating") val rating: Int
)