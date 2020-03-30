package vn.vistark.locas.core.api

import AddressComponents
import DanhMucViTriResponse
import FavoritePlacesResponse
import GeocodingResponse
import LocationToCodeResponse
import PlaceFromWardResponse
import PlaceInRangeResponse
import Results
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import vn.vistark.locas.core.request_model.coordinate.CoodinateRequest
import vn.vistark.locas.core.request_model.coordinate.Coodinates
import vn.vistark.locas.core.request_model.place_in_range.PlaceInRangeRequest
import vn.vistark.locas.core.response_model.check.CheckResponse
import vn.vistark.locas.core.response_model.login.LoginResponse

public interface APIServices {
    @GET("/api/places/all")
    fun getLocationCategories(): Call<DanhMucViTriResponse>

    @POST("/api/users/checkphone")
    @FormUrlEncoded
    fun checkPhoneNumberExist(@Field("phone") phone: String): Call<CheckResponse>

    @POST("/api/users/checkusername")
    @FormUrlEncoded
    fun checkUsernameExist(@Field("username") username: String): Call<CheckResponse>

    @POST("/api/users/signup")
    @FormUrlEncoded
    fun signup(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("email") email: String
    ): Call<CheckResponse>

    @POST("/api/users/signin")
    @FormUrlEncoded
    fun login(@Field("username") username: String, @Field("password") password: String): Call<LoginResponse>

    @POST("/api/users/updatelastcoordinate")
    fun updateLastCoodinates(@Body coordinate: CoodinateRequest): Call<CheckResponse>

    @POST("/api")
    fun getLocationTypeCode()

    @POST("/api/places/getplacesfromward")
    @FormUrlEncoded
    fun getPlacesFromward(@Field("ward") ward: String): Call<PlaceFromWardResponse>

    @GET("/api/users/getfavoriteplacesfromuser")
    fun getFavoritePlaceFromUser(): Call<FavoritePlacesResponse>

    @GET("https://maps.google.com/maps/api/geocode/json?sensor=true&key=AIzaSyDksc5_jxcH_rWobg-K3bI863_SB2q4wWI&language=vi")
    fun getLocationDetails(@Query("latlng") latlng: String): Call<GeocodingResponse>

    @POST("/api/places/getallfromdetail")
    fun convertLocationToCode(@Body results: Results): Call<LocationToCodeResponse>

    @POST("/api/places/getallfromwardid")
    @FormUrlEncoded
    fun getAllLocaTopInfoFromCode(@Field("ward_id") ward_id: Int): Call<LocationToCodeResponse>

    @POST("/api/places/getplacesinrange")
    fun placeInRange(@Body placeInRangeRequest: PlaceInRangeRequest): Call<PlaceInRangeResponse>

    @POST("/api/places/insertplacefromuser")
    @Multipart
    fun addNewPlace(
        @Part("ten_dd") ten_dd: String,
        @Part("mo_ta") mo_ta: String,
        @Part("toa_do") toa_do: Coodinates,
        @Part("gio_mo_cua") gio_mo_cua: String,
        @Part("gio_dong_cua") gio_dong_cua: String,
        @Part("ma_xap") ma_xap: Int,
        @Part("ma_dm") ma_dm: Int,
        @Part("dia_chi") dia_chi: String,
        @Part("luoc_su") luoc_su: String,
        @Part hinh_anh: MultipartBody.Part,
        @Part logo: MultipartBody.Part
    ): Call<CheckResponse>
    //=============== Check User API ========================//
//    @POST("/api/captains/checkuser")
//    @FormUrlEncoded
//    fun checkUserAPI(@Field("username") username: String): Call<CheckUser>
}