package com.example.mvvm_simple.categories.mvvm.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.base.view.fragment.MvvmNoRefreshFragment
import com.example.lib_common.linkage_kt.LinkDataController
import com.example.lib_common.linkage_kt.adapter.ContentAdapter
import com.example.lib_common.linkage_kt.adapter.MainAdapter
import com.example.lib_common.linkage_kt.adapter.MainGridAdapter
import com.example.lib_common.linkage_kt.adapter.SideListAdapter
import com.example.lib_common.linkage_kt.data.LinkData
import com.example.lib_common.linkage_kt.data.LinkDataClassifier
import com.example.lib_common.linkage_kt.data.SecondLinkData
import com.example.mvvm_simple.R
import com.example.mvvm_simple.categories.OnTitleChange
import com.example.mvvm_simple.categories.mvvm.model.CategoriesModel
import com.example.mvvm_simple.databinding.FragmentCategoriesBinding
import com.example.mvvm_simple.categories.mvvm.viewmodel.CategoriesViewModel


class CategoriesFragment : MvvmNoRefreshFragment<FragmentCategoriesBinding, CategoriesViewModel>() {

    private lateinit var cateDataObserver: Observer<ArrayList<LinkData>>
    private lateinit var mainAdapter: MainAdapter
    lateinit var testAdapter: MainGridAdapter
    private lateinit var sideAdapter: SideListAdapter

    companion object {
        const val CATEGORY_IDS = "categoryIds"
        const val CATEGORY_NAME = "categoryName"
    }

    override fun getViewModel(): Class<out ViewModel> = CategoriesViewModel::class.java

    override fun onLoadMoreFailure(message: String?) {

    }

    override fun getBindingVariable(): Int = 0

    override fun getLayoutId(): Int = R.layout.fragment_categories

    override fun getFragmentTag(): String = ""

    override fun initParameters() {
    }

    override fun onLoadMoreEmpty() {

    }

    override fun initDataAndView() {

       mViewModel.getCategoriesData()

       cateDataObserver = Observer<ArrayList<LinkData>> {
           values -> values?.let {
           mainAdapter = MainAdapter(LinkDataClassifier.getLinkDataOriginal(it), object : ContentAdapter.OnItemClickListener {
               override fun onCtgItemClick(data: SecondLinkData, position: Int, itemView: View) {
                  transToDetailFragment(ArrayList(data.id), itemView, data.name)
               }

               override fun onMainCtgItemClick(data: LinkData, position: Int, itemView: View) {
                   val idList = ArrayList<Int>()
                   for (i in data.secondList) {
                       idList.add(i.id)
                   }
                  transToDetailFragment(idList, itemView, data.name)
               }
           })
           testAdapter = MainGridAdapter(LinkDataClassifier.getLinkDataOriginal(it), object : ContentAdapter.OnItemClickListener {
               override fun onCtgItemClick(data: SecondLinkData, position: Int, itemView: View) {
                   transToDetailFragment(ArrayList(data.id), itemView, data.name)
               }

               override fun onMainCtgItemClick(data: LinkData, position: Int, itemView: View) {
                   val idList = ArrayList<Int>()
                   for (i in data.secondList) {
                       idList.add(i.id)
                   }
                   transToDetailFragment(idList, itemView, data.name)
               }
           })
           sideAdapter = SideListAdapter(LinkDataClassifier.getSideList(it))
           mViewDataBinding.fragmentCategoriesMainRv.adapter = testAdapter
           mViewDataBinding.fragmentCategoriesSideRv.adapter = sideAdapter

           LinkDataController.setManRV(mViewDataBinding.fragmentCategoriesMainRv)
                   .setSideRV(mViewDataBinding.fragmentCategoriesSideRv, sideAdapter)
                   .setUpTogether()
       }
       }

        (mViewModel.models.get(CategoriesModel.TAG)?.modelLiveData
        as MutableLiveData<ArrayList<LinkData>>).observe(this, cateDataObserver)

    }

    /**
     * 这里好像有逻辑错误，已经改正
     */
    fun transToDetailFragment(list: ArrayList<Int>, itemView: View, name: String) {
        val bundle = Bundle()
        bundle.putIntegerArrayList(CATEGORY_IDS, list)
        bundle.putString(CATEGORY_NAME, name)
        Navigation.findNavController(itemView).navigate(R.id.action_categoriesFragment_to_detailCategoryFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        (activity as OnTitleChange).onTitleChange("全部分类")
    }
}
