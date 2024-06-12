package com.sandyz.module_community.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.sandyz.alltimers.common.base.BaseFragment
import com.sandyz.alltimers.common.config.Config
import com.sandyz.alltimers.common.extensions.dp2px
import com.sandyz.alltimers.common.utils.TimeUtil
import com.sandyz.alltimers.common.widgets.ClipboardManager
import com.sandyz.alltimers.common.widgets.KeyboardController
import com.sandyz.alltimers.common.widgets.OptionalDialog
import com.sandyz.alltimers.common.widgets.OptionalPopWindow
import com.sandyz.api_community.bean.DynamicItem
import com.sandyz.api_community.bean.ReplyInfo
import com.sandyz.api_community.bean.findEquals
import com.sandyz.module_community.R
import com.sandyz.module_community.databinding.FragmentTalkDetailBinding
import com.sandyz.module_community.view.adapter.LikeRecyclerViewAdapter
import com.sandyz.module_community.view.adapter.TalkCommentRecyclerViewAdapter
import com.sandyz.module_community.view.vm.CommunityViewModel
import com.sandyz.module_community.widget.ReplyPopWindow

class FragmentTalkDetail : BaseFragment() {

    private var dynamic: DynamicItem? = null

    private lateinit var adapter: TalkCommentRecyclerViewAdapter
    private var dynamicId: Int = -1
    private lateinit var listener: SwipeRefreshLayout.OnRefreshListener
    private lateinit var binding: FragmentTalkDetailBinding

