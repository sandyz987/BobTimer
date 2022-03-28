package com.sandyz.alltimers.myhome.view.activity

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.tabs.TabLayout
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.config.SHOP_RECHARGE
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.extensions.toast
import com.sandyz.alltimers.common.widgets.OptionalDialog
import com.sandyz.alltimers.myhome.R
import com.sandyz.alltimers.myhome.view.adapter.EditPagerAdapter
import kotlinx.android.synthetic.main.myhome_activity_edit.*

class ActivityEdit : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myhome_activity_edit)

        myhome_dynamic_bg_edit.longDragTime = 0L
        myhome_dynamic_bg_edit.endDrayImmediately = true

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


        myhome_tv_save.setOnClickAction {
            myhome_dynamic_bg_edit.saveSerializationData()
            toast("已保存布局")
            finish()
        }

        myhome_iv_carrot_add2.setOnClickAction {
            ARouter.getInstance().build(SHOP_RECHARGE).navigation()
        }

        myhome_tl_sort.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                myhome_vp_edit.currentItem = if (myhome_tl_sort.getTabAt(0) == tab) 0 else 1
            }
        })


        myhome_vp_edit.adapter = EditPagerAdapter(myhome_dynamic_bg_edit)
        myhome_vp_edit.isUserInputEnabled = false

    }

}