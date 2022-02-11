package com.sandyz.alltimers.concentrate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.config.CONCENTRATE_ENTRY
import kotlinx.android.synthetic.main.concentrate_fragment_concentrate.*


@Route(path = CONCENTRATE_ENTRY)
class FragmentConcentrate : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.concentrate_fragment_concentrate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        concentrate_roll_disk.setDiskImageId(R.drawable.concentrate_ic_roll_disk_pic)
        concentrate_roll_disk.onTimeChange = { h, m ->
            val hours = if (h <= 9) "0$h" else "$h"
            val minutes = if (m <= 9) "0$m" else "$m"
            concentrate_display.mText = "$hours:$minutes"
            Log.e("sandyzhangtime", "$hours:$minutes")
        }
    }

}