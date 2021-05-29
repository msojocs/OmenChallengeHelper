package org.omenhelper.Omen;

import org.apache.hc.core5.http.ParseException;
import org.omenhelper.Utils.HTTP.HttpUtil2;
import org.omenhelper.Utils.HTTP.HttpUtilEntity;
import org.omenhelper.Utils.JsonUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author jiyec
 * @Date 2021/5/29 19:58
 * @Version 1.0
 **/
public class Device {
    private final String version = "2";
    private String deviceId;
    private final Map<String, Object> userInfo;
    private final HttpUtil2 httpUtil2 = new HttpUtil2();
    private final Map<String, String> header = new HashMap<String, String>(){{
        put("Content-Type", "application/json");
        put("Accept", "application/json");
        put("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");
    }};

    public Device(String authorization) {
        String s = new String(Base64.getDecoder().decode(authorization.split("\\.")[1]));
        userInfo = JsonUtil.string2Obj(s, Map.class);
        header.put("Authorization", "Bearer " + authorization);
    }

    // 1
    public void sendInfo(){
        Map<String, String> deviceInfo = new HashMap<>();
        deviceInfo.put("deviceName", "DESKTOP-A1B2C3");
        deviceInfo.put("friendlyName", "DESKTOP-A1B2C3");
        deviceInfo.put("deviceType", "client");
        deviceInfo.put("hardwareId", "92-AC-D4-EF-B8-84-27-AD-F2-27-DF-BA-69-2D-C9-47-9A-C9-72-E0-AB-23-6E-5C-1A-EE-14-EB-9A-3C-A0-2C");
        deviceInfo.put("chassisType", "desktop");

        try {
            String url = "https://www.hpgamestream.com/api/device";
            HttpUtilEntity httpUtilEntity = httpUtil2.doStreamPost(url, JsonUtil.obj2String(deviceInfo).getBytes(StandardCharsets.UTF_8), header);
            String body = httpUtilEntity.getBody();
            Map retDetail = JsonUtil.string2Obj(body, Map.class);
            deviceId = (String)retDetail.get("deviceId");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2
    public void sendGetEmpty(){
        Map<String, String> tHeader = new HashMap<>();
        tHeader.put("Isen-Id", (String)userInfo.get("user_id"));
        tHeader.put("DeviceId", deviceId);
        tHeader.put("Version", "1");
        tHeader.put("Content-Type", "application/json");
        tHeader.put("Accept", "application/json");
        tHeader.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");

        try {
            HttpUtilEntity httpUtilEntity = httpUtil2.doGetEntity("https://www.hpgamestream.com/api/device", tHeader);
            System.out.println(httpUtilEntity);
            httpUtilEntity = httpUtil2.doGetEntity("https://www.hpgamestream.com/api/device", tHeader);
            System.out.println(httpUtilEntity);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // 3
    public void getDetail(){
        header.put("Isen-Id", (String)userInfo.get("user_id"));
        header.put("DeviceId", deviceId);
        header.put("Version", "2");

        try {
            String detail = httpUtil2.doGet2("https://www.hpgamestream.com/api/device", header);
            System.out.println(detail);
            header.put("Version", "1");
            detail = httpUtil2.doGet2("https://www.hpgamestream.com/api/device", header);
            System.out.println(detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    // 4
    public void register(){
        String url = "https://www.hpgamestream.com/api/device/" + deviceId + "/rewards/register";
        try {
            String s = httpUtil2.doGet2(url, header);
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> genHardWareId(){
        String uuid1 = UUID.randomUUID().toString();
        uuid1 = uuid1.replaceAll("-", "").toUpperCase();

        String uuid2 = UUID.randomUUID().toString();
        uuid2 = uuid2.replaceAll("-", "").toUpperCase();

        String desktop = "DESKTOP-" + uuid1.substring(0, 3) + uuid2.substring(0, 3);

        StringBuilder uuid = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            uuid.append(uuid1.charAt(i)).append(uuid2.charAt(i));
            if(i < 31)uuid.append("-");
        }
        return new HashMap<String, String>(){{
            put("desktop", desktop);
            put("hardwareId", uuid.toString());
        }};
    }
}
