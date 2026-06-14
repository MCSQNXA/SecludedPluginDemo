package mcsq.nxa.secluded.plugin;

import mcsq.nxa.secluded.msg.Messenger;
import mcsq.nxa.secluded.msg.Msg;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author MCSQNXA
 * @CreateTime 2024-08-04 下午11:10
 * @Description 插件
 */
@SuppressWarnings("unused")
public class SecPlugin extends WebSocketClient {


    /**
     * @CreateTime 2024-08-06 13:38:43
     * @Description 发送消息
     */
    public Messenger sendMessenger(Messenger.Builder builder) {
        return this.sendMessenger(null, builder);
    }

    /**
     * @CreateTime 2024-08-04 23:54:45
     * @Description 发送消息
     */
    public Messenger sendMessenger(Messenger patent, Messenger.Builder builder) {
        return this.sendMessenger(patent, builder, true);
    }

    /**
     * @CreateTime 2024-08-04 23:54:45
     * @Description 发送消息
     */
    public Messenger sendMessenger(Messenger patent, Messenger.Builder builder, boolean needRsp) {
        Messenger messenger = new Messenger();

        if (patent != null) {
            messenger.addMsg(Msg.Account, patent.getString(Msg.Account));

            if (patent.hasMsg(Msg.Group)) {//群聊消息
                messenger.addMsg(Msg.Group);
                messenger.addMsg(Msg.GroupId, patent.getString(Msg.GroupId));
            } else if (patent.hasMsg(Msg.Friend)) {//好友消息
                messenger.addMsg(Msg.Friend);
                messenger.addMsg(Msg.Uin, patent.getString(Msg.Uin));
            } else if (patent.hasMsg(Msg.Temp)) {//群聊 临时消息
                messenger.addMsg(Msg.Temp);
                messenger.addMsg(Msg.GroupId, patent.getString(Msg.GroupId));
                messenger.addMsg(Msg.Uin, patent.getString(Msg.Uin));
            } else if (patent.hasMsg(Msg.Guild)) {//频道消息
                messenger.addMsg(Msg.Guild);
                messenger.addMsg(Msg.GuildId, patent.getString(Msg.GuildId));
                messenger.addMsg(Msg.ChannelId, patent.getString(Msg.ChannelId));
            }

            if (patent.getString(Msg.GolineMode).equals(Msg.GM_PO)) {//官方人机 发送消息需要携带 MsgId
                messenger.addMsg(Msg.MsgId, patent.getString(Msg.MsgId));
            }

            if (patent.hasMsg(Msg.Uid)) {
                messenger.addMsg(Msg.Uid, patent.getString(Msg.Uid));
            }


        }

        builder.build(messenger);

        if (!messenger.hasMsg(Msg.Account)) {
            throw new RuntimeException("缺少发送参数 " + Msg.Account);
        }

        return this.sendMessenger(messenger, needRsp);
    }

    /**
     * @CreateTime 2024-08-06 14:01:17
     * @Description 发送消息
     */
    public Messenger sendMessenger(Messenger messenger) {
        return this.sendMessenger(messenger, true);
    }

    /**
     * @CreateTime 2024-08-04 23:41:48
     * @Description 发送消息
     */
    public Messenger sendMessenger(Messenger messenger, boolean needRsp) {
        try {
            JSONObject object = this.sendWss("SendOicqMsg", this.toArray(messenger), needRsp);

            if (needRsp) {
                return this.toMsg(object.getJSONArray("data"));
            }
        } catch (Exception e) {
            printE("[SecPlugin] " + e);
        }

        return new Messenger();
    }

    /**
     * @CreateTime 2024-08-05 17:51:58
     * @Description 发送 WebSocket
     */
    private JSONObject sendWss(String cmd, Object data, boolean needRsp) {
        try {
            int seq = this.nextSeq();

            JSONObject json = new JSONObject();
            json.put("seq", seq);
            json.put("cmd", cmd);
            json.put("rsp", needRsp);

            if (data != null) {
                json.put("data", data);
            }

            printI("==========[SecPlugin]===send==========\n" + json);

            super.send(json.toString());//发送 Websocket 消息

            if (needRsp) {
                for (int i = 0; i < 3000; i++) {//等待应答包
                    SecPlugin.threadSleep(10);

                    synchronized (this.rsp) {
                        if (this.rsp.containsKey(seq)) {
                            JSONObject rsp = this.rsp.get(seq);
                            this.rsp.remove(seq);
                            return rsp;
                        }
                    }
                }
            }
        } catch (Exception e) {
            printE("[SecPlugin] " + e);
        }

        return new JSONObject();
    }

    /**
     * @CreateTime 2024-08-05 17:49:44
     * @Description 应答包
     */
    private final HashMap<Integer, JSONObject> rsp = new HashMap<>();

