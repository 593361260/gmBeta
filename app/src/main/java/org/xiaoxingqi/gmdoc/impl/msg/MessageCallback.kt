package org.xiaoxingqi.gmdoc.impl.msg

import org.xiaoxingqi.gmdoc.entity.msg.MsgInfoListData

open class MessageCallback {

    open fun msgList(data: MsgInfoListData) {}
}