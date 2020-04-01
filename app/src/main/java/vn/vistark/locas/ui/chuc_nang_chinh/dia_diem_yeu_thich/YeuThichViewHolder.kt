package vn.vistark.locas.ui.chuc_nang_chinh.dia_diem_yeu_thich

import FavoritePlaces
import android.location.Location
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import vn.vistark.locas.R
import vn.vistark.locas.core.request_model.coordinate.Coodinates
import vn.vistark.locas.core.utils.SimpfyLocationUtils
import vn.vistark.locas.core.utils.TtsLibs
import java.lang.Exception

class YeuThichViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val lnItemPlaceRoot: LinearLayout = v.findViewById(R.id.lnItemPlaceRoot)
    val ivPlaceImage: ImageView = v.findViewById(R.id.ivPlaceImage)
    val tvPlaceName: TextView = v.findViewById(R.id.tvPlaceName)
    val tvRating: TextView = v.findViewById(R.id.tvRating)
    val rbRatingStar: RatingBar = v.findViewById(R.id.rbRatingStar)
    val tvRange: TextView = v.findViewById(R.id.tvRange)

    fun bind(favoritePlaces: FavoritePlaces) {
        tvPlaceName.text = favoritePlaces.ten_dd
        tvPlaceName.isSelected = true
        Glide.with(ivPlaceImage.context).load(favoritePlaces.hinh_anh).into(ivPlaceImage)
        // Tính khoảng cách
        try {
//            val coordinates = GsonBuilder().create()
//                .fromJson<Coodinates>(favoritePlaces.toa_do, Coodinates::class.java)
            val des = Location("")
            des.latitude = favoritePlaces.toa_do.lat
            des.longitude = favoritePlaces.toa_do.lng

            tvRating.text = String.format("%.01f", favoritePlaces.rating)
            rbRatingStar.rating = favoritePlaces.rating

            val distanceInMeters = SimpfyLocationUtils.mLastLocation!!.distanceTo(des)
            if (distanceInMeters < 1000) {
                tvRange.text = String.format("%.1fm", distanceInMeters)
            } else {
                tvRange.text = String.format("%.1fkm", distanceInMeters / 1000)
            }
            lnItemPlaceRoot.setOnClickListener {
                TtsLibs.nhanVaoDiaDiem(
                    lnItemPlaceRoot.context,
                    favoritePlaces.ten_dd,
                    tvRange.text.toString()
                )
                if (DiaDiemYeuThichFragment.googleMap != null) {
                    val latLng = LatLng(favoritePlaces.toa_do.lat, favoritePlaces.toa_do.lng)
                    val camUp = CameraUpdateFactory.newLatLngZoom(latLng, 18F)
                    DiaDiemYeuThichFragment.googleMap!!.animateCamera(camUp)
                }
            }
        } catch (e: Exception) {
            tvRange.text = "Không rõ"
        }
    }
}