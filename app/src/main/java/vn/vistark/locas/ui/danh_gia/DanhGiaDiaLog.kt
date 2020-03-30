package vn.vistark.locas.ui.danh_gia

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
import de.hdodenhof.circleimageview.CircleImageView
import vn.vistark.locas.R

class DanhGiaDiaLog(context: Context) : AlertDialog(context) {

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


    init {
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
    }
}