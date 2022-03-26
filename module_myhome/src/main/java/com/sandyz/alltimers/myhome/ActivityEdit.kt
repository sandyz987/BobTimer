package com.sandyz.alltimers.myhome

import android.os.Bundle
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.extensions.getScreenHeight
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.extensions.toast
import com.sandyz.alltimers.common.widgets.OptionalDialog
import kotlinx.android.synthetic.main.myhome_activity_edit.*

class ActivityEdit : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myhome_activity_edit)

        myhome_dynamic_bg_edit.setSize(3000, 1600)
        myhome_dynamic_bg_edit.scrollToPercent(0.3f, false)
        myhome_dynamic_bg_edit.setWallPaper(R.drawable.myhome_ic_wallpaper1)
        myhome_dynamic_bg_edit.setFloor(R.drawable.myhome_ic_floor1)
        myhome_dynamic_bg_edit.fromSerializationData()
        myhome_dynamic_bg_edit.onBind()

        myhome_iv_back.setOnClickAction {
            OptionalDialog.show(this, "确定要返回吗？布局改动将不会保存~", onDeny = {}) {
                finish()
            }
        }

        myhome_dynamic_bg_edit.translationY = -(getScreenHeight() / 4f)

        myhome_tv_save.setOnClickAction {
            myhome_dynamic_bg_edit.saveSerializationData()
            toast("已保存布局")
            finish()
        }

    }

}