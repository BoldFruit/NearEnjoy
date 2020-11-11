package com.example.mvvm_simple.categories.mvvm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.mvvm_simple.R
import com.example.mvvm_simple.categories.OnTitleChange
import com.example.mvvm_simple.view.fragment.MarketFragment

class CategoriesHostActivity : AppCompatActivity(), OnTitleChange {
    private lateinit var toolbarTitle: RelativeLayout
    private lateinit var txtTitle: TextView
    private lateinit var titleBackImg: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_host)
        toolbarTitle = findViewById(R.id.categories_toolbar)
        txtTitle = toolbarTitle.findViewById(R.id.app_toolbar_title)
        titleBackImg = toolbarTitle.findViewById(R.id.app_toolbar_back_icon)

        titleBackImg.setOnClickListener {
            findNavController(R.id.categories_host_fragment).navigateUp()
        }
        watchIntent()
    }

    private fun watchIntent() {
        val intArrayExtra = intent.getIntegerArrayListExtra(MarketFragment.MARKET_FRAGMENT_IDS)
        val stringExtra = intent.getStringExtra(MarketFragment.MARKET_FRAGMENT_NAME)
        if (intArrayExtra.isNullOrEmpty() || stringExtra.isNullOrEmpty()) {
//            intArrayExtra.add(1)
//            intArrayExtra.add(2)
//            intArrayExtra.add(3)
            return
        }
        if (intArrayExtra.isNotEmpty() && stringExtra.isNotEmpty()) {
            val bundle = Bundle()
            bundle.putIntegerArrayList(CategoriesFragment.CATEGORY_IDS, intArrayExtra)
            bundle.putString(CategoriesFragment.CATEGORY_NAME, stringExtra)
            findNavController(R.id.categories_host_fragment).navigate(R.id.action_categoriesFragment_to_detailCategoryFragment, bundle)
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.categories_host_fragment).navigateUp()
    override fun onTitleChange(title: String) {
        txtTitle.text = title
    }
}
