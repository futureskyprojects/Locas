package vn.vistark.locas.core.utils

import GeocodingResponse
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.TextView
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.vistark.locas.core.api.APIUtils
import java.lang.Exception
import java.util.*
import kotlin.math.log


class CoordinatesDecoder {
    companion object {
        var previousQuery = ""
        var previousGeocodingResponse: String = ""

        fun getAddressline(textView: TextView, latitude: Double, longitude: Double) {
            APIUtils.mAPIServices?.getLocationDetails("${latitude},${longitude}")?.enqueue(object :
                Callback<GeocodingResponse> {
                override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                    textView.text = "Vị trí không xác định"
                }

                override fun onResponse(
                    call: Call<GeocodingResponse>,
                    response: Response<GeocodingResponse>
                ) {
                    if (response.isSuccessful) {
                        val geocodingResponse = response.body()
                        if (geocodingResponse != null && geocodingResponse.status.contains("OK") && geocodingResponse.results.isNotEmpty()) {
                            textView.text = geocodingResponse.results[4].formatted_address.replace(
                                "Unnamed Road, ",
                                ""
                            )
                            previousQuery = "${latitude},${longitude}"
                            previousGeocodingResponse =
                                GsonBuilder().create().toJson(geocodingResponse)
                            return
                        }
                    }
                    textView.text = "Vị trí không xác định"
                }
            })
        }
    }
}