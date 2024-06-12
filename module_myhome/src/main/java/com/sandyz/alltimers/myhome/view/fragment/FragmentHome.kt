package com.sandyz.alltimers.myhome.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sandyz.alltimers.common.base.BaseFragment
import com.sandyz.alltimers.common.config.HOME_ENTRY
import com.sandyz.alltimers.common.config.SHOP_RECHARGE
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.myhome.R
import com.sandyz.alltimers.myhome.databinding.MyhomeFragmentHomeBinding
import com.sandyz.alltimers.myhome.model.WallpaperAndFloorModel
import com.sandyz.alltimers.myhome.view.activity.ActivityEdit
import com.sandyz.alltimers.myhome.view.dialog.MissionDialog

@Route(path = HOME_ENTRY)
class FragmentHome : BaseFragment() {
    private lateinit var binding: MyhomeFragmentHomeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return MyhomeFragmentHomeBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            myhomeDynamicBg.setSize(3000, 1600)
            context?.let { WallpaperAndFloorModel.getWallpaper(it) }?.let { myhomeDynamicBg.setWallPaper(it) }
            context?.let { WallpaperAndFloorModel.getFloor(it) }?.let { myhomeDynamicBg.setFloor(it) }

            myhomeDynamicBg.onBind()

            myhomeDynamicBg.onLoadedAction = {
                val left = myhomeDynamicBg.getChildPosition("Rabbit")?.first
                if (left != null) {
                    myhomeDynamicBg.scrollToPosition(left - 250)
                }
                myhomeDynamicBg.gravityAnim()
            }

            if (myhomeDynamicBg.fromSerializationData()) {
                myhomeDynamicBg.post {
                    myhomeDynamicBg.addWidget("Widget1", "Widget1", myhomeDynamicBg.getVisibleLeft() - 300, 450)
                    myhomeDynamicBg.addWidget("Widget2", "Widget2", myhomeDynamicBg.getVisibleLeft() - 600, 450)
                    myhomeDynamicBg.addWidget("Widget3", "Widget3", myhomeDynamicBg.getVisibleLeft() + 300, 200)
                    myhomeDynamicBg.addWidget("Widget4", "Widget4", myhomeDynamicBg.getVisibleLeft() + 700, 550)
                    myhomeDynamicBg.addWidget("Rabbit", "Rabbit", myhomeDynamicBg.getVisibleLeft() + 100, 550)
                    myhomeDynamicBg.onBind()
                    myhomeDynamicBg.post {
                        myhomeDynamicBg.gravityAnim()
                    }
                }
            }

            myhomeTvEdit.setOnClickAction {
                startActivity(Intent(context, ActivityEdit::class.java).apply {
                    putExtra("type", 1)
                })
            }

            myhomeTvShop.setOnClickAction {
                startActivity(Intent(context, ActivityEdit::class.java).apply {
                    putExtra("type", 0)
                })
            }


            myhomeIvCarrotAdd.setOnClickAction {
                ARouter.getInstance().build(SHOP_RECHARGE).navigation()
            }

            myhomeTvMission.setOnClickAction {
                context?.let { MissionDialog.show(it) }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.myhomeDynamicBg.saveSerializationData()
    }

}