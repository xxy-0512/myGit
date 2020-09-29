package com.xxy.wallet.dao;

import com.xxy.wallet.commen.bean.WalletObjDetail;
import com.xxy.wallet.pojo.WalletAccount;
import com.xxy.wallet.pojo.WalletObject;
import com.xxy.wallet.pojo.WalletObjectDetail;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 12:34 2020/9/13
 */
@Repository
public interface WalletObjectDao {

    @Insert("INSERT INTO tb_wallet_object (id, money, total, type, user_id, link_type , link_id, source_type, wallet_status, description, create_time, update_time) VALUES \n" +
            "(null, #{money}, #{total}, #{type}, #{userId}, #{linkType}, #{linkId}, #{sourceType}, #{walletStatus}, #{description}, #{createDate}, #{updateDate});")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertWalletObject(WalletObject walletObject);

    @Insert("INSERT INTO tb_wallet_object_detail (id, wallet_object_id, user_id, money, is_lucky_guy, create_time, update_time) VALUES \n" +
            "(null, #{walletObjectId}, #{userId}, #{money}, #{isLuckyGuy}, #{createDate}, #{updateDate});")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertWalletObjDetail(WalletObjectDetail detail);

    @Select("SELECT id, money, total, user_id AS userId, type, link_id AS linkId, link_type as linkType, source_type AS sourceType, wallet_status AS walletStatus, description, create_time AS createDate, update_time AS updateDate FROM tb_wallet_object WHERE id = #{walletObjId} AND user_id = #{sendUserId}")
    WalletObject getWallet(@Param("walletObjId") Integer walletObjId, @Param("sendUserId") Integer sendUserId);

    @Update("UPDATE tb_wallet_object_detail SET user_id = #{receiveUserId}, update_time = NOW() WHERE id = #{id}")
    void updateDetailById(@Param("id") Integer id, @Param("receiveUserId") Integer receiveUserId);

    @Update("UPDATE tb_wallet_object SET wallet_status = #{status}, update_time = NOW() WHERE id = #{walletObjId}")
    void updateObjectStatus(@Param("walletObjId") Integer walletObjId, @Param("status") Integer status);


    @Select("SELECT\n" +
            "\tid AS walletObjDetailId,\n" +
            "\tmoney,\n" +
            "\tis_lucky_guy AS isLuckyGuy,\n" +
            "\tupdate_time AS updateTime,\n" +
            "\tuser_id AS receiveUserId\n" +
            "FROM\n" +
            "\ttb_wallet_object_detail\n" +
            "WHERE\n" +
            "\twallet_object_id = #{walletObjId}\n" +
            "AND user_id IS NOT NULL\n" +
            "ORDER BY\n" +
            "\tupdate_time DESC")
    List<WalletObjDetail> getHasGrab(@Param("walletObjId") Integer walletObjId);

    /**
     * 定时器任务返回需要退回的红包
     *
     * @param noFinish
     * @return
     */
    @Select("select id, money, total, type, user_id as userId, link_type as linkType, link_id as linkId, source_type as sourceType, wallet_status as walletStatus, description, create_time as createTime, update_time as updateTime from tb_wallet_object where wallet_status = #{noFinish} and TIMESTAMPDIFF(MINUTE,create_time,NOW()) >= 2")
    List<WalletObject> getWillReturn(@Param("noFinish") Integer noFinish);
}
