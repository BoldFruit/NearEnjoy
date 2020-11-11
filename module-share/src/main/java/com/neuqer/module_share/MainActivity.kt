package com.neuqer.module_share

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.arouter.Constants
import com.example.fw_annotations.BindPath
import com.example.lib_common.util.DensityUtil
import com.neuqer.module_share.util.ConverterImage
import com.neuqer.module_share.util.QRCodeUtil
import com.neuqer.module_share.util.ShareMessage
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.jar.Manifest

@BindPath(value = Constants.ACTIVITY_SHARE)
class MainActivity : AppCompatActivity() {

    private lateinit var goodsImage: ImageView
    private lateinit var qrCodeImage: ImageView
    private lateinit var shareBtn: Button
    private lateinit var mainContentView: ConstraintLayout
    private lateinit var shareMessage: ShareMessage
    private var share_way = ""
    private var imageUrl = ""
    private var qrCodeUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.share_activity_main)

        watchIntent()

//        imageUrl = "http://qiniu.xingheaoyou.com/8.jpeg"
        goodsImage = findViewById(R.id.share_goods_img)
        shareBtn = findViewById(R.id.share_btn_share)
        qrCodeImage = findViewById(R.id.share_img_qr_code)
        mainContentView = findViewById(R.id.share_main_view)

        val dpToPx = DensityUtil.dpToPx(this, 100)
        //构造二维码
        qrCodeImage.setImageBitmap(QRCodeUtil.createQRCodeBitmap(qrCodeUrl, dpToPx, dpToPx))
        Glide.with(this)
                .load(imageUrl)
                .into(goodsImage)

        //点击分享
        shareBtn.setOnClickListener {

            //请求权限
            val rxPermissions = RxPermissions(this)

            rxPermissions.requestEach(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe {
                        if(it.granted && it.name == android.Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                           shareMessage = ShareMessage(this)
                            when(share_way) {
                               "QQ" -> {
                                   shareMessage.shareImageToQQ("", "", mainContentView)
                               }
                                "微信" -> {
                                    shareMessage.shareImageToWechat("", "", ShareMessage.WECHAT_TYPE_FRIEND, mainContentView)
                                }
                            }

                        }
                    }
        }
    }

    private fun watchIntent() {
        share_way = intent.getStringExtra(Constants.A_KEY_DETAIL_SHARE)
        imageUrl = intent.getStringExtra(Constants.A_KEY_DETAIL_TO_SHARE_IMAGE)
        qrCodeUrl = intent.getStringExtra(Constants.A_KEY_DETAIL_TO_SHARE_URL)
        if (imageUrl.isEmpty()) {
            //TODO:如果图片链接为空，做出相应举措
        }
        if (qrCodeUrl.isEmpty()) {
            //TODO:如果二维码链接为空，做出相应举措
        }

    }

}