    companion object {
        var viewModel: CommunityViewModel? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTalkDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {


            root.findViewById<View>(R.id.textViewMore).visibility = View.GONE
            dynamicId = viewModel?.dynamicId ?: 0

            dynamic = findDynamic()


            textViewOptions.setOnClickListener {
                val optionPopWindow = OptionalPopWindow.Builder().with(requireContext()).addOptionAndCallback("回复") {
                    viewModel?.replyInfo?.value = ReplyInfo("", "", dynamic?.dynamicId ?: -1, 0)
                    KeyboardController.showInputKeyboard(requireContext(), etReply)
                }.addOptionAndCallback("复制") {
                    ClipboardManager.copyText(requireContext(), dynamic?.text ?: "")
                }
                if (dynamic?.userId == Config.userId) {
                    optionPopWindow.addOptionAndCallback("删除") {
                        OptionalDialog.show(requireContext(), "是否删除？", false, {}) {
                            viewModel?.deleteDynamic(dynamicId)
                        }
                    }
                }
                optionPopWindow.show(it, OptionalPopWindow.AlignMode.LEFT, 0)
            }

            adapter = TalkCommentRecyclerViewAdapter(requireContext(), { commentItem, view ->
                // 当一级评论被点击
                viewModel?.replyInfo?.value = ReplyInfo(commentItem.nickname, commentItem.text, commentItem.id, 1)
                KeyboardController.showInputKeyboard(requireContext(), etReply)

            }, { commentItem, view ->
                // 当二级评论被点击
                viewModel?.replyInfo?.value = ReplyInfo(commentItem.nickname, commentItem.text, commentItem.id, 2)
                KeyboardController.showInputKeyboard(requireContext(), etReply)
            }, { commentItem, view ->
                // 当一级评论被长按
                val optionPopWindow = OptionalPopWindow.Builder().with(requireContext()).addOptionAndCallback("回复") {
                    viewModel?.replyInfo?.value = ReplyInfo(commentItem.nickname, commentItem.text, commentItem.id, 1)
                    KeyboardController.showInputKeyboard(requireContext(), etReply)
                }.addOptionAndCallback("复制") {
                    ClipboardManager.copyText(requireContext(), commentItem.text)
                }
                if (commentItem.userId == Config.userId) {
                    optionPopWindow.addOptionAndCallback("删除") {
                        OptionalDialog.show(requireContext(), "是否删除？", false, {}) {
                            viewModel?.deleteComment(commentItem.id, 1)
                        }
                    }
                }
                optionPopWindow.show(view, OptionalPopWindow.AlignMode.CENTER, 0)

            }, { commentItem, view ->
                // 当二级评论被长按
                val optionPopWindow = OptionalPopWindow.Builder().with(requireContext()).addOptionAndCallback("回复") {
                    viewModel?.replyInfo?.value = ReplyInfo(commentItem.nickname, commentItem.text, commentItem.id, 2)
                    KeyboardController.showInputKeyboard(requireContext(), etReply)
                }.addOptionAndCallback("复制") {
                    ClipboardManager.copyText(requireContext(), commentItem.text)
                }
                if (commentItem.userId == Config.userId) {
                    optionPopWindow.addOptionAndCallback("删除") {
                        OptionalDialog.show(requireContext(), "是否删除？", false, {}) {
                            viewModel?.deleteComment(commentItem.id, 2)
                        }
                    }
                }
                optionPopWindow.show(view, OptionalPopWindow.AlignMode.CENTER, 0)
            })

            rvTalk.layoutManager = LinearLayoutManager(context)
            rvTalk.adapter = adapter
            adapter.setList(dynamic?.commentList ?: listOf())


            bindView()

            listener = SwipeRefreshLayout.OnRefreshListener {
                viewModel?.refreshDynamic()
            }
            swipeRefreshLayout.setOnRefreshListener(listener)
            listener.onRefresh()
            swipeRefreshLayout.isRefreshing = true



            viewModel?.dynamicList?.observe {
                bindView()
                dynamic = findDynamic()
                adapter.setList(dynamic?.commentList ?: listOf())
                swipeRefreshLayout.isRefreshing = false
                recyclerViewLikeAccount.adapter = LikeRecyclerViewAdapter(this@FragmentTalkDetail, dynamic!!.praise.reversed())
                recyclerViewLikeAccount.layoutManager = LinearLayoutManager(activity).apply {
                    orientation = LinearLayoutManager.HORIZONTAL
                }
            }

            coordinatorlayoutTouch.onReplyCancelEvent = {
                viewModel?.replyInfo?.value = ReplyInfo("", "", dynamic?.dynamicId ?: -1, 0)
                KeyboardController.hideInputKeyboard(requireContext(), etReply)
            }

            viewModel?.replyInfo?.observe {
                it.let {
                    if (it.replyId != -1 && !(it.replyId == dynamicId && it.which == 0)) {
                        coordinatorlayoutTouch.isReplyEdit = true
                        KeyboardController.showInputKeyboard(requireContext(), etReply)
                        ReplyPopWindow.with(requireContext())
                        ReplyPopWindow.setReplyName(it.nickname, it.contentPreview)
                        ReplyPopWindow.setOnClickEvent {
                            viewModel?.replyInfo?.value = ReplyInfo("", "", dynamic?.dynamicId ?: -1, 0)
                        }
                        ReplyPopWindow.show(
                            etReply, ReplyPopWindow.AlignMode.LEFT, requireContext().dp2px(6f)
                        )
                    } else {
                        if (ReplyPopWindow.isShowing()) {
                            ReplyPopWindow.dismiss()
                        }
                    }

                }
            }

            viewModel?.replyStatus?.observe {
                etReply.setText("")
                viewModel?.refreshDynamic()
                swipeRefreshLayout.isRefreshing = true
                viewModel?.replyInfo?.value = ReplyInfo("", "", dynamic?.dynamicId ?: -1, 0)
//            rv_talk.scrollToPosition(0)
                KeyboardController.hideInputKeyboard(requireContext(), etReply)
            }
            viewModel?.deleteStatus?.observe {
                when (it) {
                    1 -> {
//                    findNavController().popBackStack()
                        requireActivity().finish()
                    }

                    2 -> {
                        rvTalk.scrollToPosition(0)
                        viewModel?.refreshDynamic()
                        swipeRefreshLayout.isRefreshing = true
                    }
                }
            }

            btnSend.setOnClickListener {
                viewModel?.reply(etReply.text.toString())
            }

            viewModel?.replyInfo?.value = ReplyInfo("", "", dynamic?.dynamicId ?: -1, 0)

        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindView() {

        binding.includeItemTalkBig.apply {


            val isPraise = dynamic?.praise?.findEquals { it.userId == Config.userId }
            imageViewLike.registerLikeView(
                dynamic?.dynamicId ?: 0, 0, isPraise ?: false, dynamic?.praise?.size ?: 0
            )
            imageViewLike.callback = {
                listener.onRefresh()
//            swipeRefreshLayout.isRefreshing = true
            }




            textViewUsrName.text = dynamic?.userId
            textViewContent.text = dynamic?.text?.take(30)

            textViewContent.text?.length?.let {
                if (it >= 30) {
                    textViewContent.text = textViewContent.text.toString() + "..."
                    textViewMore.visibility = View.VISIBLE
                } else {
                    textViewMore.visibility = View.GONE
                }
            }

            textViewTime.text = TimeUtil.getChatTimeStr(dynamic?.submitTime ?: 0L)
            if (dynamic?.picUrl?.isNotEmpty() == true) {
                imageViewPic.visibility = View.VISIBLE
                dynamic?.picUrl?.let {
                    Glide.with(this@FragmentTalkDetail).load(it[0]).into(imageViewPic)
                }
            } else {
                imageViewPic.visibility = View.GONE
            }
            textViewUsrName.text = dynamic?.nickname


            imageViewTalk.setHint(dynamic?.commentList?.size.toString())


            imageViewUsrPic.setOnClickListener {
                //            val navController = Navigation.findNavController(it)
                //            val bundle = Bundle()
                //            bundle.putBoolean("isMine", false)
                //            bundle.putString("userId", dynamic?.userId)
                //            navController.navigate(R.id.action_global_fragmentIndividual, bundle)
            }

            imageViewTalk.setOnClickListener {
                //            val navController = Navigation.findNavController(it)
                //            val bundle = Bundle()
                //            bundle.putInt("dynamic_id", dynamic?.dynamicId ?: 0)
                //            navController.navigate(R.id.action_global_fragmentTalkDetails, bundle)
            }
        }


    }

    private fun findDynamic(): DynamicItem? {
        viewModel?.dynamicList?.value?.forEach {
            if (it.dynamicId == dynamicId) {
                return it
            }
        }
        return null
    }

    private fun findDynamic(dynamicId: Int): Int {
        viewModel?.dynamicList?.value?.let {
            for (i in 0..it.size) {
                if (it[i].dynamicId == dynamicId) {
                    return i
                }
            }
        }
        return 0
    }


}