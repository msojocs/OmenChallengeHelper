package org.example.Omen.Body;

import java.util.Map;

/**
 * @Author jiyec
 * @Date 2021/5/27 12:09
 * @Version 1.0
 **/
public class JoinChallengeBody extends BasicBody{
    private String method = "mobile.challenges.v2.join";

    public JoinChallengeBody(String applicationId, String sessionToken) {
        super(applicationId, sessionToken);
    }

    /**
     * {
     *   "jsonrpc": "2.0",
     *   "id": "6589915c-6aa7-4f1b-9ef5-32fa2220c844",
     *   "method": "mobile.challenges.v2.join",
     *   "params": {
     *     "sessionToken": "",
     *     "applicationId": "6589915c-6aa7-4f1b-9ef5-32fa2220c844",
     *     "campaignId": "5e7adabe-950f-4790-af3c-429f8f9cde9b",
     *     "challengeStructureId": "793fae05-429f-4bae-b733-4ce3a128869c",
     *     "sdk": "custom01",
     *     "sdkVersion": "3.0.0",
     *     "timezone": "China Standard Time",
     *     "appDefaultLanguage": "en",
     *     "userPreferredLanguage": "en"
     *   }
     * }
     */
    public Map<String, Object> genBody(String campaignId, String challengeStructureId){
        body.put("method", method);

        params.put("campaignId", campaignId);
        params.put("challengeStructureId", challengeStructureId);
        params.put("timezone", "China Standard Time");

        return gen();
    }
}
