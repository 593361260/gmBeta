package org.xiaoxingqi.gmdoc.modul.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_long_comment_list.*
import org.xiaoxingqi.gmdoc.R
import org.xiaoxingqi.gmdoc.core.BaseActivity
import org.xiaoxingqi.gmdoc.core.adapter.BaseAdapterHelper
import org.xiaoxingqi.gmdoc.core.adapter.QuickAdapter
import org.xiaoxingqi.gmdoc.entity.home.HomeUserShareData
import org.xiaoxingqi.gmdoc.impl.home.UserCallback
import org.xiaoxingqi.gmdoc.modul.global.WriteLongCommentActivity
import org.xiaoxingqi.gmdoc.presenter.home.UserPresenter
import org.xiaoxingqi.gmdoc.tools.TimeUtils
import org.xiaoxingqi.gmdoc.wegidt.RoundScoreView

/**
 *用户编辑的长评列表
 */
class UserLongListActivity : BaseActivity<UserPresenter>() {
    private var current = 0
    private var order = ""
    private lateinit var adapter: QuickAdapter<HomeUserShareData.ContributeBean>
    private val mData by lazy { ArrayList<HomeUserShareData.ContributeBean>() }
    private var isEdit = false
    private var userId: String? = null
    private var type = 1

    override fun createPresent(): UserPresenter {
        return UserPresenter(this, object : UserCallback() {
            override fun userWordList(data: HomeUserShareData) {
                for (bean in data.data.data) {
                    mData.add(bean)
                    adapter.notifyItemInserted(adapter.itemCount - 1)
                }
            }
        })
    }

    override fun setContent() {
        setContent(R.layout.activity_long_comment_list)
    }

    override fun initView() {

    }

    override fun initData() {
        userId = intent.getStringExtra("userId")
        adapter = object : QuickAdapter<HomeUserShareData.ContributeBean>(this, R.layout.item_user_content_long_view, mData) {
            @SuppressLint("SetTextI18n")
            override fun convert(helper: BaseAdapterHelper?, item: HomeUserShareData.ContributeBean?) {
                val scoreView = helper!!.getView(R.id.roundScore) as RoundScoreView
                scoreView.setScore(item!!.game.score.toFloat())
                helper.getTextView(R.id.tv_GameName).text = item.game.game_name + " | " + item.game.game_pla + item.game.game_ver
                helper.getTextView(R.id.tv_stateInfo).text = "${item.like_num}赞 · " + item.forward_num + "转发 · " + TimeUtils.getInstance().parseTime(item.created_at)
                helper.getTextView(R.id.tv_Title).text = item.title
                if (isEdit) {
                    helper.getTextView(R.id.tv_Delete).visibility = View.VISIBLE
                    helper.getTextView(R.id.tv_Edit).visibility = View.VISIBLE
                    when (type) {
                        1 -> {
                            helper.getTextView(R.id.tv_Edit).text = "编辑"
                            helper.getTextView(R.id.tv_Edit).visibility = View.VISIBLE
                        }
                        2 -> helper.getTextView(R.id.tv_Edit).visibility = View.GONE
                        3 -> {
                            helper.getTextView(R.id.tv_Edit).text = "放回原处"
                            helper.getTextView(R.id.tv_Edit).visibility = View.VISIBLE
                        }
                    }
                } else {
                    helper.getTextView(R.id.tv_Delete).visibility = View.GONE
                    helper.getTextView(R.id.tv_Edit).visibility = View.GONE
                }
                helper.getTextView(R.id.tv_Edit).setOnClickListener {//编辑  放回原处
                    v ->
                    if (type == 1) {
                        startActivity(Intent(this@UserLongListActivity, WriteLongCommentActivity::class.java).putExtra("dt_id", item.id)
                                .putExtra("gameId", item.game_id)
                        )
                    } else {//放回远处
//                        comeBackComment(item.getId())
                    }
                }
                helper.getTextView(R.id.tv_Delete).setOnClickListener { v ->
                    //删除  发布  草稿  垃圾
                    //                    delete(bean)
                }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        persent?.shortCommentList(userId!!, 2, type, current, order)
    }

    override fun initEvent() {
        viewBack.setOnClickListener { finish() }
    }

}