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
import com.sandyz.alltimers.myhome.backgroundscroll.Rabbit
import com.sandyz.alltimers.myhome.model.WallpaperAndFloorModel
import com.sandyz.alltimers.myhome.view.adapter.EditPagerAdapter
import kotlinx.android.synthetic.main.myhome_activity_edit.*

class ActivityEdit : BaseActivity() {

    private lateinit var adapter: EditPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myhome_activity_edit)

        myhome_dynamic_bg_edit.longDragTime = 0L
        myhome_dynamic_bg_edit.endDrayImmediately = true

        myhome_dynamic_bg_edit.setSize(3000, 1600)
        myhome_dynamic_bg_edit.scrollToPercent(0.3f, false)
        myhome_dynamic_bg_edit.setWallPaper(WallpaperAndFloorModel.getWallpaper(this))
        myhome_dynamic_bg_edit.setFloor(WallpaperAndFloorModel.getFloor(this))
        myhome_dynamic_bg_edit.fromSerializationData()
        myhome_dynamic_bg_edit.onBind()

        myhome_iv_back.setOnClickAction {
            OptionalDialog.show(this, "确定要返回吗？布局改动将不会保存~", onDeny = {}) {
                finish()
            }
        }


        myhome_tv_save.setOnClickAction {
            myhome_dynamic_bg_edit.saveSerializationData()
            (myhome_dynamic_bg_edit.findWidget("Rabbit")?.scrollChild as? Rabbit?)?.clothes?.saveSerializationData()
            (myhome_dynamic_bg_edit.findWidget("Rabbit")?.scrollChild as? Rabbit?)?.headdress?.saveSerializationData()
            toast("已保存布局及装扮")
            finish()
        }

        myhome_iv_carrot_add2.setOnClickAction {
            ARouter.getInstance().build(SHOP_RECHARGE).navigation()
        }

        myhome_tl_sort.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                myhome_vp_edit.currentItem = myhome_tl_sort.selectedTabPosition
            }
        })

        myhome_iv_remove_all.setOnClickAction {
            if (myhome_tl_sort.selectedTabPosition == 1) {
                OptionalDialog.show(this, "要删除所有家具吗？", onDeny = {}) {
                    myhome_dynamic_bg_edit.removeAllTypeWidget("")
                    adapter.widgetContentAdapter1?.refresh()
                    adapter.widgetContentAdapter2?.refresh()
                }
            } else {
                OptionalDialog.show(this, "要清除BOB兔身上的所有服饰吗？", onDeny = {}) {
                    (myhome_dynamic_bg_edit.findWidget("Rabbit")?.scrollChild as? Rabbit?)?.clothes?.removeAllViews()
                    (myhome_dynamic_bg_edit.findWidget("Rabbit")?.scrollChild as? Rabbit?)?.headdress?.removeAllViews()
                    adapter.widgetContentAdapter1?.refresh()
                    adapter.widgetContentAdapter2?.refresh()
                }
            }
        }


        adapter = EditPagerAdapter(myhome_dynamic_bg_edit)
        myhome_vp_edit.adapter = adapter
        myhome_vp_edit.isUserInputEnabled = false

        val intent = intent
        if (intent != null) {
            val s = intent.getIntExtra("type", -1)
            if (s != -1) {
                myhome_tl_sort.selectTab(myhome_tl_sort.getTabAt(s))
            }

        }
        myhome_dynamic_bg_edit.post {
            adapter.widgetContentAdapter1?.refresh()
            adapter.widgetContentAdapter2?.refresh()
        }

    }

}