package vn.vistark.locas.ui.them_dia_diem

import DanhMuc
import DanhMucViTriResponse
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.GsonBuilder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_danh_gia.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.vistark.locas.R
import vn.vistark.locas.core.api.APIUtils
import vn.vistark.locas.core.request_model.coordinate.Coodinates
import vn.vistark.locas.core.response_model.check.CheckResponse
import vn.vistark.locas.core.utils.*
import vn.vistark.locas.ui.chuc_nang_chinh.danh_muc_dia_diem.DanhMucAdapter
import vn.vistark.locas.ui.trashing.TempActivityForSelectFile
import java.io.File
import java.lang.Exception
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList


class ThemDiaDiemDialog(context: Context) : AlertDialog(context) {
    lateinit var loadingDialog: LoadingDialog

    companion object {
        var selectedFp: DanhMuc? = null
        var imageUri: Uri? = null
        var bm1: Bitmap? = null
        var logoUri: Uri? = null
        var bm2: Bitmap? = null

        var current: ThemDiaDiemDialog? = null

        fun imageUriUpdate(uri: Uri?) {
            imageUri = uri
            if (current != null) {
                Glide.with(current!!.ivHinhAnhDiaDiem)
                    .asBitmap()
                    .load(imageUri)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            current!!.ivHinhAnhDiaDiem.setImageBitmap(resource)
                            bm1 = resource
                        }
                    })
            }
        }

        fun logoUriUpdate(uri: Uri?) {
            logoUri = uri
            if (current != null) {
                Glide
                    .with(current!!.ivLogoHinhAnh)
                    .asBitmap()
                    .load(logoUri)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            current!!.ivLogoHinhAnh.setImageBitmap(resource)
                            bm2 = resource
                        }
                    })
            }
        }
    }

    lateinit var ivHinhAnhDiaDiem: ImageView
    lateinit var ivLogoHinhAnh: CircleImageView
    lateinit var edtPlaceName: EditText
    lateinit var edtPlaceAddress: EditText
    lateinit var edtGioMoCua: EditText
    lateinit var edtGioDongCua: EditText
    lateinit var rvPlaceCategories: RecyclerView
    lateinit var edtLuocSuHinhThanh: EditText
    lateinit var btnCancel: Button
    lateinit var btnThemDiaDiem: Button
    lateinit var tvLoaiDanhMuc: TextView

    fun initViews(v: View) {
        ivHinhAnhDiaDiem = v.findViewById(R.id.ivHinhAnhDiaDiem)
        ivLogoHinhAnh = v.findViewById(R.id.ivLogoHinhAnh)
        edtPlaceName = v.findViewById(R.id.edtPlaceName)
        edtPlaceAddress = v.findViewById(R.id.edtPlaceAddress)
        edtGioMoCua = v.findViewById(R.id.edtGioMoCua)
        edtGioDongCua = v.findViewById(R.id.edtGioDongCua)
        rvPlaceCategories = v.findViewById(R.id.rvPlaceCategories)
        edtLuocSuHinhThanh = v.findViewById(R.id.edtLuocSuHinhThanh)
        btnCancel = v.findViewById(R.id.btnCancel)
        btnThemDiaDiem = v.findViewById(R.id.btnThemDiaDiem)
        tvLoaiDanhMuc = v.findViewById(R.id.tvLoaiDanhMuc)


        initRecyclerViews()
    }

    private fun initRecyclerViews() {
        val danhMucs = ArrayList<DanhMuc>()
        val adapter = DanhMucAdapter(danhMucs)
        adapter.onClick = {
            selectedFp = it
            tvLoaiDanhMuc.text = "Địa điểm thuộc: ${selectedFp!!.ten_dm}"
            TtsLibs.defaultTalk(context, "Địa điểm thuộc: ${selectedFp!!.ten_dm}")
        }
        rvPlaceCategories.setHasFixedSize(true)
        rvPlaceCategories.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvPlaceCategories.adapter = adapter

        APIUtils.mAPIServices?.getLocationCategories()
            ?.enqueue(object : Callback<DanhMucViTriResponse> {
                override fun onFailure(call: Call<DanhMucViTriResponse>, t: Throwable) {
                    t.printStackTrace()
                    SimpleNotify.networkError(
                        context
                    )
                    this@ThemDiaDiemDialog.dismiss()
                }

                override fun onResponse(
                    call: Call<DanhMucViTriResponse>,
                    response: Response<DanhMucViTriResponse>
                ) {
                    if (response.isSuccessful) {
                        val dm = response.body()
                        if (dm != null) {
                            if (dm.places.isNotEmpty()) {
                                for (place in dm.places) {
                                    danhMucs.add(place)
                                    adapter.notifyDataSetChanged()
                                }
                                return
                            } else {
                                SimpleNotify.error(
                                    context,
                                    "Không có danh mục",
                                    ""
                                )
                                TtsLibs.defaultTalk(context, "Bạn chưa chọn danh mục")
                            }
                        } else {
                            SimpleNotify.error(
                                context,
                                "Không lấy được danh mục vị trí",
                                ""
                            )
                            TtsLibs.defaultTalk(context, "Không lấy được danh mục vị trí")
                        }
                    } else {
                        SimpleNotify.undefinedError(
                            context
                        )
                    }
                    this@ThemDiaDiemDialog.dismiss()
                }
            })

    }

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_add_place, null)

        current = this

        loadingDialog = LoadingDialog(context)

        setOnCancelListener {
            selectedFp = null
        }

        initViews(v)

        setView(v)

        initEvents()

        setOnCancelListener {
            imageUri = null
            bm1 = null
            logoUri = null
            bm2 = null

            current = null
        }

        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initEvents() {
        btnCancel.setOnClickListener {
            this.dismiss()
        }
        ivHinhAnhDiaDiem.setOnClickListener {
            val intent = Intent(context, TempActivityForSelectFile::class.java)
            intent.putExtra("KEY", TempActivityForSelectFile.PLACE_IMAGE)
            context.startActivity(intent)
        }
        ivLogoHinhAnh.setOnClickListener {
            val intent = Intent(context, TempActivityForSelectFile::class.java)
            intent.putExtra("KEY", TempActivityForSelectFile.PLACE_LOGO)
            context.startActivity(intent)
        }
        edtGioMoCua.setOnClickListener {
            edtGioMoCua.clearFocus()
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val pickedDate = String.format("%02d:%02d", hourOfDay, minute)
                    edtGioMoCua.setText(pickedDate)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        edtGioDongCua.setOnClickListener {
            edtGioDongCua.clearFocus()
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val pickedDate = String.format("%02d:%02d", hourOfDay, minute)
                    edtGioDongCua.setText(pickedDate)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
        btnThemDiaDiem.setOnClickListener {
            if (imageUri == null) {
                TtsLibs.defaultTalk(context, "Bạn chưa chọn ảnh địa điểm")
                SimpleNotify.error(context, "Chưa chọn ảnh cho địa điểm", "")
                return@setOnClickListener
            }
            if (logoUri == null) {
                TtsLibs.defaultTalk(context, "Bạn chưa chọn logo cho địa điểm")
                SimpleNotify.error(context, "Chưa chọn logo cho địa điểm", "")
                return@setOnClickListener
            }
            val ten = edtPlaceName.text.toString()
            if (ten.isEmpty()) {
                TtsLibs.defaultTalk(context, "Bạn chưa nhập tên địa điểm")
                SimpleNotify.error(context, "Chưa nhập tên địa điểm", "")
                return@setOnClickListener
            }
            val diaChi = edtPlaceAddress.text.toString()
            if (diaChi.isEmpty()) {
                TtsLibs.defaultTalk(context, "Bạn chưa nhập số nhà hoặc tên đường")
                SimpleNotify.error(context, "Chưa nhập số nhà/tên đường", "")
                return@setOnClickListener
            }
            val gioMoCua = edtGioMoCua.text.toString()
            if (gioMoCua.isEmpty()) {
                TtsLibs.defaultTalk(context, "Vui lòng chọn giờ mở cửa")
                SimpleNotify.error(context, "Chưa có giờ mở cửa", "")
                return@setOnClickListener
            }
            val gioDongCua = edtGioDongCua.text.toString()
            if (gioDongCua.isEmpty()) {
                TtsLibs.defaultTalk(context, "Vui lòng chọn giờ đóng cửa")
                SimpleNotify.error(context, "Chưa có giờ đóng cửa", "")
                return@setOnClickListener
            }
            if (SimpfyLocationUtils.mLastLocation == null) {
                TtsLibs.defaultTalk(context, "Tôi không thể biết tọa độ hiện tại của bạn")
                SimpleNotify.error(context, "Không lấy được tọa độ", "")
                return@setOnClickListener
            }

//            if (InforCode.current == null) {
//                SimpleNotify.error(context, "Không xác định được địa phương hiện tại", "")
//                return@setOnClickListener
//            }

            if (selectedFp == null) {
                TtsLibs.defaultTalk(context, "Vui lòng cho biết địa điểm thuộc mục nào")
                SimpleNotify.error(context, "Vui lòng chọn danh mục cho địa điểm", "")
                return@setOnClickListener
            }
            loadingDialog.show()
            SaveTempFile(context).execute()
        }
    }

    class SaveTempFile(val context: Context) : AsyncTask<Void, Int, ArrayList<File?>>() {
        override fun doInBackground(vararg params: Void?): ArrayList<File?> {
            val res = ArrayList<File?>()
            res.add(
                SaveFileUtils.saveImages(
                    context,
                    "img_place.${MimeTypeMap.getSingleton()
                        .getExtensionFromMimeType(context.contentResolver.getType(imageUri!!)!!)}",
                    bm1!!
                )
            )
            res.add(
                SaveFileUtils.saveImages(
                    context,
                    "logo_place.${MimeTypeMap.getSingleton()
                        .getExtensionFromMimeType(context.contentResolver.getType(logoUri!!)!!)}",
                    bm2!!
                )
            )
            return res
        }

        override fun onPostExecute(result: ArrayList<File?>) {
            if (result.size != 2) {
                SimpleNotify.error(context, "Lỗi khi lưu tạm file", "")
                current?.loadingDialog?.dismiss()
            } else {
                current?.add(result[0], result[1])
            }
            super.onPostExecute(result)

        }

    }

    fun add(imgFile: File?, logoFile: File?) {
        if (imgFile == null || logoFile == null) {
            SimpleNotify.error(context, "Có lỗi xảy ra, vui lòng thử lại", "")
            return
        }
        val imgReqBody = RequestBody.create(
            MediaType.parse(context.contentResolver.getType(imageUri!!)!!),
            imgFile
        )

        val logoReqBody = RequestBody.create(
            MediaType.parse(context.contentResolver.getType(logoUri!!)!!),
            logoFile
        )
        try {
            val ten = edtPlaceName.text.toString()
            val diaChi = edtPlaceAddress.text.toString()
            val gioMoCua = edtGioMoCua.text.toString()
            val gioDongCua = edtGioDongCua.text.toString()
            val luocSuHinhThanh = edtLuocSuHinhThanh.text.toString()
            val coodinates = Coodinates(
                SimpfyLocationUtils.mLastLocation!!.latitude,
                SimpfyLocationUtils.mLastLocation!!.longitude
            )

            APIUtils.mAPIServices?.addNewPlace(
                RequestBody.create(MediaType.parse("text/plain"), ten),
                RequestBody.create(MediaType.parse("text/plain"), ""),
                coodinates,
                gioMoCua,
                gioDongCua,
                InforCode.current?.ma_xap ?: -1,
                selectedFp!!.ma_dm,
                RequestBody.create(MediaType.parse("text/plain"), diaChi),
                RequestBody.create(MediaType.parse("text/plain"), luocSuHinhThanh),
                MultipartBody.Part.createFormData("hinh_anh", imgFile.path, imgReqBody),
                MultipartBody.Part.createFormData("logo", logoFile.name, logoReqBody)
            )?.enqueue(object : Callback<CheckResponse> {
                override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                    t.printStackTrace()
                    SimpleNotify.networkError(context)
                    loadingDialog.dismiss()
                }

                override fun onResponse(
                    call: Call<CheckResponse>,
                    response: Response<CheckResponse>
                ) {
                    if (response.isSuccessful) {
                        val checkResponse = response.body()
                        if (checkResponse == null) {
                            TtsLibs.defaultTalk(context!!, "Lỗi khi đọc phản hồi từ máy chủ")
                            SimpleNotify.error(context, "Lỗi khi đọc phản hồi từ máy chủ", "")
                        } else {
                            if (checkResponse.code == 1) {
                                TtsLibs.defaultTalk(context!!, "Thêm địa điểm thành công")
                                Toast.makeText(
                                    context,
                                    "Thêm địa điểm thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                                this@ThemDiaDiemDialog.dismiss()
                            } else {
                                SimpleNotify.error(context, checkResponse.message, "LỖI")
                            }
                        }
                    } else {
                        Log.w("AAA", response.code().toString())
                        Log.w("AAA", GsonBuilder().create().toJson(response.errorBody()))
                        SimpleNotify.undefinedError(context)
                    }
                    loadingDialog.dismiss()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            TtsLibs.defaultTalk(context!!, "Lỗi khi thêm địa điểm")
            SimpleNotify.error(context, "Lỗi khi thêm địa điểm", "")
            loadingDialog.dismiss()
        }
    }
}