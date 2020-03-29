package vn.vistark.locas.core.api

import DanhMucViTriResponse
import FavoritePlacesResponse
import GeocodingResponse
import PlaceFromWardResponse
import retrofit2.Call
import retrofit2.http.*
import vn.vistark.locas.core.request_model.coordinate.CoodinateRequest
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

    @GET("https://maps.google.com/maps/api/geocode/json?sensor=true&key=AIzaSyDksc5_jxcH_rWobg-K3bI863_SB2q4wWI")
    fun getLocationDetails(@Query("latlng") latlng: String): Call<GeocodingResponse>
    //=============== Check User API ========================//
//    @POST("/api/captains/checkuser")
//    @FormUrlEncoded
//    fun checkUserAPI(@Field("username") username: String): Call<CheckUser>
}