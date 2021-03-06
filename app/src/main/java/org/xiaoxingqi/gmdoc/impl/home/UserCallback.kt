package org.xiaoxingqi.gmdoc.impl.home

import org.xiaoxingqi.gmdoc.entity.BaseRespData
import org.xiaoxingqi.gmdoc.entity.QINiuRespData
import org.xiaoxingqi.gmdoc.entity.game.GameDetailsData
import org.xiaoxingqi.gmdoc.entity.game.GameListData
import org.xiaoxingqi.gmdoc.entity.home.HomeUserShareData
import org.xiaoxingqi.gmdoc.entity.user.FollowData
import org.xiaoxingqi.gmdoc.entity.user.LoveGameData
import org.xiaoxingqi.gmdoc.entity.user.UserContentPhotoData

open class UserCallback {
    /**
     * 查询钱包的信息
     */
    open fun walletData(data: HomeUserShareData) {}

    /**
     * 用户的相册
     */
    open fun userPhoto(data: UserContentPhotoData) {}

    /**
     * 喜欢的游戏列表
     */
    open fun loveGameList(data: LoveGameData) {}

    /**
     * 获取七牛
     */
    open fun qiniuToken(data: QINiuRespData) {}

    /**
     * 编辑游戏单
     */
    open fun changeGame(data: BaseRespData) {}

    /**
     * 想玩 在玩 待评分
     */
    open fun otherGame(data: GameListData) {}

    /**
     * 用户的文字
     */
    open fun userWordList(data: HomeUserShareData) {}

    /**
     *更新用户信息
     */
    open fun updateInfo(data: BaseRespData) {}

    /**
     * 获取用户的贡献图
     */
    open fun userContribute(data: GameDetailsData) {}

    /**
     * 贡献图发布成功
     */
    open fun addContributeSuccess(data: BaseRespData) {}

    /**
     *发布动态
     */
    open fun pushSuccess(data: BaseRespData) {}

    /**
     * 获取关注的列表
     */
    open fun loveList(data: FollowData) {

    }
}