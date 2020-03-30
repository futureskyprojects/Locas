package vn.vistark.locas.ui.dang_ky

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.login_field.*
import kotlinx.android.synthetic.main.login_field.btnDangKy
import kotlinx.android.synthetic.main.login_field.btnDangNhap
import kotlinx.android.synthetic.main.register_field.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.vistark.locas.R
import vn.vistark.locas.core.Constants
import vn.vistark.locas.core.api.APIUtils
import vn.vistark.locas.core.response_model.check.CheckResponse
import vn.vistark.locas.core.utils.Crypto
import vn.vistark.locas.core.utils.LoadingDialog
import vn.vistark.locas.core.utils.SimpleNotify
import vn.vistark.locas.ui.chuc_nang_chinh.ManHinhMenu
import vn.vistark.locas.ui.dang_nhap.ManHinhDangNhap

class ManHinhDangKy : AppCompatActivity() {
    lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // In Activity's onCreate() for instance
        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_man_hinh_dang_ky)

        initViews()

        initEvents()
    }

    private fun initViews() {
        loadingDialog = LoadingDialog(this)
    }

    private fun initEvents() {
        btnDangNhap.setOnClickListener {
            startActivity(Intent(this, ManHinhDangNhap::class.java))
            finish()
        }

        btnDangKy.setOnClickListener {
            dangKy()
        }
    }

    private fun dangKy() {
        val tenTk = rfEdtTenTaiKhoan.text.toString()
        val sdt = rfEdtSoDienThoai.text.toString()
        val mk = rfEdtMatKhau.text.toString()
        val nhapLaiMk = rfEdtNhapLaiMatKhau.text.toString()

        if (tenTk.isEmpty() || sdt.isEmpty() || mk.isEmpty() || nhapLaiMk.isEmpty()) {
            SimpleNotify.warning(this, "Thiếu thông tin", "")
        } else {
            if (mk != nhapLaiMk) {
                SimpleNotify.warning(this, "Mật khẩu nhập lại không đúng", "")
            } else {
                if (tenTk.length < 5) {
                    SimpleNotify.warning(this, "Tài khoản quá ngắn", "")
                } else if (sdt.length != 10) {
                    SimpleNotify.warning(this, "Số điện thoại phải đủ 10 chữ số", "")
                } else if (mk.length < 8) {
                    SimpleNotify.warning(this, "Mật khẩu tối thiểu 8 ký tự", "")
                } else {
                    loadingDialog.show()
                    checkUsername()
                }
            }
        }
    }

    private fun checkUsername() {
        val tenTk = rfEdtTenTaiKhoan.text.toString()
        APIUtils.mAPIServices?.checkUsernameExist(tenTk)
            ?.enqueue(object : Callback<CheckResponse> {
                override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                    SimpleNotify.networkError(this@ManHinhDangKy)
                    loadingDialog.dismiss()
                }

                override fun onResponse(
                    call: Call<CheckResponse>,
                    response: Response<CheckResponse>
                ) {
                    if (response.isSuccessful) {
                        val checkResponse = response.body()
                        if (checkResponse != null) {
                            if (checkResponse.code == 0) {
                                checkPhoneNumber()
                                return
                            } else if (checkResponse.code == 1) {
                                SimpleNotify.error(this@ManHinhDangKy, "Tài khoản đã tồn tại", "")
                                loadingDialog.dismiss()
                                return
                            }
                        }
                    }
                    SimpleNotify.error(this@ManHinhDangKy, "Kiểm tra tên tài khoản lỗi", "")
                    loadingDialog.dismiss()
                }
            })
    }

    private fun checkPhoneNumber() {
        val sdt = rfEdtSoDienThoai.text.toString()
        APIUtils.mAPIServices?.checkPhoneNumberExist(sdt)
            ?.enqueue(object : Callback<CheckResponse> {
                override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                    SimpleNotify.networkError(this@ManHinhDangKy)
                    loadingDialog.dismiss()
                }

                override fun onResponse(
                    call: Call<CheckResponse>,
                    response: Response<CheckResponse>
                ) {
                    if (response.isSuccessful) {
                        val checkResponse = response.body()
                        if (checkResponse != null) {
                            if (checkResponse.code == 0) {
                                register()
                                return
                            } else if (checkResponse.code == 1) {
                                SimpleNotify.error(this@ManHinhDangKy, "SĐT đã tồn tại", "")
                                loadingDialog.dismiss()
                                return
                            }
                        }
                    }
                    SimpleNotify.error(this@ManHinhDangKy, "Kiểm tra SĐT lỗi", "")
                    loadingDialog.dismiss()
                }
            })
    }

    private fun register() {
        loadingDialog.dismiss()
        val tenTk = rfEdtTenTaiKhoan.text.toString()
        val sdt = rfEdtSoDienThoai.text.toString()
        val mk = rfEdtMatKhau.text.toString()

        val hashPass = Crypto.md5(mk)
        if (hashPass.isNotEmpty()) {
            APIUtils.mAPIServices?.signup(tenTk, hashPass, sdt, "")
                ?.enqueue(object : Callback<CheckResponse> {
                    override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                        SimpleNotify.networkError(this@ManHinhDangKy)
                        loadingDialog.dismiss()
                    }

                    override fun onResponse(
                        call: Call<CheckResponse>,
                        response: Response<CheckResponse>
                    ) {
                        if (response.isSuccessful) {
                            val check = response.body()
                            if (check != null && check.code == 1) {
                                requestEditProfile(tenTk, mk)
                                return
                            }
                        }
                        SimpleNotify.error(this@ManHinhDangKy, "Đăng ký không thành công", "")
                        loadingDialog.dismiss()
                    }
                })
        } else {
            SimpleNotify.error(this, "Lỗi phân giải mật khẩu", "")
            loadingDialog.dismiss()
        }
    }

    private fun requestEditProfile(tenTk: String, mk: String) {
        SweetAlertDialog(this).run {
            titleText = "Đăng ký thành công"
            contentText = "Chúc mừng bạn"
            setCancelable(false)
            setConfirmButton("Đăng nhâp") {
                Constants.username = tenTk
                Constants.password = mk
                it.dismissWithAnimation()
                this@ManHinhDangKy.btnDangNhap.performClick()
            }
            showCancelButton(false)
            show()
        }
    }
}
