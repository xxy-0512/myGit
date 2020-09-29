package com.xxy.wallet.dao;

import com.xxy.wallet.commen.bean.UserObj;
import com.xxy.wallet.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 16:26 2020/9/11
 */
@Repository
public interface UserDao {
    /**
     * 根据token 获取用户信息
     * @param token 用户登陆验证码
     * @return
     */
    User getUserInfo(@Param("token") String token);

    @Select("SELECT user_name AS name FROM tb_userInfo WHERE id = #{id}")
    UserObj getUserInfoById(Integer receiveUserId);
}
