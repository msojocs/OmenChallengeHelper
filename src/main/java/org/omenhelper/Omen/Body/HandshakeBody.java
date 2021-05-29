package org.omenhelper.Omen.Body;

import java.util.Map;

/**
 * @Author jiyec
 * @Date 2021/5/29 16:47
 * @Version 1.0
 **/
public class HandshakeBody extends BasicBody{
    public HandshakeBody(String applicationId, String sessionToken) {
        super(applicationId, sessionToken);
    }

    public Map<String, Object> genBody(String token){
        body.put("method", "mobile.accounts.v1.handshake");
        params.put("userToken", token);
        params.put("Birthdate", "2001-01-02");
        return gen();
    }

}
