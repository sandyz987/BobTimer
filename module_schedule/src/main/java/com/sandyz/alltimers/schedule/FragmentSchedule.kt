package com.sandyz.alltimers.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.config.SCHEDULE_ENTRY
import com.sandyz.alltimers.common.extensions.getScreenHeight
import com.sandyz.alltimers.common.utils.LoadBitmapUtils
import kotlinx.android.synthetic.main.schedule_fragment_schedule.*

@Route(path = SCHEDULE_ENTRY)
class FragmentSchedule : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.schedule_fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoadBitmapUtils.decodeBitmapFromResourceByHeight(resources, R.drawable.common_ic_background_test, requireContext().getScreenHeight())?.let {
            dynamic_time.setBitmap(
                it
            )
        }
        dynamic_time.addWidget("recorder")
        dynamic_time.addWidget("rabbit")
        dynamic_time.scrollToPercent(0.5f)
        dynamic_time.fromSerializationData()
        dynamic_time.onBind()
    }

    override fun onPause() {
        super.onPause()
        dynamic_time.saveSerializationData()
    }

}