package mcsq.nxa.secluded;

import mcsq.nxa.secluded.msg.Messenger;
import mcsq.nxa.secluded.msg.Msg;
import mcsq.nxa.secluded.plugin.SecPlugin;

/**
 * @Author MCSQNXA
 * @CreateTime 2023-01-10 19:05:14
 * @Description 频道消息实列
 */
public class DemoGuildMsg {

    /**
     * @CreateTime 2022-05-01 10:14:20
     * @Description 频道消息
     */
    public static void fun(final SecPlugin api, final Messenger messenger) {
        final String uin = messenger.getString(Msg.Uin);//账号 这里不是QQ账号了 是在频道的账号
        final String msgid = messenger.getString(Msg.MsgId);//消息序号
        final String textmsg = messenger.getString(Msg.Text, "");//文本消息
        final String uinName = messenger.getString(Msg.UinName, "");//昵称

        final String tinyid = messenger.getString(Msg.TinyId);//频道 id 用户
        final String guildid = messenger.getString(Msg.GuildId);//频道 id
        final String channelid = messenger.getString(Msg.ChannelId);//频道 id 子

        final String user = uinName + "(" + uin + ")";
        final String userOp = messenger.getString(Msg.OpName) + "(" + messenger.getString(Msg.Op) + ")";


        if (textmsg.equals("测试")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Guild);//频道消息标识
                    msg.addMsg(Msg.GuildId, guildid);//频道id 必须
                    msg.addMsg(Msg.ChannelId, channelid);//子频道id 必须
                    msg.addMsg(Msg.Text, "OK");
                }
            });
        }


        if (textmsg.matches("移出精华[0-9]+")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.Del, textmsg.replace("移出精华", ""));//删除 精华
                            msg.addMsg(Msg.GuildId, guildid);//频道id 必须
                            msg.addMsg(Msg.ChannelId, channelid);//频道id 必须
                            msg.addMsg(Msg.GuildEssence);//精华消息 必须
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("设置精华")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.Add, msgid);//添加 精华
                            msg.addMsg(Msg.GuildId, guildid);//频道id 必须
                            msg.addMsg(Msg.ChannelId, channelid);//频道id 必须
                            msg.addMsg(Msg.GuildEssence);//精华消息 必须
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("艾特")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.AtUin, uin);//添加艾特账号
                    msg.addMsg(Msg.AtName, uinName);//添加艾特昵称
                    msg.addMsg(Msg.Text, "\nOK\n");
                    msg.addMsg(Msg.AtUin, uin);//添加艾特账号
                    msg.addMsg(Msg.AtName, uinName);//添加艾特昵称
                    msg.addMsg(Msg.Text, "\nOK");
                }
            });
        }

        if (textmsg.equals("踢出")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GuildMemberExit);
                            msg.addMsg(Msg.GuildId, guildid);//频道id 必须
                            msg.addMsg(Msg.Uin, uin);//目标用户id
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("撤回")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.Guild);//频道消息标识
                            msg.addMsg(Msg.GuildId, guildid);//频道id 必须
                            msg.addMsg(Msg.ChannelId, channelid);//频道id 必须
                            msg.addMsg(Msg.Withdraw, msgid);//待撤回的消息id
                        }
                    }));
                }
            });
        }

