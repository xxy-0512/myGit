package com.xxy.wallet.dao;

import com.xxy.wallet.pojo.WalletAccount;
import com.xxy.wallet.pojo.WalletCard;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 12:02 2020/9/13
 */
@Repository
public interface WalletAccountDao {

    @Select("SELECT id, user_id AS userId, balance, freeze_balance AS freezeBalance, `status`, create_time as createDate, update_time AS updateDate  FROM tb_wallet_account WHERE id = #{userId}")
    WalletAccount getAccount(@Param("userId") Integer userId);

    @Select("SELECT id, user_id AS userId, user_name AS userName, card_number AS cardNumber, `status`, is_authorization as isAuthorization, is_change_free AS isChangeFree, create_time AS createDate, update_time AS updateDate FROM tb_wallet_card WHERE user_id = #{userId}")
    List<WalletCard> getCards(@Param("userId") Integer userId);

    @Update("UPDATE tb_wallet_account SET balance = #{money}, update_time = #{date} WHERE id = #{id}")
    Integer updateAccout(@Param("money") BigDecimal money, @Param("date") Date now, @Param("id") Integer id);

    @Insert("INSERT INTO tb_wallet_account_detail (id, account_id, user_Id, wallet_id, money, type, create_time, update_time) VALUES (null, #{accountId}, #{userId}, #{walletId}, #{money}, #{type}, #{date}, #{date});")
    Integer insertAccountDetail(@Param("accountId") Integer accountId, @Param("userId") Integer userId,
                                @Param("walletId") Integer walletId, @Param("money") BigDecimal money, @Param("type") Integer type, @Param("date") Date now);

    @Insert("INSERT INTO tb_wallet_account (id, user_id, balance, freeze_balance, `status`, create_time, update_time)\n" +
            " VALUES (null, #{userId}, #{zeroMoney}, #{zeroMoney}, #{status}, #{date}, #{date})")
    Integer insertAccount(@Param("userId") Integer userId, @Param("zeroMoney") BigDecimal zeroMoney, @Param("status") Integer status, @Param("date") Date now);
}

