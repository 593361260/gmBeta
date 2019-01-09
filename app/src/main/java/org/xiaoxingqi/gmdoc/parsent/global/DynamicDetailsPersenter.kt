package org.xiaoxingqi.gmdoc.parsent.global

import android.content.Context
import com.alibaba.fastjson.JSON
import org.xiaoxingqi.gmdoc.entity.DynamicDetailsData
import org.xiaoxingqi.gmdoc.impl.IConstant
import org.xiaoxingqi.gmdoc.impl.global.DynamicDetailsCallback
import org.xiaoxingqi.gmdoc.parsent.BasePresent
import rx.Subscriber

class DynamicDetailsPersenter : BasePresent {
    private var callback: DynamicDetailsCallback? = null

    constructor(context: Context, callback: DynamicDetailsCallback) : super(context) {
        this.callback = callback
    }

    fun getDetails(dynamicId: String) {

        addObaser(apiServer.base_get("detail/$dynamicId${IConstant.GET_END}"), object : Subscriber<String>() {
            override fun onNext(t: String?) {
                callback?.dynamicInfo(JSON.parseObject(t, DynamicDetailsData::class.java))
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }
        })

    }
}