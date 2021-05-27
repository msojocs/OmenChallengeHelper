package org.example.Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public abstract class SignatureUtils {
    private static final String ALGORITHM = "HmacSHA256";

    public static boolean valid(String message, String secret, byte[] signature) {
        return signature != null && Arrays.equals(signature, sign(message.getBytes(StandardCharsets.UTF_8), secret.getBytes(StandardCharsets.UTF_8)));
    }

    public static byte[] sign(byte[] message, byte[] secret) {
        try {

            Mac hmac = Mac.getInstance(ALGORITHM);
            SecretKeySpec secret_key = new SecretKeySpec(secret, ALGORITHM);
            hmac.init(secret_key);
            // System.out.println("service sign is "+byteArrayToHexString(bytes));
            return hmac.doFinal(message);
            // return byteArrayToHexString(bytes);
        } catch (Exception ex) {
            System.out.println("签名错误：" + ex);
        }
        return null;
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder hs = new StringBuilder();
        String tempStr;
        for (int index = 0; bytes != null && index < bytes.length; index++) {
            tempStr = Integer.toHexString(bytes[index] & 0XFF);
            if (tempStr.length() == 1)
                hs.append('0');
            hs.append(tempStr);
        }
        return hs.toString().toLowerCase();
    }

}