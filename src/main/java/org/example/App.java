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

    public static void main( String[] args ) throws Exception {

        // 输入SESSION
        // String sessionToken = System.getenv("SESSION");
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入SESSION：");
        String sessionToken = scanner.next();

        // 生成数据
        String applicationId = "6589915c-6aa7-4f1b-9ef5-32fa2220c844";

        Challenge challenge = new Challenge(applicationId, sessionToken);

        log.info("获取可参与挑战列表");
        List<String[]> allList = challenge.getAllList();
        log.info("可加入的挑战数：{}", allList.size());
        allList.forEach(s->{
            log.info("加入挑战：{}", s);
            challenge.join(s[0], s[1]);
        });

        List<String> eventList = challenge.currentList();
        log.info("待完成任务数：{}", eventList.size());
        eventList.forEach(en-> {
            log.info("当前执行任务：{}", en);
            challenge.doIt(en, 45);
        });

    }

}
