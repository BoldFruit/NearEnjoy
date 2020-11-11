package com.example.module_chat.viewmodel;

import com.example.base.viewmodel.MvvmNetworkViewModel;
import com.example.lib_neuqer_chat.client.IReceiver;
import com.example.lib_neuqer_chat.client.NQDispatcher;
import com.example.lib_neuqer_chat.config.DefaultConfig;
import com.example.lib_neuqer_chat.message.BaseChatMessage;
import com.example.lib_neuqer_chat.message.Packet;
import com.example.lib_neuqer_chat.util.LiveDataUtil;
import com.example.module_chat.model.UploadImageRepository;
import com.example.network.exception.ApiException;
import com.example.network.observer.BaseObserver;

import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/3/25 14:19
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatViewModel extends MvvmNetworkViewModel implements IReceiver {

    /**
     * 消息ID以及其对应的Status(发送状态)
     */
    public class IdToStatus {
        String id;
        DefaultConfig.SendType sendStatus;

        IdToStatus(String id, DefaultConfig.SendType sendStatus) {
            this.id = id;
            this.sendStatus = sendStatus;
        }

        public String getId() {
            return id;
        }

        public DefaultConfig.SendType getSendStatus() {
            return sendStatus;
        }
    }

    private MutableLiveData<BaseChatMessage> messageLiveData = new MutableLiveData<>();
    private MutableLiveData<IdToStatus> mutableLiveData = new MutableLiveData<>();

    @Override
    protected void initModels() {
        NQDispatcher.getInstance().register("chatViewModel", this);

    }


    public MutableLiveData<BaseChatMessage> getMessageLiveData() {
        return messageLiveData;
    }

    @Override
    public void onReceive(BaseChatMessage packet) {
       if (packet.getContent() != null) {
           if (packet.getMsgCommand() == Packet.SINGLE_COMMAND) {
               LiveDataUtil.setValue(messageLiveData, packet);
           }
       }
    }

    @Override
    public void onLogin(Packet packet) {

    }

    @Override
    public void sendResult(BaseChatMessage packet, boolean isSuccess) {
//        /**
//         * ！！！！！！！！！！！！记得切换线程
//         */
//        IdToStatus item = new IdToStatus(packet.getUuid(), isSuccess ? DefaultConfig.SendType.SEND_SUCCESS : DefaultConfig.SendType.SEND_FAILD);
//        LiveDataUtil.setValue(mutableLiveData, item);
    }

    @Override
    public void sendResult(BaseChatMessage packet, DefaultConfig.SendType type) {
        IdToStatus item = new IdToStatus(packet.getUuid(), type);
        LiveDataUtil.setValue(mutableLiveData, item);
    }

    public MutableLiveData<IdToStatus> getMutableLiveData() {
        return mutableLiveData;
    }

    public void uploadImage(String path, String uuid) {

    }
}
