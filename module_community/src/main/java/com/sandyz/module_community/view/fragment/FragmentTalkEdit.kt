package com.sandyz.module_community.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.sandyz.alltimers.common.base.BaseFragment
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.network.FileUploadUtil
import com.sandyz.alltimers.common.widgets.KeyboardController
import com.sandyz.alltimers.common.widgets.OptionalDialog
import com.sandyz.alltimers.common.widgets.ProgressShowDialog
import com.sandyz.module_community.R
import com.sandyz.module_community.view.vm.CommunityViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_talk_edit.*

class FragmentTalkEdit : BaseFragment() {

    companion object {
        var viewModel: CommunityViewModel? = null
    }

    private var imgPath: String? = null
    private var upLoading = false
    private var imgUrl: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_talk_edit, container, false)
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        viewModel?.replyInfo?.value?.let {
            if (it.replyId != -1) {
                textViewReplyText.text = "回复：@${it.nickname}"
            }
        }


        progressBar.indeterminateDrawable.setColorFilter(
            resources.getColor(
                R.color.colorLightRed,
                null
            ), PorterDuff.Mode.SRC_IN
        )

        imageViewPic.setOnClickListener {

            RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe {
                if (it) {
                    selectPic()
                }
            }

        }



        textViewSend.setOnClickListener {
            KeyboardController.hideInputKeyboard(requireContext(), it)

            if (upLoading) {
                Toast.makeText(requireContext(), "图片还没上传好鸭~", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            activity?.let { it1 -> ProgressShowDialog.show(it1, "请稍后", "正在上传~", false) }
            if ((viewModel?.replyInfo?.value?.replyId ?: -1) == -1) {
                // 发帖
                if (imgUrl.isNullOrBlank()) {
                    viewModel?.releaseDynamic(editTextTalk.text.toString(), "BOB")
                } else {
                    viewModel?.releaseDynamic(editTextTalk.text.toString(), "BOB", listOf(imgUrl!!))
                }
            } else {
                // 回复
                viewModel?.reply(editTextTalk.text.toString())
            }
        }
        viewModel?.replyStatus?.observe {
            ProgressShowDialog.hide()
            if (it) {
                viewModel?.refreshDynamic()
//                findNavController().popBackStack()
                requireActivity().finish()
            }
        }
        viewModel?.releaseDynamicStatus?.observe {
            ProgressShowDialog.hide()
            if (it) {
//                val navController = findNavController()
//                while (navController.backStack.size >= 1) {
//                    navController.popBackStack()
//                }
//                navController.navigate(R.id.action_global_fragmentCommunity)
                // TODO 返回
                requireActivity().finish()
            }
        }

        iv_back.setOnClickAction {
            OptionalDialog.show(requireContext(), "放弃发布吗？内容将不会保存", onDeny = {}) {
                requireActivity().finish()
            }
        }


    }

    private fun selectPic() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, 11)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            val selectedImage = data!!.data
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? =
                selectedImage?.let {
                    requireContext().contentResolver.query(
                        it,
                        filePathColumn,
                        null,
                        null,
                        null
                    )
                }
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
            imgPath = columnIndex?.let { cursor.getString(it) }
            cursor?.close()


            //imgPath?.let { LogW.d(it) }//===================================================
            progressBar.visibility = View.VISIBLE
            imageViewPic.visibility = View.GONE
            imageViewPic.setImageBitmap(null)

            upLoading = true

            // TODO imgPath
            if (imgPath.isNullOrEmpty()) {
                return
            }
            FileUploadUtil.uploadMultiFile(listOf(imgPath!!)) {
                activity?.runOnUiThread {
                    if (it.picUrls.isNotEmpty()) {
                        imgUrl = it.picUrls[0]
                        progressBar.visibility = View.GONE
                        imageViewPic.visibility = View.VISIBLE
                        Glide.with(this).load(it.picUrls[0]).into(imageViewPic)
                        upLoading = false
                    }
                }
            }


        }
    }


}