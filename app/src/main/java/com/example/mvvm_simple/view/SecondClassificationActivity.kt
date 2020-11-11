package com.example.mvvm_simple.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lib_common.statusbar.StatusBarUtil
import com.example.mvvm_simple.R
import com.example.mvvm_simple.databinding.ActivitySecondClassificationBinding
import com.example.mvvm_simple.model.bean.ClassificationBean
import com.example.mvvm_simple.view.adapter.SecondClassificationAdapter

class SecondClassificationActivity : AppCompatActivity() {
    lateinit var mViewDataBinding: ActivitySecondClassificationBinding

    companion object {
        fun actionStart(context: Context, data: ArrayList<ClassificationBean.SecondListBean>) {
            val intent = Intent(context, SecondClassificationActivity::class.java)
            intent.putParcelableArrayListExtra("data", data)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView<ActivitySecondClassificationBinding>(this, R.layout.activity_second_classification)
        val data = intent.getParcelableArrayListExtra<ClassificationBean.SecondListBean>("data")
        if (data != null) {
            mViewDataBinding.recyclerview.layoutManager = LinearLayoutManager(this)
            mViewDataBinding.recyclerview.adapter = SecondClassificationAdapter(data, this)
        }
        mViewDataBinding.imgReturn.setOnClickListener {
            this.finish()
        }
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarDarkTheme(this, true)
    }
}
