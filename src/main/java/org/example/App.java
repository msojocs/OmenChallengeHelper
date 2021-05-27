package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.Omen.Challenge;

import java.util.*;

/**
 * Hello world!
 *
 */
@Slf4j
public class App 
{
    public static final String API = "https://rpc-prod.versussystems.com/rpc";
    public static final Map<String, String> HEADERS = new HashMap<String, String>(){{
        put("Content-Type", "application/json; charset=utf-8");
        put("User-Agent", "");
    }};

    public static void main( String[] args ) throws Exception {
        // 游戏项目，抓包取得
        String[] eventName = {
                "PLAY:LEAGUE_OF_LEGENDS",
                "PLAY:WORLD_OF_WARCRAFT"
        };
        // 需要做任务的id eventName的下标
        int eventId = 1;
        //游戏时间
        int playTime = 2;

        // 输入SESSION
        // String sessionToken = System.getenv("SESSION");
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入SESSION：");
        String sessionToken = scanner.next();

        // 生成数据
        String applicationId = "6589915c-6aa7-4f1b-9ef5-32fa2220c844";

        Challenge challenge = new Challenge(applicationId, sessionToken);
        // challenge.getAllList();
        challenge.doIt(eventName[eventId], playTime);

    }

}