    /**
     * @CreateTime 2024-08-05 17:54:28
     * @Description 线程休眠
     */
    public static void threadSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @CreateTime 2024-08-06 08:57:11
     * @Description 连接建立
     */
    @Override
    public void onOpen(final ServerHandshake serverHandshake) {
    }

    /**
     * @CreateTime 2024-08-06 12:33:11
     * @Description 连接丢失
     */
    @Override
    public void onClose(final int i, final String s, final boolean b) {
        printE("[SecPlugin] " + s);

        if (this.reconnecting) {
            return;
        }

        //noinspection Convert2Lambda
        SecPlugin.thread(new Runnable() {
            @Override
            public void run() {
                SecPlugin.this.reconnecting = true;

                while (!SecPlugin.super.isOpen()) {
                    try {
                        SecPlugin.threadSleep(3000);

                        if (SecPlugin.super.getReadyState().equals(ReadyState.NOT_YET_CONNECTED)) {
                            SecPlugin.super.connect();
                        } else if (SecPlugin.super.getReadyState().equals(ReadyState.CLOSED)) {
                            SecPlugin.super.reconnect();
                        }
                    } catch (Exception e) {
                        printE("[SecPlugin] " + e);
                    }
                }

                SecPlugin.this.reconnecting = false;
            }
        });
    }

    /**
     * @CreateTime 2026-06-14 20:29:09
     * @Description 发送错误
     */
    @Override
    public void onError(Exception e) {
    }

