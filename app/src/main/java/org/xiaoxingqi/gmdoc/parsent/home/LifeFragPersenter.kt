package org.xiaoxingqi.gmdoc.parsent.home

import android.content.Context
import com.alibaba.fastjson.JSON
import org.xiaoxingqi.gmdoc.entity.GroupData
import org.xiaoxingqi.gmdoc.impl.IConstant
import org.xiaoxingqi.gmdoc.impl.home.LifeFragCallback
import org.xiaoxingqi.gmdoc.parsent.BasePresent
import rx.Subscriber

class LifeFragPersenter : BasePresent {
    private var callback: LifeFragCallback? = null

    constructor(context: Context, callback: LifeFragCallback) : super(context) {
        this.callback = callback
    }

    fun queryGroup() {
        addObaser(apiServer.base_get("list_tem${IConstant.GET_END}"), object : Subscriber<String>() {
            override fun onNext(t: String?) {
                callback?.getGroupData(JSON.parseObject(t, GroupData::class.java))
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }
        })
    }
}