//        if (messenger.hasMsg(Msg.Withdraw)) {
//            final Messenger cache = api.sendMessenger(new Messenger.Builder() {
//                @Override
//                public void build(Messenger msg) {
//                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
//                    msg.addMsg(Msg.GuildId, guildid);//频道id
//                    msg.addMsg(Msg.ChannelId, channelid);//子频道id
//                    msg.addMsg(Msg.GuildMsgCacheGet, messenger.getString(Msg.Withdraw));//指定消息id
//                }
//            });//获取频道缓存消息
//
//            if (cache.getListSize() < 1) {
//                return;//获取缓存消息失败
//            }
//
//            api.sendMessenger(messenger, new Messenger.Builder() {
//                @Override
//                public void build(Messenger msg) {
//                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
//                    msg.addMsg(Msg.Text, user + " 撤回了 " + cache);
//                }
//            });
//        }

        if (textmsg.equals("回复")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Reply, msgid);//目标消息id
                    msg.addMsg(Msg.Text, "OK");
                }
            });
        }

        if (textmsg.matches("猜拳(石头|剪刀|布)")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.FingerGuess, textmsg.replace("猜拳", ""));
                }
            });
        }

        if (messenger.hasMsg(Msg.FingerGuess)) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Text, user + " 发了猜拳 " + messenger.getString(Msg.FingerGuess));
                }
            });
        }

        if (textmsg.equals("菜单")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Text, "移出精华+消息id\n设置精华\n艾特\n踢出\n撤回\n回复\n视频\n图片\n图文\n骰子+点数\nJSON\n我的消息\n猜拳+石头/剪刀/布");
                }
            });
        }

        if (textmsg.matches("视频")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Img, "S:/AAA.png");//视频封面图片
                    msg.addMsg(Msg.Video, "S:/AAA.mp4");//视频源文件
                    msg.addMsg(Msg.Time, 30);//视频时长
                    msg.addMsg(Msg.ProgressPush, System.currentTimeMillis());//指定上传进度回调id 如果不需要进度回调把这一行去掉就行了
                }
            });
        }

        if (textmsg.equals("图文")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Text, "OK");
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=3337140142&s=140");
                    msg.addMsg(Msg.Text, "OK");
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=3337140142&s=140");
                }
            });
        }

        if (textmsg.equals("图片")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.ProgressPush);
                    msg.addMsg(Msg.Img, "http://api.starrobotwl.com/api/jk.php");
                }
            });
        }

        if (messenger.hasMsg(Msg.ProgressPush)) {//进度推送
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Text, "进度 " +
                                         ((long) (((double) messenger.getLong(Msg.Offset) / messenger.getLong(Msg.Size)) * 100)) +
                                         "%");
                }
            });
        }

        if (textmsg.matches("骰子[1-6]")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Dice, textmsg.replace("骰子", ""));//点数 1~6
                }
            });
        }

        if (messenger.hasMsg(Msg.Dice)) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Text, user + " 发了骰子 " + messenger.getString(Msg.Dice) + " 点");
                }
            });
        }

        if (textmsg.equalsIgnoreCase("json")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Json, "{\"app\":\"com.tencent.structmsg\",\"config\":{\"ctime\":1647043280,\"forward\":true,\"token\":\"356aa7568f832701cd5047c6dcfc833a\",\"type\":\"normal\"},\"desc\":\"音乐\",\"extra\":{\"app_type\":1,\"appid\":205141,\"msg_seq\":7073997018748087532,\"uin\":3337140142},\"meta\":{\"music\":{\"action\":\"\",\"android_pkg_name\":\"\",\"app_type\":1,\"appid\":205141,\"ctime\":1647043280,\"desc\":\"Alan Walker、Benjamin Ingrosso · World Of…\",\"jumpUrl\":\"https://t1.kugou.com/song.html?id=3ReQpd3zxV2\",\"musicUrl\":\"https://m.kugou.com/api/v1/wechat/index?uuid=7b82aea001d40c5dbddd885ae42ea9a3&album_audio_id=351076529&ext=m4a&apiver=2&cmd=101&album_id=50530456&hash=01c4b2497492d3a2475262b52ad62571&plat=0&version=11123&share_chl=qq_client&mid=3320991661582202751225362500412588691&key=512905974aaff6c1ce23f4052747a0f6&_t=1647043273&user_id=746798753&sign=a0ed201997b0377ed0d7ee7d801f734c\",\"preview\":\"http://imge.kugou.com/stdmusic/120/20211118/20211118203703380169.jpg\",\"sourceMsgId\":\"0\",\"source_icon\":\"https://open.gtimg.cn/open/app_icon/00/20/51/41/205141_100_m.png?t=1639645811\",\"source_url\":\"\",\"tag\":\"酷狗音乐\",\"title\":\"Man On The Moon\",\"uin\":3337140142}},\"prompt\":\"[分享]Man On The Moon\",\"ver\":\"0.0.0.1\",\"view\":\"music\"}");
                }
            });
        }

        if (textmsg.equals("我的消息")) {
            api.sendMessenger(messenger, new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Text, messenger.toString().replace("\n", "\\n"));
                }
            });
        }


    }

}
