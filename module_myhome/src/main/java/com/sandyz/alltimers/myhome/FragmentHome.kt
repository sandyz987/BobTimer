package com.sandyz.alltimers.myhome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.config.HOME_ENTRY
import com.sandyz.alltimers.myhome.backgroundscroll.pasteWidget
import kotlinx.android.synthetic.main.myhome_fragment_home.*

@Route(path = HOME_ENTRY)
class FragmentHome : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.myhome_fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        LoadBitmapUtils.decodeBitmapFromResourceByHeight(resources, R.drawable.myhome_ic_background_test, requireContext().getScreenHeight())?.let {
//            dynamic_time.setBitmap(
//                it
//            )
//        }
        dynamic_time.setBgResourceId(R.drawable.myhome_ic_background)
        dynamic_time.addWidget("0","recorder")
        dynamic_time.addWidget("1","rabbit")
        dynamic_time.pasteWidget("2", 50, 50, 2999, 950, R.drawable.myhome_ic_gif, null)
        dynamic_time.scrollToPercent(0.5f)
//        dynamic_time.fromSerializationData()
        dynamic_time.onBind()
    }

    override fun onPause() {
        super.onPause()
        dynamic_time.saveSerializationData()
    }

}