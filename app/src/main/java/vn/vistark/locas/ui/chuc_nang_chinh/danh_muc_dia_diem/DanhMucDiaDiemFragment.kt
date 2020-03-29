package vn.vistark.locas.ui.chuc_nang_chinh.danh_muc_dia_diem


import DanhMuc
import DanhMucViTriResponse
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import vn.vistark.locas.R
import vn.vistark.locas.core.Constants
import vn.vistark.locas.core.api.APIUtils
import vn.vistark.locas.core.utils.CoordinatesDecoder.Companion.getAddressline
import vn.vistark.locas.core.utils.LoadingDialog
import vn.vistark.locas.core.utils.SimpfyLocationUtils
import vn.vistark.locas.core.utils.SimpleNotify

class DanhMucDiaDiemFragment : Fragment() {
    lateinit var loading: LoadingDialog

    lateinit var mTvDiaDiemHienTai: TextView
    lateinit var dmRvDanhSachDanhMuc: RecyclerView
    lateinit var tvWelcome: TextView
    lateinit var userAvatar: CircleImageView

    var danhMucs = ArrayList<DanhMuc>()
    lateinit var danhMucAdapter: DanhMucAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_danh_muc_dia_diem, container, false)
        initViews(v)

        Log.w(
            "VỊ TRÍ",
            "${SimpfyLocationUtils.mLastLocation?.latitude} ${SimpfyLocationUtils.mLastLocation?.longitude}"
        )

        getAddressline(
            mTvDiaDiemHienTai,
            SimpfyLocationUtils.mLastLocation?.latitude!!,
            SimpfyLocationUtils.mLastLocation?.longitude!!
        )
        loadDanhMucDiaDiem()
        return v
    }

    private fun loadDanhMucDiaDiem() {
        loading.show()
        APIUtils.mAPIServices?.getLocationCategories()
            ?.enqueue(object : Callback<DanhMucViTriResponse> {
                override fun onFailure(call: Call<DanhMucViTriResponse>, t: Throwable) {
                    t.printStackTrace()
                    SimpleNotify.networkError(
                        this@DanhMucDiaDiemFragment.context!!
                    )
                    loading.dismiss()
                }

                override fun onResponse(
                    call: Call<DanhMucViTriResponse>,
                    response: Response<DanhMucViTriResponse>
                ) {
                    if (response.isSuccessful) {
                        val dm = response.body()
                        if (dm != null) {
                            if (dm.places.isNotEmpty()) {
                                for (place in dm.places) {
                                    danhMucs.add(place)
                                    danhMucAdapter.notifyDataSetChanged()
                                }
                            } else {
                                SimpleNotify.error(
                                    this@DanhMucDiaDiemFragment.context!!,
                                    "Không có danh mục",
                                    ""
                                )
                            }
                        } else {
                            SimpleNotify.error(
                                this@DanhMucDiaDiemFragment.context!!,
                                "Không lấy được danh mục vị trí",
                                ""
                            )
                        }
                    } else {
                        SimpleNotify.undefinedError(
                            this@DanhMucDiaDiemFragment.context!!
                        )
                    }
                    loading.dismiss()
                }
            })
    }

    private fun initViews(v: View) {
        loading = LoadingDialog(context!!)

        tvWelcome = v.findViewById(R.id.tvWelcome)
        getWelcomeString()
        userAvatar = v.findViewById(R.id.userAvatar)
        loadAvatar()

        mTvDiaDiemHienTai = v.findViewById(R.id.dmddTvDiaDiemHienTai)
        mTvDiaDiemHienTai.isSelected = true
        dmRvDanhSachDanhMuc = v.findViewById(R.id.dmRvDanhSachDanhMuc)
        dmRvDanhSachDanhMuc.setHasFixedSize(true)
        dmRvDanhSachDanhMuc.layoutManager = GridLayoutManager(context, 2)
        danhMucAdapter = DanhMucAdapter(danhMucs)
        dmRvDanhSachDanhMuc.adapter = danhMucAdapter

    }

    private fun loadAvatar() {
        if (context != null)
            Glide.with(context!!).load(Constants.avatar).into(userAvatar)
    }

    private fun getWelcomeString() {
        tvWelcome.text =
            "Chào buổi tối ${Constants.getDisplayName()}, hãy chọn loại địa điểm bạn muốn"
    }


}
