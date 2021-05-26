package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.HTTP.HttpUtil;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Hello world!
 *
 */
@Slf4j
public class App 
{
    public static void main( String[] args ) throws Exception {
        // 游戏项目，抓包取得
        String[] eventName = {
                "PLAY:LEAGUE_OF_LEGENDS"
        };
        // 需要做任务的id eventName的下标
        int eventId = 0;
        // genTime参数为玩的时间，3为玩了3分钟
        String[] time = genTime(3);

        // 输入SESSION
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入SESSION：");
        String sessionToken = scanner.next();

        // 生成数据
        String applicationId = "6589915c-6aa7-4f1b-9ef5-32fa2220c844";
        String start = time[0];
        String end = time[1];


        String signature = new VsJSONProgressEvent(
                eventName[eventId],
                start,
                end,
                1
        ).GetSignature(
                applicationId,
                sessionToken);

        Map<String, Object> params = new HashMap<String, Object>(){{
            put("applicationId", applicationId);
            put("sessionToken", sessionToken);
            put("startedAt", start);
            put("endedAt", end);
            put("eventName", eventName[eventId]);
            put("value", 1);
            put("sdk", "custom01");
            put("sdkVersion", "3.0.0");
            put("signature", signature);
            put("appDefaultLanguage", "en");
            put("userPreferredLanguage", "en");
        }};
        Map<String, Object> body = new HashMap<String, Object>(){{
           put("jsonrpc", "2.0");
           put("id", applicationId);
           put("method", "mobile.challenges.v2.progressEvent");
           put("params", params);
        }};

        Map<String, String> h = new HashMap<String, String>(){{
            put("Content-Type", "application/json");
            put("User-Agent", "");
        }};

        // 发送请求
        log.info("开始发送请求");
        String ret = HttpUtil.doStreamPost(
                "https://rpc-prod.versussystems.com/rpc",
                JsonUtil.obj2String(body).getBytes(StandardCharsets.UTF_8),
                h
                );

        log.info("请求完毕");
        // 解析结果
        Map<String, Object> retMap = JsonUtil.string2Obj(ret, Map.class);
        List<Map<String, Object>> result = (List<Map<String, Object>>)retMap.get("result");

        result.forEach(o->{
            List<String> relevantEvents = (List<String>)o.get("relevantEvents");
            int percentage = (int)o.get("progressPercentage");

            log.info("事件：{} -- 已完成 {}%", relevantEvents, percentage);
        });

    }

    /**
     * 计算玩耍时间
     * @param playTime  分钟
     * @return
     */
    private static String[] genTime(int playTime){

        SimpleDateFormat endTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

        endTime.setTimeZone(TimeZone.getTimeZone("+0"));

        String start = endTime.format(new Date(System.currentTimeMillis() - 1000 * 60 * playTime));
        String end = endTime.format(new Date());


        return new String[]{
                start,
                end
        };
    }
}
