package vn.vistark.locas.ui.dang_nhap

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.login_field.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.vistark.locas.R
import vn.vistark.locas.core.Constants
import vn.vistark.locas.core.api.APIUtils
import vn.vistark.locas.core.response_model.login.LoginResponse
import vn.vistark.locas.core.utils.Crypto
import vn.vistark.locas.core.utils.LoadingDialog
import vn.vistark.locas.core.utils.SimpfyLocationUtils
import vn.vistark.locas.core.utils.SimpleNotify
import vn.vistark.locas.ui.dang_ky.ManHinhDangKy
import vn.vistark.locas.ui.chuc_nang_chinh.ManHinhMenu
import java.util.*

class ManHinhDangNhap : AppCompatActivity() {
    lateinit var loading: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // In Activity's onCreate() for instance
        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_man_hinh_dang_nhap)

        Constants.initialize(this)

        initViews()

        permissionRequest()

        initEvents()
    }

    private fun initViews() {
        loading = LoadingDialog(this)
        edtUsername.setText(Constants.username)
        edtPassword.setText(Constants.password)
    }

    private fun initLocation() {
        SimpfyLocationUtils.getLastLocation(FusedLocationProviderClient(this))
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (SimpfyLocationUtils.mLastLocation != null) {
                    if (Constants.token.isNotEmpty()) {
                        runOnUiThread {
                            btnDangNhap.performClick()
                        }
                    } else {
                        loading.dismiss()
                    }
                    this.cancel()
                }
            }
        }, 300, 1000)
    }

    private fun initEvents() {
        btnDangKy.setOnClickListener {
            startActivity(Intent(this, ManHinhDangKy::class.java))
            finish()
        }
        btnDangNhap.setOnClickListener {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()
            if (username.length < 5) {
                SimpleNotify.error(this, "Tài khoản không hợp lệ", "")
                return@setOnClickListener
            } else if (password.length < 8) {
                SimpleNotify.error(this, "Mật khẩu không hợp lệ", "")
                return@setOnClickListener
            }

            loading.show()
            val md5Pass = Crypto.md5(password)
            if (md5Pass.isNotEmpty()) {
                APIUtils.mAPIServices?.login(username, md5Pass)
                    ?.enqueue(object : Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            SimpleNotify.networkError(this@ManHinhDangNhap)
                            loading.dismiss()
                        }

                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            if (response.isSuccessful) {
                                val login = response.body()
                                if (login != null && login.code == 1) {
                                    Constants.username = edtUsername.text.toString()
                                    Constants.password = edtPassword.text.toString()

                                    Constants.token = login.token
                                    Constants.avatar = login.avatar ?: ""
                                    APIUtils.replaceAPIServices()
                                    goToMenuScreen()
                                    loading.dismiss()
                                    return
                                }
                            }
                            SimpleNotify.error(this@ManHinhDangNhap, "Đăng nhập thất bại", "")
                            loading.dismiss()
                        }
                    })
            } else {
                SimpleNotify.error(this, "Lỗi phân giải mật khẩu", "")
                loading.dismiss()
            }
        }
    }

    private fun goToMenuScreen() {
        startActivity(
            Intent(
                this@ManHinhDangNhap,
                ManHinhMenu::class.java
            )
        )
        finish()
    }

    private fun permissionRequest() {
        loading.show()
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                1234
            )
        } else {
            initLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1234 -> {
                if (grantResults.size >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {
                    initLocation()
                } else {
                    permissionRequest()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
