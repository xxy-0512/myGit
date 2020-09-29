package com.xxy.wallet.service;

import com.alibaba.fastjson.JSONObject;
import com.xxy.wallet.commen.bean.Commens;
import com.xxy.wallet.commen.constant.WalletConstant;
import com.xxy.wallet.utils.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 17:41 2020/9/22
 */
@EnableAsync
@Service
public class EventService {
    /**
     * 日志
     */
    Logger logger = LoggerFactory.getLogger(EventService.class);

    /**
     * 任务中心host
     */
    @Value("${schedule.wallet.service}")
    private String host;
    /**
     * 调用方服务器名
     */
    @Value("${service.name}")
    private String serviceName;

    /**
     * 红包服务host
     */
    @Value("${wallet.service.host}")
    private String walletHost;

    @Autowired
    private EventService eventService;

    @Autowired
    private HttpUtils httpUtils;
    /**
     * 异步请求任务中心
     * @param userId 用户ID
     * @param walletObjectId 红包ID
     * @param now 时间
     */
    public void onEvent(Integer userId, Integer walletObjectId, Date now) throws IOException {
        //出发时间
        Date trigger = eventService.getNextDayDate(now);
        //将触发时间改为Cron表达式
        String dateTime = Commens.dateTimeFormat().format(trigger);
        //拼接Cron表达式
        String crons = getCron(dateTime);
        //拼接任务中心注册url
        String url = "http://" + host + "/api/xxy/job";
        //回调函数Url
        String backUrl = "http://" + walletHost + "/api/xxy/wallet/return?userId=" + userId + "&walletObjectId=" + walletObjectId;
        //uuId
        String id = UUID.randomUUID().toString();
        JSONObject parm = eventService.parm(backUrl, crons, id, now);
        //发送Post请求
        String httpPost = httpUtils.httpPost(url, parm.toJSONString());
        if(StringUtils.isEmpty(httpPost)){
            logger.error("add event fail");
        }else {
            logger.info("add event success");
        }
    }

    /**
     * 封装参数
     * @param backUrl 回调地址
     * @param crons cron表达式
     * @param id 任务ID
     * @return
     */
    private JSONObject parm(String backUrl, String crons, String id, Date now) {
        JSONObject jobs = new JSONObject();
        JSONObject object = new JSONObject();
        object.put("callBack", backUrl);
        object.put("crons", crons);
        object.put("id", id);
        object.put("type", WalletConstant.JobsType.ONCE);
        object.put("beginTime", now);
        object.put("serviceName", serviceName);
        jobs.put("jobs", object);
        return jobs;
    }

    /**
     * crom表达式拼接
     * @param dateTime String类型时间字符串
     * @return
     */
    private String getCron(String dateTime) {
        //将字符串按：进行分割
        String[] split = dateTime.split(":");
        //拼接Cron表达式
        return split[2] + " " + split[1] + " " + split[0] + " * * ?";
    }


    /**
     * 获取24小时之后的时间
     * @param now
     * @return
     */
    private Date getNextDayDate(Date now) {
        //Calendar对象获取时间
        Calendar calendar = Calendar.getInstance();
        //需要增加的时间
        calendar.setTime(now);
        //时间后24小时
        calendar.add(Calendar.MINUTE, +2);
        return calendar.getTime();
    }
}
