package com.xxy.wallet.filter;

import com.xxy.wallet.dao.UserDao;
import com.xxy.wallet.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 15:48 2020/9/11
 */
@WebFilter(filterName = "WalletFilter", urlPatterns = "/api/xxy/wallet/*")
public class WalletFilter implements Filter {

    public static final String USER_ID_KEY = "user_id";

    @Autowired
    private UserDao userDao;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //如果请求中包含return则放行
        if (req.getRequestURI().contains("return")){
            chain.doFilter(request,response);
            return;
        }
        //从请求头中获取用户token
        String token = req.getHeader("AUTHTOKEN");
        if(StringUtils.isEmpty(token)){
            Cheak(resp);
        }
        User user = userDao.getUserInfo(token);
        if (null == user){
            //身份校验失败
            Cheak(resp);
        }else {
            //将用户信息放进request中
            req.setAttribute(USER_ID_KEY, user.getId());
            //请求放行
            chain.doFilter(request,response);
        }
    }

    private void Cheak(HttpServletResponse response) throws IOException{
        //身份验证失败
        response.setStatus(401);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("{\"errors\":{\"message\":\"permission validation failed\",\"status_code\":\"20000\"}}");
        response.getWriter().flush();
        return;
    }
    @Override
    public void destroy() {

    }
}
