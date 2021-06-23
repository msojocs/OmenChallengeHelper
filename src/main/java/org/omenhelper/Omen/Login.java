package org.omenhelper.Omen;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.omenhelper.Omen.Body.HandshakeBody;
import org.omenhelper.Omen.Body.StartBody;
import org.omenhelper.Utils.HTTP.HttpUtil2;
import org.omenhelper.Utils.HTTP.HttpUtilEntity;
import org.omenhelper.Utils.JsonUtil;
import org.omenhelper.Utils.URLUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author jiyec
 * @Date 2021/5/29 14:15
 * @Version 1.0
 **/
@Slf4j
public class Login {
    @Setter
    private String email;
    @Setter
    private String pass;
    private final String applicationId = "6589915c-6aa7-4f1b-9ef5-32fa2220c844";
    private final String client_id = "130d43f1-bb22-4a9c-ba48-d5743e84d113";
    private String idpProvider = "hpid";

    HttpUtil2 httpUtil2 = new HttpUtil2();
    private String backendCsrf;
    private String baseUrl;

    public Login() {
    }

    public Login(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    @Deprecated
    public String doIt(){

        String localhostUrl = webLogin();
        log.info("开始模拟Omen登录操作");
        String tokenInfo = clientLogin(localhostUrl);
        Map akMap = JsonUtil.string2Obj(tokenInfo, Map.class);

        // 设备处理，按需
        // Device device = new Device((String) akMap.get("access_token"));
        // device.sendInfo();
        // device.sendGetEmpty();
        // device.getDetail();
        // device.register();

        log.info("开始获取挑战SESSION");
        return genSession((String) akMap.get("access_token"));
    }

    public void webPrepare(){
        Map<String, Object> config = new HashMap<String, Object>(){{
            put("redirection", 1);
        }};
        Map<String, String> header = new HashMap<String, String>(){{
            put("Content-Type", "application/json;charset=utf-8");
        }};
        String url = "https://oauth.hpbp.io/oauth/v1/auth?response_type=code&client_id=" + client_id +"&redirect_uri=http://localhost:9081/login&scope=email+profile+offline_access+openid+user.profile.write+user.profile.username+user.profile.read&state=G5g495-R4cEE" + (Math.random()*100000) +"&max_age=28800&acr_values=urn:hpbp:hpid&prompt=consent";
        String backendUrl = "https://ui-backend.id.hp.com/bff/v1/auth/session";
        try {
            httpUtil2.updateConfig(config);
            HttpUtilEntity httpUtilEntity = httpUtil2.doGetEntity(url);

            String location = httpUtilEntity.getHeaders().get("location");
            Map<String, String> body = new HashMap<String, String>(){{
                put("flow", location.substring(location.indexOf("=") + 1));
            }};
            HttpUtilEntity temp = httpUtil2.doStreamPost(backendUrl, JsonUtil.obj2String(body).getBytes(StandardCharsets.UTF_8), header);
            Map result = JsonUtil.string2Obj(temp.getBody(), Map.class);
            backendCsrf = (String) result.get("csrfToken");
            baseUrl = (String) result.get("regionEndpointUrl");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void idpProvider(){
        String url = baseUrl + "/session/check-username";
        Map<String, String> data = new HashMap<String, String>(){{
            put("username", email);
        }};
        Map<String, String> header = new HashMap<String, String>(){{
            put("Content-Type", "application/json;charset=utf-8");
            put("csrf-token", backendCsrf);
        }};
        Map<String, Object> result = null;
        try {
            HttpUtilEntity httpUtilEntity = httpUtil2.doStreamPost(url, JsonUtil.obj2String(data).getBytes(StandardCharsets.UTF_8), header);
            result = JsonUtil.string2Obj(httpUtilEntity.getBody(), Map.class);
            if("captchaRequired".equals(result.get("error"))){
                log.info("请求过于频繁，需要验证码！信息 -- {}", result);
                log.info("请登录一次， https://myaccount.id.hp.com/uaa");
                System.exit(-1);
            }
            List<Map<String, String>> identify = (List<Map<String, String>>) result.get("identities");
            if(identify == null || identify.size() == 0){
                log.info("检查账号出错！信息 -- {}", result);
                System.exit(-1);
            }
            idpProvider = identify.get(0).get("idpProvider");
            log.info("ID类型：{}, 地区：{}", idpProvider, identify.get(0).get("locale"));
        }catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
    public String webLogin(){

        String loginAddr = baseUrl + "/session/username-password";
        Map<String, String> data = new HashMap<String, String>(){{
            put("username", email + "@" + idpProvider);
            put("password", pass);
        }};
        Map<String, String> header = new HashMap<String, String>(){{
            put("Content-Type", "application/json;charset=utf-8");
            put("csrf-token", backendCsrf);
        }};
        try {
            HttpUtilEntity httpUtilEntity = httpUtil2.doStreamPost(loginAddr, JsonUtil.obj2String(data).getBytes(StandardCharsets.UTF_8), header);
            Map<String, String> result = JsonUtil.string2Obj(httpUtilEntity.getBody(), Map.class);
            if(!"success".equals(result.get("status"))){
                System.out.println("登录失败~");
                log.info(httpUtilEntity.getBody());
                System.exit(-1);
            }
            String nextUrl = result.get("nextUrl");
            httpUtil2.updateConfig(new HashMap<String, Object>(){{
                put("redirection", 0);
            }});
            HttpUtilEntity httpUtilEntity1 = httpUtil2.doGetEntity(nextUrl);
            return httpUtilEntity1.getHeaders().get("location");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String clientLogin(String localhostUrl){
        Map<String, String> urlQuery = URLUtil.getURLQuery(localhostUrl);
        String oauthUrl = "https://oauth.hpbp.io/oauth/v1/token";
        Map<String, String> body = new LinkedHashMap<String, String>(){{
            put("grant_type", "authorization_code");
            put("code", urlQuery.get("code"));
            put("client_id", client_id);
            put("redirect_uri", "http://localhost:9081/login");
        }};
        try {
            Thread.sleep(3000);
            return httpUtil2.doPost(oauthUrl, body, new LinkedHashMap<String, String>(){{
                put("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");
                put("Accept", "application/json");
                put("Except", "100-continue");
            }}, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String genSession(String authorization){
        // https://www.hpgamestream.com/api/thirdParty/session/temporaryToken?applicationId=6589915c-6aa7-4f1b-9ef5-32fa2220c844
        Map<String, String> header = new HashMap<String, String>(){{
            put("Authorization", "Bearer " + authorization);
        }};
        try {
            String url1 = "https://www.hpgamestream.com/api/thirdParty/session/temporaryToken?applicationId=" + applicationId;
            String tokenBody = httpUtil2.doGet2(url1, header);
            log.debug(tokenBody);
            Map tokenMap = JsonUtil.string2Obj(tokenBody, Map.class);
            String token = (String) tokenMap.get("token");

            // mobile.accounts.v1.handshake
            Map<String, Object> handshakeBody = new HandshakeBody(applicationId, null).genBody(token);
            HttpUtilEntity httpUtilEntity = httpUtil2.doStreamPost("https://rpc-prod.versussystems.com/rpc", JsonUtil.obj2String(handshakeBody).getBytes(StandardCharsets.UTF_8), new HashMap<String, String>() {{
                put("Content-Type", "application/json;charset=utf-8");
            }});
            Map resultMap = JsonUtil.string2Obj(httpUtilEntity.getBody(), Map.class);
            token = (String) ((Map<String, Object>) resultMap.get("result")).get("token");

            // mobile.sessions.v2.start
            String[] split = authorization.split("\\.");
            String uinfo = new String(Base64.getDecoder().decode(split[1]));
            Map userMap = JsonUtil.string2Obj(uinfo, Map.class);
            String startBody = new StartBody(applicationId, token).genBody((String)userMap.get("hpid_user_id"));
            HttpUtilEntity startResult = httpUtil2.doStreamPost("https://rpc-prod.versussystems.com/rpc", startBody.getBytes(StandardCharsets.UTF_8), new HashMap<String, String>() {{
                put("Content-Type", "application/json;charset=utf-8");
            }});
            Map sessionMap = JsonUtil.string2Obj(startResult.getBody(), Map.class);
            Map<String, String> result = (Map<String, String>) sessionMap.get("result");
            return result.get("sessionId");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
