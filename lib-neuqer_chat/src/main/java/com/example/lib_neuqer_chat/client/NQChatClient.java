package com.example.lib_neuqer_chat.client;

import android.app.Application;
import android.util.Log;
import android.util.SparseArray;

import com.example.lib_neuqer_chat.coder.CodecFunction;
import com.example.lib_neuqer_chat.coder.MsgHeader;
import com.example.lib_neuqer_chat.coder.Spliter;
import com.example.lib_neuqer_chat.config.DefaultConfig;
import com.example.lib_neuqer_chat.excutor.ExecutorServiceFactory;
import com.example.lib_neuqer_chat.excutor.MsgTimeOutManager;
import com.example.lib_neuqer_chat.handler.HeartBeatHandler;
import com.example.lib_neuqer_chat.handler.NQEventHandler;
import com.example.lib_neuqer_chat.handler.NQInitializer;
import com.example.lib_neuqer_chat.message.BaseChatMessage;
import com.example.lib_neuqer_chat.message.ChatMessageLogin;
import com.example.lib_neuqer_chat.message.Packet;
import com.example.lib_neuqer_chat.network.NetworkManager;
import com.example.lib_neuqer_chat.util.CheckUtil;
import com.example.lib_neuqer_chat.util.LiveDataUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.MutableLiveData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Time:2020/3/15 14:00
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 *
 *
 *
 *           _____                    _____                    _____                   _______                   _____                    _____
 *          /\    \                  /\    \                  /\    \                 /::\    \                 /\    \                  /\    \
 *         /::\____\                /::\    \                /::\____\               /::::\    \               /::\    \                /::\    \
 *        /::::|   |               /::::\    \              /:::/    /              /::::::\    \             /::::\    \              /::::\    \
 *       /:::::|   |              /::::::\    \            /:::/    /              /::::::::\    \           /::::::\    \            /::::::\    \
 *      /::::::|   |             /:::/\:::\    \          /:::/    /              /:::/~~\:::\    \         /:::/\:::\    \          /:::/\:::\    \
 *     /:::/|::|   |            /:::/__\:::\    \        /:::/    /              /:::/    \:::\    \       /:::/__\:::\    \        /:::/__\:::\    \
 *    /:::/ |::|   |           /::::\   \:::\    \      /:::/    /              /:::/    / \:::\    \     /::::\   \:::\    \      /::::\   \:::\    \
 *   /:::/  |::|   | _____    /::::::\   \:::\    \    /:::/    /      _____   /:::/____/   \:::\____\   /::::::\   \:::\    \    /::::::\   \:::\    \
 *  /:::/   |::|   |/\    \  /:::/\:::\   \:::\    \  /:::/____/      /\    \ |:::|    |     |:::|    | /:::/\:::\   \:::\    \  /:::/\:::\   \:::\____\
 * /:: /    |::|   /::\____\/:::/__\:::\   \:::\____\|:::|    /      /::\____\|:::|____|     |:::|____|/:::/__\:::\   \:::\____\/:::/  \:::\   \:::|    |
 * \::/    /|::|  /:::/    /\:::\   \:::\   \::/    /|:::|____\     /:::/    / \:::\   _\___/:::/    / \:::\   \:::\   \::/    /\::/   |::::\  /:::|____|
 *  \/____/ |::| /:::/    /  \:::\   \:::\   \/____/  \:::\    \   /:::/    /   \:::\ |::| /:::/    /   \:::\   \:::\   \/____/  \/____|:::::\/:::/    /
 *          |::|/:::/    /    \:::\   \:::\    \       \:::\    \ /:::/    /     \:::\|::|/:::/    /     \:::\   \:::\    \            |:::::::::/    /
 *          |::::::/    /      \:::\   \:::\____\       \:::\    /:::/    /       \::::::::::/    /       \:::\   \:::\____\           |::|\::::/    /
 *          |:::::/    /        \:::\   \::/    /        \:::\__/:::/    /         \::::::::/    /         \:::\   \::/    /           |::| \::/____/
 *          |::::/    /          \:::\   \/____/          \::::::::/    /           \::::::/    /           \:::\   \/____/            |::|  ~|
 *          /:::/    /            \:::\    \               \::::::/    /             \::::/____/             \:::\    \                |::|   |
 *         /:::/    /              \:::\____\               \::::/    /               |::|    |               \:::\____\               \::|   |
 *         \::/    /                \::/    /                \::/____/                |::|____|                \::/    /                \:|   |
 *          \/____/                  \/____/                  ~~                       ~~                       \/____/                  \|___|
 *
 *
 */
