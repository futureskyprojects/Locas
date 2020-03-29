package vn.vistark.locas.core.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import com.bumptech.glide.Glide
import vn.vistark.locas.R


class LoadingDialog(context: Context) : AlertDialog(context) {
    init {
        val v = LayoutInflater.from(context).inflate(R.layout.loading, null)
        val loadingIvIcon = v.findViewById<ImageView>(R.id.loadingIvIcon)
        Glide.with(context).asGif().load(R.raw.loading_location).into(loadingIvIcon)

        setView(v)

        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
    }

}