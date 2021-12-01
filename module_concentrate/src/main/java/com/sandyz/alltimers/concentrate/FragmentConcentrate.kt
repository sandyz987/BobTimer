package com.sandyz.alltimers.concentrate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.config.CONCENTRATE_ENTRY


@Route(path = CONCENTRATE_ENTRY)
class FragmentConcentrate : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_concentrate, container, false)
    }

}