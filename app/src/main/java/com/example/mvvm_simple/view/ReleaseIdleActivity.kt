package com.example.mvvm_simple.view
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.app.ProgressDialog.STYLE_SPINNER
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.GridLayout.VERTICAL
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.base.model.bean.BaseNetworkStatus
import com.example.base.utils.ToastUtil
import com.example.base.view.activity.MvvmNetworkActivity
import com.example.lib_common.util.statusbar.StatusBarUtil
import com.example.lib_common.widget.dialog.SelfDialog
import com.example.lib_common.widget.dialog.SelfDialog.CONFIRM_DIALOG
import com.example.mvvm_simple.NetworkRequestInfo
import com.example.mvvm_simple.R
import com.example.mvvm_simple.databinding.ActivityReleaseIdleBinding
import com.example.mvvm_simple.databinding.DialogInputBinding
import com.example.mvvm_simple.model.bean.ClassificationBean
import com.example.mvvm_simple.model.bean.LabelBean
import com.example.mvvm_simple.model.bean.PublicGoodsReqBean
import com.example.mvvm_simple.model.models.PublicGoodsModel
import com.example.mvvm_simple.model.models.UploadFileModel
import com.example.mvvm_simple.view.adapter.IdleGoodsAdapter
import com.example.mvvm_simple.view.adapter.StartDragListener
import com.example.mvvm_simple.view.adapter.manager.FullyGridLayoutManager
import com.example.mvvm_simple.viewmodel.ReleaseIdleViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.broadcast.BroadcastAction
import com.luck.picture.lib.broadcast.BroadcastManager
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE
import com.luck.picture.lib.config.PictureConfig.MULTIPLE
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.permissions.PermissionChecker
import com.luck.picture.lib.tools.PictureFileUtils
import kotlinx.android.synthetic.main.activity_release_idle.*
import kotlinx.android.synthetic.main.dialog_input.*
import java.io.File
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.isEmpty as isEmpty1

class ReleaseIdleActivity : MvvmNetworkActivity<ActivityReleaseIdleBinding, ReleaseIdleViewModel>() {

