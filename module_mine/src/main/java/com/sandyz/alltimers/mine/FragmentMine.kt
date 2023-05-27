package com.sandyz.alltimers.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sandyz.alltimers.common.config.Config
import com.sandyz.alltimers.common.config.MINE_ENTRY
import com.sandyz.alltimers.common.config.SHOP_RECHARGE
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.widgets.OptionalDialog
import com.sandyz.alltimers.mine.vm.IndividualViewModel
import com.sandyz.moneycounter4.base.BaseViewModelFragment
import kotlinx.android.synthetic.main.mine_fragment_mine.*


@Route(path = MINE_ENTRY)
class FragmentMine : BaseViewModelFragment<IndividualViewModel>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mine_fragment_mine, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.user.observeNotNull { user ->
            mine_tv_nickname.text = user.nickname
            mine_tv_id.text = "ID：${user.userId}"
        }
        viewModel.getUser(Config.userId)
        mine_ll_logout.setOnClickAction {
            context?.let {
                OptionalDialog.show(it, "确定要退出登录吗？", onDeny = {}) {
                    Config.password = ""
                    ARouter.getInstance().build(SHOP_RECHARGE).navigation()
                    requireActivity().finish()
                }
            }
        }
    }

}