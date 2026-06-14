package mcsq.nxa.secluded;

import mcsq.nxa.secluded.msg.Messenger;
import mcsq.nxa.secluded.msg.Msg;
import mcsq.nxa.secluded.plugin.SecPlugin;

/**
 * @Author MCSQNXA
 * @CreateTime 2023-01-10 19:00:47
 * @Description 临时消息实列
 */
public class DemoTempMsg {
    /**
     * @CreateTime 2022-04-05 00:37:08
     * @Description 临时消息
     */
    public static void onMsgHandler(final SecPlugin api, final Messenger messenger) {
        final String uin = messenger.getString(Msg.Uin);//账号
        final String groupid = messenger.getString(Msg.GroupId);//群号
        final String textmsg = messenger.getString(Msg.Text, "");//文本消息
        final String uinName = messenger.getString(Msg.UinName, "");//昵称
        final String groupName = messenger.getString(Msg.GroupName);//群名

//        api.sendMessenger(new Messenger.Builder() {//发送临时消息
//            @Override
//            public void build(Messenger msg) {
//            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
//                msg.addMsg(Msg.Temp);//必须携带
//                msg.addMsg(Msg.GroupId, groupid);//必须携带群号
//                msg.addMsg(Msg.Uin, uin);//目标账号
//                msg.addMsg(Msg.Text, "哈哈哈");
//            }
//        });
        api.sendMessenger(new Messenger.Builder() {//发送临时消息
            @Override
            public void build(Messenger msg) {
                msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                msg.addMsg(Msg.Temp);//必须携带
                msg.addMsg(Msg.GroupId, groupid);//必须携带群号
                msg.addMsg(Msg.Uin, uin);//目标账号
                msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");
            }
        });


    }


}
