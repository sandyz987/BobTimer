package com.sandyz.alltimers.myhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.base.BaseFragment
import com.sandyz.alltimers.common.config.HOME_ENTRY
import com.sandyz.alltimers.myhome.backgroundscroll.pasteWidget
import kotlinx.android.synthetic.main.myhome_fragment_home.*

@Route(path = HOME_ENTRY)
class FragmentHome : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.myhome_fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myhome_dynamic_bg.setSize(3000, 1600)
        myhome_dynamic_bg.scrollToPercent(0.3f, false)
        myhome_dynamic_bg.setWallPaper(R.drawable.myhome_ic_wallpaper1)
        myhome_dynamic_bg.setFloor(R.drawable.myhome_ic_floor1)

        if (myhome_dynamic_bg.fromSerializationData()) {
            myhome_dynamic_bg.addWidget("widget1", "widget1", myhome_dynamic_bg.getVisibleLeft(), 700)
            myhome_dynamic_bg.addWidget("widget2", "widget2", myhome_dynamic_bg.getVisibleLeft(), 700)
            myhome_dynamic_bg.addWidget("widget3", "widget3", myhome_dynamic_bg.getVisibleLeft(), 700)
            myhome_dynamic_bg.addWidget("widget4", "widget4", myhome_dynamic_bg.getVisibleLeft(), 700)
        }
        myhome_dynamic_bg.onBind()
    }

    override fun onPause() {
        super.onPause()
        myhome_dynamic_bg.saveSerializationData()
    }

}