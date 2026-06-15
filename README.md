# 🔌 Secluded WebSocket Plugin

本文档用于说明 Secluded WebSocket 插件的数据交互协议。

---

## 鉴权认证

当插件连接主程序（WebSocket）时，必须在请求头中携带认证令牌进行身份验证。

### 🔑 鉴权配置

方式二 请求时需在 Header 中添加 `Authorization` 字段，格式为 `Bearer <Token>`：

```java
// 方式一 SecPlugin("ws://127.0.0.1:24804/?token=y5s9WERi");
// 方式二 super.addHeader("Authorization","Bearer y5s9WERi");
```

> 📌 **安全提示**：示例中的 `y5s9WERi` 为**协议令牌**，用户可在 `软件设置 -> 插件协议` 中自行查看和配置。

### ❌ 鉴权失败

如果 Token 错误或未携带，主程序将拒绝连接并返回错误码：

* **状态码**：`401` (Unauthorized)

### 鉴权成功应答

鉴权成功后，服务端会主动下发当前运行环境的基础信息。

#### 📥 响应数据示例 (Server -> Client)

```json
{
  "cmd": "Sync",
  "ver": "2026-05-01",
  "data": {
    "time": 1781440657491,
    "version": "v2026-06-11 23:05:52",
    "platform": "Windows",
    "list": [
      1000000,
      3889527093
    ]
  }
}
```

#### 📋 响应字段说明

| 字段     | 类型     | 说明                 | 示例值            |
|--------|--------|--------------------|----------------|
| `cmd`  | String | 协议命令，固定为 `"Sync"`。 | `"Sync"`       |
| `ver`  | String | 协议版本号。             | `"2026-05-01"` |
| `data` | Object | 服务端返回的业务数据体。       | `{...}`        |

#### 🔍 Data 内部字段详情

| 字段         | 类型     | 说明                                  | 示例值                      |
|------------|--------|-------------------------------------|--------------------------|
| `time`     | Long   | 主程序当前时间戳（毫秒级）。                      | `1781440657491`          |
| `version`  | String | 主程序当前运行的版本号（包含构建时间）。                | `"v2026-06-11 23:05:52"` |
| `platform` | String | 主程序运行的操作系统平台（如 `Windows`, `Linux`）。 | `"Windows"`              |
| `list`     | Array  | 当前在线/已授权的账号列表（QQ号/机器人账号）。           | `[1000000, 3889527093]`  |

---

## 日志打印 (Print)

插件可以向 Secluded 控制台发送不同颜色的日志信息。所有日志的结构相同，仅通过 `cmd` 命令来区分日志颜色。

### 📤 请求数据示例 (Client -> Server)

```json
{
  "seq": 1,
  "cmd": "PrintI",
  "data": "这是一条发送到控制台的日志内容"
}
```

### 🎨 cmd 取值与日志颜色对应表

| cmd 命令   | 日志颜色  | 推荐用途                  |
|:---------|:------|:----------------------|
| `PrintD` | 🔵 蓝色 | 调试信息 / 过程记录 (Debug)   |
| `PrintE` | 🔴 红色 | 错误信息 / 异常崩溃 (Error)   |
| `PrintI` | ⚪ 白色  | 普通信息 / 基础提示 (Info)    |
| `PrintS` | 🟢 绿色 | 成功提示 / 运行正常 (Success) |
| `PrintW` | 🟡 黄色 | 警告信息 / 潜在风险 (Warning) |

---

## 消息事件推送 (PushOicqMsg)

当主程序收到 **好友 / 群聊 / 频道 / 系统** 的新消息时，会通过 WebSocket 向插件异步推送该事件。

### 📰 协议基础结构

* **`cmd` 字段固定为**：`"PushOicqMsg"`
* **`data` 字段固定为**：包含多个键值对对象的 **`Array` 数组** `[{}, {}, {}...]`。
* **⚠️ 核心解析原则**：
  数组中每个 `{}` 内部的键称为 **`tag`**（全部为字符串类型）。解析消息时，**必须通过循环遍历判断 `tag`
  的存在性来识别消息内容与类型，绝对不能依赖固定的数组下标（JSON 位置）进行解析**，因为数组元素的顺序在不同消息或版本中可能会发生变动。

```json
{
  "ver": "2026-05-01",
  "cmd": "PushOicqMsg",
  "data": [
    {},
    {},
    {}
  ]
}

```

### 💬 场景示例：收到群聊纯文本消息

