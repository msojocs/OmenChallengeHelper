package org.omenhelper.Omen.Body;

import org.omenhelper.Omen.VsJSONProgressEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * @Author jiyec
 * @Date 2021/5/27 11:04
 * @Version 1.0
 **/
public class ChallengePostBody extends BasicBody{

    private final String method = "mobile.challenges.v2.progressEvent";

    public ChallengePostBody(String applicationId, String sessionToken) {
        super(applicationId, sessionToken);
    }


    /**
     * {
     *   "jsonrpc": "2.0",
     *   "id": "6589915c-6aa7-4f1b-9ef5-32fa2220c844",
     *   "method": "mobile.challenges.v2.progressEvent",
     *   "params": {
     *     "applicationId": "6589915c-6aa7-4f1b-9ef5-32fa2220c844",
     *     "sessionToken": "",
     *     "startedAt": "2021-05-27T04:09:03.725462Z",
     *     "endedAt": "2021-05-27T04:09:21.782303Z",
     *     "eventName": "PLAY:LEAGUE_OF_LEGENDS",
     *     "value": 1,
     *     "sdk": "custom01",
     *     "sdkVersion": "3.0.0",
     *     "signature": "BqasFes+kiAcQLyJH/LJckQKDJWHEQDejViBzeoGSqg=",
     *     "appDefaultLanguage": "en",
     *     "userPreferredLanguage": "en"
     *   }
     * }
     * @param eventName
     * @param playTime
     * @return
     */
    public Map<String, Object> genBody(String eventName, int playTime){
        String[] time = genTime(playTime);

        body.put("method", method);

        params.put("startedAt", time[0]);
        params.put("endedAt", time[1]);
        params.put("eventName", eventName);
        params.put("value", 1);
        try {
            params.put("signature", new VsJSONProgressEvent(
                    eventName,
                    time[0],
                    time[1],
                    1
            ).GetSignature(applicationId,sessionToken));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gen();
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
