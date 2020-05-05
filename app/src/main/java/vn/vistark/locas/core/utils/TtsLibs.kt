package vn.vistark.locas.core.utils

import android.content.Context
import android.provider.SyncStateContract
import vn.vistark.locas.core.Constants

class TtsLibs {
    companion object {
        fun chaoMung(context: Context) {
            val s = arrayListOf(
                "Chào ${Constants.getDisplayName()}",
                "Chào mừng bạn đến với Locas",
                "Hãy chọn danh mục địa điểm bạn muốn",
                "Hôm nay bạn muốn đến địa điểm như thế nào"
            )

            if (Constants.isUseAssistant)
                TtsUtils(context).execute(s.random())
        }

        fun taiDanhMuc(context: Context, tenDanhMuc: String) {
            val s = arrayListOf(
                "Đây là danh sách các $tenDanhMuc",
                "Các $tenDanhMuc đang được hiển thị",
                "Đây là các $tenDanhMuc cho bạn lựa chọn"
            )
            if (Constants.isUseAssistant)
                TtsUtils(context).execute(s.random())
        }

        fun nhanVaoDiaDiem(context: Context, tenDiaDiem: String, khoangCach: String) {
            val s = arrayListOf(
                "Từ bạn đến $tenDiaDiem khoảng $khoangCach",
                "Đang hiển thị bản đồ khu vực $tenDiaDiem",
                "Bạn có thể nhấn vào ghim của $tenDiaDiem trên bản đồ để chọn dẫn đường"
            )
            if (Constants.isUseAssistant)
                TtsUtils(context).execute(s.random())
        }

        fun hienThiDanhGia(context: Context, tenDiaDiem: String) {
            val s = arrayListOf(
                "Đang hiển thị chi tiết đánh giá về $tenDiaDiem",
                "Các đánh giá về $tenDiaDiem đang được hiển thị"
            )
//            if (Constants.isUseAssistant)
//                TtsUtils(context).execute(s.random())
        }

        fun defaultTalk(context: Context, msg: String) {
            if (Constants.isUseAssistant)
                TtsUtils(context).execute(msg)
        }
    }
}