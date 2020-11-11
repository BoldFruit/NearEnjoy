package com.example.mvvm_simple.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.base.utils.ToastUtil
import com.example.lib_common.util.statusbar.StatusBarUtil
import com.example.lib_common.widget.flowlayout.FlowLayout
import com.example.lib_common.widget.flowlayout.TagAdapter
import com.example.lib_data.data_user.token.TokenManager
import com.example.mvvm_simple.R
import com.example.mvvm_simple.databinding.ActivityIdleInformationBinding
import com.example.mvvm_simple.model.bean.ClassificationBean
import com.example.mvvm_simple.model.bean.LabelBean
import kotlinx.android.synthetic.main.activity_categories_host.*

class IdleInformationActivity : AppCompatActivity() {
    private lateinit var mViewDataBinding: ActivityIdleInformationBinding
    private var mCategoryId: ClassificationBean.SecondListBean? = null
    private var mInitSelete: ArrayList<LabelBean>? = null
    private var mLabels: HashMap<Int, String>? = null
    private var mTagAdapter: TagAdapter<LabelBean>? = null

    companion object {
        fun actionStart(context: Context, data: ClassificationBean.SecondListBean) {
            val intent = Intent(context, IdleInformationActivity::class.java)
            intent.putExtra("data", data)
            context.startActivity(intent)
        }

        fun actionStart(context: Context, categoryId: ClassificationBean.SecondListBean?, labelsId: ArrayList<LabelBean>?) {
            val intent = Intent(context, IdleInformationActivity::class.java)
            intent.putExtra("categoryId", categoryId)
            intent.putParcelableArrayListExtra("labelsId", labelsId)
            (context as Activity).startActivityForResult(intent, ReleaseIdleActivity.GET_LABELS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_idle_information)
        mViewDataBinding.eventPresenter = EventPresenter()
        initIntent()
        mLabels = TokenManager.getLabels()
        if (mLabels != null) {
            initLabels()
        }
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarDarkTheme(this, true)

    }

    private fun initIntent() {
        //获取选择分类页面的返回值
        val data = intent.getParcelableExtra<ClassificationBean.SecondListBean>("data")
        if (data != null) {
            mViewDataBinding.txtTagName.text = data.name
            mCategoryId = data
        }
        //获取已经设置好的值
        val categoryId = intent.getParcelableExtra<ClassificationBean.SecondListBean>("categoryId")
        if (categoryId != null) {
            mCategoryId = categoryId
            mViewDataBinding.txtTagName.text = categoryId.name
        }
        val labelsId = intent.getParcelableArrayListExtra<LabelBean>("labelsId")
        if (labelsId != null) {
            mInitSelete = labelsId
        }
    }

    /**
     * singleTask重新返回活动时不会调用onCreate，只能在这个方法中获取intent.
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        val data = intent?.getParcelableExtra<ClassificationBean.SecondListBean>("data")
        if (data != null) {
            mViewDataBinding.txtTagName.text = data.name
            mCategoryId = data
        }
    }

    /**
     * 初始化TagFlowLayout
     */
    private fun initLabels() {
        val data = ArrayList<LabelBean>()
        //记录下labelId与adapter中的position与的关系
        val map = HashMap<Int, Int>()
        for ((position, item) in mLabels?.entries!!.withIndex()) {
            val labelBean = LabelBean()
            labelBean.id = item.key
            labelBean.name = item.value
            data.add(labelBean)
            map[item.key] = position
        }
        mTagAdapter = object : TagAdapter<LabelBean>(data) {
            override fun getView(parent: FlowLayout?, position: Int, t: LabelBean?): View {
                val mLayout: LinearLayout = LayoutInflater.from(this@IdleInformationActivity).inflate(R.layout.item_idle_tag, parent, false) as LinearLayout
                val textView: TextView = mLayout.findViewById(R.id.txt_tag_name)
                textView.text = getItem(position).name
                mLayout.findViewById<ImageView>(R.id.img_remove).visibility = View.GONE
                return mLayout
            }

            override fun onSelected(position: Int, view: View?) {
                view?.setBackgroundResource(R.drawable.bg_idle_big_yellow_tag)
                view?.findViewById<ImageView>(R.id.img_remove)?.visibility = View.VISIBLE
            }

            override fun unSelected(position: Int, view: View?) {
                view?.setBackgroundResource(R.drawable.bg_idle_tag_gray)
                view?.findViewById<ImageView>(R.id.img_remove)?.visibility = View.GONE
            }
        }
        mViewDataBinding.tlayoutLabels.adapter = mTagAdapter
        if (mInitSelete != null) {
            var selectSet = HashSet<Int>()
            for (item in mInitSelete!!) {
                selectSet.add(map[item.id]!!)
                mViewDataBinding.tlayoutLabels.adapter.setSelectedList(selectSet)
            }
        }
        mViewDataBinding.tlayoutLabels.setMaxSelectCount(3)
    }

    inner class EventPresenter {
        fun onClick(v : View) {
            when(v.id) {
                R.id.llayout_all -> {
                    startActivity(Intent(this@IdleInformationActivity, FirstClassificationActivity::class.java))
                }
                R.id.txt_cancel -> {
                    this@IdleInformationActivity.finish()
                }
                R.id.txt_confirm -> {
                    if (mCategoryId != null) {
                        val intent = Intent()
                        intent.putExtra("categoryId", mCategoryId)
                        //获取选中的tag的id
                        val selectData = mViewDataBinding.tlayoutLabels.selectedList
                        val labelsId = java.util.ArrayList<LabelBean>()
                        for (item in selectData) {
                            labelsId.add(mTagAdapter?.tagDatas?.get(item)!!)
                        }
                        intent.putParcelableArrayListExtra("labelsId", labelsId)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        ToastUtil.show(this@IdleInformationActivity, "请选择商品的分类")
                    }
                }
            }
        }
    }


}
