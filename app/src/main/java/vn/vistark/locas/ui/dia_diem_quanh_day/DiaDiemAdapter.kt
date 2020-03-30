package vn.vistark.locas.ui.dia_diem_quanh_day

import PlaceInRangeResult
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.locas.R

class DiaDiemAdapter(val arr: ArrayList<PlaceInRangeResult>) :
    RecyclerView.Adapter<DiaDiemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaDiemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return DiaDiemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: DiaDiemViewHolder, position: Int) {
        val plr = arr[position]
        holder.bind(plr)
    }

}