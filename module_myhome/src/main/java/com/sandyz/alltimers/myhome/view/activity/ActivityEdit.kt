package com.sandyz.alltimers.myhome.view.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.tabs.TabLayout
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.config.SHOP_RECHARGE
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.extensions.toast
import com.sandyz.alltimers.common.widgets.OptionalDialog
import com.sandyz.alltimers.myhome.R
import com.sandyz.alltimers.myhome.backgroundscroll.Rabbit
import com.sandyz.alltimers.myhome.databinding.MyhomeActivityEditBinding
import com.sandyz.alltimers.myhome.model.WallpaperAndFloorModel
import com.sandyz.alltimers.myhome.view.adapter.EditPagerAdapter

class ActivityEdit : BaseActivity() {

    private lateinit var adapter: EditPagerAdapter
    private lateinit var binding: MyhomeActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myhome_activity_edit)
        DataBindingUtil.setContentView<MyhomeActivityEditBinding>(this, R.layout.myhome_activity_edit).also {
            binding = it
        }

        binding.apply {
            myhomeDynamicBgEdit.longDragTime = 0L
            myhomeDynamicBgEdit.endDrayImmediately = true

            myhomeDynamicBgEdit.setSize(3000, 1600)
            myhomeDynamicBgEdit.scrollToPercent(0.3f, false)
            myhomeDynamicBgEdit.setWallPaper(WallpaperAndFloorModel.getWallpaper(this@ActivityEdit))
            myhomeDynamicBgEdit.setFloor(WallpaperAndFloorModel.getFloor(this@ActivityEdit))
            myhomeDynamicBgEdit.fromSerializationData()
            myhomeDynamicBgEdit.onBind()

            myhomeIvBack.setOnClickAction {
                OptionalDialog.show(this@ActivityEdit, "确定要返回吗？布局改动将不会保存~", onDeny = {}) {
                    finish()
                }
            }


            myhomeTvSave.setOnClickAction {
                myhomeDynamicBgEdit.saveSerializationData()
                (myhomeDynamicBgEdit.findWidget("Rabbit")?.scrollChild as? Rabbit?)?.clothes?.saveSerializationData()
                (myhomeDynamicBgEdit.findWidget("Rabbit")?.scrollChild as? Rabbit?)?.headdress?.saveSerializationData()
                toast("已保存布局及装扮")
                finish()
            }

            myhomeIvCarrotAdd2.setOnClickAction {
                ARouter.getInstance().build(SHOP_RECHARGE).navigation()
            }

            myhomeTlSort.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    myhomeVpEdit.currentItem = myhomeTlSort.selectedTabPosition
                }
            })

            myhomeIvRemoveAll.setOnClickAction {
                if (myhomeTlSort.selectedTabPosition == 1) {
                    OptionalDialog.show(this@ActivityEdit, "要删除所有家具吗？", onDeny = {}) {
                        myhomeDynamicBgEdit.removeAllTypeWidget("")
                        adapter.widgetContentAdapter1?.refresh()
                        adapter.widgetContentAdapter2?.refresh()
                    }
                } else {
                    OptionalDialog.show(this@ActivityEdit, "要清除BOB兔身上的所有服饰吗？", onDeny = {}) {
                        (myhomeDynamicBgEdit.findWidget("Rabbit")?.scrollChild as? Rabbit?)?.clothes?.removeAllViews()
                        (myhomeDynamicBgEdit.findWidget("Rabbit")?.scrollChild as? Rabbit?)?.headdress?.removeAllViews()
                        adapter.widgetContentAdapter1?.refresh()
                        adapter.widgetContentAdapter2?.refresh()
                    }
                }
            }


            adapter = EditPagerAdapter(myhomeDynamicBgEdit)
            myhomeVpEdit.adapter = adapter
            myhomeVpEdit.isUserInputEnabled = false

            val intent = intent
            if (intent != null) {
                val s = intent.getIntExtra("type", -1)
                if (s != -1) {
                    myhomeTlSort.selectTab(myhomeTlSort.getTabAt(s))
                }

            }
            myhomeDynamicBgEdit.post {
                adapter.widgetContentAdapter1?.refresh()
                adapter.widgetContentAdapter2?.refresh()
            }
        }
    }

}