    private var mAdapter: IdleGoodsAdapter? = null
    //每次最多选择数量
    private var mMaxSelectNum = 9
    private var mMaxPicNumber = 9
    private var mNeedScaleBig = true;
    private var mNeedScaleSmall = true
    private var mItemTouchHelper: ItemTouchHelper? = null
    private var mStartDragListener: StartDragListener? = null
    private var saveData: Bundle? = null
    private var categoryId: ClassificationBean.SecondListBean? = null
    private var labelsId: java.util.ArrayList<LabelBean>? = null
    //网络请求返回的图片地址
    private var urls: ArrayList<String> = ArrayList()
    private var completeCount: MutableLiveData<Int> = MutableLiveData(0)
    private lateinit var mProgressDialog: ProgressDialog
    //输入弹出框
    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private lateinit var mBottomSheetBinding: DialogInputBinding
    //记录焦点是否在价格输入框
    private var isLocatePrice = true
    private var mHandler: Handler? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_release_idle;
    }

    override fun getViewModel(): Class<out ViewModel> {
        return ReleaseIdleViewModel::class.java
    }

    /**
     * 当配置发生改变时，记录下adapter中的data
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mAdapter != null && mAdapter?.getData() != null && mAdapter?.getData()?.size!! > 0) {
            outState.putParcelableArrayList("selectorList",
                    mAdapter?.getData() as ArrayList<out Parcelable?>?)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            saveData = savedInstanceState
        } else {
            clearCache()
        }
    }

    /**
     * 清空缓存包括裁剪、压缩、AndroidQToPath所生成的文件，注意调用时机必须是处理完本身的业务逻辑后调用；非强制性
     */
    private fun clearCache() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PictureFileUtils.deleteAllCacheDirFile(this)
        } else {
            PermissionChecker.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    APPLY_STORAGE_PERMISSIONS_CODE)
        }
    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun initParameters() {

    }

    override fun initDataAndView() {
        initDataBinding()
        initAdapter()
        addObserver()
        initBottomSheetDialog()
        //状态栏变化
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarDarkTheme(this, true)
    }

    /**
     * 初始化bottomSheetDialog,用于用户输入信息
     */
    private fun initBottomSheetDialog() {
        mBottomSheetDialog = BottomSheetDialog(this, R.style.dialog_bottom_sheet)
        mBottomSheetBinding = DataBindingUtil.inflate<DialogInputBinding>(LayoutInflater.from(this), R.layout.dialog_input, null, false)
        mBottomSheetDialog.setContentView(mBottomSheetBinding.root)
        mBottomSheetDialog.setCanceledOnTouchOutside(true)
        //过滤一些字符，设置格式，最大值
        mBottomSheetBinding.editPrice.filters = arrayOf(object : InputFilter{

            override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
                //判断是增加数据还是减少
                val isAdd = dstart == dend
                val curString = dest.toString()
                val replaceString = source.toString()
//                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
//                if(replaceString != "." && curString == "0"){
//                    return ""
//                }
                //当只输入一个点时
                if (source!! == "." && curString.isEmpty1()) {
                    return "0."
                }
                //如果以0.开头，则禁止在小数点前添加数据
                if (isAdd && curString.startsWith("0.")) {
                    //防止多个0开头
                    if (dend == 0 && replaceString == "0" || dend == 1) {
                        return ""
                    }
                }
                //静止在前方插入多个0
                if (isAdd && curString.startsWith("0") && replaceString == "0" && dend <= 1) {
                    return ""
                }
                //设置不能超过1亿
                if (isAdd && !curString.isEmpty1() && replaceString != ".") {
                    //计算增加后的值
                    val numString = curString + replaceString
                    if (numString.toDouble() > 10000) {
                        ToastUtil.show(this@ReleaseIdleActivity, "设置的金额无法超过一万")
                        return ""
                    }
                }
                //保持两位小数
                if (isAdd && curString.contains(".")) {
                    val index = curString.indexOf(".")
                    if (dend > index) {
                        val length = curString.substring(index).length
                        if (length == 3) {
                            return ""
                        }
                    }
                }
                return null
            }
        })
        mBottomSheetBinding.editNumber.filters = arrayOf(object : InputFilter{
            override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
                //判断是增加数据还是减少
                val isAdd = dstart == dend
                source?.let {
                    //静止在前方插入多个0
                    if (isAdd && source.startsWith("0")) {
                        val replace = dest.toString()
                        if (dend == 0 && replace == "0" || dend > 0) {
                            return ""
                        }
                    }
                    //保留6位数
                    if (isAdd && source.length >= 6) {
                        return ""
                    }
                }
                return null
            }
        } )
        //监听数据的改变
        mBottomSheetBinding.editPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    //为空时，改变颜色
                    mBottomSheetBinding.txtMoney.setTextColor(resources.getColor(R.color.common_light_gray))
                } else {
                    mBottomSheetBinding.txtMoney.setTextColor(resources.getColor(R.color.black))
                }
                if (s != null) {
                    //静止"."开头
                    if (s.isNotEmpty() && s.startsWith(".")) {
                        val string = StringBuilder(s)
                        mBottomSheetBinding.editPrice.setText(string.replace(0, 1, "0."))
                    }
                }

            }
        })

        /**
         * 确认按钮的点击事件
         */
        mBottomSheetBinding.txtConfirm.setOnClickListener {
            var number = mBottomSheetBinding.editNumber.text.toString()
            var price = mBottomSheetBinding.editPrice.text.toString()
            //排除以小数点结尾的情况
            if (price.contains(".") && price.indexOf(".") == price.length - 1) {
                price = price.replace(".", "")
            }
            if (price != "") {
                //把价格设置为两位数的形式
                if (!price.contains(".")) {
                    price += ".00"
                } else {
                    if (price.indexOf(".") > price.length - 3) {
                        price += "0"
                    }
                }
                mViewModel.number.value = number
            }
            //默认为1
            if (price.isEmpty1() || price == "0") {
                price = "1"
            }
            mViewModel.price.value = price
            mBottomSheetDialog.cancel()
        }

        mBottomSheetDialog.setOnShowListener {
            //将以前的数据代入
            if (mViewModel.price.value != "0.00") {
                mBottomSheetBinding.editPrice.setText(mViewModel.price.value)
            }
            if (!mViewModel.number.value.isNullOrEmpty()) {
                mBottomSheetBinding.editNumber.setText(mViewModel.number.value)
            }
            if (mHandler == null) mHandler = Handler()
            //延迟显示键盘
            mHandler?.postDelayed({
                if (isLocatePrice) {
                    showKeyBoard(mBottomSheetBinding.editPrice)
                } else {
                    showKeyBoard(mBottomSheetBinding.editNumber)
                }
            }, 200)
        }
    }

    /**
     * 初始化adapter
     */
    private fun initAdapter() {
        val manager = FullyGridLayoutManager(this, 3, VERTICAL, false)
        mViewDataBinding.recyclerview.layoutManager = manager
        mAdapter = IdleGoodsAdapter()
        mAdapter?.setOnAddPicClickListener(mOnAddPicClickListener)
        //判断是否恢复数据
        if (saveData != null && saveData?.getParcelableArrayList<LocalMedia>("selectorList") != null) {
            mAdapter?.setList(saveData!!.getParcelableArrayList("selectorList"))
        }
        mAdapter?.setSelectMax(mMaxPicNumber)
        mViewDataBinding.recyclerview.adapter = mAdapter

        //设置点击事件
        mAdapter?.setOnItemClickListener(object : IdleGoodsAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                mAdapter?.let {
                    val list: MutableList<LocalMedia> = it.getData()
                    if (list.size > 0) {
                        val data = list[position]
                        val type = data.mimeType
                        val mediaType = PictureMimeType.getMimeType(type)
                        when(mediaType) {
                            PictureConfig.TYPE_VIDEO-> {
                            }
                            else -> {
                                //预览图片
                                PictureSelector.create(this@ReleaseIdleActivity)
                                        .themeStyle(R.style.picture_default_style)
                                        .isNotPreviewDownload(false)
                                        .loadImageEngine(com.example.mvvm_simple.GlideEngine.createGlideEngine())
                                        .openExternalPreview(position, list)
                            }
                        }
                    }
                }
            }
        })

        mAdapter?.setItemLongClickListener(object : IdleGoodsAdapter.OnItemLongClickListener {
            override fun onItemLongClick(holder: RecyclerView.ViewHolder, position: Int, v: View) {
                //如果item不是最后一个，则执行拖拽
                mNeedScaleBig = true
                mNeedScaleSmall = true
                val size: Int = mAdapter!!.getData().size
                if (size != mMaxPicNumber) {
                    mItemTouchHelper!!.startDrag(holder)
                    return
                }
                if (holder.layoutPosition !== size) {
                    mItemTouchHelper!!.startDrag(holder)
                }
            }
        })

        //设置对item拖动的监听，并且进行相应的操作,暂时没用，后期扩展功能时再用
        mStartDragListener = object : StartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
            }
        }
        //实现拖拽换位
        mItemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {

            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            /**
             * 在这里返回你需要监听的动作
             */
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                if (viewHolder.itemViewType != IdleGoodsAdapter.TYPE_ADD_PIC) {
                    viewHolder.itemView.alpha = 0.7f
                }
                return makeMovementFlags(ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0)
            }

            /**
             * 这里执行item拖拽到另外一个item的位置时候的事件。一般用于交换数据，换位
             */
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                if (target.itemViewType != IdleGoodsAdapter.TYPE_ADD_PIC) {
                    val fromPosition = viewHolder.adapterPosition
                    val toPosition = target.adapterPosition
                    if (fromPosition < toPosition) {
                        for (i in fromPosition until toPosition) {
                            Collections.swap(mAdapter?.getData(), i, i + 1)
                        }
                    } else if (fromPosition > toPosition){
                        for (i in fromPosition downTo toPosition + 1) {
                            Collections.swap(mAdapter?.getData(), i, i - 1)
                        }
                    }
                    mAdapter?.notifyItemMoved(fromPosition, toPosition)
                }
                return true
            }

            /**
             * 监听item左右滑动事件，一般用于左右滑动删除item.这里暂时用不到
             */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            /**
             * 看解释，是当itemView中的OnDraw()调用时才调用的。
             */
            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                if (viewHolder.itemViewType != IdleGoodsAdapter.TYPE_ADD_PIC) {
                    if (mNeedScaleBig) {
                        viewHolder.itemView.animate().scaleXBy(0.1f).scaleYBy(0.1f).setDuration(100);
                        //执行完后不需要再放大了，置为false
                        mNeedScaleSmall = false;
                        mNeedScaleBig = false
                    }
                    if (mNeedScaleSmall) { //需要松手后才能执行
                        viewHolder.itemView.animate().scaleXBy(1f).scaleYBy(1f).duration = 100
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }

            /**
             * 动画完成，即代表松手了
             */
            override fun getAnimationDuration(recyclerView: RecyclerView, animationType: Int, animateDx: Float, animateDy: Float): Long {
                mNeedScaleSmall = true
                return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                if (viewHolder.itemViewType != IdleGoodsAdapter.TYPE_ADD_PIC) {
                    super.clearView(recyclerView, viewHolder)
                    //当拖拉完成后恢复透明度
                    viewHolder.itemView.alpha = 1.0f
                    mAdapter?.notifyDataSetChanged()
                }
            }
        })

        mItemTouchHelper?.attachToRecyclerView(mViewDataBinding.recyclerview)

        //注册外部预览图片删除按钮回调
        BroadcastManager.getInstance(this).registerReceiver(mBroadCastReceiver, BroadcastAction.ACTION_DELETE_PREVIEW_POSITION)
    }


    private val mBroadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            var bundle: Bundle?
            when (intent?.action) {
                BroadcastAction.ACTION_DELETE_PREVIEW_POSITION -> {
                    bundle = intent.extras
                    val position = bundle.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                    if (position < mAdapter!!.getData().size) {
                        mAdapter?.remove(position)
                        mAdapter?.notifyItemRemoved(position)
                    }
                }
            }
        }
    }

    /**
     * 启动图库选择
     */
    private var mOnAddPicClickListener = object : IdleGoodsAdapter.OnAddPicClickListener {
        override fun onAddPic() {
            PictureSelector.create(this@ReleaseIdleActivity)
                    .openGallery(PictureMimeType.ofImage())
                    .theme(R.style.picture_default_style)
                    .isMultipleSkipCrop(true)
                    .isMultipleRecyclerAnimation(true)
                    .loadImageEngine(com.example.mvvm_simple.GlideEngine.createGlideEngine())
                    .maxSelectNum(mMaxSelectNum - mAdapter!!.getData().size)
                    .minSelectNum(1)
                    .imageSpanCount(4)
                    .isReturnEmpty(false)   // 未选择数据时点击按钮是否可以返回
                    .selectionMode(MULTIPLE)
                    .previewImage(true)
                    .isCamera(true)
                    .imageFormat(PictureMimeType.PNG)
                    .isZoomAnim(true)
                    .enableCrop(true)
                    .compress(true)
                    .hideBottomControls(false)
                    .isGif(false)
                    .freeStyleCropEnabled(true)
                    .openClickSound(true)
                    .previewEggs(true)
                    .rotateEnabled(false)
                    .scaleEnabled(true)
                    .isOriginalImageControl(true)   // 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onCancel() {
                        }

                        override fun onResult(result: MutableList<LocalMedia>?) {
                            if (result != null) {
                                val list = ArrayList<LocalMedia>(result)
                                mAdapter?.getData()?.addAll(list)
                                mAdapter?.notifyDataSetChanged()
                            }
                        }

                    })

        }
    }

    /**
     * 在这里监听数据的变化
     */
    private fun addObserver() {
        mViewModel.goodsName.observe(this, Observer<String> {
            if (!it.isNullOrEmpty()) {
                mViewDataBinding.txtPublish.isEnabled = true
                //设置为粗体
                mViewDataBinding.txtPublish.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            } else {
                mViewDataBinding.txtPublish.isEnabled = false
                mViewDataBinding.txtPublish.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            }
        })

        completeCount.observe(this, Observer {
            //当图片全部上传完后，再上传商品信息
            if (completeCount.value == mAdapter!!.getData().size && completeCount.value != 0) {
                completeCount.value = 0
                Log.e("上传", "${completeCount.value}")
                val body: PublicGoodsReqBean = PublicGoodsReqBean()
                //记录下当前数据，并请求保存
                with(body) {
                    name = mViewModel.goodsName.value
                    price = mViewModel.price.value
                    categoryId = this@ReleaseIdleActivity.categoryId?.id
                    images = urls
                    if (mViewModel.number.value != null) {
                        nums = (mViewModel.number.value)?.toInt()
                    }
                    if (labelsId != null) {
                        val ids = ArrayList<Int>()
                        labelsId?.forEach {
                            ids.add(it.id)
                        }
                        labelIds = ids
                    }
                    description = if (mViewModel.goodsDetail.value != null) {
                        mViewModel.goodsDetail.value
                    } else {
                        ""
                    }
                }
                mViewModel.uploadGoods(body)
            } else {
                Log.e("上传", "${completeCount.value}")
            }
        })
    }

    /**
     * 初始化dataBinding的数据
     */
    private fun initDataBinding() {
        mViewDataBinding.eventPresenter = EventPresenter()
        mViewDataBinding.lifecycleOwner = this
        mViewDataBinding.viewModel = mViewModel
    }

    /**
     * 手动控制显示软键盘
     */
    private fun showKeyBoard(editText: EditText) {
        editText.requestFocus()
        val inputManager = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(editText, 0)
        editText.setSelection(editText.text.length)
    }

    inner class EventPresenter {
        fun onClick(v: View) {
            when(v.id) {
                R.id.relayout_price -> {
                    //显示输入框
                    mBottomSheetDialog.show()
                    isLocatePrice = true
                }
                R.id.relayout_number -> {
                    mBottomSheetDialog.show()
                    isLocatePrice = false
                }
                R.id.relayout_more_information -> {
                    if (categoryId != null ) {
                        IdleInformationActivity.actionStart(this@ReleaseIdleActivity, categoryId, labelsId)
                    } else {
                        val intent = Intent(this@ReleaseIdleActivity, IdleInformationActivity::class.java)
                        startActivityForResult(intent, GET_LABELS)
                    }
                }
                R.id.txt_cancel -> {
                    this@ReleaseIdleActivity.finish()
                }
                R.id.txt_publish -> {
                    if (checkEnablePublish()) {
                        mProgressDialog = ProgressDialog(this@ReleaseIdleActivity)
                        mProgressDialog.setProgressStyle(STYLE_SPINNER)
                        mProgressDialog.show()
                        uploadFile()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //解除广播否则会造成内存泄漏
        BroadcastManager.getInstance(this).unregisterReceiver(mBroadCastReceiver, BroadcastAction.ACTION_DELETE_PREVIEW_POSITION)
    }

    private fun uploadFile() {
        val data = mAdapter!!.getData()
        for (item in data) {
            val uri = item.compressPath
            val file = File(uri)
            mViewModel.uploadFile(file)
        }
    }

    /**
     * 判断是否能够发送，是否填写好了必要的数据
     */
    private fun checkEnablePublish(): Boolean {
        if (mViewModel.price.value != "0.00" && categoryId != null && mAdapter?.getData()?.size!! > 0) {
            if (mViewModel.number.value == null) {
                mViewModel.number.value = "1"
            }
            return true
        } else {
            val dialog = SelfDialog(this)
            //弹出dialog提醒用户
            dialog.setMessage("商品图片、价格、分类为必填项，请检查是否填写完整")
            dialog.setYesOnclickListener("确定") {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }
            dialog.setLayout(CONFIRM_DIALOG)
            dialog.show()
            return false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            GET_LABELS -> {
                if (resultCode == Activity.RESULT_OK) {
                    labelsId = data?.getParcelableArrayListExtra("labelsId")
                    categoryId = data?.getParcelableExtra("categoryId")
                    mViewDataBinding.txtMore.text = "${categoryId?.name}"
                    if (labelsId != null && labelsId?.size!! > 0) {
                        mViewDataBinding.txtMore.text = "${categoryId?.name}、${(labelsId as java.util.ArrayList<LabelBean>)[0].name}"
                    }
                }
            }
        }
    }

    override fun onNetDone(key: String?, status: BaseNetworkStatus?) {
        when (key) {
            UploadFileModel.TAG -> {
                urls.add("${NetworkRequestInfo.BASE_URL}${mViewModel.picUrl.value!!}")
                completeCount.setValue(completeCount.value?.plus(1))
            }
            PublicGoodsModel.TAG -> {
                if (mProgressDialog.isShowing) {
                    mProgressDialog.dismiss()
                }
                MainActivity.actionStart(this, categoryId)
                this.finish()
                ToastUtil.show(this, "发布成功")
            }
        }
    }

    companion object {
        const val GET_LABELS = 1
    }

}