    /**
     * @CreateTime 2026-06-14 20:28:47
     * @Description 收到消息
     */
    @Override
    public void onMessage(final String s) {
        //noinspection Convert2Lambda
        SecPlugin.thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(s);

                    printS("==========[SecPlugin]===recv==========\n" + json);

                    String cmd = json.getString("cmd");

                    if (cmd.equals("PushOicqMsg")) {//框架 推送过来的消息
                        SecPlugin.this.parsePushOicqMsg(json.getJSONArray("data"));
                    } else if (cmd.equals("Response")) {//收到发送消息的应答包
                        SecPlugin.this.parseResponse(json);
                    }
                } catch (Exception e) {
                    printE("[SecPlugin] " + e);
                }
            }
        });
    }

    /**
     * @CreateTime 2024-08-06 12:54:03
     * @Description 重连中
     */
    private volatile boolean reconnecting = false;

    /**
     * @CreateTime 2024-08-06 00:11:49
     * @Description 解析
     */
    private void parsePushOicqMsg(JSONArray a) {
        this.handler.onMsgHandler(this, this.toMsg(a));
    }

    /**
     * @CreateTime 2024-08-06 09:31:18
     * @Description 转成数组
     */
    private JSONArray toArray(Messenger messenger) {
        JSONArray a = new JSONArray();

        for (int i = 0; i < messenger.getListSize(); i++) {
            JSONObject j = new JSONObject();
            Map<String, String> map = messenger.getList().get(i);

            for (Map.Entry<String, String> entry : map.entrySet()) {
                j.put(entry.getKey(), entry.getValue());
            }

            a.put(j);
        }

        return a;
    }

    /**
     * @CreateTime 2024-08-06 09:29:29
     * @Description 转成消息
     */
    private Messenger toMsg(JSONArray a) {
        Messenger messenger = new Messenger();

        for (int i = 0; i < a.length(); i++) {
            JSONObject o = a.getJSONObject(i);

            for (String k : o.keySet()) {
                messenger.addMsg(k, o.getString(k));
            }
        }

        return messenger;
    }

    /**
     * @CreateTime 2024-08-06 00:12:51
     * @Description 解析
     */
    private void parseResponse(JSONObject json) {
        synchronized (this.rsp) {
            long time = System.currentTimeMillis();

            if (this.rsp.size() > 10) {// 触发过期应答包回收机制
                ArrayList<Integer> list = new ArrayList<>();

                for (Map.Entry<Integer, JSONObject> m : this.rsp.entrySet()) {
                    if (m.getValue().getLong("expire") < time) {
                        list.add(m.getKey());
                    }
                }

                for (Integer i : list) {
                    this.rsp.remove(i);
                }
            }

            json.put("expire", time + 30 * 1000);// 设置数据包过期时间

            this.rsp.put(json.getInt("seq"), json);
        }
    }

    /**
     * @CreateTime 2024-08-05 17:46:16
     * @Description 推送 蓝色日志
     */
    public void pushD(String log) {
        this.sendWss("PrintD", log, false);
    }

    /**
     * @CreateTime 2024-08-05 17:46:16
     * @Description 推送 红色日志
     */
    public void pushE(String log) {
        this.sendWss("PrintE", log, false);
    }

    /**
     * @CreateTime 2024-08-05 17:46:16
     * @Description 推送 白色日志
     */
    public void pushI(String log) {
        this.sendWss("PrintI", log, false);
    }

    /**
     * @CreateTime 2024-08-05 17:46:16
     * @Description 推送 绿色日志
     */
    public void pushS(String log) {
        this.sendWss("PrintS", log, false);
    }

    /**
     * @CreateTime 2024-08-05 17:46:16
     * @Description 推送 黄色日志
     */
    public void pushW(String log) {
        this.sendWss("PrintW", log, false);
    }

    /**
     * @CreateTime 2024-08-06 14:09:30
     * @Description 上次 Ping
     */
    private volatile long last_ping = 0;

    /**
     * @CreateTime 2024-08-06 14:07:21
     * @Description 连接可用
     */
    public boolean ping() {
        if (this.last_ping > System.currentTimeMillis() - 3000) {
            return true;// 3s 内 不用进行 ping 测试
        }

        this.last_ping = System.currentTimeMillis();

        return !this.sendWss("Ping", null, true).isEmpty();
    }

    /**
     * @CreateTime 2024-08-04 23:55:56
     * @Description 序号
     */
    private int seq = 0;

    /**
     * @CreateTime 2024-08-06 00:10:31
     * @Description 锁
     */
    private final Object seq_lock = new Object();

    /**
     * @CreateTime 2024-08-04 23:56:09
     * @Description 序号
     */
    public int nextSeq() {
        synchronized (this.seq_lock) {
            return ++this.seq;
        }
    }

    /**
     * @CreateTime 2024-08-04 23:19:47
     * @Description 停止插件
     */
    public synchronized void stop() {
        if (!this.running) {
            return;
        }

        try {
            super.closeBlocking();
        } catch (InterruptedException e) {
            printE("[SecPlugin] " + e);
        }

        this.running = false;
    }

    /**
     * @CreateTime 2024-08-04 23:22:45
     * @Description 运行中
     */
    private volatile boolean running = false;

    /**
     * @CreateTime 2024-08-04 23:18:07
     * @Description 启动插件
     */
    public synchronized void start() {
        if (this.running) {
            return;
        }

        this.running = true;

        try {
            super.addHeader("Authorization", "Bearer " + this.access_token);// 设置鉴权令牌
            super.connectBlocking();
        } catch (InterruptedException e) {
            printE("[SecPlugin] " + e);
        }
    }

    /**
     * @CreateTime 2024-08-04 23:16:55
     * @Description 处理 对象
     */
    private SecPluginHandler handler;

    /**
     * @CreateTime 2024-08-04 23:17:16
     * @Description 设置 对象
     */
    public void setHandler(SecPluginHandler handler) {
        this.handler = handler;
    }

    /**
     * @CreateTime 2024-08-04 23:15:12
     * @Description 口令
     */
    private String access_token;

    /**
     * @CreateTime 2024-08-04 23:15:25
     * @Description 设置 口令
     */
    public void setAccessToken(String token) {
        this.access_token = token;
    }

    /**
     * @CreateTime 2024-08-04 23:13:34
     * @Description 构造函数
     */
    public SecPlugin(String uri) {
        super(URI.create(uri), new Draft_6455());
        super.setDaemon(true);
    }

    /**
     * @CreateTime 2022-05-28 12:37:58
     * @Description 线程池
     */
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * @CreateTime 2022-05-28 19:52:23
     * @Description 新建子线程
     */
    public static void thread(Runnable runnable) {
        SecPlugin.threadPool.submit(runnable);
    }

    /**
     * @CreateTime 2022-04-10 08:16:46
     * @Description 时间格式
     */
    private static final SimpleDateFormat format = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss:SSS] ", Locale.CHINA);

    /**
     * @CreateTime 2024-07-01 17:46:30
     * @Description 打印 蓝色日志
     */
    public static void printD(String log0) {
        System.out.println("\033[1;94m" + SecPlugin.format.format(System.currentTimeMillis()) + log0 + "\033[0m");
    }

    /**
     * @CreateTime 2024-07-01 17:34:20
     * @Description 打印 红色日志
     */
    public static void printE(String log0) {
        System.out.println("\033[1;91m" + SecPlugin.format.format(System.currentTimeMillis()) + log0 + "\033[0m");
    }

    /**
     * @CreateTime 2024-07-01 17:46:30
     * @Description 打印 白色 日志
     */
    public static void printI(String log0) {
        System.out.println("\033[1;98m" + SecPlugin.format.format(System.currentTimeMillis()) + log0 + "\033[0m");
    }

    /**
     * @CreateTime 2024-07-01 17:45:07
     * @Description 打印 绿色日志
     */
    public static void printS(String log0) {
        System.out.println("\033[1;92m" + SecPlugin.format.format(System.currentTimeMillis()) + log0 + "\033[0m");
    }

    /**
     * @CreateTime 2024-07-01 17:40:59
     * @Description 打印 黄色日志
     */
    public static void printW(String log0) {
        System.out.println("\033[1;93m" + SecPlugin.format.format(System.currentTimeMillis()) + log0 + "\033[0m");
    }


}
