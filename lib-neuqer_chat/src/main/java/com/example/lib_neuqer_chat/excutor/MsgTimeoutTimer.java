package com.example.lib_neuqer_chat.excutor;
import com.example.lib_neuqer_chat.client.IClientInterface;
import com.example.lib_neuqer_chat.client.NQDispatcher;
import com.example.lib_neuqer_chat.config.DefaultConfig;
import com.example.lib_neuqer_chat.message.BaseChatMessage;
import com.example.lib_neuqer_chat.message.Packet;
import com.example.lib_neuqer_chat.util.LiveDataUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Time:2020/3/16 16:27
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:用于消息重发，一条消息
 */
public class MsgTimeoutTimer extends Timer {

    private IClientInterface iClientInterface;
    private int currentResendCount;
    private Packet message;
    private MsgTimeoutTask timeTask;

    public MsgTimeoutTimer(IClientInterface iClientInterface, Packet message) {
        this.iClientInterface = iClientInterface;
        this.message = message;
        timeTask = new MsgTimeoutTask();
    }

    public void startTimeTask() {
        super.schedule(timeTask, iClientInterface.getResentInterval(), iClientInterface.getResentInterval());
    }

    private class MsgTimeoutTask extends TimerTask {

        @Override
        public void run() {
            if (iClientInterface.isClosed()) {
                iClientInterface.getTimeOutManager().removeTimeoutMsg(message.getUuid());
                return;
            }

            currentResendCount++;
            if (currentResendCount > iClientInterface.getReconnectCount()) {
                //超过重发次数，则放弃该消息
                NQDispatcher.getInstance().sendMsgReact(message, DefaultConfig.SendType.SEND_FAILD);
                if (message instanceof BaseChatMessage) {
//                    LiveDataUtil.setValue(((BaseChatMessage) message).getSendTypeLiveData(), DefaultConfig.SendType.SEND_FAILD);
                }
                //移除该消息
                iClientInterface.getTimeOutManager().removeTimeoutMsg(message.getUuid());
                //重新连接
                iClientInterface.reConnect();
                currentResendCount = 0;
            } else {
                NQDispatcher.getInstance().sendMsgReact(message, DefaultConfig.SendType.SENDING);
                sendMessage();
            }
        }

    }

    public void sendMessage() {
        iClientInterface.sendMsg(message, false);
    }

    public Packet getMessage() {
        return message;
    }
}
