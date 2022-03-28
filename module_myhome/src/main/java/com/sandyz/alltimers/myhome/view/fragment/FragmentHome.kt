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
import com.sandyz.alltimers.myhome.view.activity.ActivityEdit
import com.sandyz.alltimers.myhome.view.dialog.MissionDialog
import kotlinx.android.synthetic.main.myhome_fragment_home.*

@Route(path = HOME_ENTRY)
class FragmentHome : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.myhome_fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        myhome_dynamic_bg.setSize(3000, 1600)
        myhome_dynamic_bg.scrollToPercent(0.3f, false)
        myhome_dynamic_bg.setWallPaper(R.drawable.myhome_ic_wallpaper1)
        myhome_dynamic_bg.setFloor(R.drawable.myhome_ic_floor1)

        if (myhome_dynamic_bg.fromSerializationData()) {
            myhome_dynamic_bg.addWidget("Widget1", "Widget1", myhome_dynamic_bg.getVisibleLeft() - 300, 700)
            myhome_dynamic_bg.addWidget("Widget2", "Widget2", myhome_dynamic_bg.getVisibleLeft() - 600, 700)
            myhome_dynamic_bg.addWidget("Widget3", "Widget3", myhome_dynamic_bg.getVisibleLeft() + 400, 300)
            myhome_dynamic_bg.addWidget("Widget4", "Widget4", myhome_dynamic_bg.getVisibleLeft() + 700, 800)
        }
        myhome_dynamic_bg.onBind()


        myhome_tv_edit.setOnClickAction {
            startActivity(Intent(context, ActivityEdit::class.java))
        }


        myhome_iv_carrot_add.setOnClickAction {
            ARouter.getInstance().build(SHOP_RECHARGE).navigation()
        }

        myhome_tv_mission.setOnClickAction {
            context?.let { MissionDialog.show(it) }
        }
    }

    override fun onPause() {
        super.onPause()
        myhome_dynamic_bg.saveSerializationData()
    }

}