```json
{
  "ver": "2026-05-01",
  "cmd": "PushOicqMsg",
  "data": [
    {
      "GroupName": "⑤疾旋鼬交流群",
      "Account": "2267540168",
      "Group": "Group",
      "Uid": "u_E5D4n5vA54_DTlMgZn7X1w",
      "UinName": "[群文件]qq9.2.90醇酸.cpp                              .",
      "Uin": "3337140142",
      "Debug": "Debug",
      "MsgId": "250610",
      "GolineMode": "SL",
      "GroupId": "980552471"
    },
    {
      "Typeface": "宋体"
    },
    {
      "Text": "1"
    },
    {
      "Bubble": "0"
    }
  ]
}

```

#### 📋 Data 内部可用标签 (Tag) 详解

通过在循环中检测是否存在以下对应的键（Key），来提取相关字段信息：

| 标签 (Tag)     | 类型     | 说明                           | 示例值                          |
|--------------|--------|------------------------------|------------------------------|
| `Group`      | String | **消息源标识**：存在该键表示此消息来自**群聊**。 | `"Group"`                    |
| `GroupId`    | String | 群号。                          | `"980552471"`                |
| `GroupName`  | String | 群聊名称。                        | `"⑤疾旋鼬交流群"`                  |
| `Account`    | String | 当前接收到消息的机器人/登录账号。            | `"2267540168"`               |
| `Uid`        | String | 发言者的唯一标识符 (Uid)。             | `"u_E5D4n5vA54_DTlMgZn7X1w"` |
| `Uin`        | String | 发言者的底层账号标识 (Uin)。            | `"3337140142"`               |
| `UinName`    | String | 发言者当前的群昵称 / 昵称。              | `" [群文件]qq9.2.90醇酸.cpp ..."` |
| `MsgId`      | String | 唯一消息 ID。                     | `"250610"`                   |
| `GolineMode` | String | 登录上线模式，具体取值请参考下方。            | `"SL"`                       |
| `Text`       | String | 纯文本消息的文本内容。                  | `"1"`                        |
| `Typeface`   | String | 发言者使用的客户端字体类型。               | `"宋体"`                       |
| `Bubble`     | String | 发言者使用的聊天气泡 ID。               | `"0"`                        |
| `Debug`      | String | 调试状态标签，存在此键代表该登录账号开启了调试模式。   | `"Debug"`                    |

### ⚙️ GolineMode 上线模式映射表

当解析到 `GolineMode` 标签时，其字符串值代表主程序当前挂载账号的上线模式：

| 模式代码     | 上线模式 |
|----------|------|
| **`PO`** | 官方人机 |
| **`SL`** | 企鹅扫码 |
| **`SA`** | 手表扫码 |
| **`PA`** | 安卓手机 |
| **`PP`** | 安卓平板 |

### 收到群聊图片消息

```json
{
  "ver": "2026-05-01",
  "cmd": "PushOicqMsg",
  "data": [
    {
      "GroupName": "⑤疾旋鼬交流群",
      "Account": "2267540168",
      "Group": "Group",
      "Uid": "u_E5D4n5vA54_DTlMgZn7X1w",
      "UinName": "[群文件]qq9.2.90醇酸.cpp                              .",
      "Uin": "3337140142",
      "Debug": "Debug",
      "MsgId": "250613",
      "GolineMode": "SL",
      "GroupId": "980552471"
    },
    {
      "Typeface": "宋体"
    },
    {
      "Img": "3EE648A0BA7D9FC296F8197D18D416FF.png",
      "Size": "8146516",
      "Height": "1600",
      "Width": "2560",
      "Url": "https://multimedia.nt.qq.com.cn/download?appid=1407&fileid=EhRrXBDc0s2oL1VSddet0Uo9sbCQyBjUnPEDIP8KKKqyrqflhpUDMgRwcm9kUIC9owFaEODpUBfme5ChP5iYjCOw5xF6Ao31ggECZ3o&rkey=CAESQDTSQ4M7GIckGTX_5dkvSvQb-mjU0B6TnYEuoRVTBwxTcQfmr8XrMkeTTPosk_sc5RSZcHMYaO7yL0Rk5ksOy-U",
      "MD5": "3EE648A0BA7D9FC296F8197D18D416FF"
    },
    {
      "Bubble": "0"
    }
  ]
}
```

### 收到群聊图文混合

