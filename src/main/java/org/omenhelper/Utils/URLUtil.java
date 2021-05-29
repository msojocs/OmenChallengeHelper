package org.omenhelper.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author jiyec
 * @Date 2021/5/1 15:47
 * @Version 1.0
 **/
public class URLUtil {
    public static String getRealPath(String url){
        url = url.replaceAll("/\\w+/\\.\\./", "/");
        url = url.replaceAll("/\\./", "/");
        return url;
    }

    /**
     * 解析url
     *
     * @param url
     * @return
     */
    public static Map<String, String> getURLQuery(String url) {
        if (url == null) {
            return null;
        }
        url = url.trim();
        if (url.equals("")) {
            return null;
        }
        String[] urlParts = url.split("\\?");
        String baseUrl = urlParts[0];
        //没有参数
        if (urlParts.length == 1) {
            return null;
        }
        //有参数
        String[] paramSplit = urlParts[1].split("&");
        Map<String, String> params = new HashMap<>();
        for (String param : paramSplit) {
            String[] keyValue = param.split("=");
            params.put(keyValue[0], keyValue[1]);
        }

        return params;
    }
}
