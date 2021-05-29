package org.omenhelper.Omen.Body;

import java.util.Map;

/**
 * @Author jiyec
 * @Date 2021/5/27 12:18
 * @Version 1.0
 **/
public class CurrentChallengeListBody extends BasicBody{
    private final String method = "mobile.challenges.v2.current";
    public CurrentChallengeListBody(String applicationId, String sessionToken) {
        super(applicationId, sessionToken);
    }

    public Map<String, Object> genBody(){
        body.put("method", method);

        params.put("page", 1);
        params.put("pageSize", 10);
        return gen();
    }
}
