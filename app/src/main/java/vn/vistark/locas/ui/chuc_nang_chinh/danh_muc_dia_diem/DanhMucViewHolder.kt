package vn.vistark.locas.ui.chuc_nang_chinh.danh_muc_dia_diem

import DanhMuc
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.vistark.locas.R


class DanhMucViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val idmRlRoot = v.findViewById<RelativeLayout>(R.id.idmRlRoot)
    val dmTvTenDanhMuc = v.findViewById<TextView>(R.id.dmTvTenDanhMuc)
    val dmIvAnhDanhMuc = v.findViewById<ImageView>(R.id.dmIvAnhDanhMuc)


    fun bind(danhMuc: DanhMuc) {
        val vto: ViewTreeObserver = idmRlRoot.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                idmRlRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width: Int = idmRlRoot.measuredWidth
                val height: Int = idmRlRoot.measuredHeight
                // cập nhật
                val params = idmRlRoot.layoutParams
                params.height = width
                idmRlRoot.layoutParams = params
            }
        })

        dmTvTenDanhMuc.text = danhMuc.ten_dm
        Glide.with(dmIvAnhDanhMuc.context).load(danhMuc.icon_dm).into(dmIvAnhDanhMuc)
    }
}