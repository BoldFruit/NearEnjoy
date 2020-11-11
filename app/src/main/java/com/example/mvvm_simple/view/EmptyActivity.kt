package com.example.mvvm_simple.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arouter.ARouter
import com.example.arouter.Constants

class EmptyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().startActivity(this, Constants.ACTIVITY_SPLASH)
    }
}
