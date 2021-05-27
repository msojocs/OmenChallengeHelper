package org.example.Omen.Body;

import java.util.Map;

/**
 * @Author jiyec
 * @Date 2021/5/27 10:48
 * @Version 1.0
 **/
public class AllChallengeListBody extends BasicBody{
    private final String method = "mobile.challenges.v4.list";

    public AllChallengeListBody(String applicationId, String sessionToken) {
        super(applicationId, sessionToken);
    }

    public Map<String, Object> genBody(){
        body.put("method", method);
        params.put("onlyShowEligibleChallenges", true);
        params.put("page", 1);
        params.put("pageSize", 10);

        return gen();
    }


}
