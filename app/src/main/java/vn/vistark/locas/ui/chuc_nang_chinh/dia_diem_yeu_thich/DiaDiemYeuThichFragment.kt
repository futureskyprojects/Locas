package vn.vistark.locas.ui.chuc_nang_chinh.dia_diem_yeu_thich


import FavoritePlaces
import FavoritePlacesResponse
import android.app.Activity
import android.os.Bundle
import android.telecom.Call
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_dia_diem_yeu_thich.*
import retrofit2.Callback
import retrofit2.Response

import vn.vistark.locas.R
import vn.vistark.locas.core.api.APIUtils
import vn.vistark.locas.core.request_model.coordinate.Coodinates
import vn.vistark.locas.core.utils.LoadingDialog
import vn.vistark.locas.core.utils.SimpfyLocationUtils
import vn.vistark.locas.core.utils.SimpleNotify
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class DiaDiemYeuThichFragment : Fragment() {

    companion object {
        var googleMap: GoogleMap? = null
    }

    lateinit var loading: LoadingDialog

    lateinit var adapter: YeuThichAdapter
    lateinit var rvYeuThich: RecyclerView
    val favoritePlaces = ArrayList<FavoritePlaces>()

    lateinit var mapView: MapView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_dia_diem_yeu_thich, container, false)
        //////
        mapView = v.findViewById(R.id.mapView)
        loading = LoadingDialog(context!!)
        loading.show()
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        MapsInitializer.initialize(context)
        mapView.getMapAsync { gMaps ->
            googleMap = gMaps
            googleMap?.setOnMapLoadedCallback {
                googleMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
                googleMap!!.uiSettings.isZoomControlsEnabled = true
                googleMap!!.isMyLocationEnabled = true
                if (SimpfyLocationUtils.mLastLocation != null) {
                    val latLng = LatLng(
                        SimpfyLocationUtils.mLastLocation!!.latitude,
                        SimpfyLocationUtils.mLastLocation!!.longitude
                    )
                    val camUp = CameraUpdateFactory.newLatLngZoom(latLng, 14F)
                    googleMap!!.animateCamera(camUp)
                }
            }
        }
        //////

        initViews(v)
        return v
    }

    private fun initViews(v: View) {
        rvYeuThich = v.findViewById(R.id.rvTopPlaces)
        rvYeuThich.setHasFixedSize(true)
        rvYeuThich.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        adapter = YeuThichAdapter(favoritePlaces)
        rvYeuThich.adapter = adapter

        getFavoritePlaces()
    }

    private fun getFavoritePlaces() {
        loading.show()
        APIUtils.mAPIServices?.getFavoritePlaceFromUser()?.enqueue(object :
            Callback<FavoritePlacesResponse> {
            override fun onFailure(call: retrofit2.Call<FavoritePlacesResponse>, t: Throwable) {
                SimpleNotify.networkError(this@DiaDiemYeuThichFragment.context!!)
                loading.dismiss()
            }

            override fun onResponse(
                call: retrofit2.Call<FavoritePlacesResponse>,
                response: Response<FavoritePlacesResponse>
            ) {
                if (response.isSuccessful) {
                    val fps = response.body()
                    if (fps != null && fps.code == 1) {
                        for (fp in fps.places) {
                            favoritePlaces.add((fp))
                            try {
                                val coordinates = GsonBuilder().create()
                                    .fromJson<Coodinates>(fp.toa_do, Coodinates::class.java)
                                val makerOptions = MarkerOptions()
                                val latLng = LatLng(coordinates.lat, coordinates.lng)
                                makerOptions.position(latLng)
                                makerOptions.title(fp.ten_dd)
                                makerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.heart_pin));
                                googleMap!!.addMarker(makerOptions)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        loading.dismiss()
                        return
                    }
                }
                loading.dismiss()
                SimpleNotify.error(
                    this@DiaDiemYeuThichFragment.context!!,
                    "Không lấy được danh sách yêu thích"
                    , ""
                )
            }
        })
    }
}
