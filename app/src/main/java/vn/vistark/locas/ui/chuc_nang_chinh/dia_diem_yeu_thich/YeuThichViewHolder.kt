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
            val coordinates = GsonBuilder().create()
                .fromJson<Coodinates>(favoritePlaces.toa_do, Coodinates::class.java)
            val des = Location("")
            des.latitude = coordinates.lat
            des.longitude = coordinates.lng

            val distanceInMeters = SimpfyLocationUtils.mLastLocation!!.distanceTo(des)
            if (distanceInMeters < 1000) {
                tvRange.text = String.format("%.1fm", distanceInMeters)
            } else {
                tvRange.text = String.format("%.1fkm", distanceInMeters / 1000)
            }
            lnItemPlaceRoot.setOnClickListener {
                if (DiaDiemYeuThichFragment.googleMap != null) {
                    val latLng = LatLng(coordinates.lat, coordinates.lng)
                    val camUp = CameraUpdateFactory.newLatLngZoom(latLng, 24F)
                    DiaDiemYeuThichFragment.googleMap!!.animateCamera(camUp)
                }
            }
        } catch (e: Exception) {
            tvRange.text = "Không rõ"
        }
    }
}