package com.sandyz.alltimers.mine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.base.BaseFragment
import com.sandyz.alltimers.common.config.MINE_ENTRY


@Route(path = MINE_ENTRY)
class FragmentMine : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mine_fragment_mine, container, false)
    }

}