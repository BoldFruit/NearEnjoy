package com.example.mvvm_simple.categories.mvvm.view

import android.content.Intent
import android.os.Bundle
import android.view.animation.Transformation
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.util.StringUtil
import com.example.base.view.fragment.MvvmNetworkFragment
import com.example.mvvm_simple.R
import com.example.mvvm_simple.categories.OnTitleChange
import com.example.mvvm_simple.categories.mvvm.adapter.CategoryPagingAdapter
import com.example.mvvm_simple.categories.mvvm.adapter.CategoryWithFooterAdapter
import com.example.mvvm_simple.categories.mvvm.viewmodel.DetailCategoryViewModel
import com.example.mvvm_simple.databinding.FragmentDetailCategoryBinding
import com.example.mvvm_simple.goods_detail.view.GoodsDetailActivity
import com.example.mvvm_simple.model.bean.FirstClassificationBean
import com.example.mvvm_simple.view.adapter.FirstClassificationAdapter
import java.util.stream.Collector
import javax.xml.transform.Transformer

class DetailCategoryFragment : MvvmNetworkFragment<FragmentDetailCategoryBinding, DetailCategoryViewModel>() {

    lateinit var bundle: Bundle
    var categoryName = ""
    var categoryIds = ArrayList<Int>()
    lateinit var adapter: FirstClassificationAdapter
    lateinit var testAdapter: CategoryWithFooterAdapter
    companion object {
        const val START_GOODS_DETAIL = "start_goods_detail"
    }
    override fun getViewModel(): Class<out ViewModel> = DetailCategoryViewModel::class.java

    override fun onLoadMoreFailure(message: String?) {

    }


    override fun getLayoutId() = R.layout.fragment_detail_category

    override fun getFragmentTag(): String = ""

    override fun initParameters() {
        bundle = this.arguments!!
        categoryName = bundle.getString(CategoriesFragment.CATEGORY_NAME) as String
        categoryIds = bundle.getIntegerArrayList(CategoriesFragment.CATEGORY_IDS) as ArrayList<Int>

    }

    override fun onLoadMoreEmpty() {

    }

    override fun initDataAndView() {
        (this.activity as OnTitleChange).onTitleChange(categoryName)

        var str = ""
        for (i in categoryIds.indices) {
            str += categoryIds[i].toString()
            if (i < categoryIds.size - 1) {
                str += ","
            }
        }

        //如果id为空，添加假数据
        if (str.isEmpty()) {
            str = "1,2,3"
        }

        mViewModel.ids.value = str
        val listing = mViewModel.createData()
        testAdapter = CategoryWithFooterAdapter(object : CategoryWithFooterAdapter.OnGoodsItemClick {
            override fun onClick(goodId: Int) {
                val intent = Intent(activity, GoodsDetailActivity::class.java)
                intent.putExtra(START_GOODS_DETAIL, goodId)
                startActivity(intent)
            }

        })
        adapter = FirstClassificationAdapter(context!!) {
            listing.retry
        }
        mViewDataBinding.detailCategoryRv.adapter = testAdapter
        mViewModel.resultList.observe(this, Observer {
            if (it.size == 0) {
                testAdapter.submitList(it)
            } else {
                testAdapter.submitList(it)
            }

        })
    }

    override fun getBindingVariable(): Int = 0

}
