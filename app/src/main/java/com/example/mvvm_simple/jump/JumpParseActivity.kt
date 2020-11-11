package com.example.mvvm_simple.jump

import android.app.TaskStackBuilder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mvvm_simple.goods_detail.view.GoodsDetailActivity
import java.lang.Exception

class JumpParseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent.data
        try {
            if (data != null) {
                val resultIntent = JumpUtils.parseIntent(this, data.toString(), null)
                if (resultIntent == null) {
                    finish()
                    return
                }
                resultIntent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK
                if (ViewUtils.isLaunchedActivity(this, GoodsDetailActivity::class.java)) {
                    startActivity(resultIntent)
                    finish()
                } else {
                    val stackBuilder = TaskStackBuilder.create(this)
                            .addParentStack(resultIntent.component)
                            .addNextIntent(resultIntent)
                    stackBuilder.startActivities()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }
}
