package vn.vistark.locas.ui.chuc_nang_chinh

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_SELECTED
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.vistark.locas.R
import vn.vistark.locas.core.Constants
import vn.vistark.locas.core.api.APIUtils
import vn.vistark.locas.core.request_model.coordinate.CoodinateRequest
import vn.vistark.locas.core.request_model.coordinate.Coodinates
import vn.vistark.locas.core.response_model.check.CheckResponse
import vn.vistark.locas.core.utils.*
import vn.vistark.locas.ui.chuc_nang_chinh.danh_muc_dia_diem.DanhMucDiaDiemFragment
import vn.vistark.locas.ui.chuc_nang_chinh.dia_diem_yeu_thich.DiaDiemYeuThichFragment
import vn.vistark.locas.ui.chuc_nang_chinh.thiet_lap.ManHinhThietLapFragment


class ManHinhMenu : AppCompatActivity() {

    var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // In Activity's onCreate() for instance
        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_man_hinh_menu)

        navigationOptions()

        initLocationManager()

        TtsLibs.chaoMung(this)
    }

    @SuppressLint("MissingPermission")
    private fun initLocationManager() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            10000L,
            0f,
            listener
        )
    }


    private fun navigationOptions() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setBackgroundColor(Color.parseColor("#00000000"))
        navView.itemIconTintList = null
        navView.labelVisibilityMode = LABEL_VISIBILITY_SELECTED
        var previousSelectedMenuItemId = R.id.navigation_danh_muc_dia_diem
        loadFragment(DanhMucDiaDiemFragment())
        navView.setOnNavigationItemSelectedListener {
            if (it.itemId != previousSelectedMenuItemId) {
                previousSelectedMenuItemId = it.itemId
                when (it.itemId) {
                    R.id.navigation_danh_muc_dia_diem -> {
                        loadFragment(DanhMucDiaDiemFragment())
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.navigation_dia_diem_yeu_thich -> {
                        TtsLibs.defaultTalk(
                            this,
                            "Đang hiển thị danh sách các địa điểm yêu thích của bạn"
                        )
                        loadFragment(DiaDiemYeuThichFragment())
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.navigation_thiet_lap -> {
                        TtsLibs.defaultTalk(this, "Hãy chọn thiết lập mà bạn mong muốn")
                        loadFragment(ManHinhThietLapFragment())
                        return@setOnNavigationItemSelectedListener true
                    }
                    else -> {
                        return@setOnNavigationItemSelectedListener true
                    }
                }
            } else {
                return@setOnNavigationItemSelectedListener false
            }
        }
    }

    val listener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            Log.w("Vị trí", "${location?.latitude} ${location?.longitude}")
            if (location != null) {
                SimpfyLocationUtils.mLastLocation = location
                APIUtils.mAPIServices?.updateLastCoodinates(
                    CoodinateRequest(
                        Coodinates(
                            location.latitude,
                            location.longitude
                        )
                    )
                )?.enqueue(object : Callback<CheckResponse> {
                    override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                        Log.e("Lỗi", "Cập nhật vị trí lỗi")
                    }

                    override fun onResponse(
                        call: Call<CheckResponse>,
                        response: Response<CheckResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.w("Vị trí", "Cập nhật vị trí thành công")
                        } else {
                            Log.e(
                                "Lỗi",
                                "Cập nhật vị trí chưa được " + response.body()?.message
                            )
                        }
                    }
                })
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
            SimpleNotify.error(this@ManHinhMenu, "Vui lòng bật GPS", "")
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_main, fragment)
        transaction.commit()
    }

    override fun onDestroy() {
        locationManager?.removeUpdates(listener)
        super.onDestroy()
    }
}
