package vn.vistark.locas.core.utils

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog

class SimpleNotify {
    companion object {
        fun error(context: Context, title: String, msg: String, canClose: Boolean = true) {
            val s = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(msg)
            s.setCancelable(canClose)
            s.show()
        }

        fun networkError(context: Context) {
            val s = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("LỖI MẠNG")
                .setContentText("")
            s.show()
        }

        fun undefinedError(context: Context) {
            val s = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("LỖI KHÔNG XÁC ĐỊNH")
                .setContentText("")
            s.show()
        }

        fun success(context: Context, title: String, msg: String) {
            SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(msg)
                .show()
        }

        fun warning(context: Context, title: String, msg: String) {
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(msg)
                .show()
        }

    }
}