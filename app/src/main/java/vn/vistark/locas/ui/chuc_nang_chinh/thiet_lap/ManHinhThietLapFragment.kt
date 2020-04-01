package vn.vistark.locas.ui.chuc_nang_chinh.thiet_lap


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

import vn.vistark.locas.R
import vn.vistark.locas.core.Constants
import vn.vistark.locas.core.utils.TtsLibs
import vn.vistark.locas.ui.dang_nhap.ManHinhDangNhap
import vn.vistark.locas.ui.sua_ho_so.SuaHoSoDialog
import vn.vistark.locas.ui.them_dia_diem.ThemDiaDiemDialog

/**
 * A simple [Fragment] subclass.
 */
class ManHinhThietLapFragment : Fragment() {
    lateinit var rlBtnDangXuat: RelativeLayout
    lateinit var swTroLyAo: SwitchCompat
    lateinit var rlBtnThemDiaDiem: RelativeLayout
    lateinit var rlBtnSuaHoSo: RelativeLayout
    lateinit var userAvatar: CircleImageView
    lateinit var tvUsername: TextView

    fun initViews(v: View) {
        rlBtnDangXuat = v.findViewById(R.id.rlBtnDangXuat)
        swTroLyAo = v.findViewById(R.id.swTroLyAo)
        rlBtnThemDiaDiem = v.findViewById(R.id.rlBtnThemDiaDiem)
        rlBtnSuaHoSo = v.findViewById(R.id.rlBtnSuaHoSo)
        userAvatar = v.findViewById(R.id.userAvatar)
        tvUsername = v.findViewById(R.id.tvUsername)
        tvUsername.text = Constants.getDisplayName()
        loadAvatar()
        swTroLyAo.isChecked = Constants.isUseAssistant
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_man_hinh_thiet_lap, container, false)
        initViews(v)
        initEvents()
        return v
    }

    private fun initEvents() {
        logOut()
        swTroLyAo.setOnCheckedChangeListener { buttonView, isChecked ->
            Constants.isUseAssistant = isChecked
        }
        rlBtnSuaHoSo.setOnClickListener {
            val suaHoSoDialog = SuaHoSoDialog(context!!)
            TtsLibs.defaultTalk(context!!, "Đang hiển thị mục sửa hồ sơ")
            suaHoSoDialog.show()
        }

        rlBtnThemDiaDiem.setOnClickListener {
            TtsLibs.defaultTalk(context!!, "Đang hiển thị mục thêm địa điểm")
            val themDiaDiemDialog = ThemDiaDiemDialog(context!!)
            themDiaDiemDialog.show()
        }
    }

    private fun logOut() {
        rlBtnDangXuat.setOnClickListener {
            SweetAlertDialog(context).apply {
                titleText = "Tạm biệt Locas?"
                contentText = "ĐĂNG XUẤT"
                setConfirmButton("Đúng vậy") {
                    Constants.token = ""
                    it.dismissWithAnimation()
                    startActivity(Intent(context, ManHinhDangNhap::class.java))
                    activity?.finish()
                }
                setCancelButton("Không phải") {
                    it.dismissWithAnimation()
                }
                setCancelable(false)
                show()
            }
        }
    }

    private fun loadAvatar() {
        if (context != null)
            Glide.with(context!!).load(Constants.avatar).into(userAvatar)
    }
}
