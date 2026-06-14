package mcsq.nxa.secluded;

import mcsq.nxa.secluded.msg.Messenger;
import mcsq.nxa.secluded.msg.Msg;
import mcsq.nxa.secluded.plugin.SecPlugin;

import java.util.Random;

/**
 * @Author MCSQNXA
 * @CreateTime 2023-01-10 19:02:59
 * @Description 好友消息实列
 */
@SuppressWarnings("Convert2Lambda")
public class DemoFriendMsg {
    /**
     * @CreateTime 2022-04-05 00:36:28
     * @Description 好友消息
     */
    public static void onMsgHandler(final SecPlugin api, final Messenger messenger) {
        final String uin = messenger.getString(Msg.Uin);
//        final String msgid = messenger.getString(Msg.MsgId);//消息序号
        final String textmsg = messenger.getString(Msg.Text, "");
        final String uinName = messenger.getString(Msg.UinName, "");//昵称

        final String user = uinName + "(" + uin + ")";


        if (textmsg.equals("聊天记录")) {
            final String m1 = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.MultiMsgPut);//构建一条消息
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.UinName, uinName);
                    msg.addMsg(Msg.Text, textmsg);
                }
            }).getString(Msg.MsgId);

            final String m2 = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.MultiMsgPut);//构建一条消息
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.UinName, uinName);
                    msg.addMsg(Msg.Text, "哈哈哈");
                }
            }).getString(Msg.MsgId);

            final Messenger rsp = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);//声明 聊天记录 来源是 好友
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Time, System.currentTimeMillis());//时间戳秒

                    msg.addMsg(Msg.MultiMsg, m1);//添加消息列表
                    msg.addMsg(Msg.MultiMsg, m2);

                }
            });

            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);

                    if (rsp.hasMsg(Msg.Id)) {
                        msg.addMsg(Msg.Xml, "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>");
                        msg.addMsg(Msg.Xml, "<msg serviceID=\"35\" templateID=\"1\" action=\"viewMultiMsg\" brief=\"[聊天记录]\" m_resid=\"", rsp.getString(Msg.Id), "\" m_fileName=\"", rsp.getString(Msg.Name), "\" tSum=\"2\" sourceMsgId=\"0\" url=\"\" flag=\"3\" adverSign=\"0\" multiMsgFlag=\"0\">");
                        msg.addMsg(Msg.Xml, "<item layout=\"1\" advertiser_id=\"0\" aid=\"0\">");
                        msg.addMsg(Msg.Xml, "<title size=\"34\" maxLines=\"2\" lineSpace=\"12\">好友的聊天记录</title>");
                        msg.addMsg(Msg.Xml, "<title size=\"26\" color=\"#777777\" maxLines=\"2\" lineSpace=\"12\">MCSQNXA:  小问题</title>");
                        msg.addMsg(Msg.Xml, "<hr hidden=\"false\" style=\"0\" />");
                        msg.addMsg(Msg.Xml, "<summary size=\"26\" color=\"#777777\">查看多条转发消息</summary>");
                        msg.addMsg(Msg.Xml, "</item>");
                        msg.addMsg(Msg.Xml, "<source name=\"聊天记录\" icon=\"\" action=\"\" appid=\"-1\" />");
                        msg.addMsg(Msg.Xml, "</msg>");
                    } else {
                        msg.addMsg(Msg.Text, "合成失败");
                    }
                }
            });
        }


        if (textmsg.equals("语音")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);//发送好友语音不能设置时长
                    msg.addMsg(Msg.Ptt, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//语音路径
                    msg.addMsg(Msg.ProgressPush, System.currentTimeMillis());//指定上传进度回调id 如果不需要进度回调把这一行去掉就行了
                }
            });
        }

        if (textmsg.matches("猜拳(石头|剪刀|布)")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.FingerGuess, textmsg.replace("猜拳", ""));
                }
            });
        }

        if (messenger.hasMsg(Msg.FingerGuess)) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Text, user + " 发了猜拳 " + messenger.getString(Msg.FingerGuess));
                }
            });
        }

        if (messenger.hasMsg(Msg.ProgressPush)) {//进度推送
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Text, "进度 " + ((long) (((double) messenger.getLong(Msg.Offset) / messenger.getLong(Msg.Size)) * 100)) + "%");
                }
            });
        }

        if (textmsg.matches("视频")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=3337140142&s=140");//视频封面图片
                    msg.addMsg(Msg.Video, "S:/AAA.mp4");//视频源文件
                    msg.addMsg(Msg.Time, 30);//视频时长
                    msg.addMsg(Msg.ProgressPush, System.currentTimeMillis());//指定上传进度回调id 如果不需要进度回调把这一行去掉就行了
                }
            });
        }

        if (textmsg.matches("闪字[0-9]+")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Type, textmsg.replace("闪字", ""));//闪字类型
                    msg.addMsg(Msg.FlashWord, "哈哈哈");//闪字内容
                }
            });
        }

        if (textmsg.equals("闪字")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Type, "2002");//闪字类型
                    msg.addMsg(Msg.FlashWord, "哈哈哈");//闪字内容
                }
            });
        }

        if (messenger.hasMsg(Msg.FlashWord)) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Text, "闪字类型 " + messenger.getString(Msg.Type) + "\n闪字内容 " + messenger.getString(Msg.FlashWord));
                }
            });
        }

        if (textmsg.matches("窗口抖动")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.WindowJitter);
                }
            });
        }

        if (textmsg.matches("骰子[1-6]")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Dice, textmsg.replace("骰子", ""));//点数 1~6
                }
            });
        }

        if (messenger.hasMsg(Msg.Dice)) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Text, user + " 发了骰子 " + messenger.getString(Msg.Dice) + " 点");
                }
            });
        }

        if (messenger.hasMsg(Msg.PokeID)) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Text, "不要戳我啦");
                }
            });
        }

        if (textmsg.equals("gif")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Gif, "S:\\0.gif");
                    msg.addMsg(Msg.Gif, "S:\\0.gif");
                }
            });
        }


        if (messenger.hasMsg(Msg.Withdraw)) {
            final Messenger cache = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Uin, uin);//目标好友 必须
                    msg.addMsg(Msg.FriendMsgCacheGet, messenger.getString(Msg.Withdraw));//flag 必须
                }
            });//获取历史消息

            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Text, user, "撤回了:", cache.toString());
                }
            });
        }


        if (textmsg.equals("菜单")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Text, "文本\n图片\n图文\n闪照\n戳一戳\nXML\nJSON\n视频\n闪字\n骰子+点数\n猜拳+石头/剪刀/布");
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=" + uin + "&s=140");
                }
            });
        }

        if (textmsg.equals("文本")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Text, "OK");
                }
            });
        }

        if (textmsg.equals("图片")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Img, "S:/AAA.png");
                }
            });
        }

        if (textmsg.equals("图文")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Text, "OK");
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=" + uin + "&s=140");
                }
            });
        }

        if (textmsg.equals("闪照")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Flash, "S:/AAA.png");
                }
            });
        }

        if (textmsg.equals("戳一戳")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.PokeID, "1");//戳一戳id
                    msg.addMsg(Msg.PokeIDSub, "0");//子id
                    msg.addMsg(Msg.PokeSize, new Random().nextInt(10));//戳一戳大小
                }
            });
        }

        if (textmsg.equalsIgnoreCase("xml")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
//                    msg.addMsg(Msg.Xml, "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"128\" templateID=\"12345\" action=\"native\" brief=\"[链接]邀请你加入群聊\" sourceMsgId=\"0\" url=\"\"><item layout=\"2\"><picture cover=\"\"/><title>邀请你加入群聊</title><summary /></item><data groupcode=\"799528749\" groupname=\"Welcome to MCSQ\" msgseq=\"1649562980153713\" msgtype=\"2\"/></msg>");
                    msg.addMsg(Msg.Xml, "<?xml version='1.0' encoding='UTF-8' ?><msg serviceID=\"104\" templateID=\"1\" brief=\"大家好，我是群主。水瓶座女一枚~\"><item layout=\"2\"><picture cover=\"\" /><title>新人入群</title></item><source /></msg>");
                }
            });
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Text, "请不要点击“欢迎”可能导致QQ闪退");
                }
            });
        }

        if (textmsg.equalsIgnoreCase("json")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Friend);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.Json, "{\"app\":\"com.tencent.structmsg\",\"config\":{\"ctime\":1647043280,\"forward\":true,\"token\":\"356aa7568f832701cd5047c6dcfc833a\",\"type\":\"normal\"},\"desc\":\"音乐\",\"extra\":{\"app_type\":1,\"appid\":205141,\"msg_seq\":7073997018748087532,\"uin\":3337140142},\"meta\":{\"music\":{\"action\":\"\",\"android_pkg_name\":\"\",\"app_type\":1,\"appid\":205141,\"ctime\":1647043280,\"desc\":\"Alan Walker、Benjamin Ingrosso · World Of…\",\"jumpUrl\":\"https://t1.kugou.com/song.html?id=3ReQpd3zxV2\",\"musicUrl\":\"https://m.kugou.com/api/v1/wechat/index?uuid=7b82aea001d40c5dbddd885ae42ea9a3&album_audio_id=351076529&ext=m4a&apiver=2&cmd=101&album_id=50530456&hash=01c4b2497492d3a2475262b52ad62571&plat=0&version=11123&share_chl=qq_client&mid=3320991661582202751225362500412588691&key=512905974aaff6c1ce23f4052747a0f6&_t=1647043273&user_id=746798753&sign=a0ed201997b0377ed0d7ee7d801f734c\",\"preview\":\"http://imge.kugou.com/stdmusic/120/20211118/20211118203703380169.jpg\",\"sourceMsgId\":\"0\",\"source_icon\":\"https://open.gtimg.cn/open/app_icon/00/20/51/41/205141_100_m.png?t=1639645811\",\"source_url\":\"\",\"tag\":\"酷狗音乐\",\"title\":\"Man On The Moon\",\"uin\":3337140142}},\"prompt\":\"[分享]Man On The Moon\",\"ver\":\"0.0.0.1\",\"view\":\"music\"}");
                }
            });
        }


    }

}
