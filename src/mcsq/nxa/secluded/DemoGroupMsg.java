package mcsq.nxa.secluded;

import mcsq.nxa.secluded.msg.Messenger;
import mcsq.nxa.secluded.msg.Msg;
import mcsq.nxa.secluded.plugin.SecPlugin;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * @Author MCSQNXA
 * @CreateTime 2023-01-10 19:02:03
 * @Description 群聊消息实列
 */
@SuppressWarnings("Convert2Lambda")
public class DemoGroupMsg {
    /**
     * @CreateTime 2022-03-27 14:00:06
     * @Description 群聊消息
     */
    public static void onMsgHandler(final SecPlugin api, final Messenger messenger) {
        final String uin = messenger.getString(Msg.Uin);//账号
        final String title = messenger.getString(Msg.Title);//头衔
        final String msgid = messenger.getString(Msg.MsgId);//消息序号
        final String bubble = messenger.getString(Msg.Bubble);//气泡id
        final String textmsg = messenger.getString(Msg.Text, "");//文本消息
        final String groupid = messenger.getString(Msg.GroupId);//群号
        final String uinName = messenger.getString(Msg.UinName, "");//昵称
        final String groupName = messenger.getString(Msg.GroupName);//群名

        final String user = uinName + "(" + uin + ")";
        final String userOp = messenger.getString(Msg.OpName) + "(" + messenger.getString(Msg.Op) + ")";


        if (textmsg.equals("69")) {
            SecPlugin.printI("发送消息应答 " + api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));// 指定发送账号
                    msg.addMsg(Msg.Group);//定义发送类型
                    msg.addMsg(Msg.GroupId, messenger.getString(Msg.GroupId));// 指定目标群聊
                    msg.addMsg(Msg.Text, "696");//发送内容
                }
            }));
        }

        if (textmsg.equals("图片69")) {
            SecPlugin.printI("发送消息应答 " + api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));// 指定发送账号
                    msg.addMsg(Msg.Group);// 群聊标识
                    msg.addMsg(Msg.GroupId, messenger.getString(Msg.GroupId));// 指定目标群聊
                    msg.addMsg(Msg.Img, "S:/A.jpg");// 路径/链接
                }
            }));
        }

        if (textmsg.equals("图文69")) {
            SecPlugin.printI("发送消息应答 " + api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, messenger.getString(Msg.GroupId));
                    msg.addMsg(Msg.Text, "696");
                    msg.addMsg(Msg.Img, "S:/A.jpg");
                    msg.addMsg(Msg.Text, "ABC");
                }
            }));
        }

        if (messenger.hasMsg(Msg.ProgressPush)) {// 进度推送
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, "进度 " +
                                         ((long) (((double) messenger.getLong(Msg.Offset) / messenger.getLong(Msg.Size)) * 100)) +
                                         " %");
                }
            });
        }

        if (textmsg.equals("语音69")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Ptt, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//语音路径
                    msg.addMsg(Msg.Time, 60);//语音时长
                    msg.addMsg(Msg.SilkEncode);// 需要语音编码
//                    msg.addMsg(Msg.Value, "sss");//语音评级 'a' 'b' 'c' 's' 'ss' 'sss' ...
                    msg.addMsg(Msg.ProgressPush, System.currentTimeMillis());//指定上传进度回调id 如果不需要进度回调把这一行去掉就行了
                }
            });
        }

        if (textmsg.matches("视频69")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=3337140142&s=140");//视频封面图片
                    msg.addMsg(Msg.Video, "S:/AAA.mp4");//视频源文件
                    msg.addMsg(Msg.Time, 30);//视频时长
                    msg.addMsg(Msg.ProgressPush, System.currentTimeMillis());//指定上传进度回调id 如果不需要进度回调把这一行去掉就行了
                    msg.addMsg(Msg.Name, "Sorry");//如果视频文件大小超过短视频限制 则启用上传群文件功能 这里指定文件名称
                }
            });
        }

        if (textmsg.equals("换头像")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.UserInfoModify);
                            msg.addMsg(Msg.HeadPortrait, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");
                        }
                    }));
                }
            });
        }


        if (textmsg.equals("聊天记录")) {
            final String m1 = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.MultiMsgPut);//构建一条消息
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.UinName, uinName);
                    msg.addMsg(Msg.Text, "@QQ小冰 ~嗷呜~\u2067\u202D 好喜欢你的声音");
                }
            }).getString(Msg.MsgId);

            final String m2 = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.MultiMsgPut);//构建一条消息
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.UinName, uinName);
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=3337140142&s=140");
                }
            }).getString(Msg.MsgId);

            final String m3 = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.MultiMsgPut);//构建一条消息
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.UinName, uinName);
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=3337140142&s=140");
                    msg.addMsg(Msg.Text, "@QQ小冰 ~嗷呜~\u2067\u202D 好喜欢你的声音");
                }
            }).getString(Msg.MsgId);

            final String m5 = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.MultiMsgPut);//构建一条消息
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.UinName, uinName);
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=3337140142&s=140");//视频封面图片  可以不携带
                    msg.addMsg(Msg.Video, "S:/AAA.mp4");//视频源文件
                    msg.addMsg(Msg.Time, 30);//视频时长

                    //msg.addMsg(Msg.Bubble);//泡泡消息 不支持 手表


                }
            }).getString(Msg.MsgId);

            final String m6 = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.MultiMsgPut);//构建一条消息
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Uin, uin);
                    msg.addMsg(Msg.UinName, uinName);
                    msg.addMsg(Msg.Xml, "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"35\" templateID=\"1\" action=\"viewMultiMsg\" brief=\"[聊天记录]\" m_resid=\"M/QnGVtiTzYVkPcD3BnkuGNBrBE3qCnG/3cAbCfJbUL2eV92QUX1G9xRZuzxzCGZ\" m_fileName=\"7192596872910932198\" tSum=\"5\" sourceMsgId=\"0\" url=\"\" flag=\"3\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"1\" advertiser_id=\"0\" aid=\"0\"><title size=\"34\" maxLines=\"2\" lineSpace=\"12\">群聊的聊天记录</title><title size=\"26\" color=\"#777777\" maxLines=\"2\" lineSpace=\"12\">MCSQNXA:  聊天记录</title><title size=\"26\" color=\"#777777\" maxLines=\"2\" lineSpace=\"12\">MCSQ海沙:  [聊天记录]</title><hr hidden=\"false\" style=\"0\" /><summary size=\"26\" color=\"#777777\">查看2条转发消息</summary></item><source name=\"聊天记录\" icon=\"\" action=\"\" appid=\"-1\" /></msg>");
                }
            }).getString(Msg.MsgId);

            final Messenger rsp = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);//声明 聊天记录 来源是 群聊
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Time, System.currentTimeMillis() / 1000);//时间戳秒

                    msg.addMsg(Msg.MultiMsg, m1);//添加消息元素
                    msg.addMsg(Msg.MultiMsg, m2);
                    msg.addMsg(Msg.MultiMsg, m3);
                    msg.addMsg(Msg.MultiMsg, m5);
                    msg.addMsg(Msg.MultiMsg, m6);

                }
            });//构建 聊天记录

            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);

                    if (rsp.hasMsg(Msg.Id)) {
                        msg.addMsg(Msg.Xml, "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>");
                        msg.addMsg(Msg.Xml, "<msg serviceID=\"35\" templateID=\"1\" action=\"viewMultiMsg\" brief=\"[聊天记录]\" m_resid=\"", rsp.getString(Msg.Id), "\" m_fileName=\"", rsp.getString(Msg.Name), "\" tSum=\"2\" sourceMsgId=\"0\" url=\"\" flag=\"3\" adverSign=\"0\" multiMsgFlag=\"0\">");
                        msg.addMsg(Msg.Xml, "<item layout=\"1\" advertiser_id=\"0\" aid=\"0\">");
                        msg.addMsg(Msg.Xml, "<title size=\"34\" maxLines=\"2\" lineSpace=\"12\">群聊的聊天记录</title>");
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


        if (textmsg.equals("打卡")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupClockin, groupid);//指定群号
                        }
                    }));
                }
            });
        }

        if (textmsg.matches("获取文件下载链接.*")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupFile);
                            msg.addMsg(Msg.Url);
                            msg.addMsg(Msg.Get);
                            msg.addMsg(Msg.GroupId, groupid);
                            msg.addMsg(Msg.Id, textmsg.replace("获取文件下载链接", ""));//文件别名
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("删除文件夹")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupId, groupid);
                            msg.addMsg(Msg.GroupFileRemoveFolder, "/d5b48a10-6c37-442a-94ea-9f6f2f9e13f2");//文件夹id
                        }
                    }).hasMsg(Msg.Ok) ? "删除成功" : "删除失败");
                }
            });
        }

        if (textmsg.equals("创建文件夹")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupId, groupid);
                            msg.addMsg(Msg.GroupFileCreate, "我是文件夹");//文件夹名称
                        }
                    }).hasMsg(Msg.Ok) ? "创建成功" : "创建失败");
                }
            });
        }

        if (textmsg.equals("重命名文件夹")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupId, groupid);
                            msg.addMsg(Msg.GroupFile, "/42bc0957-b60d-4f36-96b7-0c683e149403");//文件夹id
                            msg.addMsg(Msg.GroupFileRename, "我是文件夹");//新文件夹名称
                        }
                    }).hasMsg(Msg.Ok) ? "重命名成功" : "重命名失败");
                }
            });
        }

        if (textmsg.equals("移动文件")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupId, groupid);
                            msg.addMsg(Msg.GroupFile, "/");//目标文件的 父文件夹id   指定 子文件夹 这里填 别名   看 群文件列表 文件夹的别名   根目录别名就是 /
                            msg.addMsg(Msg.GroupFileMove, "/cef60aa0-9439-11ed-bd31-525400834a1a");//目标文件id
                            msg.addMsg(Msg.Id, "/e2082523-dc2e-42b1-8c04-34eae202a525");//新文件夹的id   如果从文件夹移到根目录这里就是填 /
                        }
                    }).hasMsg(Msg.Ok) ? "移动成功" : "移动失败");
                }
            });
        }

        if (textmsg.equals("删除文件")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupId, groupid);
                            msg.addMsg(Msg.GroupFile, "/");//指定 子文件夹 这里填 别名   看 群文件列表 文件夹的别名   根目录别名就是 /
                            msg.addMsg(Msg.GroupFileRemove, "/57dbc8f1-174d-41af-8048-08196d4b420e");//目标文件id
                        }
                    }).hasMsg(Msg.Ok) ? "删除成功" : "删除失败");
                }
            });
        }

        if (textmsg.equals("上传文件")) {
            final Messenger rsp = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.GroupFile, "/");//指定 子文件夹 这里填 别名   看 群文件列表 文件夹的别名   根目录别名就是 /
                    msg.addMsg(Msg.Name, "大漂亮.zip");//文件名称
                    msg.addMsg(Msg.GroupFileUpload, "C:\\FFOutput\\A.zip");//文件路径
                    msg.addMsg(Msg.ProgressPush, System.currentTimeMillis());//上传进度回调
                }
            });

            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);

                    if (rsp.hasMsg(Msg.No)) {
                        msg.addMsg(Msg.Text, "上传失败 " + rsp.getString(Msg.No));
                    } else {
                        msg.addMsg(Msg.Text, "上传成功 新文件id " + rsp.getString(Msg.Id));
                    }
                }
            });
        }

        if (textmsg.equals("群文件列表")) {
            Messenger list = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupFileListGet, "/");//指定 子文件夹 这里填 别名   看 群文件列表 文件夹的别名   根目录就是 /
                    msg.addMsg(Msg.GroupId, groupid);
                }
            });

            if (list.hasMsg(Msg.No)) {
                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, groupid);
                        msg.addMsg(Msg.Text, "获取失败");
                    }
                });
            } else {
                ArrayList<String> type = list.getList(Msg.Type);
                ArrayList<String> id = list.getList(Msg.Id);
                ArrayList<String> name = list.getList(Msg.Name);
                ArrayList<String> size = list.getList(Msg.Size);
                ArrayList<String> value = list.getList(Msg.Value);

                final StringBuilder builder = new StringBuilder();

                for (int i = 0; i < type.size(); i++) {
                    if (type.get(i).equals("1")) {//文件
                        builder.append("类型 文件\n");
                        builder.append("名称 ").append(name.get(i)).append("\n");
                        builder.append("大小 ").append(size.get(i)).append(" 字节\n");
                        builder.append("下载 ").append(value.get(i)).append(" 次\n");
                        builder.append("别名 ").append(id.get(i));
                    } else if (type.get(i).equals("2")) {//文件夹
                        builder.append("类型 文件夹\n");
                        builder.append("名称 ").append(name.get(i)).append("\n");
                        builder.append("别名 ").append(id.get(i));
                    }

                    if (i + 1 < type.size()) {
                        builder.append("\n\n");
                    }
                }

                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, groupid);
                        msg.addMsg(Msg.Text, builder);
                    }
                });
            }
        } else if (textmsg.matches("群文件列表.*")) {
            Messenger list = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupFileListGet, textmsg.replace("群文件列表", ""));
                    msg.addMsg(Msg.GroupId, groupid);
                }
            });

            if (list.hasMsg(Msg.No)) {
                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, groupid);
                        msg.addMsg(Msg.Text, "获取失败");
                    }
                });
            } else {
                ArrayList<String> type = list.getList(Msg.Type);
                ArrayList<String> id = list.getList(Msg.Id);
                ArrayList<String> name = list.getList(Msg.Name);
                ArrayList<String> size = list.getList(Msg.Size);
                ArrayList<String> value = list.getList(Msg.Value);

                final StringBuilder builder = new StringBuilder();

                for (int i = 0; i < type.size(); i++) {
                    if (type.get(i).equals("1")) {//文件
                        builder.append("类型 文件\n");
                        builder.append("名称 ").append(name.get(i)).append("\n");
                        builder.append("大小 ").append(size.get(i)).append(" 字节\n");
                        builder.append("下载 ").append(value.get(i)).append(" 次\n");
                        builder.append("别名 ").append(id.get(i));
                    } else if (type.get(i).equals("2")) {//文件夹
                        builder.append("类型 文件夹\n");
                        builder.append("名称 ").append(name.get(i)).append("\n");
                        builder.append("别名 ").append(id.get(i));
                    }

                    if (i + 1 < type.size()) {
                        builder.append("\n\n");
                    }
                }

                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, groupid);
                        msg.addMsg(Msg.Text, builder);
                    }
                });
            }
        }

        if (textmsg.matches("猜拳(石头|剪刀|布)")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.FingerGuess, textmsg.replace("猜拳", ""));
                }
            });
        }

        if (messenger.hasMsg(Msg.FingerGuess)) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, user + " 发了猜拳 " + messenger.getString(Msg.FingerGuess));
                }
            });
        }

        if (textmsg.matches("闪字[0-9]+")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
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
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
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
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, "闪字类型 " + messenger.getString(Msg.Type) + "\n闪字内容 " + messenger.getString(Msg.FlashWord));
                }
            });
        }

        if (textmsg.matches("窗口抖动")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.WindowJitter);
                }
            });
        }

        if (textmsg.matches("骰子[1-6]")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Dice, textmsg.replace("骰子", ""));//点数 1~6
                }
            });
        }

        if (messenger.hasMsg(Msg.Dice)) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, user + " 发了骰子 " + messenger.getString(Msg.Dice) + " 点");
                }
            });
        }

        if (messenger.hasMsg(Msg.PokeID)) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, user + " 发了戳一戳 " + messenger);
                }
            });
        }

        if (textmsg.equals("戳一戳")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.PokeID, "1");//戳一戳id
                    msg.addMsg(Msg.PokeIDSub, "0");//子id
                    msg.addMsg(Msg.PokeSize, new Random().nextInt(10));//戳一戳大小
                }
            });
        }

        if (textmsg.matches("解散群聊[0-9]+")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupDissolut);
                            msg.addMsg(Msg.GroupId, textmsg.replace("解散群聊", ""));
                        }
                    }));
                }
            });
        }

        if (textmsg.matches("设置精华[0-9]+")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupEssence);
                            msg.addMsg(Msg.GroupId, groupid);//群号
                            msg.addMsg(Msg.Add, textmsg.replace("设置精华", ""));//消息id
                        }
                    }));
                }
            });
        }

        if (textmsg.matches("移出精华[0-9]+")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupEssence);
                            msg.addMsg(Msg.GroupId, groupid);//群号
                            msg.addMsg(Msg.Del, textmsg.replace("移出精华", ""));//消息id
                        }
                    }));
                }
            });
        }


        if (textmsg.equals("拍一拍我")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupBeatABeat);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Uin, uin);
                }
            });
        }

        if (messenger.hasMsg(Msg.GroupBeatABeat)) {
            if (uin.equals(messenger.getString(Msg.Account))) {
                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        String[] rands = {"噢~", "别拍~", "别拍了."};

                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, groupid);
                        msg.addMsg(Msg.Text, rands[new Random().nextInt(rands.length)]);
                    }
                });
            } else {
                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, groupid);
                        msg.addMsg(Msg.Text, userOp + " " + messenger.getString(Msg.GroupBeatABeat) + " " + user);
                    }
                });
            }
        }


        if (textmsg.equals("咪咕音乐")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_MG);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                    msg.addMsg(Msg.Audio, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//音频链接
                }
            });
        }

        if (textmsg.equals("咪咕分享")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_MG);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                }
            });
        }

        if (textmsg.equals("快手音乐")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_KS);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                    msg.addMsg(Msg.Audio, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//音频链接
                }
            });
        }

        if (textmsg.equals("快手分享")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_KS);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                }
            });
        }

        if (textmsg.equals("哔哩哔哩音乐")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_BL);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                    msg.addMsg(Msg.Audio, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//音频链接
                }
            });
        }

        if (textmsg.equals("哔哩哔哩分享")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_BL);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                }
            });
        }

        if (textmsg.equals("波点音乐")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_BD);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                    msg.addMsg(Msg.Audio, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//音频链接
                }
            });
        }

        if (textmsg.equals("波点音乐分享")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_BD);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                }
            });
        }


        if (textmsg.equals("爱奇艺音乐")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_IQY);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                    msg.addMsg(Msg.Audio, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//音频链接
                }
            });
        }

        if (textmsg.equals("爱奇艺分享")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_IQY);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                }
            });
        }

        if (textmsg.equals("优酷音乐")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_YK);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                    msg.addMsg(Msg.Audio, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//音频链接
                }
            });
        }

        if (textmsg.equals("优酷分享")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_YK);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                }
            });
        }


        if (textmsg.equals("百度音乐")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_BAIDU);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                    msg.addMsg(Msg.Audio, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//音频链接
                }
            });
        }

        if (textmsg.equals("百度分享")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_BAIDU);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                }
            });
        }

        if (textmsg.equals("gif")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Gif, "S:\\0.gif");
                }
            });
        }


        if (textmsg.equals("我的信息")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupMemberListGetInfo);
                            msg.addMsg(Msg.GroupId, groupid);
                            msg.addMsg(Msg.Uin, uin);
                        }
                    }));
                }
            });
        }


        if (textmsg.equals("简书音乐")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_JSHU);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                    msg.addMsg(Msg.Audio, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//音频链接
                }
            });
        }

        if (textmsg.equals("简书分享")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_JSHU);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                }
            });
        }

        if (textmsg.equals("酷狗音乐")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_KG);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                    msg.addMsg(Msg.Audio, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//音频链接
                }
            });
        }

        if (textmsg.equals("酷狗音乐分享")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_KG);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                }
            });
        }

        if (textmsg.equals("网易云音乐")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_WY);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                    msg.addMsg(Msg.Audio, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//音频链接
                }
            });
        }

        if (textmsg.equals("网易云音乐分享")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_WY);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                }
            });
        }

        if (textmsg.equals("QQ音乐")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_QQ);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                    msg.addMsg(Msg.Audio, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//音频链接
                }
            });
        }

        if (textmsg.equals("QQ音乐分享")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_QQ);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                }
            });
        }

        if (textmsg.equals("酷我音乐")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_KW);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                    msg.addMsg(Msg.Audio, "https://m.kugou.com/api/v1/wechat/index?uuid=574d1101a331614d56f05f232dbb330d&album_audio_id=106863208&ext=m4a&apiver=2&cmd=101&album_id=8448864&hash=9d28faa0941c2e12c00dc4f8d20a7730&plat=0&version=11309&share_chl=qq_client&mid=304000542424130980213570746762493114069&key=8be2f563b18b4a8483ddc4ef4c4cd432&_t=1663479934&user_id=746798753&sign=c141a9e2a33a3215ea69346e7b633c70");//音频链接
                }
            });
        }

        if (textmsg.equals("酷我音乐分享")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                    msg.addMsg(Msg.CustomJson);//自定义JSON
                    msg.addMsg(Msg.JSON_KW);//JSON类型
                    msg.addMsg(Msg.Title, "我是标题");//标题
                    msg.addMsg(Msg.Info, "我是简介");//简介
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//图片
                    msg.addMsg(Msg.Url, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");//跳转链接
                }
            });
        }

        if (textmsg.equals("获取被禁用群聊列表")) {//这个就是主程序 群聊管理 被关闭的群聊列表
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupListDisable);
                            msg.addMsg(Msg.Get);
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("获取被禁用好友列表")) {//这个就是主程序 好友管理 被关闭的好友列表
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.FriendListDisable);
                            msg.addMsg(Msg.Get);
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("获取被禁言成员列表")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupMemberListGetProhibit);
                            msg.addMsg(Msg.GroupId, groupid);
                        }
                    }));
                }
            });
        }

        if (messenger.hasMsg(Msg.Video)) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, messenger.toString());
                }
            });
        }

        if (messenger.hasMsg(Msg.GroupMemberJoin)) {//新成员加入
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);

                    if (messenger.hasMsg(Msg.Op)) {
                        msg.addMsg(Msg.Text, userOp, "邀请了", user, "加入了本群");
                    } else {
                        msg.addMsg(Msg.Text, user, "加入了本群");
                    }
                }
            });
        }

        if (messenger.hasMsg(Msg.GroupMemberExit)) {
            if (messenger.hasMsg(Msg.Op)) {
                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, messenger.getString(Msg.GroupId));
                        msg.addMsg(Msg.Text, "[" +
                                             messenger.getString(Msg.UinName) +
                                             "][" + messenger.getString(Msg.Uin) + "]被[" +
                                             messenger.getString(Msg.OpName) + "][" + messenger.getString(Msg.Op) + "]踢出群聊");
                    }
                });
            } else {
                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, messenger.getString(Msg.GroupId));
                        msg.addMsg(Msg.Text, "[" + messenger.getString(Msg.UinName) + "][" + messenger.getString(Msg.Uin) + "]退出了本群");
                    }
                });
            }
        }

        if (messenger.hasMsg(Msg.GroupModifyAdmin)) {//群聊管理员升降
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, messenger.getString(Msg.GroupId));

                    if (messenger.hasMsg(Msg.Add)) {
                        msg.addMsg(Msg.Text, "[" + messenger.getString(Msg.OpName) + "][" + messenger.getString(Msg.Op) + "]已将[" + messenger.getString(Msg.UinName) + "][" + messenger.getLong(Msg.Uin) + "]设置为管理员");
                    } else {
                        msg.addMsg(Msg.Text, "[" + messenger.getString(Msg.OpName) + "][" + messenger.getString(Msg.Op) + "]取消了[" + messenger.getString(Msg.UinName) + "][" + messenger.getLong(Msg.Uin) + "]的管理员身份");
                    }
                }
            });
        }

        if (messenger.hasMsg(Msg.GroupNotify)) {//群通知
            if (messenger.hasMsg(Msg.Op)) {
                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, messenger.getString(Msg.GroupId));
                        msg.addMsg(Msg.Text, "[" + messenger.getString(Msg.OpName) + "][" + messenger.getString(Msg.Op) + "]邀请[" + messenger.getString(Msg.UinName) + "][" + messenger.getString(Msg.Uin) + "]申请加入本群");
                    }
                });

            } else {
                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, messenger.getString(Msg.GroupId));
                        msg.addMsg(Msg.Text, "[" + messenger.getString(Msg.UinName) + "][" + messenger.getString(Msg.Uin) + "]申请加入本群,他的自我介绍:" + messenger.getString(Msg.Info));
                    }
                });
            }

            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupNotify);
                    msg.addMsg(Msg.GroupId, messenger.getString(Msg.GroupId));
                    msg.addMsg(Msg.MsgId, messenger.getString(Msg.MsgId));
                    msg.addMsg(Msg.Code, messenger.getString(Msg.Code));
                    msg.addMsg(Msg.Text, "我是拒绝的理由");
                    msg.addMsg(Msg.Agree);//Msg.Agree=同意 Msg.Refuse=拒绝 Msg.Ignore=忽略
                }
            });
        }

        if (textmsg.equals("菜单")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    StringBuilder text = new StringBuilder();
                    text.append("获取点赞列表").append("\n");
                    text.append("设置我的专属头衔").append("\n");
                    text.append("艾特全体成员").append("\n");
                    text.append("赞我 语音").append("\n");
                    text.append("闪照 图片").append("\n");
                    text.append("图文 回复 艾特").append("\n");
                    text.append("表情+id").append("\n");
                    text.append("Xml").append("\n");
                    text.append("Json").append("\n");
                    text.append("我的消息id").append("\n");
                    text.append("撤回+消息id").append("\n");
                    text.append("禁言QQ号[空格]时间(秒)").append("\n");
                    text.append("开启/关闭全体禁言").append("\n");
                    text.append("踢人@对方").append("\n");
                    text.append("改名片@对方").append("\n");
                    text.append("添加我为管理员").append("\n");
                    text.append("删除我的管理员").append("\n");
                    text.append("刷新群聊列表").append("\n");
                    text.append("获取群聊列表").append("\n");
                    text.append("刷新好友列表").append("\n");
                    text.append("获取好友列表").append("\n");
                    text.append("测试匿名消息").append("\n");
                    text.append("刷新成员列表").append("\n");
                    text.append("获取成员列表").append("\n");
                    text.append("获取成员等级").append("\n");
                    text.append("获取成员头衔").append("\n");
                    text.append("搜索+内容").append("\n");
                    text.append("获取管理员列表").append("\n");
                    text.append("获取不活跃成员").append("\n");
                    text.append("获取被禁言成员列表");

                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.MsgId, messenger.getString(Msg.MsgId));
                    msg.addMsg(Msg.Group);//发送群聊消息请求
                    msg.addMsg(Msg.GroupId, groupid);//群号
                    msg.addMsg(Msg.Reply, msgid);//回复
                    msg.addMsg(Msg.Text, text.toString());//发送内容
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");
                }
            });
        }

        if (textmsg.equals("文本")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, "OK");
                }
            });
        }

        if (textmsg.equals("获取点赞列表")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.FavoriteCardListGet);
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("设置我的专属头衔")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupModifySpecialTitle);
                            msg.addMsg(Msg.Group);
                            msg.addMsg(Msg.GroupId, groupid);
                            msg.addMsg(Msg.Uin, uin);
                            msg.addMsg(Msg.Title, "012345678912345678");//最长18字节 也就是6个纯汉字
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("艾特全体成员")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.AtAll);
                    msg.addMsg(Msg.Text, " OK");
                }
            });
        }

        if (textmsg.equals("赞我")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Reply, msgid);//回复
                    msg.addMsg(Msg.Text, "请您先赞我,我会按量回赞");
                }
            });
        }

        if (textmsg.equals("闪照")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Flash, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");
                }
            });
        }

        if (textmsg.equals("图片")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Img, "S://AAA.png");
                }
            });
        }

        if (textmsg.equals("图文")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, "OK");
                    msg.addMsg(Msg.Img, "https://q4.qlogo.cn/g?b=qq&nk=", uin, "&s=140");
                    msg.addMsg(Msg.Text, "OK");
                    msg.addMsg(Msg.Img, "https://pic.rmb.bdstatic.com/1530971282b420d77bdfb6444d854f952fe31f0d1e.jpeg");
                    msg.addMsg(Msg.Text, "OK");
                }
            });
        }

        if (textmsg.equals("回复")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Reply, msgid);//回复
                    msg.addMsg(Msg.Text, "ok");
                }
            });
        }

        if (textmsg.equals("艾特")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.AtUin, uin);//添加艾特账号
                    msg.addMsg(Msg.AtName, uinName);//添加艾特昵称
                    msg.addMsg(Msg.Text, "\nOK\n");
                    msg.addMsg(Msg.AtUin, uin);//添加艾特账号
                    msg.addMsg(Msg.AtName, uinName);//添加艾特昵称
                    msg.addMsg(Msg.Text, "\nOk");
                }
            });
        }

        if (textmsg.matches("表情[0-9]+")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Emoy, textmsg.replace("表情", ""));
                }
            });
        }

        if (textmsg.equalsIgnoreCase("xml")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Xml, "<?xml version='1.0' encoding='UTF-8' ?><msg serviceID=\"104\" templateID=\"1\" brief=\"大家好，我是群主。水瓶座女一枚~\"><item layout=\"2\"><picture cover=\"\" /><title>新人入群</title></item><source /></msg>");
                }
            });
        }

        if (textmsg.equalsIgnoreCase("json")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Json, "{\"app\":\"com.tencent.structmsg\",\"config\":{\"ctime\":1647043280,\"forward\":true,\"token\":\"356aa7568f832701cd5047c6dcfc833a\",\"type\":\"normal\"},\"desc\":\"音乐\",\"extra\":{\"app_type\":1,\"appid\":205141,\"msg_seq\":7073997018748087532,\"uin\":3337140142},\"meta\":{\"music\":{\"action\":\"\",\"android_pkg_name\":\"\",\"app_type\":1,\"appid\":205141,\"ctime\":1647043280,\"desc\":\"Alan Walker、Benjamin Ingrosso · World Of…\",\"jumpUrl\":\"https://t1.kugou.com/song.html?id=3ReQpd3zxV2\",\"musicUrl\":\"https://m.kugou.com/api/v1/wechat/index?uuid=7b82aea001d40c5dbddd885ae42ea9a3&album_audio_id=351076529&ext=m4a&apiver=2&cmd=101&album_id=50530456&hash=01c4b2497492d3a2475262b52ad62571&plat=0&version=11123&share_chl=qq_client&mid=3320991661582202751225362500412588691&key=512905974aaff6c1ce23f4052747a0f6&_t=1647043273&user_id=746798753&sign=a0ed201997b0377ed0d7ee7d801f734c\",\"preview\":\"http://imge.kugou.com/stdmusic/120/20211118/20211118203703380169.jpg\",\"sourceMsgId\":\"0\",\"source_icon\":\"https://open.gtimg.cn/open/app_icon/00/20/51/41/205141_100_m.png?t=1639645811\",\"source_url\":\"\",\"tag\":\"酷狗音乐\",\"title\":\"Man On The Moon\",\"uin\":3337140142}},\"prompt\":\"[分享]Man On The Moon\",\"ver\":\"0.0.0.1\",\"view\":\"music\"}");
                }
            });
        }

        if (textmsg.equalsIgnoreCase("我的消息id")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, "你的当前消息id:", msgid);
                }
            });
        }

        if (textmsg.matches("撤回[0-9]+")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Withdraw, textmsg.replace("撤回", ""));
                }
            });
        }

        if (textmsg.matches("禁言[0-9]+ [0-9]+")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    String[] value = textmsg.replace("禁言", "").split(" ");

                    msg.addMsg(Msg.GroupProhibit);
                    msg.addMsg(Msg.People);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Uin, value[0]);//支持批量禁言 需要同时携带Uin和Time
                    msg.addMsg(Msg.Time, value[1]);
                }
            });
        }

        if (textmsg.equals("开启全体禁言")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupProhibit);
                            msg.addMsg(Msg.All);
                            msg.addMsg(Msg.GroupId, groupid);
                            msg.addMsg(Msg.Open);
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("关闭全体禁言")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupProhibit);
                            msg.addMsg(Msg.All);
                            msg.addMsg(Msg.GroupId, groupid);
                            msg.addMsg(Msg.Close);
                        }
                    }));
                }
            });
        }

        if (textmsg.matches("踢人.*")) {
            for (final String target : messenger.getList(Msg.AtUin)) {
                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.GroupMemberExit);//踢人flag
                        msg.addMsg(Msg.GroupId, groupid);
                        msg.addMsg(Msg.Uin, target);
                    }
                });
                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, groupid);
                        msg.addMsg(Msg.Text, "踢出:", target);
                    }
                });
            }
        }

        if (textmsg.matches("改名片@.*")) {
            for (final String target : messenger.getList(Msg.AtUin)) {
                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, groupid);
                        msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                            @Override
                            public void build(Messenger msg) {
                                msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                                msg.addMsg(Msg.GroupMemberNickModify);
                                msg.addMsg(Msg.GroupId, groupid);
                                msg.addMsg(Msg.Uin, target);//修改对象
                                msg.addMsg(Msg.Nick, "我是名片666");
                            }
                        }));
                    }
                });
            }
        }

        if (textmsg.equals("添加我为管理员")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupModifyAdmin);
                            msg.addMsg(Msg.GroupId, groupid);
                            msg.addMsg(Msg.Add, uin);
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("删除我的管理员")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupModifyAdmin);
                            msg.addMsg(Msg.GroupId, groupid);
                            msg.addMsg(Msg.Del, uin);
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("刷新群聊列表")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupListGet);
                            msg.addMsg(Msg.Refresh);
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("获取群聊列表")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupListGet);
                        }
                    }).toString());
                }
            });
        }

        if (textmsg.equals("刷新好友列表")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.FriendListGet);
                            msg.addMsg(Msg.Refresh);
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("获取好友列表")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.FriendListGet);
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("测试匿名消息")) {
            if (api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.GroupMsgAnonymous);
                    msg.addMsg(Msg.Text, "OK");
                }
            }).hasMsg(Msg.Close)) {
                api.sendMessenger(new Messenger.Builder() {
                    @Override
                    public void build(Messenger msg) {
                        msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                        msg.addMsg(Msg.Group);
                        msg.addMsg(Msg.GroupId, groupid);
                        msg.addMsg(Msg.Text, "本群关闭了匿名消息功能");
                    }
                });
            }
        }

        if (textmsg.equals("刷新成员列表")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupMemberListGet);
                            msg.addMsg(Msg.GroupId, groupid);//目标群号
                            msg.addMsg(Msg.Refresh);
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("获取成员列表")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupMemberListGet);
                            msg.addMsg(Msg.GroupId, groupid);//目标群号
                        }
                    }).getList(Msg.Uin));
                }
            });
        }

        if (textmsg.equals("获取成员等级")) {
            Messenger rsp = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupMemberListGet);
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                }
            });

            final StringBuilder builder = new StringBuilder();

            for (Map<String, String> map : rsp.getList()) {
                builder.append(map.get(Msg.Uin)).append("=");
                builder.append(map.get(Msg.Level)).append("\n");
            }

            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, builder);
                }
            });
        }

        if (textmsg.equals("获取成员头衔")) {
            Messenger rsp = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.GroupMemberListGet);
                    msg.addMsg(Msg.GroupId, groupid);//目标群号
                }
            });

            final StringBuilder builder = new StringBuilder();

            for (Map<String, String> map : rsp.getList()) {
                builder.append(map.get(Msg.Uin)).append("=");
                builder.append(map.get(Msg.Title)).append("\n");
            }

            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, builder);
                }
            });
        }

        if (textmsg.equals("获取管理员列表")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupMemberListGetAdmin);
                            msg.addMsg(Msg.GroupId, groupid);
                        }
                    }));
                }
            });
        }

        if (textmsg.equals("获取不活跃成员")) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, api.sendMessenger(new Messenger.Builder() {
                        @Override
                        public void build(Messenger msg) {
                            msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                            msg.addMsg(Msg.GroupMemberListGetInactive);
                            msg.addMsg(Msg.GroupId, groupid);
                        }
                    }));
                }
            });
        }


        if (messenger.hasMsg(Msg.GroupProhibit) && messenger.hasMsg(Msg.All)) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, userOp, messenger.getLong(Msg.Time) > 0 ? "开启了全体禁言" : "关闭了全体禁言");
                }
            });
        }

        if (messenger.hasMsg(Msg.GroupProhibit) && messenger.hasMsg(Msg.People)) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);

                    if (messenger.getLong(Msg.Time) > 0) {
                        msg.addMsg(Msg.Text, user, "被", userOp, "禁言了", messenger.getString(Msg.Time), "秒");
                    } else {
                        msg.addMsg(Msg.Text, user, "被", userOp, "解除禁言了");
                    }
                }
            });
        }

        if (messenger.hasMsg(Msg.Withdraw)) {
            final Messenger cache = api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);//必须携带
                    msg.addMsg(Msg.GroupId, groupid);//目标群号 必须
                    msg.addMsg(Msg.GroupMsgCacheGet, messenger.getString(Msg.Withdraw));//flag 必须
                }
            });//获取历史消息

            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, user, "撤回了:", cache.toString());
                }
            });
        }

        if (messenger.hasMsg(Msg.GroupAnonymous)) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, user, (messenger.hasMsg(Msg.Open) ? "开启了匿名聊天" : "关闭了匿名聊天"));
                }
            });
        }

        if (messenger.hasMsg(Msg.GroupFileUpload)) {
            api.sendMessenger(new Messenger.Builder() {
                @Override
                public void build(Messenger msg) {
                    msg.addMsg(Msg.Account, messenger.getString(Msg.Account));
                    StringBuilder text = new StringBuilder();
                    text.append(user).append("上传了文件").append("\n\n");
                    text.append("文件名称 ").append(messenger.getString(Msg.Name)).append("\n");
                    text.append("文件大小 ").append(messenger.getString(Msg.Size)).append("字节\n");
                    text.append("文件别名 ").append(messenger.getString(Msg.Id));

                    msg.addMsg(Msg.Group);
                    msg.addMsg(Msg.GroupId, groupid);
                    msg.addMsg(Msg.Text, text);
                }
            });
        }


    }

}
