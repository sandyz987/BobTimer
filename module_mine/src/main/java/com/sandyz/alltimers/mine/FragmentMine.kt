package com.sandyz.alltimers.mine

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.BaseApp
import com.sandyz.alltimers.common.config.Config
import com.sandyz.alltimers.common.config.MINE_ENTRY
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.widgets.OptionalDialog
import com.sandyz.alltimers.mine.databinding.MineFragmentMineBinding
import com.sandyz.alltimers.mine.vm.IndividualViewModel
import com.sandyz.moneycounter4.base.BaseViewModelFragment


@Route(path = MINE_ENTRY)
class FragmentMine : BaseViewModelFragment<IndividualViewModel>() {
    private lateinit var binding: MineFragmentMineBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return MineFragmentMineBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.user.observeNotNull { user ->
            binding.mineTvNickname.text = user.nickname
            binding.mineTvId.text = "ID：${user.userId}"
        }
        viewModel.getUser(Config.userId)
        binding.mineLlLogout.setOnClickAction {
            context?.let {
                OptionalDialog.show(it, "确定要退出登录吗？", onDeny = {}) {
                    Config.password = ""
                    val sharedPreferences =
                        BaseApp.app.getSharedPreferences("usrInfo", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("user_id", Config.userId)
                    editor.putString("password", "")
                    editor.commit()
                    requireActivity().finish()
                }
            }
        }
    }

}