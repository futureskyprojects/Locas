package vn.vistark.locas.ui.chuc_nang_chinh.dia_diem_yeu_thich

import FavoritePlaces
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.locas.R
import vn.vistark.locas.ui.danh_gia.DanhGiaDiaLog

class YeuThichAdapter(val favoritePlaces: ArrayList<FavoritePlaces>) :
    RecyclerView.Adapter<YeuThichViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YeuThichViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return YeuThichViewHolder(v)
    }

    override fun getItemCount(): Int {
        return favoritePlaces.size
    }

    override fun onBindViewHolder(holder: YeuThichViewHolder, position: Int) {
        val fp = favoritePlaces[position]
        holder.bind(fp)
        holder.lnItemPlaceRoot.setOnClickListener {
            DanhGiaDiaLog(holder.lnItemPlaceRoot.context, fp)
        }
    }

}