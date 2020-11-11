package com.example.module_chat.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.LinearLayout;
import com.example.base.utils.ToastUtil;
import com.example.base.utils.parse_utils.FileUtils;
import com.example.base.view.activity.MvvmNetworkActivity;
import com.example.lib_neuqer_chat.client.NQChatClient;
import com.example.lib_neuqer_chat.coder.CodecHelper;
import com.example.lib_neuqer_chat.message.ChatMessagePrivatePlus;
import com.example.lib_neuqer_chat_ui.controller.ChatController;
import com.example.lib_neuqer_chat_ui.message.BaseMessage;
import com.example.lib_neuqer_chat_ui.message.ImgMessage;
import com.example.lib_neuqer_chat_ui.message.TxtMessage;
import com.example.module_chat.R;
import com.example.module_chat.adapter.ChatAdapter;
import com.example.module_chat.databinding.ActivityMainBinding;
import com.example.module_chat.engine.GlideEngine;
import com.example.module_chat.viewmodel.ChatViewModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends MvvmNetworkActivity<ActivityMainBinding, ChatViewModel> {

    private ChatAdapter adapter;
    public static final int REQUEST_CODE_CHOOSE_PIC = 23;
    private List<Uri> mSelectedPics = new ArrayList<>();
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Class<? extends ViewModel> getViewModel() {
        return ChatViewModel.class;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    protected void initParameters() {

    }

    @Override
    protected void initDataAndView() {
        initPermission();
        initView();
        initNQChat();
        mViewModel.getMutableLiveData().observe(this, idToStatus ->
                adapter.setStatus(idToStatus.getId(), idToStatus.getSendStatus()));

        mViewModel.getMessageLiveData().observe(this, baseChatMessage -> {
            TxtMessage message = MessageConverter.chatMsg2txtMsg(baseChatMessage);
            adapter.addData(message);
            //每次添加消息后，将RecyclerView滚动到最后一位
            scrollToLast();
        });
    }

    /**
     * 初始化权限
     */
    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case  1001:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    ToastUtil.show(this, "申请失败");
                } else {
                    ToastUtil.show(this, "申请成功");
                }
                break;
            default:
                break;
        }
    }

    private void initView() {
        adapter = new ChatAdapter();
        mViewDataBinding.rvChatList.setAdapter(adapter);
        mViewDataBinding.rvChatList.setLayoutManager(new LinearLayoutManager(this));
        ChatController.with(this, new ChatUIPacket())
                .bindAddLayout(null)
                .bindEmojiLayout((LinearLayout) mViewDataBinding.rlEmotion)
                .bindEmoji(mViewDataBinding.ivEmo)
                .bindPlus(mViewDataBinding.ivAdd)
                .bindEditText(mViewDataBinding.etContent)
                .bindSendButton(mViewDataBinding.btnSend)
                .bindContentLayout(mViewDataBinding.llContent)
                .bindBottomLayout(mViewDataBinding.bottomLayout)
                .initEmojiData(getApplication(),
                        "com.example.module_chat",
                        "emoji.db", "emoji",
                        new String[]{"unicodeInt","_id"})
                .initView();

        mViewDataBinding.btnSend.setOnClickListener(v -> {
           scrollToLast();
//            ImgMessage imgMessage = new ImgMessage(BaseMessage.IMG, "https://i.ibb.co/bbDf9cN/download.jpg");
           sendTxtMessage();
        });


        mViewDataBinding.ivAdd.setOnClickListener(v -> {
            chooseImage();
        });

        addListenerToRV();


    }

    /**
     * 选择图片
     */
    private void chooseImage() {
//        Matisse.from(this)
//                .choose(MimeType.ofAll())
//                .capture(true)
//                .captureStrategy(new CaptureStrategy(true, "com.example.module_chat.fileprovider", "test"))
//                .countable(true)
//                .maxSelectable(9)
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                .thumbnailScale(0.85f)
//                .imageEngine(new MyGlideEngine())
//                .forResult(REQUEST_CODE_CHOOSE_PIC);
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .isCamera(true)
                .maxSelectNum(8)
                .imageSpanCount(3)
                .freeStyleCropEnabled(true)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .enableCrop(true)
                .compress(true)
                .showCropFrame(true)
                .showCropGrid(true)
                .cameraFileName(System.currentTimeMillis() + ".jpg")// 使用相机时保存至本地的文件名称,注意这个只在拍照时可以使用
                .renameCompressFile(System.currentTimeMillis() + ".jpg")// 重命名压缩文件名、 注意这个不要重复，只适用于单张图压缩使用
                .renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 注意这个不要重复，只适用于单张图裁剪使用
                .forResult(PictureConfig.CHOOSE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_CODE_CHOOSE_PIC) {
//                if (data == null) {
//                    ToastUtil.show(this, "打开失败");
//                } else {
//                    sendImg(Matisse.obtainResult(data));
//                    for (Uri uri:
//                         Matisse.obtainResult(data)) {
//
//
//                    }
//                }
//            }
            List<String> compressList = new ArrayList<>();
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media :
                            selectList) {
                        compressList.add(media.getCompressPath());
                        System.out.println("压缩路径："+media.getCompressPath());
                    }
                    if (compressList.size() != 0) {
                        sendImages(compressList);
                    }
                    break;
            }
        }
    }

    /**
     * 对RecyclerView添加滚动监听
     */
    private void addListenerToRV() {
        //对RecyclerView添加滚动监听
        mViewDataBinding.rvChatList
                .addOnLayoutChangeListener
                        ((v, left, top,
                          right, bottom,
                          oldLeft, oldTop,
                          oldRight, oldBottom) -> {
                            //比如当软键盘弹出时，bottom就会小于之前的oldBottom，这时候我们
                            //就需要将list滚动到最后一位
                            mViewDataBinding.rvChatList.post(() -> {
                                int itemCount;
                                if (bottom < oldBottom) {
                                    if ((itemCount = adapter.getItemCount()) > 0) {
                                        mViewDataBinding.rvChatList.smoothScrollToPosition(itemCount - 1);
                                    }
                                }
                            });

                        });
    }

    /**
     * 每次点击发送键，将list滚动到最后一位
     *
     * 每次发送图片时，将list滚动到最后一位
     *
     * note:记得是adapter.getItemCount()而不是adapter.getItemCount() - 1
     * 因为要算上新加上的这个消息的位置
     *
     */
    private void scrollToLast() {
        //**每次点击发送键，将list滚动到最后一位**//
        mViewDataBinding.rvChatList.smoothScrollToPosition(adapter.getItemCount());
    }

    /**
     * 发送文本消息
     */
    private void sendTxtMessage() {
        TxtMessage message = new TxtMessage(BaseMessage.TXT, mViewDataBinding.etContent.getText().toString().trim());
        adapter.addData(message);
        mViewDataBinding.etContent.setText("");
        ChatMessagePrivatePlus<TxtMessage> txtMessageChatMessagePrivatePlus = MessageConverter.txtMsg2chatMsg(message, 20, 38, 222);
        NQChatClient.getInstance().sendMsg(txtMessageChatMessagePrivatePlus);
    }

    private void sendImg(List<Uri> uris) {
        for (Uri uri :
                uris) {
            String url = FileUtils.getFilePathByUri(this, uri);
            System.out.println(url);
            ImgMessage imgMessage = new ImgMessage(BaseMessage.IMG);
            imgMessage.setThumbImgPath(url);
            adapter.addData(imgMessage);
        }

    }

    private void sendImages(List<String> imageList) {
        List<ImgMessage> list = new ArrayList<>();
        for (String path :
                imageList) {
            ImgMessage imgMessage = new ImgMessage(BaseMessage.IMG);
            imgMessage.setThumbImgPath(path);
            list.add(imgMessage);
        }
        adapter.addDatas(list);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        NQDispatcher.getInstance().unRegister("ChatActivity");
    }

    /**
     * 配置基础链
     */
    private void initNQChat() {

//        NQDispatcher.getInstance().register("ChatActivity",this);

        NQChatClient.getInstance().builder().setHeader(0x12345678, (byte) 1, CodecHelper.JSON_ALGORITHM);

        NQChatClient.getInstance().init(new ChatConnectPacket(), "192.168.1.101:8001");
    }

}
