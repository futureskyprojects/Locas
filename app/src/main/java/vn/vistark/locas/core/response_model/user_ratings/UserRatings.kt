import com.google.gson.annotations.SerializedName
import vn.vistark.locas.core.request_model.coordinate.Coodinates

/*
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class UserRatings(

    @SerializedName("ma_dg") val ma_dg: Int,
    @SerializedName("description") val description: String,
    @SerializedName("rating") val rating: Float,
    @SerializedName("anh_1") val anh_1: String,
    @SerializedName("anh_2") val anh_2: String,
    @SerializedName("anh_3") val anh_3: String,
    @SerializedName("nguoi_dg") val nguoi_dg: Int,
    @SerializedName("ma_dd") val ma_dd: Int,
    @SerializedName("ngay_tao") val ngay_tao: String,
    @SerializedName("ngay_sua") val ngay_sua: String,
    @SerializedName("ma_nd") val ma_nd: Int,
    @SerializedName("ho_nd") val ho_nd: String,
    @SerializedName("ten_nd") val ten_nd: String,
    @SerializedName("email") val email: String,
    @SerializedName("sdt") val sdt: Int,
    @SerializedName("toa_do_sau_cung") val toa_do_sau_cung: Coodinates,
    @SerializedName("ngay_sinh") val ngay_sinh: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("username") val username: String,
    @SerializedName("md5_pass") val md5_pass: String,
    @SerializedName("trang_thai") val trang_thai: Int,
    @SerializedName("dg_ngay_sua") val dg_ngay_sua: String,
    @SerializedName("dg_ngay_tao") val dg_ngay_tao: String,
    @SerializedName("nguoi_dung_ngay_tao") val nguoi_dung_ngay_tao: String,
    @SerializedName("nguoi_dung_ngay_sua") val nguoi_dung_ngay_sua: String
)