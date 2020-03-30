package vn.vistark.locas.ui.them_dia_diem

import DanhMuc
import DanhMucViTriResponse
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
import vn.vistark.locas.core.utils.LoadingDialog
import vn.vistark.locas.core.utils.SimpfyLocationUtils
import vn.vistark.locas.core.utils.SimpleNotify
import vn.vistark.locas.ui.chuc_nang_chinh.danh_muc_dia_diem.DanhMucAdapter
import vn.vistark.locas.ui.trashing.TempActivityForSelectFile
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class ThemDiaDiemDialog(context: Context) : AlertDialog(context) {
    lateinit var loadingDialog: LoadingDialog

    companion object {
        var selectedFp: DanhMuc? = null
        var imageUri: Uri? = null
        var logoUri: Uri? = null

        var current: ThemDiaDiemDialog? = null

        fun imageUriUpdate(uri: Uri?) {
            imageUri = uri
            if (current != null) {
                Glide.with(current!!.ivHinhAnhDiaDiem).load(imageUri)
                    .into(current!!.ivHinhAnhDiaDiem)
            }
        }

        fun logoUriUpdate(uri: Uri?) {
            logoUri = uri
            if (current != null) {
                Glide.with(current!!.ivLogoHinhAnh).load(logoUri)
                    .into(current!!.ivLogoHinhAnh)
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
                            }
                        } else {
                            SimpleNotify.error(
                                context,
                                "Không lấy được danh mục vị trí",
                                ""
                            )
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
                    val pickedDate = String.format("%02d-%02d", hourOfDay, minute)
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
                    val pickedDate = String.format("%02d-%02d", hourOfDay, minute)
                    edtGioDongCua.setText(pickedDate)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
        btnThemDiaDiem.setOnClickListener {
            if (imageUri == null) {
                SimpleNotify.error(context, "Chưa chọn ảnh cho địa điểm", "")
                return@setOnClickListener
            }
            val ten = edtPlaceName.text.toString()
            if (ten.isEmpty()) {
                SimpleNotify.error(context, "Chưa nhập tên địa điểm", "")
                return@setOnClickListener
            }
            val diaChi = edtPlaceAddress.text.toString()
            if (diaChi.isEmpty()) {
                SimpleNotify.error(context, "Chưa nhập số nhà/tên đường", "")
                return@setOnClickListener
            }
            val gioMoCua = edtGioMoCua.text.toString()
            if (gioMoCua.isEmpty()) {
                SimpleNotify.error(context, "Chưa có giờ mở cửa", "")
                return@setOnClickListener
            }
            val gioDongCua = edtGioDongCua.text.toString()
            if (gioDongCua.isEmpty()) {
                SimpleNotify.error(context, "Chưa có giờ đóng cửa", "")
                return@setOnClickListener
            }
            val luocSuHinhThanh = edtLuocSuHinhThanh.text.toString()
            if (SimpfyLocationUtils.mLastLocation == null) {
                SimpleNotify.error(context, "Không lấy được tọa độ", "")
                return@setOnClickListener
            }

            val coodinates = Coodinates(
                SimpfyLocationUtils.mLastLocation!!.latitude,
                SimpfyLocationUtils.mLastLocation!!.longitude
            )

            if (InforCode.current == null) {
                SimpleNotify.error(context, "Không xác định được địa phương hiện tại", "")
                return@setOnClickListener
            }

            if (selectedFp == null) {
                SimpleNotify.error(context, "Vui lòng chọn danh mục cho địa điểm", "")
                return@setOnClickListener
            }

            val imgFile = File(imageUri!!.path!!)
            val imgReqBody = RequestBody.create(
                MediaType.parse(context.contentResolver.getType(imageUri!!)!!),
                imgFile
            )

            val logoFile = File(logoUri!!.path!!)
            val logoReqBody = RequestBody.create(
                MediaType.parse(context.contentResolver.getType(imageUri!!)!!),
                imgFile
            )
            loadingDialog.show()
            try {
                APIUtils.mAPIServices?.addNewPlace(
                    ten,
                    "",
                    coodinates,
                    gioMoCua,
                    gioDongCua,
                    InforCode.current!!.ma_xap,
                    selectedFp!!.ma_dm,
                    diaChi,
                    luocSuHinhThanh,
                    MultipartBody.Part.createFormData("hinh_anh", imgFile.name, imgReqBody),
                    MultipartBody.Part.createFormData("logo", logoFile.name, logoReqBody)
                )?.enqueue(object : Callback<CheckResponse> {
                    override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
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
                                SimpleNotify.error(context, "Lỗi khi đọc phản hồi từ máy chủ", "")
                            } else {
                                if (checkResponse.code == 1) {
                                    Toast.makeText(
                                        context,
                                        "Thêm địa điểm thành công",
                                        Toast.LENGTH_SHORT
                                    )
                                    this@ThemDiaDiemDialog.dismiss()
                                } else {
                                    SimpleNotify.error(context, checkResponse.message, "LỖI")
                                }
                            }
                        } else {
                            SimpleNotify.undefinedError(context)
                        }
                        loadingDialog.dismiss()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                SimpleNotify.error(context, "Lỗi khi thêm địa điểm", "")
                loadingDialog.dismiss()
            }

        }
    }

}