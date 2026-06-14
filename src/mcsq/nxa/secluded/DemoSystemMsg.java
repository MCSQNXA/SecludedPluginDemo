package mcsq.nxa.secluded;

import mcsq.nxa.secluded.msg.Messenger;
import mcsq.nxa.secluded.msg.Msg;
import mcsq.nxa.secluded.plugin.SecPlugin;

/**
 * @Author MCSQNXA
 * @CreateTime 2023-01-10 19:04:19
 * @Description 系统消息实列
 */
public class DemoSystemMsg {
    /**
     * @CreateTime 2022-04-03 12:44:08
     * @Description 系统消息
     */
    public static void onMsgHandler(final SecPlugin api, final Messenger messenger) {
        if (messenger.hasMsg(Msg.FavoriteCard)) {//赞了我的资料卡
            SecPlugin.printI(api.sendMessenger(new Messenger.Builder() {//发送点赞请求
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.FavoriteCard);
                    msg.addMsg(Msg.Uin, messenger.getString(Msg.Uin));//点赞对象
                    msg.addMsg(Msg.Value, messenger.getString(Msg.Info).replace("赞了我的资料卡", "").replace("次", ""));//点赞n次
                }
            }).toString());
        }

        if (messenger.hasMsg(Msg.FriendNotify) && messenger.hasMsg(Msg.Add)) {
            SecPlugin.printI("有人加我好友 " + messenger);

            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.FriendNotify);
                    msg.addMsg(Msg.Uid, messenger.getString(Msg.Uid));
                    msg.addMsg(Msg.Agree);// Agree=同意 Refuse=拒绝
                }
            });
        } else if (messenger.hasMsg(Msg.FriendNotify) && messenger.hasMsg(Msg.Del)) {
            SecPlugin.printE("有人把我删了 " + messenger);
        }


    }

}