```json
{
  "ver": "2026-05-01",
  "cmd": "PushOicqMsg",
  "data": [
    {
      "GroupName": "⑤疾旋鼬交流群",
      "Account": "2267540168",
      "Group": "Group",
      "Uid": "u_E5D4n5vA54_DTlMgZn7X1w",
      "UinName": "[群文件]qq9.2.90醇酸.cpp                              .",
      "Uin": "3337140142",
      "Debug": "Debug",
      "MsgId": "250611",
      "GolineMode": "SL",
      "GroupId": "980552471"
    },
    {
      "Typeface": "宋体"
    },
    {
      "Text": "123\n"
    },
    {
      "Img": "3EE648A0BA7D9FC296F8197D18D416FF.png",
      "Size": "8146516",
      "Height": "1600",
      "Width": "2560",
      "Url": "https://multimedia.nt.qq.com.cn/download?appid=1407&fileid=EhRrXBDc0s2oL1VSddet0Uo9sbCQyBjUnPEDIP8KKLyB8qzkhpUDMgRwcm9kUIC9owFaEEVmvTryt_gfNhUWd-TJ1xl6ArUHggECZ3o&rkey=CAQSQD3FKqtg2XbchYCLLmXyi-0R7UY0i7BpyhC-MjPXCEgkMuZ_CZVTcRcutf8SfaIW1_H5euP2IlU7Epd7s_lpY8k",
      "MD5": "3EE648A0BA7D9FC296F8197D18D416FF"
    },
    {
      "Text": "ABC"
    },
    {
      "Bubble": "0"
    }
  ]
}
```

### 收到群聊语音消息

```json
{
  "ver": "2026-05-01",
  "cmd": "PushOicqMsg",
  "data": [
    {
      "GroupName": "⑤疾旋鼬交流群",
      "Account": "2267540168",
      "Group": "Group",
      "Uid": "u_E5D4n5vA54_DTlMgZn7X1w",
      "UinName": "[群文件]qq9.2.90醇酸.cpp                              .",
      "Uin": "3337140142",
      "Debug": "Debug",
      "MsgId": "250614",
      "GolineMode": "SL",
      "GroupId": "980552471"
    },
    {
      "Typeface": "宋体"
    },
    {
      "Bubble": "0"
    },
    {
      "Ptt": "905977c9200bbf389105cdb2e56207ce.amr",
      "Size": "3878",
      "Time": "3",
      "MD5": "905977C9200BBF389105CDB2E56207CE"
    }
  ]
}
```

### 收到群聊视频消息

```json
{
  "ver": "2026-05-01",
  "cmd": "PushOicqMsg",
  "data": [
    {
      "GroupName": "⑤疾旋鼬交流群",
      "Account": "2267540168",
      "Group": "Group",
      "Uid": "u_E5D4n5vA54_DTlMgZn7X1w",
      "UinName": "[群文件]qq9.2.90醇酸.cpp                              .",
      "Uin": "3337140142",
      "Debug": "Debug",
      "MsgId": "250616",
      "GolineMode": "SL",
      "GroupId": "980552471"
    },
    {
      "Typeface": "宋体"
    },
    {
      "Bubble": "0"
    },
    {
      "Img": "7e969d488f505e1c0c0ccf64606650c6.mp4",
      "Size": "5703019",
      "Height": "480",
      "Width": "853",
      "Url": "https://multimedia.nt.qq.com.cn&rkey=CAESMNt2kVoSywgNzSMKWrenYe_-TwJgXfGoKfnjCSG8SH8POVNvP_BSVwAwgBxaF-3zHA",
      "MD5": "7E969D488F505E1C0C0CCF64606650C6"
    }
  ]
}
```

---

## 发送群聊消息 (SendOicqMsg)

插件可以通过该命令向指定群聊发送文本、图片、图文混合、语音及视频消息。

### 📰 协议基础结构

* **`cmd` 字段固定为**：`"SendOicqMsg"`
* **`data` 数组构造原则**：
    * 数组的**第一个元素**固定为消息的目标元数据（包含发送账号、群组标识、目标群号等）。
    * 数组的**后续元素**为具体的消息内容片段（可以自由排列组合，形成图文混排等多媒体消息）。

#### 📋 公共字段说明

| 字段     | 类型      | 说明                                                   |
|:-------|:--------|:-----------------------------------------------------|
| `seq`  | Integer | **序列号**：每次客户端发包时需自增 `+1`。                            |
| `cmd`  | String  | **协议命令**：固定为 `"SendOicqMsg"`。                        |
| `rsp`  | Boolean | **应答标识**：设为 `true` 时，主程序发送成功后会异步返回本条消息的 `MsgId` 应答包。 |
| `data` | Array   | **消息内容数组**，第一个对象为目标路由，后面为消息组件。                       |

#### 🔍 基础路由标签（data 数组第一个对象）

| 标签 (Tag)  | 类型     | 说明                         | 示例值            |
|:----------|:-------|:---------------------------|:---------------|
| `Account` | String | 指定执行发送操作的机器人 / 登录账号。       | `"2267540168"` |
| `Group`   | String | 目标渠道标识，固定为 `"Group"` 代表群聊。 | `"Group"`      |
| `GroupId` | String | 接收消息的目标群号。                 | `"980552471"`  |

