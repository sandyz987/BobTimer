package com.sandyz.module_community.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sandyz.api_community.bean.ReplyInfo
import com.sandyz.module_community.R
import com.sandyz.module_community.databinding.FragmentCommunityBinding
import com.sandyz.module_community.view.activity.ActivityCommunityDetail
import com.sandyz.module_community.view.activity.ActivityCommunityEdit
import com.sandyz.module_community.view.adapter.TalkRecyclerViewAdapter
import com.sandyz.module_community.view.vm.CommunityViewModel
import com.sandyz.moneycounter4.base.BaseViewModelFragment


class FragmentCommunity : BaseViewModelFragment<CommunityViewModel>() {

    private lateinit var binding: FragmentCommunityBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var adapter: TalkRecyclerViewAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.apply {
            recyclerViewTalkList.layoutManager = LinearLayoutManager(context)
            if (recyclerViewTalkList.itemDecorationCount == 0) {
                recyclerViewTalkList.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }


            // 设置点击事件
            adapter = TalkRecyclerViewAdapter(requireContext()) { dynamic, v ->
                FragmentTalkDetail.viewModel = viewModel
//            Navigation.findNavController(v)
//                .navigate(R.id.action_global_fragmentTalkDetails, Bundle().apply {
//                    putInt("dynamic_id", dynamic.dynamicId)
//                })
                viewModel.dynamicId = dynamic.dynamicId
                startActivity(Intent(activity, ActivityCommunityDetail::class.java))
            }


            recyclerViewTalkList.adapter = adapter


            val listener = {
                viewModel.refreshDynamic()
            }

            dragHeadView.onRefreshAction = listener
            listener.invoke()

            sendDynamic.setOnClickListener {
                viewModel.replyInfo.value = ReplyInfo("", "", -1, -1)
                FragmentTalkEdit.viewModel = viewModel
//            val navController = findNavController()
//            navController.navigate(R.id.action_global_fragmentTalkEdit)
                startActivity(Intent(activity, ActivityCommunityEdit::class.java))
            }

            viewModel.dynamicList.observeNotNull {
                adapter.setList(it)
                dragHeadView.finishRefresh()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshDynamic()
    }


}