public class NQChatClient implements IClientInterface {

    //是否使用LiveDataBus进行全局信息广播
    private boolean isInfoGlobal = false;
    private boolean isFirst = true;
    private boolean isClosed = false;
    private boolean isReconnecting = false;
    private MsgHeader msgHeader;
    private CodecMap codecMap;
    private ConfigPacket configPacket;
    private ExecutorServiceFactory loopFactory;
    private EventLoopGroup loopGroup;
    private Bootstrap bootstrap;
    private Channel channel;
    private DefaultConfig.AppStatus appStatus;
    private MsgTimeOutManager timeOutManager;
    private NQWorkShop nqWorkShop;
    private Vector<String> urlList = new Vector<>();
    private MutableLiveData<DefaultConfig.ConnectType> mutableConnectStatus;

    private int heartBeatIntervalF = DefaultConfig.DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND;
    private int heartBeatIntervalB = DefaultConfig.DEFAULT_HEARTBEAT_INTERVAL_BACKGROUND;
    private int reconnectInterval = DefaultConfig.DEFAULT_RECONNECT_DELAY_TIME;
    private int reconnectCount = DefaultConfig.DEFAULT_RECONNECT_COUNT;
    private int resentInterval = DefaultConfig.DEFAULT_RESEND_INTERVAL;
    private int connectTimeout = DefaultConfig.DEFAULT_CONNECT_TIMEOUT;

    private NQChatClient() {
        codecMap = new CodecMap();
        msgHeader = new MsgHeader();
        mutableConnectStatus = new MutableLiveData<>();
        timeOutManager = new MsgTimeOutManager(this);
    }

    private static final class NQChatClientHolder {
        private static final NQChatClient INSTANCE = new NQChatClient();
    }

    public static NQChatClient getInstance() {
        return NQChatClientHolder.INSTANCE;
    }

    /**
     * @param urlList 限定条件为"ip:port"
     * @param configPacket 基础配置包
     */
    @Override
    public void init(Vector<String> urlList, ConfigPacket configPacket) {
        close();
        isClosed = false;
        appStatus = DefaultConfig.AppStatus.FOREGRAOUND;
        isFirst = true;
        this.urlList = urlList;
        this.configPacket = configPacket;
        loopFactory = new ExecutorServiceFactory();
        loopFactory.initReconnectGroup();
        reConnect(true);
    }

    public void init(ConfigPacket packet, String...urls) {
        Vector<String> vector = new Vector<>(Arrays.asList(urls));
        this.init(vector, packet);
    }

    public void initNetWatcher(Application application) {
        if (!(application == null)) {
            NetworkManager.getInstance().init(application);
        } else {
            throw new NullPointerException("application is must be provided when init NetWatcher");
        }
    }

