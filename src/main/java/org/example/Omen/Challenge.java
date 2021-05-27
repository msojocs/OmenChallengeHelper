package org.example.Omen;

import lombok.extern.slf4j.Slf4j;
import org.example.App;
import org.example.Omen.Body.AllChallengeListBody;
import org.example.Omen.Body.ChallengePostBody;
import org.example.Omen.Body.CurrentChallengeListBody;
import org.example.Omen.Body.JoinChallengeBody;
import org.example.Utils.HTTP.HttpUtil;
import org.example.Utils.HTTP.HttpUtilEntity;
import org.example.Utils.JsonUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jiyec
 * @Date 2021/5/27 12:21
 * @Version 1.0
 **/
@Slf4j
public class Challenge {

    public static final String API = "https://rpc-prod.versussystems.com/rpc";
    public static final Map<String, String> HEADERS = new HashMap<String, String>() {{
        put("Content-Type", "application/json; charset=utf-8");
        put("User-Agent", "");
    }};
    private final String applicationId;
    private final String sessionToken;

    public Challenge(String applicationId, String sessionToken) {
        this.applicationId = applicationId;
        this.sessionToken = sessionToken;
    }

    /**
     * 获取所有挑战，对应[Available Rewards]
     *
     * @return String[] {challengeStructureId, campaignId, relevantEvent}
     */
    public List<String[]> getAllList() {
        Map<String, Object> body = new AllChallengeListBody(applicationId, sessionToken).genBody();
        List<String[]> allList = new ArrayList<>();
        HttpUtilEntity ret = request(body);
        if(ret == null)return allList;

        Map listMap = JsonUtil.string2Obj(ret.getBody(), Map.class);
        List<Map<String, Object>> collection = (List<Map<String, Object>>) (((Map) listMap.get("result")).get("collection"));

        collection.forEach(item -> {
            String challengeStructureId = (String) (item.get("challengeStructureId"));
            List<String> relevantEvents = (List<String>) item.get("relevantEvents");
            Map<String, Object> prize = (Map<String, Object>) (item.get("prize"));
            String category = (String) prize.get("category");
            String campaignId = (String) prize.get("campaignId");
            if ("sweepstake".equals(category)) {
                allList.add(new String[]{
                        challengeStructureId,
                        campaignId,
                        relevantEvents.get(0)
                });
            }
        });

        return allList;
    }

    // 参加挑战
    public boolean join(String challengeStructureId, String campaignId) {
        Map<String, Object> body = new JoinChallengeBody(applicationId, sessionToken).genBody(campaignId, challengeStructureId);
        HttpUtilEntity ret = request(body);
        if(ret == null)return false;
        return true;
    }

    // 获取进行中的任务
    public List<String> currentList(){
        Map<String, Object> body = new CurrentChallengeListBody(applicationId, sessionToken).genBody();
        List<String> currentList = new ArrayList<>();
        HttpUtilEntity ret = request(body);
        if(ret == null) return null;
        log.info("{}", ret);
        Map listMap = JsonUtil.string2Obj(ret.getBody(), Map.class);
        List<Map<String, Object>> collection = (List<Map<String, Object>>) (((Map) listMap.get("result")).get("collection"));

        collection.forEach(item -> {
            List<String> relevantEvents = (List<String>) item.get("relevantEvents");
            Map<String, Object> prize = (Map<String, Object>) (item.get("prize"));
            String category = (String) prize.get("category");
            if ("sweepstake".equals(category)) {
                currentList.add(relevantEvents.get(0));
            }
        });

        return currentList;
    }

    // 执行挑战
    public boolean doIt(String eventName, int playTime) {
        Map<String, Object> body = new ChallengePostBody(applicationId, sessionToken).genBody(eventName, playTime);

        // 发送请求
        log.info("开始发送请求");
        HttpUtilEntity ret = request(body);
        if(ret == null)return false;
        log.info("请求完毕");

        // 解析结果
        Map<String, Object> retMap = JsonUtil.string2Obj(ret.getBody(), Map.class);
        List<Map<String, Object>> result = (List<Map<String, Object>>) retMap.get("result");

        result.forEach(o -> {
            List<String> relevantEvents = (List<String>) o.get("relevantEvents");
            int percentage = (int) o.get("progressPercentage");

            log.info("事件：{} -- 已完成 {}%", relevantEvents, percentage);
        });
        return true;
    }

    private HttpUtilEntity request(Map<String, Object> body) {
        HttpUtilEntity ret = null;
        try {
            ret = HttpUtil.doStreamPost(
                    API,
                    JsonUtil.obj2String(body).getBytes(StandardCharsets.UTF_8),
                    HEADERS
            );
            int code = ret.getStatusCode();
            if(code == 400){
                log.info("SESSION过期，请更新-响应码：{}, {}", ret.getStatusCode(), ret);
                return null;
            }else if (code != 200) {
                log.info("响应异常-响应码：{}, {}", ret.getStatusCode(), ret);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
