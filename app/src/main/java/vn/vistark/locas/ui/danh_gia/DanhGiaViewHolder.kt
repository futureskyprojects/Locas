package vn.vistark.locas.ui.danh_gia

import UserRatings
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.vistark.locas.R
import vn.vistark.locas.core.Constants

class DanhGiaViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val ivRatingImgs: ImageView = v.findViewById(R.id.ivRatingImgs)
    val ivLogoHinhAnh: ImageView = v.findViewById(R.id.ivLogoHinhAnh)
    val tvTenNguoiDanhGia: TextView = v.findViewById(R.id.tvTenNguoiDanhGia)
    val tvThoiDiemDanhGia: TextView = v.findViewById(R.id.tvThoiDiemDanhGia)
    val tvNoiDungDanhGia: TextView = v.findViewById(R.id.tvNoiDungDanhGia)
    val rbRatingStar: RatingBar = v.findViewById(R.id.rbRatingStar)
    val tvRating: TextView = v.findViewById(R.id.tvRating)


    fun bind(userRatings: UserRatings) {
        Glide.with(ivRatingImgs).load(userRatings.anh_1).into(ivRatingImgs)
        Glide.with(ivLogoHinhAnh).load(userRatings.avatar).into(ivLogoHinhAnh)


        tvTenNguoiDanhGia.text = getDisplayName(userRatings)
        tvThoiDiemDanhGia.text = userRatings.dg_ngay_tao
        tvNoiDungDanhGia.text = userRatings.description
        rbRatingStar.rating = userRatings.rating
        tvRating.text = String.format("%.01f", userRatings.rating)
        tvThoiDiemDanhGia.isSelected = true
    }

    fun getDisplayName(userRatings: UserRatings): String {
        if (userRatings.ten_nd.isEmpty()) {
            return userRatings.username
        } else {
            if (userRatings.ho_nd.isNotEmpty()) {
                return "${userRatings.ho_nd} ${userRatings.ten_nd}"
            } else {
                return userRatings.ten_nd
            }
        }
    }
}