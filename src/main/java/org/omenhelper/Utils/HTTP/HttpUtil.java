package org.omenhelper.Utils.HTTP;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.io.ManagedHttpClientConnectionFactory;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.Header;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.config.CharCodingConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * 网络请求工具类[公共]
 *
 * @author jiyec
 */
public class HttpUtil {

    private static final CustomCookieStore httpCookieStore;
    private static final HttpClientContext defaultContext;
    private static final RequestConfig.Builder unBuildConfig;
    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";

    // 采用静态代码块，初始化超时时间配置，再根据配置生成默认httpClient对象
    static {

        // SSL准备
        SSLContext sslContext = null;
        SSLConnectionSocketFactory sslCSF = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslCSF = new SSLConnectionSocketFactory(sslContext);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }

        ManagedHttpClientConnectionFactory managedHttpClientConnectionFactory = new ManagedHttpClientConnectionFactory(
                null, CharCodingConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(StandardCharsets.UTF_8)
                .build(), null, null);
        // 连接池管理
        PoolingHttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslCSF)
                .setConnectionFactory(managedHttpClientConnectionFactory)
                .build();

        // Cookie存储
        httpCookieStore = new CustomCookieStore();

        unBuildConfig = RequestConfig.custom();

        // 配置
        final RequestConfig config = unBuildConfig
                .setConnectTimeout(Timeout.ofSeconds(5))
                .setRedirectsEnabled(false)
                // .setProxy(new HttpHost("127.0.0.1", 8866))      // TODO:开发环境设置代理
                .setCircularRedirectsAllowed(true)
                .build();

        // 动态配置
        defaultContext = HttpClientContext.create();
        defaultContext.setCookieStore(httpCookieStore);
        defaultContext.setRequestConfig(config);

        // 创建客户端
        httpClient = HttpClients.custom()
                .setDefaultCookieStore(httpCookieStore)
                .setDefaultRequestConfig(config)
                .setConnectionManager(cm)
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36 Edg/90.0.818.51")
                .build();
    }

    /**
     * 获取Cookie
     *
     * @return Map<String, String>
     */
    public static Map<String, String> getCookie() {
        List<Cookie> cookiesCustom = httpCookieStore.getCookiesCustom();
        Map<String, String> cookies = new HashMap<>();
        for (Cookie cookie : cookiesCustom) {
            cookies.put(cookie.getName(), cookie.getValue());
        }
        return cookies;
    }


    /**
     *    _____/\\\\\\\\\\\\__/\\\\\\\\\\\\\\\__/\\\\\\\\\\\\\\\_
     *     ___/\\\//////////__\/\\\///////////__\///////\\\/////__
     *      __/\\\_____________\/\\\___________________\/\\\_______
     *       _\/\\\____/\\\\\\\_\/\\\\\\\\\\\___________\/\\\_______
     *        _\/\\\___\/////\\\_\/\\\///////____________\/\\\_______
     *         _\/\\\_______\/\\\_\/\\\___________________\/\\\_______
     *          _\/\\\_______\/\\\_\/\\\___________________\/\\\_______
     *           _\//\\\\\\\\\\\\/__\/\\\\\\\\\\\\\\\_______\/\\\_______
     *            __\////////////____\///////////////________\///________
     *            FROM:http://patorjk.com/software/taag
     */

    /**
     * @param url 发送get请求的url
     * @return String 响应体
     */
    public static String doGet(String url) throws IOException, ParseException {
        return getString(Objects.requireNonNull(doGet(url, null, null, CHARSET, defaultContext)), CHARSET);
    }

    public static String doGet(String url, Map<String, String> params) throws IOException, ParseException {
        return getString(Objects.requireNonNull(doGet(url, params, null, CHARSET, defaultContext)), CHARSET);
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doGet(String url, Map<String, String> params, String charset) throws IOException, ParseException {
        return getString(Objects.requireNonNull(doGet(url, params, null, charset, defaultContext)), charset);
    }

    public static String doGet(String url, String charset, Map<String, String> headers) throws IOException, ParseException {
        return getString(Objects.requireNonNull(doGet(url, null, headers, charset, defaultContext)), charset);
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param headers 自定义的请求头信息
     * @return 页面内容
     */
    public static String doGet2(String url, Map<String, String> headers) throws IOException, ParseException {
        return getString(Objects.requireNonNull(doGet(url, null, headers, null, defaultContext)), CHARSET);
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url 请求的url地址 ?之前的地址
     * @return 页面内容
     */
    public static HttpUtilEntity doGetEntity(String url) throws IOException, ParseException {
        return doGetEntity(url, null, null, CHARSET);
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param headers 自定义的请求头信息
     * @return 页面内容
     */
    public static HttpUtilEntity doGetEntity(String url, Map<String, String> headers) throws IOException, ParseException {
        return doGetEntity(url, null, headers, CHARSET);
    }

    public static HttpUtilEntity doGetEntity(String url, Map<String, String> headers, String charset) throws IOException, ParseException {
        return doGetEntity(url, null, headers, charset);
    }

    /**
     * HTTP GET 请求 [实体方法]
     *
     * @param url     待请求链接
     * @param params  链接参数
     * @param headers 自定义请求头
     * @param charset 请求编码
     * @return HttpUtilEntity
     */
    public static HttpUtilEntity doGetEntity(String url, Map<String, String> params, Map<String, String> headers, String charset) throws IOException, ParseException {
        CloseableHttpResponse closeableHttpResponse = doGet(url, params, headers, charset, defaultContext);

        if(null == closeableHttpResponse)return null;
        HttpUtilEntity httpUtilEntity = response2entity(closeableHttpResponse, charset);
        closeableHttpResponse.close();

        return httpUtilEntity;
    }
    public static CloseableHttpResponse doGet(
            String url,
            Map<String, String> params,
            Map<String, String> headers,
            String charset
    ) {
        return doGet(url, params, headers, charset, defaultContext);
    }
    /**
     * HTTP Get 获取内容 [主方法]
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param headers 请求头信息
     * @param charset 编码格式
     * @return CloseableHttpResponse
     */
    public static CloseableHttpResponse doGet(
            String url,
            Map<String, String> params,
            Map<String, String> headers,
            String charset,
            HttpClientContext context
    ) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = genFormEntity(params, charset);

            // 将请求参数和url进行拼接
            if (null != urlEncodedFormEntity)
                url += "?" + EntityUtils.toString(urlEncodedFormEntity);

            HttpGet httpGet = new HttpGet(url);
            addHeader(httpGet, headers);

            return httpClient.execute(httpGet, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     *    __/\\\\\\\\\\\\\_________/\\\\\__________/\\\\\\\\\\\____/\\\\\\\\\\\\\\\_
     *     _\/\\\/////////\\\_____/\\\///\\\______/\\\/////////\\\_\///////\\\/////__
     *      _\/\\\_______\/\\\___/\\\/__\///\\\___\//\\\______\///________\/\\\_______
     *       _\/\\\\\\\\\\\\\/___/\\\______\//\\\___\////\\\_______________\/\\\_______
     *        _\/\\\/////////____\/\\\_______\/\\\______\////\\\____________\/\\\_______
     *         _\/\\\_____________\//\\\______/\\\__________\////\\\_________\/\\\_______
     *          _\/\\\______________\///\\\__/\\\_____/\\\______\//\\\________\/\\\_______
     *           _\/\\\________________\///\\\\\/_____\///\\\\\\\\\\\/_________\/\\\_______
     *            _\///___________________\/////_________\///////////___________\///________
     *            FROM:http://patorjk.com/software/taag
     */
    public static String doPost(String url, Map<String, String> params) throws IOException, ParseException {
        return getString(Objects.requireNonNull(doPost(url, params, null, CHARSET)), CHARSET);
    }
    public static String doPost2(String url, Map<String, String> headers) throws IOException, ParseException {
        return getString(Objects.requireNonNull(doPost(url, null, headers, CHARSET)), CHARSET);
    }

    public static HttpUtilEntity doStreamPost(String url, byte[] data, Map<String, String> headers) throws IOException {
        InputStreamEntity inputStreamEntity = genStreamEntity(data);
        HttpPost httpPost = new HttpPost(url);
        addHeader(httpPost, headers);
        httpPost.setEntity(inputStreamEntity);

        CloseableHttpResponse response = null;
        try {
            response = doPost1(url, httpPost, headers, CHARSET);
            HttpUtilEntity httpUtilEntity = response2entity(response, CHARSET);
            response.close();
            return httpUtilEntity;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (response != null)
                response.close();
        }
        return null;
    }

    public static String doFilePost(String url, byte[] data) throws IOException {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addBinaryBody("captcha", data, ContentType.DEFAULT_BINARY, URLEncoder.encode("captcha.jpg", "utf-8"));
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(multipartEntityBuilder.build());

        try (CloseableHttpResponse response = httpClient.execute(httpPost, defaultContext)) {
            int statusCode = response.getCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (HttpHostConnectException e) {
            throw new RuntimeException("Connect to host failed!", e);
        } catch (ConnectException e) {
            throw new RuntimeException("Connection failed!", e);
        }
        return null;
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     * @throws IOException IO异常
     */
    public static String doPost(String url, Map<String, String> params, String charset)
            throws IOException, ParseException {
        return getString(Objects.requireNonNull(doPost(url, params, null, charset)), charset);
    }
    public static String doPost(String url, Map<String, String> params, Map<String, String> header)
            throws IOException, ParseException {
        return getString(Objects.requireNonNull(doPost(url, params, header, CHARSET)), CHARSET);
    }

    public static HttpUtilEntity doPostEntity(String url, Map<String, String> params) throws IOException, ParseException {
        return doPostEntity(url, params, null, "UTF-8");
    }

    public static HttpUtilEntity doPostEntity(String url, Map<String, String> params, Map<String, String> headers) throws IOException, ParseException {
        return doPostEntity(url, params, headers, "UTF-8");
    }

    public static HttpUtilEntity doPostEntity(String url, Map<String, String> params, String charset) throws IOException, ParseException {
        return doPostEntity(url, params, null, charset);
    }

    public static HttpUtilEntity doPostEntity(String url, Map<String, String> params, Map<String, String> headers, String charset) throws IOException, ParseException {
        CloseableHttpResponse closeableHttpResponse = doPost(url, params, headers, charset);
        if(null == closeableHttpResponse)return null;
        HttpUtilEntity httpUtilEntity = response2entity(closeableHttpResponse, charset);
        closeableHttpResponse.close();
        return httpUtilEntity;
    }

    public static CloseableHttpResponse doPost1(
            String url,
            HttpPost httpPost,
            Map<String, String> headers,
            String charset
    ) throws IOException {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        return httpClient.execute(httpPost, defaultContext);
    }

    public static CloseableHttpResponse doPost(
            String url,
            Map<String, String> params,
            Map<String, String> headers,
            String charset
    ) throws IOException {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        HttpPost httpPost = new HttpPost(url);
        // 添加请求头
        addHeader(httpPost, headers);

        // 生成、添加请求体
        UrlEncodedFormEntity urlEncodedFormEntity = genFormEntity(params, charset);
        if (null != urlEncodedFormEntity) {
            httpPost.setEntity(urlEncodedFormEntity);
        }

        return httpClient.execute(httpPost, defaultContext);
    }

    /***
     *    __/\\\________/\\\_____/\\\\\\\\\_____/\\\\\_____/\\\__/\\\\\\\\\\\\_____/\\\______________/\\\\\\\\\\\\\\\_
     *     _\/\\\_______\/\\\___/\\\\\\\\\\\\\__\/\\\\\\___\/\\\_\/\\\////////\\\__\/\\\_____________\/\\\///////////__
     *      _\/\\\_______\/\\\__/\\\/////////\\\_\/\\\/\\\__\/\\\_\/\\\______\//\\\_\/\\\_____________\/\\\_____________
     *       _\/\\\\\\\\\\\\\\\_\/\\\_______\/\\\_\/\\\//\\\_\/\\\_\/\\\_______\/\\\_\/\\\_____________\/\\\\\\\\\\\_____
     *        _\/\\\/////////\\\_\/\\\\\\\\\\\\\\\_\/\\\\//\\\\/\\\_\/\\\_______\/\\\_\/\\\_____________\/\\\///////______
     *         _\/\\\_______\/\\\_\/\\\/////////\\\_\/\\\_\//\\\/\\\_\/\\\_______\/\\\_\/\\\_____________\/\\\_____________
     *          _\/\\\_______\/\\\_\/\\\_______\/\\\_\/\\\__\//\\\\\\_\/\\\_______/\\\__\/\\\_____________\/\\\_____________
     *           _\/\\\_______\/\\\_\/\\\_______\/\\\_\/\\\___\//\\\\\_\/\\\\\\\\\\\\/___\/\\\\\\\\\\\\\\\_\/\\\\\\\\\\\\\\\_
     *            _\///________\///__\///________\///__\///_____\/////__\////////////_____\///////////////__\///////////////__
     *
     */
    private static HttpUtilEntity response2entity(CloseableHttpResponse closeableHttpResponse, String charset) throws IOException, ParseException {

        // 处理头信息
        Header[] allHeaders = closeableHttpResponse.getHeaders();
        Map<String, String> allHeaderMap = new HashMap<>();

        for (Header header : allHeaders) {
            // if(allHeaderMap.containsKey(header.getName()))
            //     allHeaderMap.put(header.getName(), allHeaderMap.get(header.getName()) + ";" + header.getValue());
            // else
            allHeaderMap.put(header.getName(), header.getValue());
        }

        HttpUtilEntity httpUtilEntity = new HttpUtilEntity();
        // 状态码
        httpUtilEntity.setStatusCode(closeableHttpResponse.getCode());
        httpUtilEntity.setHeaders(allHeaderMap);
        HttpEntity entity = closeableHttpResponse.getEntity();
        httpUtilEntity.setBody(EntityUtils.toString(entity, charset));
        httpUtilEntity.setCookies(getCookie());
        return httpUtilEntity;
    }

    private static InputStreamEntity genStreamEntity(byte[] data) {
        return new InputStreamEntity(new ByteArrayInputStream(data), ContentType.MULTIPART_FORM_DATA);
    }

    private static UrlEncodedFormEntity genFormEntity(Map<String, String> params, String charset) throws UnsupportedEncodingException {
        if (null == params || params.isEmpty()) return null;

        List<NameValuePair> pairs = null;

        pairs = new ArrayList<>(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value;
            value = entry.getValue();
            if (value != null) {
                pairs.add(new BasicNameValuePair(entry.getKey(), value));
            }
        }
        return new UrlEncodedFormEntity(pairs, Charset.forName(charset));
    }

    private static void addHeader(HttpUriRequestBase http, Map<String, String> headers) {
        if (null != headers) headers.forEach(http::setHeader);
    }

    private static String getString(CloseableHttpResponse response, String charset) throws IOException, ParseException {
        HttpEntity entity = response.getEntity();

        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, charset);
        }
        EntityUtils.consume(entity);
        response.close();
        return result;
    }

    public static HttpClientContext genConfig(Map<String, Object> customConfig){
        Object timeout = customConfig.get("timeout");
        Object maxRedirect = customConfig.get("maxRedirect");

        // 配置
        final RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(timeout!=null?(int)timeout:5))
                .setMaxRedirects(maxRedirect!=null?(int)maxRedirect:0)
                .setRedirectsEnabled(false)
                // .setProxy(new HttpHost("127.0.0.1", 8866))      // TODO:开发环境设置代理
                .setCircularRedirectsAllowed(true)
                .build();

        BasicCookieStore cookieStore = new BasicCookieStore();
        // 动态配置
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        context.setRequestConfig(config);

        return context;
    }
}