    private void initBootStrap() {
        loopGroup = new NioEventLoopGroup(4);
        bootstrap = new Bootstrap();
        bootstrap.group(loopGroup).channel(NioSocketChannel.class);
        // 设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        //消息立即发送，禁止使用nagle算法
        //https://blog.csdn.net/linuu/article/details/51512606
        bootstrap.option(ChannelOption.TCP_NODELAY, true);

        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getConnectTimeout());
        //设置初始化Channel
        bootstrap.handler(new NQInitializer(this));
    }

    @Override
    public void reConnect() {
        reConnect(false);
    }

    @Override
    public void reConnect(boolean isFirst) {
        if (!isFirst) {
            try {
                Thread.sleep(DefaultConfig.DEFAULT_RECONNECT_DELAY_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!isClosed && !isReconnecting) {
            synchronized (this) {
                if (!isClosed && !isReconnecting) {
                    isReconnecting = true;
                    connectStatusCallback(DefaultConfig.ConnectType.CONNECTING);
                    closeChannel();//先关闭远程通道
                    loopFactory.execReconnectGroup(new ReconnectRunnable(isFirst));
                }
            }
        }

    }

    /**
     * 处理连接状态的改变
     * @param type 连接状态
     */
    private void connectStatusCallback(DefaultConfig.ConnectType type) {
        //转发连接状态
        NQDispatcher.getInstance().connectStatusReact(type);
        //TODO:保存连接状态
//        LiveDataUtil.setValue(mutableConnectStatus, type);
        switch (type) {
            case CONNECTING:
//                Log.d("connecting", "is connecting......");
                break;
            case SUCCESS:
                if (configPacket != null && configPacket.buildLoginMessage() != null) {
                    ChatMessageLogin chatMessageLogin = configPacket.buildLoginMessage();
                    sendMsg(chatMessageLogin);
                } else {
                    throw new NullPointerException("the configPacket or loginMessage is null");
                }
                break;
            case FAILED:

            default:
                break;

        }

    }

    /**
     * 发送消息
     * @param packet 内容
     */
    @Override
    public void sendMsg(Packet packet) {
        sendMsg(packet, true);
    }

    /**
     * 发送消息
     * @param packet 内容
     * @param isJoinToTimerManager 是否加入超时管理
     */
    @Override
    public void sendMsg(Packet packet, boolean isJoinToTimerManager) {
        //TODO:发送消息
        if (packet != null) {
            if (isJoinToTimerManager) {
                timeOutManager.add(packet);
            }
            //设置LiveData，动态记录发送状态
            final boolean isMessage = packet instanceof BaseChatMessage;
            if (isMessage) {
                //TODO:
//                LiveDataUtil.setValue(((BaseChatMessage)packet).getSendTypeLiveData(),
//                        DefaultConfig.SendType.SENDING);
            }
            //通过future来判断消息是否成功写入socket
           if (channel == null) {
               NQDispatcher.getInstance().sendMsgReact(packet, DefaultConfig.SendType.SEND_FAILD);
           } else {
               channel.writeAndFlush(packet).addListener(new GenericFutureListener<Future<? super Void>>() {
                   @Override
                   public void operationComplete(Future<? super Void> future) throws Exception {
                       if (isJoinToTimerManager) {
                           if (!future.isSuccess()) {
                               timeOutManager.startTimerTask(packet.getUuid());
                           } else {
                               NQDispatcher.getInstance().sendMsgReact(packet, DefaultConfig.SendType.SEND_SUCCESS);
                               if (isMessage) {
//                                LiveDataUtil.setValue(((BaseChatMessage)packet).getSendTypeLiveData(),
//                                        DefaultConfig.SendType.SEND_SUCCESS);
                               }
                               timeOutManager.removeTimeoutMsg(packet.getUuid());
                           }
                       }
                   }
               });
           }
        }
    }

    @Override
    public void close() {
        if (isClosed) {
            return;
        }

        isClosed = true;
        if (bootstrap != null) {
            bootstrap.group().shutdownGracefully();
        }

        closeChannel();

        if (loopGroup != null) {
            loopGroup = null;
        }

        if (loopFactory != null) {
            loopFactory.destroyAll();
        }
        isReconnecting = false;
        channel = null;
        bootstrap = null;
    }

    @Override
    public void setAppStatus(DefaultConfig.AppStatus status) {
        this.appStatus = status;
        //TODO: 添加handler
        addHeartBeatHandler();
    }



    @Override
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public ConfigPacket getConfigPacket() {
        return configPacket;
    }

    private void closeChannel() {
        //TODO: 关闭channel
        try {
            if (channel != null) {
                try {
                    removeHandler(HeartBeatHandler.class.getSimpleName());
                    removeHandler(NQEventHandler.class.getSimpleName());
                    removeHandler(IdleStateHandler.class.getSimpleName());
                } finally {
                    try {
                        channel.close();
                    } catch (Exception e) {

                    }

                    try {
                        channel.eventLoop().shutdownGracefully();
                    } catch (Exception ex) {
                    }
                    channel = null;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void removeHandler(String simpleName) {
        try {
            if (channel.pipeline().get(simpleName) != null) {
                channel.pipeline().remove(simpleName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("removeHandler: ", "failed to remove handler");
        }
    }

    public void addHeartBeatHandler() {
        if (channel == null || !channel.isActive() || channel.pipeline() == null) {
            return;
        }

        try {
            removeHandler(IdleStateHandler.class.getSimpleName());
            channel.pipeline().addFirst(IdleStateHandler.class.getSimpleName(),
                    new IdleStateHandler(getHeartBeatInterval() * 3, getHeartBeatInterval(), 0, TimeUnit.MILLISECONDS));

            removeHandler(HeartBeatHandler.class.getSimpleName());
            if (channel.pipeline().get(NQEventHandler.class.getSimpleName()) != null) {
                channel.pipeline().addBefore(NQEventHandler.class.getSimpleName(), HeartBeatHandler.class.getSimpleName(),
                        new HeartBeatHandler(this));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setBuilder(Builder builder) {
        msgHeader.setMagicNumber(builder.magicNumber);
        msgHeader.setVersion(builder.version);
        msgHeader.setAlgorithm(builder.algorithm);
        isInfoGlobal = builder.isInfoGlobal;
        nqWorkShop = builder.nqWorkShop;
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setCodecFunctionEntry(byte key, CodecFunction function) {
        codecMap.addFunctionEntry(key, function);
    }

    public void setCodecClazzEntry(byte key, Class<? extends Packet> clazz) {
        codecMap.addCommandClazzEntry(key, clazz);
    }

    /**
     * Get method for parsing data
     * @param key: The type of parsing algorithm, which is defined by the front engineer and back-ends engineer.
     * @return Get the corresponding parsing method(CodecFunction) stored in the map
     */
    public CodecFunction getCodecFunction(byte key) {
        return codecMap.getFunction(key);
    }

    /**
     * 获得用于解析的目标类
     * @param key：即发送数据包中的command，根据不同的command设计不同的类
     * @return 获得在map存储的command对应类
     * notice: get7PlusCommandClass()这样的命名是有缺陷的（本意是项目默认有6种command，使用者必须使用7或者7+ 作为自定义的command
     * TODO: 在Packet类中设计方法来返回自带的command数量，以此进行计算
     */
    public Class<? extends Packet> get7PlusCommandClass(byte key) {
        return codecMap.getClazz(key);
    }

    public boolean isInfoGlobal() {
        return isInfoGlobal;
    }

    public MutableLiveData<DefaultConfig.ConnectType> getMutableConnectStatus() {
        return mutableConnectStatus;
    }

    /**
     * 根据app的状态不同获得不同的心跳间隔
     * @return
     */
    @Override
    public int getHeartBeatInterval() {
        if (appStatus == DefaultConfig.AppStatus.FOREGRAOUND) {
            return getHeartBeatIntervalF();
        } else {
            return getHeartBeatIntervalB();
        }
    }

    public int getHeartBeatIntervalF() {
        if (configPacket != null && !(configPacket.getHeartIntervalF() == 0)) {
            return configPacket.getHeartIntervalF();
        }
        return heartBeatIntervalF;
    }

    public int getHeartBeatIntervalB() {
        if (configPacket != null && !(configPacket.getHeartIntervalB() == 0)) {
            return configPacket.getHeartIntervalB();
        }
        return heartBeatIntervalB;
    }

    @Override
    public int getReconnectInterval() {
        if (configPacket != null && !(configPacket.getReconnectInterval() == 0)) {
            return configPacket.getReconnectInterval();
        }
        return reconnectInterval;
    }

    @Override
    public int getReconnectCount() {
        if (configPacket != null && !(configPacket.getConnectCount() == 0)) {
            return configPacket.getConnectCount();
        }
        return reconnectCount;
    }

    @Override
    public int getResentInterval() {
        if (configPacket != null && !(configPacket.getResendInterval() == 0)) {
            return configPacket.getResendInterval();
        }
        return resentInterval;
    }

    @Override
    public int getConnectTimeout() {
        if (configPacket != null && !(configPacket.getConnectTimeOut() == 0)) {
            return configPacket.getConnectTimeOut();
        }
        return connectTimeout;
    }

    @Override
    public boolean getNetworkAvailable() {
        if (configPacket != null) {
            return configPacket.isNetAvailable();
        }
        return false;
    }

    @Override
    public MsgTimeOutManager getTimeOutManager() {
        return timeOutManager;
    }

    public ExecutorServiceFactory getLoopFactory() {
        return loopFactory;
    }

    /**
     * 用于转换收到的消息
     * @return
     */
    public NQWorkShop getNqWorkShop() {
        return nqWorkShop;
    }

    public NQChatClient.Builder builder() {
        return new Builder();
    }

    /**
     * 用于Client的基础信息配置
     * */
    public static final class Builder {

        private int magicNumber;
        private byte version;
        private byte algorithm;
        private boolean isInfoGlobal;
        private NQWorkShop nqWorkShop;
        //TODO:是否DEBUG
        private boolean isDebug;

        Builder() {
            this.algorithm = 0;
            this.magicNumber = Spliter.magicNumber;
            this.version = 1;
            this.isInfoGlobal = false;
            this.nqWorkShop = null;
            isDebug = false;
        }

        public Builder setHeader(int magicNumber, byte version, byte algorithm) {
            this.magicNumber = magicNumber;
            this.version = version;
            this.algorithm = algorithm;
            return this;
        }

        public Builder setMagicNumber(int magicNumber) {
            this.magicNumber = magicNumber;
            return this;
        }

        public Builder setCodecAlgorithm(Byte algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public Builder setVersion(byte version) {
            this.version = version;
            return this;
        }

        public Builder setIsInfoGlobal(boolean isGlobal) {
            this.isInfoGlobal = isGlobal;
            return this;
        }

        public Builder addCodecFunctionEntry(byte key, CodecFunction function) {
            NQChatClient.getInstance().setCodecFunctionEntry(key, function);
            return this;
        }

        public Builder addCodecClazzEntry(byte key, Class<? extends Packet> clazz) {
            NQChatClient.getInstance().setCodecClazzEntry(key, clazz);
            return this;
        }

        public Builder setResultConverter(NQWorkShop nqWorkShop) {
            this.nqWorkShop = nqWorkShop;
            return this;
        }

        public void build() {
            NQChatClient.getInstance().setBuilder(this);
        }
    }

    /**
     * 存储不同的解析需求对应的不同解析方法
     */
    public class CodecMap {
        //对HashMap的优化
        private HashMap<Byte, CodecFunction> functionMap;
        private HashMap<Byte, Class<? extends Packet>> commandClazzMap;

        CodecMap() {
            this.functionMap = new HashMap<>();
            this.commandClazzMap = new HashMap<>();
        }

        void addFunctionEntry(byte algorithm, CodecFunction function) {
            functionMap.put(algorithm, function);
        }

        CodecFunction getFunction(byte key) {
            return functionMap.get((int)key);
        }

        void addCommandClazzEntry(byte command, Class<? extends Packet> clazz) {
            commandClazzMap.put(command, clazz);
        }

        Class<? extends Packet> getClazz(byte key) {
            return commandClazzMap.get((int)key);
        }

        public void clearMap() {
            functionMap.clear();
        }
    }

    private class ReconnectRunnable implements Runnable {

        private boolean isFirst;

        ReconnectRunnable(boolean isFirst) {
            this.isFirst = isFirst;
        }

        @Override
        public void run() {
            if (!isFirst) {
                //这个项目将第一次连接也认为是重连
                //但如果不是第一次的话，重连就说明上一次连接失败
                //运行到这一步就可以确认上一次连接失败
                connectStatusCallback(DefaultConfig.ConnectType.FAILED);
            } else {
                isFirst = false;
            }
            try {
                //停止心跳线程
                loopFactory.destroyHeartBeatGroup();
                while (!isClosed) {
                    //如果网络不可用，等两秒钟再重新连接
                    if (!getNetworkAvailable()) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    DefaultConfig.ConnectType resultType = retryConnect();
                    if (DefaultConfig.ConnectType.SUCCESS == resultType) {
                        connectStatusCallback(DefaultConfig.ConnectType.SUCCESS);
                        //连接成功，跳出循环
                        break;
                    }

                    if (resultType == DefaultConfig.ConnectType.FAILED) {
                        connectStatusCallback(DefaultConfig.ConnectType.FAILED);
                        try {
                            //让线程停止一会，再进行重连
                            Thread.sleep(getReconnectInterval());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } finally {
                isReconnecting = false;
            }
        }

        private DefaultConfig.ConnectType retryConnect() {
            if (!isClosed) {
                try {
                   if (bootstrap != null) {
                       bootstrap.group().shutdownGracefully();
                   }
                } finally {
                    bootstrap = null;
                    loopGroup = null;
                }
                initBootStrap();
                return connectServer();
            }
            return DefaultConfig.ConnectType.FAILED;
        }

        private DefaultConfig.ConnectType connectServer() {
            if (urlList == null || urlList.size() == 0) {
                Log.d("connectServer:", "the net address you provide is null");
                return DefaultConfig.ConnectType.FAILED;
            }

            for (String url :
                    urlList) {
                try {
                    String[] tempArray = url.split(":");
                    String host = tempArray[0];
                    int port = Integer.parseInt(tempArray[1]);
                    if (!CheckUtil.ipCheck(host)) {
                        throw new IllegalArgumentException("the ip address you provide is valid.");
                    }
                    connectServerActual(host, port);
                    if (channel != null) {
                        return DefaultConfig.ConnectType.SUCCESS;
                    } else {
                        Thread.sleep(getReconnectInterval());
                    }
                } catch (InterruptedException e) {
                    close();
                    break;
                }
            }

            return DefaultConfig.ConnectType.FAILED;

        }
    }

    /**
     * 2019/3/27 添加对连接失败的监控，失败
     * @param host
     * @param port
     */
    private void connectServerActual(String host, int port) throws InterruptedException {
//        try {
//            ChannelFuture future = bootstrap.connect(host, port).sync();
//            future.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture future) throws Exception {
//                    if (!future.isSuccess()) {
//                        //TODO:回调
//                        handleConnectCaught(future.cause());
//                    }
//                }
//            });
//
//            channel = future.channel();
//            channel.closeFuture().sync();
//        } catch (InterruptedException e) {
//            channel = null;
//            e.printStackTrace();
//            Log.d("connectServerActual", "connect remote failed");
//        } finally {
//            loopGroup.shutdownGracefully().sync();
//        }
        /**
         * 小朋友有很多问号：为啥上面那么写会崩溃？下面就不会？为啥？？？？？？？？？？？？
         */
        try {
            channel = bootstrap.connect(host, port).sync().channel();
        } catch (Exception e) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.println("连接失败");
            channel = null;
        }
    }

    private void handleConnectCaught(Throwable cause) {
        cause.printStackTrace();
    }


}
