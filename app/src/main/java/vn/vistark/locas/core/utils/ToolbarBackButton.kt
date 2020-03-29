package vn.vistark.locas.core.utils

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import vn.vistark.locas.R

class ToolbarBackButton(var act: AppCompatActivity) {
    fun show() {
        act.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        act.supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    fun overrideAnimationOnEnterAndExitActivity() {
        act.overridePendingTransition(R.anim.enter, R.anim.exit)
    }

    fun overrideAnimationOnEnterAndExitActivityReveret() {
        act.overridePendingTransition(
            R.anim.animation_enter,
            R.anim.animation_leave
        )
    }
}