---

### 🧱 场景化消息构建示例

#### 1️⃣ 发送纯文本消息

适用于发送单纯的文字内容。

```json
{
  "seq": 1,
  "cmd": "SendOicqMsg",
  "rsp": true,
  "data": [
    {
      "Account": "2267540168",
      "Group": "Group",
      "GroupId": "980552471"
    },
    {
      "Text": "这里是纯文本消息内容"
    }
  ]
}
```

#### 2️⃣ 发送纯图片消息

支持本地绝对路径或网络图片 URL。

```json
{
  "seq": 1,
  "cmd": "SendOicqMsg",
  "rsp": true,
  "data": [
    {
      "Account": "2267540168",
      "Group": "Group",
      "GroupId": "980552471"
    },
    {
      "Img": "S:/A.jpg"
    }
  ]
}

```

#### 3️⃣ 发送图文混合消息

在 `data` 数组中任意组合 `Text` 与 `Img` 节点，主程序会按照数组顺序拼接并渲染。

```json
{
  "seq": 1,
  "cmd": "SendOicqMsg",
  "rsp": true,
  "data": [
    {
      "Account": "2267540168",
      "Group": "Group",
      "GroupId": "980552471"
    },
    {
      "Text": "第一段文字"
    },
    {
      "Img": "S:/A.jpg"
    },
    {
      "Text": "紧跟在图片后的第二段文字"
    }
  ]
}

```

#### 4️⃣ 发送群聊语音消息

用于发送 Silk 格式或需要编码的语音信息。

```json
{
  "seq": 1,
  "cmd": "SendOicqMsg",
  "rsp": true,
  "data": [
    {
      "Account": "2267540168",
      "Group": "Group",
      "GroupId": "980552471"
    },
    {
      "Ptt": "[https://m.kugou.com/api/v1/wechat/index?uuid=](https://m.kugou.com/api/v1/wechat/index?uuid=)...",
      "Time": "60",
      "SilkEncode": "SilkEncode",
      "ProgressPush": "1781446312189"
    }
  ]
}

```

* **语音字段说明：**
* `Ptt` (String): 语音文件的本地路径或网络直链。
* `Time` (String): 语音时长（秒）。
* `SilkEncode` (String): 存在该键表示告知主程序对该音频进行 Silk 语音格式编码。
* `ProgressPush` (String): 存在该键时（填入时间戳或标识），主程序会向插件异步回调文件上传进度。

#### 5️⃣ 发送群聊视频消息

支持短视频发送。若超过大小限制，主程序会自动将其转化为群文件上传。

```json
{
  "seq": 1,
  "cmd": "SendOicqMsg",
  "rsp": true,
  "data": [
    {
      "Account": "2267540168",
      "Group": "Group",
      "GroupId": "980552471"
    },
    {
      "Video": "S:/AAA.mp4",
      "Name": "Sorry",
      "Time": "30",
      "Img": "https://q4.qlogo.cn/g?b=qq&nk=3337140142&s=140",
      "ProgressPush": "1781446485450"
    }
  ]
}
```

* **视频字段说明：**
* `Video`: 视频文件的本地路径或网络链接。
* `Name`: 文件名称。当视频过大自动转为“群文件”发送时，将以此名称命名。
* `Time`: 视频时长（秒）。
* `Img`: *[可选]* 视频封面图的 URL 链接或路径，不携带则使用系统默认封面。
* `ProgressPush`: 存在该键时，主程序会向插件异步回调大文件上传进度。

---

## ⚙️ 格式转换

对于不熟悉 **Java** 语法的开发者，可以参考以下示例，将 Java 的消息发送格式无缝转换为通用的 **JSON** 格式。

### Java 原始消息构建

```java

@Override
public void build(Messenger msg) {
    msg.addMsg(Msg.Account, messenger.getString(Msg.Account)); // 指定发送账号
    msg.addMsg(Msg.Group);                                     // 标记为群聊消息
    msg.addMsg(Msg.GroupId, messenger.getString(Msg.GroupId)); // 指定目标群聊
    msg.addMsg(Msg.Text, "696");                               // 发送文本内容
}
```

### 转换后的 Json 报文

```json
{
  "seq": 1,
  "cmd": "SendOicqMsg",
  "rsp": true,
  "data": [
    {
      "Account": "登录账号",
      "Group": "Group",
      "GroupId": "群号"
    },
    {
      "Text": "696"
    }
  ]
}
```
