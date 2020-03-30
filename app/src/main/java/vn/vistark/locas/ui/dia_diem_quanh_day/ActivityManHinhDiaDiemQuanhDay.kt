package vn.vistark.locas.ui.dia_diem_quanh_day

import PlaceInRangeResponse
import PlaceInRangeResult
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_man_hinh_dia_diem_quanh_day.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.vistark.locas.R
import vn.vistark.locas.core.Constants
import vn.vistark.locas.core.api.APIUtils
import vn.vistark.locas.core.request_model.coordinate.Coodinates
import vn.vistark.locas.core.request_model.place_in_range.PlaceInRangeRequest
import vn.vistark.locas.core.utils.LoadingDialog
import vn.vistark.locas.core.utils.SimpfyLocationUtils
import vn.vistark.locas.core.utils.SimpleNotify
import vn.vistark.locas.ui.chuc_nang_chinh.danh_muc_dia_diem.DanhMucDiaDiemFragment
import vn.vistark.locas.ui.chuc_nang_chinh.dia_diem_yeu_thich.DiaDiemYeuThichFragment

class ActivityManHinhDiaDiemQuanhDay : AppCompatActivity() {

    val arrayList = ArrayList<PlaceInRangeResult>()
    lateinit var loading: LoadingDialog

    lateinit var adapter: DiaDiemAdapter


    companion object {
        var googleMap: GoogleMap? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_man_hinh_dia_diem_quanh_day)
        loading = LoadingDialog(this)
        initEvents()
        initMaps(savedInstanceState)
        initRecyclerView()
        layDiaDiemQuanhDay()
    }

    private fun layDiaDiemQuanhDay() {
        if (SimpfyLocationUtils.mLastLocation != null) {
            if (DanhMucDiaDiemFragment.selectedDanhMuc != null) {
                val coordinates = Coodinates(
                    SimpfyLocationUtils.mLastLocation!!.latitude,
                    SimpfyLocationUtils.mLastLocation!!.longitude
                )
                val placeInRangeRequest = PlaceInRangeRequest(coordinates, Constants.range)
                loading.show()
                APIUtils.mAPIServices?.placeInRange(placeInRangeRequest)?.enqueue(object :
                    Callback<PlaceInRangeResponse> {
                    override fun onFailure(call: Call<PlaceInRangeResponse>, t: Throwable) {
                        SimpleNotify.networkError(this@ActivityManHinhDiaDiemQuanhDay)
                        loading.dismiss()
                    }

                    override fun onResponse(
                        call: Call<PlaceInRangeResponse>,
                        response: Response<PlaceInRangeResponse>
                    ) {
                        if (response.isSuccessful) {
                            val placeInRangeResponse = response.body()
                            if (placeInRangeResponse != null) {
                                if (placeInRangeResponse.results.isEmpty()) {
                                    SimpleNotify.success(
                                        this@ActivityManHinhDiaDiemQuanhDay,
                                        "Không có địa điểm thỏa điều kiện",
                                        ""
                                    )
                                    loading.dismiss()
                                    return
                                } else {
                                    loading.dismiss()
                                    for (re in placeInRangeResponse.results) {
                                        if (re.ma_dm == DanhMucDiaDiemFragment.selectedDanhMuc!!.ma_dm) {
                                            arrayList.add(re)
                                            try {
                                                val theCoordinates = re.toa_do
                                                val makerOptions = MarkerOptions()
                                                val latLng =
                                                    LatLng(theCoordinates.lat, theCoordinates.lng)
                                                makerOptions.position(latLng)
                                                makerOptions.title(re.ten_dd)
                                                makerOptions.icon(
                                                    BitmapDescriptorFactory.fromResource(
                                                        R.drawable.locas_pin
                                                    )
                                                );
                                                googleMap!!.addMarker(makerOptions)
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                                    if (arrayList.isEmpty()) {
                                        SimpleNotify.success(
                                            this@ActivityManHinhDiaDiemQuanhDay,
                                            "Không có địa điểm thỏa điều kiện",
                                            ""
                                        )
                                    }
                                    return
                                }
                            }
                        }
                        SimpleNotify.error(
                            this@ActivityManHinhDiaDiemQuanhDay,
                            "Không lấy được các địa điểm quanh đây",
                            ""
                        )
                        loading.dismiss()
                    }
                })
            } else {
                Toast.makeText(this, "Bạn chưa chọn danh mục địa điểm", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Chưa lấy được vị trí hiện tại. Hãy thử lại", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun initRecyclerView() {
        rvTopPlaces.setHasFixedSize(true)
        rvTopPlaces.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = DiaDiemAdapter(arrayList)
        rvTopPlaces.adapter = adapter
    }

    private fun initEvents() {
        ivBackBtn.setOnClickListener {
            onBackPressed()
        }
        tvPlaceCategoryName.text = DanhMucDiaDiemFragment.selectedDanhMuc?.ten_dm
    }

    private fun initMaps(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        MapsInitializer.initialize(this)
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
    }
}
