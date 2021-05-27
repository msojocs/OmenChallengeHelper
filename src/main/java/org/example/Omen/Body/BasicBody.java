package org.example.Omen.Body;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author jiyec
 * @Date 2021/5/27 10:50
 * @Version 1.0
 **/
@Setter
public class BasicBody {
    protected String applicationId;
    protected String sessionToken;

    protected Map<String, Object> params = new LinkedHashMap<String, Object>(){{
        put("sdk", "custom01");
        put("sdkVersion", "3.0.0");
        put("appDefaultLanguage", "en");
        put("userPreferredLanguage", "en");
    }};
    protected Map<String, Object> body = new LinkedHashMap<String, Object>(){{
        put("jsonrpc", "2.0");
        put("params", params);
    }};

    public BasicBody(String applicationId, String sessionToken) {
        params.put("applicationId", applicationId);
        params.put("sessionToken", sessionToken);
        body.put("id", applicationId);
        this.applicationId = applicationId;
        this.sessionToken = sessionToken;
    }

    Map<String, Object> gen(){
        return body;
    }
}
