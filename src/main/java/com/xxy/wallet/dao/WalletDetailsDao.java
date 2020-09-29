package com.xxy.wallet.dao;

import com.xxy.wallet.commen.bean.PesonSendWallet;
import com.xxy.wallet.commen.bean.WalletStatus;
import com.xxy.wallet.pojo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 17:57 2020/9/16
 */
@Repository
public interface WalletDetailsDao {
    /**
     * 查询个人余额
     * @param userId
     * @return
     */
    @Select("select id, user_id as userId, balance, freeze_balance as freezeBalance, status from tb_wallet_account where user_id = #{userId} ")
    WalletAccount getWallet(@Param("userId") Integer userId);

    /**
     * 查询红包状态以及个人领取状态
     * @param walletObjId 红包ID
     * @param sendUserId   发送红包用户ID
     * @return
     */
    @Select("SELECT  link_id as linkId, source_type as SourceType, wallet_status as walletStatus, description from tb_wallet_object where id = #{walletObjId} and user_id = #{sendUserId} ")
    WalletObject walletObject(@Param("walletObjId") Integer walletObjId, @Param("sendUserId") Integer sendUserId);

    /**
     * 获取红包领取状态
     * @param walletObjId
     * @param userId
     * @return
     */
    @Select(" select user_id as userId from tb_wallet_object_detail where wallet_object_id = #{walletObjId} and user_id = #{userId}")
    WalletObjectDetail getHasWallet(@Param("walletObjId") Integer walletObjId, @Param("userId") Integer userId);

    /**
     * 查询用户余额明细
     * @param userId
     * @return
     */
    @Select("select id ,user_id as userId, wallet_id as walletId, cash_book_id as cashBookId, money, type, create_time as createDate, update_time as updateDate from tb_wallet_account_detail where user_id = #{userId}")
    List<WalletAccountDetail> walletDetail(@Param("userId") Integer userId);

    /**
     * 查询原心个人余额明细列表
     * @param userId
     * @return
     */
    @Select("select id, user_id as userId, wallet_id as walletId, cash_book_id as cashBookId, money, type, create_time as createDate, update_time as updateDate from tb_wallet_account_detail where user_id = #{userId}")
    List<WalletAccountDetail> walletDetails(@Param("userId") Integer userId);

    /**
     * 获取用户发送总金额以及红包个数
     * @param userId
     * @return
     */
    @Select("select id, money, total, type, user_id as userId,link_id as linkId ,create_time as createDate from tb_wallet_object where user_id = #{userId}")
    List<WalletObject> sendWallet(@Param("userId") Integer userId);

    /**
     * 获取红包明细
     * @param userId
     * @return
     */
    @Select("select wallet_object_id as walletObjectId, money, is_lucky_guy as isLuckyGuy, update_time as updateDate from tb_wallet_object_detail where user_id = #{userId}")
    List<WalletObjectDetail> getWalletDetail(@Param("userId") Integer userId);

    /**
     * 查询大红包信息
     * @param walletObjectId
     * @return
     */
    @Select("select id, user_id as userId, wallet_status as walletStatus, type from tb_wallet_object where id = #{walletObjectId}")
    List<WalletObject> getWalletStatus(@Param("walletObjectId") Integer walletObjectId);

    /**
     * 查询用户姓名
     * @param id
     * @return
     */
    @Select("select real_name as realName from tb_userinfo where id = #{id}")
    List<User> getRealName(@Param("id") Integer id);
}
