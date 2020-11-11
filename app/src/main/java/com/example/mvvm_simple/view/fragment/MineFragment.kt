package com.example.mvvm_simple.view.fragment

import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.base.model.bean.BaseNetworkStatus
import com.example.base.view.fragment.LazyMvvmNetworkFragment
import com.example.base.view.fragment.MvvmNetworkFragment
import com.example.lib_common.util.DensityUtil
import com.example.lib_common.widget.dialog.SelfDialog
import com.example.lib_common.widget.dialog.SelfDialog.LOADING_DIALOG
import com.example.lib_common.widget.smallredpoint.Badge.OnDragStateChangedListener.STATE_SUCCEED
import com.example.lib_common.widget.smallredpoint.QBadgeView
import com.example.lib_data.data_user.NearEnjoyUser
import com.example.lib_data.data_user.UserInfoIO
import com.example.mvvm_simple.NetworkRequestInfo
import com.example.mvvm_simple.R
import com.example.mvvm_simple.databinding.FragmentMineBinding
import com.example.mvvm_simple.model.bean.UserGoodsBean
import com.example.mvvm_simple.model.models.GetUserGoodsModel
import com.example.mvvm_simple.model.models.GetUserInfoModel
import com.example.mvvm_simple.model.models.UpdateAvatarModel
import com.example.mvvm_simple.model.models.UploadFileModel
import com.example.mvvm_simple.view.OnSaleActivity
import com.example.mvvm_simple.view.adapter.MyVerticalBannerAdapter
import com.example.mvvm_simple.viewmodel.MineViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.fragment_mine.view.*
import java.io.File


/**
 * @author DuLong
 * @since 2020/4/11
 * email 798382030@qq.com
 */
class MineFragment: LazyMvvmNetworkFragment<FragmentMineBinding, MineViewModel>() {
    private var mUserInfo: NearEnjoyUser? = null
    private var mUserGoodsBean: UserGoodsBean? = null
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private var mLoadingDialog: SelfDialog? = null

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun getViewModel(): Class<out ViewModel> {
        return MineViewModel::class.java
    }

    override fun onLoadMoreFailure(message: String?) {

    }

    override fun getFragmentTag(): String {
        return "MineFragment"
    }

    override fun initParameters() {

    }

    override fun onLoadMoreEmpty() {

    }

    override fun initDataAndView() {
        mViewDataBinding.eventPresenter = EventPresenter()
        //这里由于状态栏透明，所以动态设置margin,防止图片与statusBar重合
        val lp = mViewDataBinding.imgMineAvatar.layoutParams as RelativeLayout.LayoutParams
        lp.topMargin = (DensityUtil.dpToPx(context, 7) + DensityUtil.getStatusBarHeight(context)).toInt()
        mViewDataBinding.imgMineAvatar.layoutParams = lp;
        initRedPoint()
        addObserver()
        mViewModel.getUserGoodsInfo(page = "1", size = "10")
        mUserInfo = UserInfoIO.getLasterUser()
        if (mUserInfo != null) {
           initUserInfo()
        } else {
            mViewModel.getUserInfo()
        }
    }

    /**
     * 观察ViewModel中的数据
     */
    private fun addObserver() =//弹出bottomSheet用于修改头像
            mViewDataBinding.imgMineAvatar.setOnClickListener {
                if (mBottomSheetDialog == null) {
                    mBottomSheetDialog = BottomSheetDialog(requireContext())
                }
                mBottomSheetDialog!!.setContentView(R.layout.dialog_mine_edit_avatar)
                mBottomSheetDialog?.findViewById<TextView>(R.id.txt_change_avatar)?.setOnClickListener {
                    mBottomSheetDialog?.dismiss()
                    openGallery()
                }
                mBottomSheetDialog?.findViewById<TextView>(R.id.cancel)?.setOnClickListener {
                    mBottomSheetDialog?.dismiss()
                }
                mBottomSheetDialog?.show()
            }

