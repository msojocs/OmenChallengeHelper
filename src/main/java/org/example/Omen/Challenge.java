package org.example.Omen;

import lombok.extern.slf4j.Slf4j;
import org.example.App;
import org.example.Omen.Body.AllChallengeListBody;
import org.example.Omen.Body.ChallengePostBody;
import org.example.Omen.Body.JoinChallengeBody;
import org.example.Utils.HTTP.HttpUtil;
import org.example.Utils.HTTP.HttpUtilEntity;
import org.example.Utils.JsonUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @Author jiyec
 * @Date 2021/5/27 12:21
 * @Version 1.0
 **/
@Slf4j
public class Challenge {

    private String applicationId;
    private String sessionToken;

    public Challenge(String applicationId, String sessionToken) {
        this.applicationId = applicationId;
        this.sessionToken = sessionToken;
    }

    // 获取所有挑战，对应[Available Rewards]
    public List<Object> getAllList(){
        Map<String, Object> body = new AllChallengeListBody(applicationId, sessionToken).genBody();
        try {
            HttpUtilEntity ret = HttpUtil.doStreamPost(
                    "https://rpc-prod.versussystems.com/rpc",
                    JsonUtil.obj2String(body).getBytes(StandardCharsets.UTF_8),
                    App.HEADERS
            );
            int code = ret.getStatusCode();
            if(code != 200) {
                log.info("响应异常{}", ret);
                return null;
            }
            // JsonUtil.string2Obj(ret.getBody(), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 参加挑战
    public void join(String campaignId, String challengeStructureId){
        Map<String, Object> body = new JoinChallengeBody(applicationId, sessionToken).genBody(campaignId, challengeStructureId);
        try {
            HttpUtilEntity ret = HttpUtil.doStreamPost(
                    "https://rpc-prod.versussystems.com/rpc",
                    JsonUtil.obj2String(body).getBytes(StandardCharsets.UTF_8),
                    App.HEADERS
            );
            System.out.println(ret);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 执行挑战
    public void doIt(String eventName, int playTime){
        Map<String, Object> body = new ChallengePostBody(applicationId, sessionToken).genBody(eventName, playTime);

        // 发送请求
        log.info("开始发送请求");
        HttpUtilEntity ret = null;
        try {
            ret = HttpUtil.doStreamPost(
                    "https://rpc-prod.versussystems.com/rpc",
                    JsonUtil.obj2String(body).getBytes(StandardCharsets.UTF_8),
                    App.HEADERS
            );
            log.info("请求完毕");
            // 解析结果
            Map<String, Object> retMap = JsonUtil.string2Obj(ret.getBody(), Map.class);
            List<Map<String, Object>> result = (List<Map<String, Object>>)retMap.get("result");

            result.forEach(o->{
                List<String> relevantEvents = (List<String>)o.get("relevantEvents");
                int percentage = (int)o.get("progressPercentage");

                log.info("事件：{} -- 已完成 {}%", relevantEvents, percentage);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
