package vn.vistark.locas.ui.trashing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.vistark.locas.ui.sua_ho_so.SuaHoSoDialog
import vn.vistark.locas.ui.them_dia_diem.ThemDiaDiemDialog


class TempActivityForSelectFile : AppCompatActivity() {
    companion object {
        val CODE = 323
        val PLACE_IMAGE = "PLACE_IMAGE"
        val PLACE_LOGO = "PLACE_LOGO"
        val USER_AVATAR = "USER_AVATAR"
    }

    var key = PLACE_IMAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        key = intent.getStringExtra("KEY") ?: PLACE_IMAGE
        pickImage()
    }

    fun pickImage() { // Gọi intent của hệ thống để chọn ảnh nhé.
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Chọn ảnh"),
            CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            if (key == PLACE_IMAGE) {
                ThemDiaDiemDialog.imageUriUpdate(data.data)
            } else if (key == PLACE_LOGO) {
                ThemDiaDiemDialog.logoUriUpdate(data.data)
            } else if (key == USER_AVATAR) {
                SuaHoSoDialog.updateSelectedUri(data.data)
            }
        }
        finish()
    }
}