    /**
     * 打开图库选择图片
     */
    private fun openGallery() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)
                .isMultipleRecyclerAnimation(true)
                .loadImageEngine(com.example.mvvm_simple.GlideEngine.createGlideEngine())   // 外部传入图片加载引擎，必传项
                .imageSpanCount(3)  //每行显示个数
                .selectionMode(PictureConfig.SINGLE)    //单选
                .isSingleDirectReturn(false)    //// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .previewImage(true)// 是否可预览图片
                .isCamera(true) //是否显示拍照按钮
                .enableCrop(true)   //是否裁剪
                .compress(true) //是否压缩
                .withAspectRatio(1, 1)  //压缩比例
                .freeStyleCropEnabled(false) //裁剪框是否可拖拽
                .circleDimmedLayer(true)    //是否圆形裁剪
                .showCropFrame(false)   //是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isReturnEmpty(false)   // 未选择数据时点击按钮是否可以返回
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .rotateEnabled(false) // 裁剪是否可旋转图片
                .scaleEnabled(true)// 裁剪是否可放大缩小图片
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onCancel() {
                        if (mBottomSheetDialog!!.isShowing) {
                            mBottomSheetDialog?.dismiss()
                        }
                    }

                    override fun onResult(result: MutableList<LocalMedia>?) {
                        if (mLoadingDialog == null) {
                            mLoadingDialog = SelfDialog(requireContext())
                            mLoadingDialog?.setLayout(LOADING_DIALOG)
                            mLoadingDialog?.setMessage("上传图片中")
                        }
                        mLoadingDialog?.show()
                        if (!result.isNullOrEmpty()) {
                            mViewModel.uploadFile(File(result[0].compressPath))
                        }
                    }

                })
    }

    /**
     * 更新用户数据
     */
    private fun initUserInfo() {
        val option = RequestOptions()
                .placeholder(R.drawable.ic_placehodler)
                .error(R.drawable.ic_placehodler)
        Glide.with(requireContext())
                .load(mUserInfo?.avatar)
                .apply(option)
                .into(mViewDataBinding.imgMineAvatar)
        mViewDataBinding.txtMineName.text = mUserInfo?.name
        mViewDataBinding.txtMineIntroduction.text = mUserInfo?.signature
    }

    /**
     * 初始化轮播图
     */
    private fun initBanner(data: UserGoodsBean?, loadFailed: Boolean) {
        if (data?.searchList != null && data.searchList.isNotEmpty()) {
            mViewDataBinding.banner.adapter = MyVerticalBannerAdapter(data.searchList)
            //设置换页时的特效
//            mViewDataBinding.banner.setPageTransformer(ZoomOutPageTransformer())
        } else {
            //没数据时显示占位图
            mViewDataBinding.banner.stop()
            mViewDataBinding.banner.isEnabled = false
            mViewDataBinding.banner.visibility = View.GONE
            mViewDataBinding.relayoutNoData.visibility = View.VISIBLE
            if (loadFailed) mViewDataBinding.txtHint.text = "网络好像有点问题"
        }
    }

    /**
     * 初始化小红点
     * 暂时不用，留着以后用
     */
    private fun initRedPoint() {
        var confirmBadge = QBadgeView(context).bindTarget(mViewDataBinding.relayoutMineWaitConfirm)
                .setBadgeNumber(5)
                .setBadgeTextSize(8f, true)
                .setBadgeBackground(resources.getDrawable(R.drawable.bg_mine_red_round_4dp))
                .setBadgeGravity(Gravity.TOP or Gravity.END)
                .setGravityOffset(24f, 1f, true)
                .setOnDragStateChangedListener { dragState, badge, _ ->
                    if (dragState == STATE_SUCCEED) {
                        (badge as QBadgeView).hide(true)
                    }
                }
        var judgeBadge = QBadgeView(context).bindTarget(mViewDataBinding.relayoutMineWaitJudge)
                .setBadgeNumber(5)
                .setBadgeTextSize(8f, true)
                .setBadgeBackground(resources.getDrawable(R.drawable.bg_mine_red_round_4dp))
                .setBadgeGravity(Gravity.TOP or Gravity.END)
                .setGravityOffset(24f, 1f, true)
                .setOnDragStateChangedListener { dragState, badge, _ ->
                    if (dragState == STATE_SUCCEED) {
                        (badge as QBadgeView).hide(true)
                    }
                }
        var goodsBadge = QBadgeView(context).bindTarget(mViewDataBinding.relayoutMineWaitReceiveGoods)
                .setBadgeNumber(5)
                .setBadgeTextSize(8f, true)
                .setBadgeBackground(resources.getDrawable(R.drawable.bg_mine_red_round_4dp))
                .setBadgeGravity(Gravity.TOP or Gravity.END)
                .setGravityOffset(24f, 1f, true)
                .setOnDragStateChangedListener { dragState, badge, _ ->
                    if (dragState == STATE_SUCCEED) {
                        (badge as QBadgeView).hide(true)
                    }
                }
    }

    inner class EventPresenter {
        fun onClick(view: View) {
            when(view.id) {
                R.id.txt_check_user_goods -> {
                    OnSaleActivity.actionStart(context!!)
                }
            }
        }
    }

    override fun onNetFailed(key: String?, status: BaseNetworkStatus?) {
        key?.let {
            when(key) {
                GetUserGoodsModel.TAG -> {
                    initBanner(null, true)
                }
                else -> {
                    if (mLoadingDialog != null && mLoadingDialog!!.isShowing) {
                        mLoadingDialog?.dismiss()
                    }
                    Log.e("网络请求失败", key + status?.message)
                }
            }
        }
    }

    override fun onNetDone(key: String?, status: BaseNetworkStatus?) {
        if (key != null) {
            when(key) {
                GetUserInfoModel.TAG -> {
                    val data = mViewModel.mUserInfo.value
                    if (data != null) {
                        val userInfo = NearEnjoyUser()
                        userInfo.avatar = data.avatar
                        userInfo.name = data.name
                        userInfo.id = data.id
                        userInfo.signature = data.signature
                        userInfo.schoolId = data.schoolId
                        mUserInfo = userInfo
                    }
                    initUserInfo()
                }
                GetUserGoodsModel.TAG -> {
                    mUserGoodsBean = mViewModel.mUserGoods.value
                    initBanner(mViewModel.mUserGoods.value, false)
                }
                UploadFileModel.TAG -> {
                    val data = mViewModel.mFileUrl.value
                    if (data != null) {
                        val trueUrl = NetworkRequestInfo.BASE_URL + data
                        //更新图片
                        Glide.with(this).load(trueUrl).into(mViewDataBinding.imgMineAvatar)
                        //更新数据到服务器
                        mViewModel.updateAvatar(trueUrl)
                    }
                }
                UpdateAvatarModel.TAG -> {
                    //服务器成功接收到数据，结束dialog
                    if (mLoadingDialog != null && mLoadingDialog!!.isShowing) {
                        mLoadingDialog?.dismiss()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mViewDataBinding.banner.start()
    }

    override fun onStop() {
        super.onStop()
        mViewDataBinding.banner.stop()
    }
}