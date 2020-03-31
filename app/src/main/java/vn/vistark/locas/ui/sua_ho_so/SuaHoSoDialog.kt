package vn.vistark.locas.ui.sua_ho_so

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.vistark.locas.R
import vn.vistark.locas.core.Constants
import vn.vistark.locas.core.api.APIUtils
import vn.vistark.locas.core.response_model.check.CheckResponse
import vn.vistark.locas.core.utils.LoadingDialog
import vn.vistark.locas.core.utils.SaveFileUtils
import vn.vistark.locas.core.utils.SimpleNotify
import vn.vistark.locas.ui.trashing.TempActivityForSelectFile
import java.io.File
import java.util.*

class SuaHoSoDialog(context: Context) : AlertDialog(context), DatePickerDialog.OnDateSetListener {
    lateinit var userAvatar: CircleImageView
    lateinit var edtSurname: EditText
    lateinit var edtName: EditText
    lateinit var edtEmail: EditText
    lateinit var edtPhone: EditText
    lateinit var edtNgaySinh: EditText
    lateinit var btnCancel: Button
    lateinit var btnCapNhatHoSo: Button


    lateinit var loadingDialog: LoadingDialog

    companion object {
        var current: SuaHoSoDialog? = null
        var bitmap: Bitmap? = null
        var selectedUri: Uri? = null
        fun updateSelectedUri(uri: Uri?) {
            selectedUri = uri
            if (current != null) {
                Glide.with(current!!.userAvatar)
                    .asBitmap()
                    .load(uri)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            current!!.userAvatar.setImageBitmap(resource)
                            bitmap = resource
                        }
                    })
            }
        }
    }

    fun initViews(v: View) {
        loadingDialog = LoadingDialog(context)
        userAvatar = v.findViewById(R.id.userAvatar)
        edtSurname = v.findViewById(R.id.edtSurname)
        edtName = v.findViewById(R.id.edtName)
        edtEmail = v.findViewById(R.id.edtEmail)
        edtPhone = v.findViewById(R.id.edtPhone)
        edtNgaySinh = v.findViewById(R.id.edtNgaySinh)
        btnCancel = v.findViewById(R.id.btnCancel)
        btnCapNhatHoSo = v.findViewById(R.id.btnCapNhatHoSo)
        //================================//
        loadAvatar()
        edtSurname.setText(Constants.surname)
        edtName.setText(Constants.name)
        edtEmail.setText(Constants.email)
        edtPhone.setText(Constants.phone)
        edtNgaySinh.setText(Constants.birthDay)
        //===============================//
        edtNgaySinh.setOnClickListener {
            edtNgaySinh.clearFocus()
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        userAvatar.setOnClickListener {
            val intent = Intent(context, TempActivityForSelectFile::class.java)
            intent.putExtra("KEY", TempActivityForSelectFile.USER_AVATAR)
            context.startActivity(intent)
        }
        btnCapNhatHoSo.setOnClickListener {
            val ho = edtSurname.text.toString()
            val ten = edtName.text.toString()
            val email = edtEmail.text.toString()
            val sdt = edtPhone.text.toString()
            val ngaySinh = edtNgaySinh.text.toString()

            if (ho.isEmpty()) {
                SimpleNotify.error(context, "Họ và tên đệm trốn", "")
                return@setOnClickListener
            }
            if (ten.isEmpty()) {
                SimpleNotify.error(context, "Tên không thể trống", "")
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                SimpleNotify.error(context, "Thiếu địa chỉ email", "")
                return@setOnClickListener
            }
            if (!email.contains("@")) {
                SimpleNotify.error(context, "Địa chỉ email không hợp lệ", "")
                return@setOnClickListener
            }
            if (sdt.isEmpty()) {
                SimpleNotify.error(context, "Số điện thoại trống", "")
                return@setOnClickListener
            }
            if (ngaySinh.isEmpty()) {
                SimpleNotify.error(context, "Chưa chọn ngày sinh", "")
                return@setOnClickListener
            }
            loadingDialog.show()
            APIUtils.mAPIServices?.updateProfile(ho, ten, ngaySinh, sdt, email)?.enqueue(object :
                Callback<CheckResponse> {
                override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                    SimpleNotify.networkError(context)
                }

                override fun onResponse(
                    call: Call<CheckResponse>,
                    response: Response<CheckResponse>
                ) {
                    if (response.isSuccessful) {
                        val checkResponse = response.body()
                        if (checkResponse != null && checkResponse.code == 1) {
                            if (selectedUri == null) {
                                Toast.makeText(
                                    context,
                                    "Cập nhật hồ sơ thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                                loadingDialog.dismiss()
                                this@SuaHoSoDialog.dismiss()
                                return
                            } else {
                                saveTempFile(context).execute()
                                return
                            }
                        }
                    }


                    SimpleNotify.error(context, "Cập nhật hồ sơ thất bại", "")
                    loadingDialog.dismiss()
                    return
                }
            })
        }

    }

    init {
        current = this
        val v = LayoutInflater.from(context).inflate(R.layout.layout_update_profile, null)
        initViews(v)
        initEvents()

        setView(v)

        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setOnCancelListener {
            current = null
            bitmap = null
            selectedUri = null
        }
    }

    private fun initEvents() {
        btnCancel.setOnClickListener {
            this.cancel()
        }
    }

    private fun loadAvatar() {
        Glide.with(context).load(Constants.avatar).into(userAvatar)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val pickedDate = String.format("%04d-%02d-%02d", year, month, dayOfMonth)
        edtNgaySinh.setText(pickedDate)
    }

    class saveTempFile(val context: Context) : AsyncTask<Void, Boolean, File?>() {
        override fun onPostExecute(result: File?) {
            super.onPostExecute(result)
            current?.uploadAvatar(result)
        }

        override fun doInBackground(vararg params: Void?): File? {
            return SaveFileUtils.saveImages(
                context,
                "user_avatar.${MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(context.contentResolver.getType(selectedUri!!)!!)}",
                bitmap!!
            )
        }
    }

    fun uploadAvatar(f: File?) {
        if (f == null) {
            SimpleNotify.error(context, "Có lỗi xảy ra, vui lòng thử lại", "")
            return
        }
        val avatarReqBody = RequestBody.create(
            MediaType.parse(context.contentResolver.getType(selectedUri!!)!!),
            f
        )

        APIUtils.mAPIServices?.uploadAvatar(
            MultipartBody.Part.createFormData("avatar", f.path, avatarReqBody)
        )?.enqueue(object : Callback<CheckResponse> {
            override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                SimpleNotify.error(context, "Lỗi khi cập nhật avatar", "")
                loadingDialog.dismiss()
            }

            override fun onResponse(call: Call<CheckResponse>, response: Response<CheckResponse>) {
                if (response.isSuccessful) {
                    val checkResponse = response.body()
                    if (checkResponse != null && checkResponse.code == 1) {
                        Toast.makeText(
                            context,
                            "Cập nhật hồ sơ thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingDialog.dismiss()
                        this@SuaHoSoDialog.dismiss()
                        return
                    }
                }

                SimpleNotify.error(context, "Cập avatar thất bại", "")
                loadingDialog.dismiss()
                return
            }
        })
    }
}