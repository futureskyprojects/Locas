package vn.vistark.locas.ui.danh_gia

import FavoritePlaces
import PlaceInRangeResult
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import vn.vistark.locas.R

class DanhGiaDiaLog(context: Context) : AlertDialog(context) {
    constructor(context: Context, fp: FavoritePlaces) : this(context) {
        initialize()
        rbRatingStar.rating = fp.rating
        tvRating.text = String.format("%.01f", fp.rating)
        Glide.with(ivHinhAnhDiaDiem).load(fp.hinh_anh).into(ivHinhAnhDiaDiem)
        Glide.with(ivLogoHinhAnh).load(fp.logo).into(ivLogoHinhAnh)
        tvTenDiaDiem.text = fp.ten_dd
        addRating(fp.ma_dd)
    }

    constructor(context: Context, pir: PlaceInRangeResult) : this(context) {
        initialize()
        rbRatingStar.rating = pir.rating
        tvRating.text = String.format("%.01f", pir.rating)
        Glide.with(ivHinhAnhDiaDiem).load(pir.hinh_anh).into(ivHinhAnhDiaDiem)
        Glide.with(ivLogoHinhAnh).load(pir.logo).into(ivLogoHinhAnh)
        tvTenDiaDiem.text = pir.ten_dd
        addRating(pir.ma_dd)
    }

    fun addRating(placeId: Int) {
        ivBtnSend.setOnClickListener {


        }
    }

    lateinit var ivCloseBtn: ImageView
    lateinit var rbRatingStar: RatingBar
    lateinit var tvRating: TextView
    lateinit var ivHinhAnhDiaDiem: ImageView
    lateinit var ivLogoHinhAnh: CircleImageView
    lateinit var tvTenDiaDiem: TextView
    lateinit var rvUserComments: RecyclerView
    lateinit var cmtRating: RatingBar
    lateinit var cmtRatingNumber: TextView
    lateinit var attImg1: ImageView
    lateinit var edtCommentContent: EditText
    lateinit var ivBtnSend: ImageView

    fun initViews(v: View) {
        ivCloseBtn = v.findViewById(R.id.ivCloseBtn)
        rbRatingStar = v.findViewById(R.id.rbRatingStar)
        ivHinhAnhDiaDiem = v.findViewById(R.id.ivHinhAnhDiaDiem)
        ivLogoHinhAnh = v.findViewById(R.id.ivLogoHinhAnh)
        tvRating = v.findViewById(R.id.tvRating)
        tvTenDiaDiem = v.findViewById(R.id.tvTenDiaDiem)
        rvUserComments = v.findViewById(R.id.rvUserComments)
        cmtRating = v.findViewById(R.id.cmtRating)
        cmtRatingNumber = v.findViewById(R.id.cmtRatingNumber)
        attImg1 = v.findViewById(R.id.attImg1)
        edtCommentContent = v.findViewById(R.id.edtCommentContent)
        ivBtnSend = v.findViewById(R.id.ivBtnSend)
    }

    fun initialize() {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_danh_gia, null)
        initViews(v)
        initEvents()

        setView(v)

        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        show()
    }

    private fun initEvents() {
        ivCloseBtn.setOnClickListener {
            dismiss()
        }
        cmtRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                cmtRatingNumber.text = String.format("%.01f", rating)
            }
        }
    }
}