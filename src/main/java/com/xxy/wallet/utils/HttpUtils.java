package com.xxy.wallet.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 19:49 2020/9/22
 */
@Component
public class HttpUtils {

    /**
     * 加载日志配置
     */
    Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public String httpPost(String url, String param) throws IOException {
        CloseableHttpResponse response = null;
                //创建HttpClients连接
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //实例化一个Post请求
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        //设置编码格式
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(param, charSet);
        httpPost.setEntity(entity);
        try{
             //发送请求
             response = httpClient.execute(httpPost);
             //获取发送请求状态
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK){
                HttpEntity entity1 = response.getEntity();
                return EntityUtils.toString(entity1, "UTF-8");
            }else {
                logger.error("register schedule fail, status is :" + statusCode);
            }
        }catch (Exception e){
            logger.error("send post is fail" + e);
        }finally {
            //释放资源
            assert response != null;
            response.close();
            httpClient.close();
        }
        return null;
    }

}
