package vn.vistark.locas.ui.danh_gia

import UserRatings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.locas.R

class DanhGiaAdapter(val userRatings: ArrayList<UserRatings>) :
    RecyclerView.Adapter<DanhGiaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DanhGiaViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_cmt_rating, parent, false)
        return DanhGiaViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userRatings.size
    }

    override fun onBindViewHolder(holder: DanhGiaViewHolder, position: Int) {
        val ur = userRatings[position]
        holder.bind(ur)
    }

}