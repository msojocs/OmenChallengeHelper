package org.omenhelper;

import lombok.extern.slf4j.Slf4j;
import org.omenhelper.Omen.Challenge;
import org.omenhelper.Omen.Login;
import org.omenhelper.Utils.JsonUtil;

import java.util.*;

/**
 * Hello world!
 *
 */
@Slf4j
public class App 
{

    public static void main( String[] args ) throws Exception {

        Scanner scanner = new Scanner(System.in);
        String sessionToken;
        Login login = new Login();
        log.info("登录准备");
        login.webPrepare();

        System.out.print("请输入EMAIL：");
        String email = scanner.nextLine();
        login.setEmail(email);
        log.info("开始账号检查");
        login.idpProvider();
        System.out.print("请输入密码：");
        String pass = scanner.nextLine();
        login.setPass(pass);
        String localhostUrl = login.webLogin();
        log.info("开始模拟Omen登录操作");
        String tokenInfo = login.clientLogin(localhostUrl);
        Map akMap = JsonUtil.string2Obj(tokenInfo, Map.class);

        // 设备处理，按需
        // Device device = new Device((String) akMap.get("access_token"));
        // device.sendInfo();
        // device.sendGetEmpty();
        // device.getDetail();
        // device.register();

        log.info("开始获取挑战SESSION");
        sessionToken = login.genSession((String) akMap.get("access_token"));

        // System.out.print("请输入SESSION：");
        // sessionToken = scanner.nextLine();

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

        List<Map<String, Object>> eventList = challenge.currentList();
        log.info("待完成任务数：{}", eventList.size());
        System.out.println("注意，时间设置细节：\n要小于等于当前时间减去上一次提交任务的时间");
        eventList.forEach(en-> {
            System.out.println("");
            log.info("当前执行任务：{} - {}%", en.get("eventName"), en.get("progress"));
            System.out.print("请输入玩的时间（分钟）：");
            String str = scanner.nextLine();
            int time;
            try{
                time = Integer.parseInt(str);
            }catch (NumberFormatException e){
                System.out.println("输入字符非法，使用45作为默认值");
                // 默认值
                time = 45;
            }
            Map<String, Object> result = challenge.doIt((String) en.get("eventName"), time);
            if((int)result.get("progress") == (int)en.get("progress")){
                System.out.println("进度没有变化，你设置的时间不合理！(时间要小于等于当前时间减去上一次提交任务的时间)");
            }
        });

    }

}
