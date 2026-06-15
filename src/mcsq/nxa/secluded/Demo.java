package mcsq.nxa.secluded;

import mcsq.nxa.secluded.msg.Messenger;
import mcsq.nxa.secluded.msg.Msg;
import mcsq.nxa.secluded.plugin.SecPlugin;
import mcsq.nxa.secluded.plugin.SecPluginHandler;

/**
 * @Author MCSQNXA
 * @CreateTime 2024-08-04 下午11:04
 * @Description 例子
 */
public class Demo implements SecPluginHandler {

    /**
     * @CreateTime 2024-08-04 23:04:58
     * @Description 主函数
     */
    public static void main(String[] args) {
        SecPlugin.printD("蓝色日志");
        SecPlugin.printE("红色日志");
        SecPlugin.printI("白色日志");
        SecPlugin.printS("绿色日志");
        SecPlugin.printW("黄色日志");

        for (int i = 0; i < 1; i++) {
            //noinspection Convert2Lambda
            SecPlugin.thread(new Runnable() {
                @Override
                public void run() {
                    SecPlugin plugin = new SecPlugin("ws://127.0.0.1:24804/?token=y5s9WERi");// 令牌 方式一
                    plugin.setAccessToken("y5s9WERi");// 令牌 方式二
                    plugin.setHandler(new Demo());
                    plugin.start();// 启动插件
                    //plugin.stop();// 停止插件
                }
            });
        }


    }


    /**
     * @CreateTime 2024-08-04 23:22:05
     * @Description 收到消息
     */
    @Override
    public void onMsgHandler(final SecPlugin api, final Messenger messenger) {


        if (messenger.getString(Msg.Text).equals("6")) {
            //noinspection Convert2Lambda
            SecPlugin.printI("发送消息应答 " + api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Text, "696");//发送内容
                }
            }));
        }

        if (messenger.hasMsg(Msg.Group)) {
            DemoGroupMsg.onMsgHandler(api, messenger);
        } else if (messenger.hasMsg(Msg.Friend)) {
            DemoFriendMsg.onMsgHandler(api, messenger);
        } else if (messenger.hasMsg(Msg.Temp)) {
            DemoTempMsg.onMsgHandler(api, messenger);
        } else if (messenger.hasMsg(Msg.System)) {
            DemoSystemMsg.onMsgHandler(api, messenger);
        }


    }


}
