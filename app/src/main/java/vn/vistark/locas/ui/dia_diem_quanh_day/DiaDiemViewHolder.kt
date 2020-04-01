package vn.vistark.locas.ui.dia_diem_quanh_day

import PlaceInRangeResult
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
import vn.vistark.locas.R
import vn.vistark.locas.core.utils.SimpfyLocationUtils
import vn.vistark.locas.core.utils.TtsLibs

class DiaDiemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val lnItemPlaceRoot: LinearLayout = v.findViewById(R.id.lnItemPlaceRoot)
    val ivPlaceImage: ImageView = v.findViewById(R.id.ivPlaceImage)
    val tvPlaceName: TextView = v.findViewById(R.id.tvPlaceName)
    val tvRating: TextView = v.findViewById(R.id.tvRating)
    val rbRatingStar: RatingBar = v.findViewById(R.id.rbRatingStar)
    val tvRange: TextView = v.findViewById(R.id.tvRange)

    fun bind(placeInRangeResult: PlaceInRangeResult) {
        tvPlaceName.text = placeInRangeResult.ten_dd
        tvPlaceName.isSelected = true
        Glide.with(ivPlaceImage.context).load(placeInRangeResult.hinh_anh).into(ivPlaceImage)
        // Tính khoảng cách
        try {
            val des = Location("")
            des.latitude = placeInRangeResult.toa_do.lat
            des.longitude = placeInRangeResult.toa_do.lng

            tvRating.text = String.format("%.01f", placeInRangeResult.rating)
            rbRatingStar.rating = placeInRangeResult.rating

            val distanceInMeters = SimpfyLocationUtils.mLastLocation!!.distanceTo(des)
            if (distanceInMeters < 1000) {
                tvRange.text = String.format("%.1fm", distanceInMeters)
            } else {
                tvRange.text = String.format("%.1fkm", distanceInMeters / 1000)
            }
            lnItemPlaceRoot.setOnClickListener {
                TtsLibs.nhanVaoDiaDiem(
                    lnItemPlaceRoot.context,
                    placeInRangeResult.ten_dd,
                    tvRange.text.toString()
                )
                if (ActivityManHinhDiaDiemQuanhDay.googleMap != null) {
                    val latLng =
                        LatLng(placeInRangeResult.toa_do.lat, placeInRangeResult.toa_do.lng)
                    val camUp = CameraUpdateFactory.newLatLngZoom(latLng, 18F)
                    ActivityManHinhDiaDiemQuanhDay.googleMap!!.animateCamera(camUp)
                }
            }
        } catch (e: Exception) {
            tvRange.text = "Không rõ"
        }
    }
}