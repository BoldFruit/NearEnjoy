package com.example.mvvm_simple.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_common.statusbar.StatusBarUtil
import com.example.mvvm_simple.R
import com.example.mvvm_simple.model.MainRepository
import com.example.mvvm_simple.model.bean.ClassificationBean
import com.example.mvvm_simple.model.bean.LabelBean
import com.example.mvvm_simple.model.bean.SecondClassificationBean
import com.example.mvvm_simple.view.adapter.ClassificationAdapter
import com.example.mvvm_simple.view.paging.PagingObserver
import com.example.network.HttpClient

class FirstClassificationActivity : AppCompatActivity() {

    private lateinit var returnImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_classification)
        MainRepository.INSTANCE.getClassifications(object : PagingObserver<List<ClassificationBean>>() {
            override fun onNext(t: List<ClassificationBean>) {

                val adapter = ClassificationAdapter(context = this@FirstClassificationActivity, data = t)
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                recyclerView.layoutManager = LinearLayoutManager(this@FirstClassificationActivity)
                recyclerView.adapter = adapter
            }
        })
        returnImg = findViewById(R.id.img_return)
        returnImg.setOnClickListener {
            this.finish()
        }
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarDarkTheme(this, true)
    }

}
