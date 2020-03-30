package vn.vistark.locas.ui.sua_ho_so

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import vn.vistark.locas.R
import vn.vistark.locas.core.Constants
import java.util.*

class SuaHoSoDialog(context: Context) : AlertDialog(context), DatePickerDialog.OnDateSetListener {
    lateinit var userAvatar: CircleImageView
    lateinit var edtSurname: EditText
    lateinit var edtName: EditText
    lateinit var edtEmail: EditText
    lateinit var edtPhone: EditText
    lateinit var edtNgaySinh: EditText
    lateinit var btnCancel: Button
    lateinit var btnCapNhatHoSo: Button

    fun initViews(v: View) {
        userAvatar = v.findViewById(R.id.userAvatar)
        edtSurname = v.findViewById(R.id.edtSurname)
        edtName = v.findViewById(R.id.edtName)
        edtEmail = v.findViewById(R.id.edtEmail)
        edtPhone = v.findViewById(R.id.edtPhone)
        edtNgaySinh = v.findViewById(R.id.edtNgaySinh)
        btnCancel = v.findViewById(R.id.btnCancel)
        btnCapNhatHoSo = v.findViewById(R.id.btnCapNhatHoSo)
        //================================//
        loadAvatar()
        edtSurname.setText(Constants.surname)
        edtName.setText(Constants.name)
        edtEmail.setText(Constants.email)
        edtPhone.setText(Constants.phone)
        edtNgaySinh.setText(Constants.birthDay)
        //===============================//
        edtNgaySinh.setOnClickListener {
            edtNgaySinh.clearFocus()
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_update_profile, null)
        initViews(v)
        initEvents()

        setView(v)

        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initEvents() {
        btnCancel.setOnClickListener {
            this.cancel()
        }
    }

    private fun loadAvatar() {
        Glide.with(context).load(Constants.avatar).into(userAvatar)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val pickedDate = String.format("%02d-%02d-%04d", dayOfMonth, year, month)
        edtNgaySinh.setText(pickedDate)
    }
}