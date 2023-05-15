package com.ulwx.tool;


import okhttp3.*;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HttpUtilsOk {
    private static Logger log = LoggerFactory.getLogger(HttpUtilsOk.class);
    private static int maxIdleConnections = 10;
    private static int keepAliveDurationMinutes = 20;
    private static int timeOutSeconds = 6000;
    private static HashMap<String, String> defaultHeaders = new HashMap<String, String>();

    static {
        defaultHeaders.put("User-Agent", "Opera/9.80 (Windows NT 5.1; U; zh-cn) " + "Presto/2.2.15 Version/10.10");
        defaultHeaders.put("Accept",
                "text/html, application/xml;q=0.9, application/xhtml+xml, image/png, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1");
        defaultHeaders.put("Accept-Language", "zh-cn,en;q=0.9,zh;q=0.8");
        defaultHeaders.put("Accept-Encoding", "identity, *;q=0");
        defaultHeaders.put("Accept-Charset", "iso-8859-1, utf-8, utf-16, *;q=0.1");
        defaultHeaders.put("Content-Type", "text/html; charset=utf-8");

    }

    private static LogInterceptor logInterceptor = new LogInterceptor();

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .connectionPool(new ConnectionPool(maxIdleConnections, 20, TimeUnit.MINUTES))
            .connectTimeout(timeOutSeconds, TimeUnit.SECONDS)
            .readTimeout(timeOutSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeOutSeconds, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
            .hostnameVerifier(new TrustAllHostnameVerifier())
            .followRedirects(true)
            .followSslRedirects(true)
            .addNetworkInterceptor(logInterceptor)
            .build();

    /**
     * okHttp get方法的url拼接参数,并返回request对象
     *
     * @param url
     * @param paramMap
     * @return
     */
    private static Request getRequest(String url, Map<String, String> paramMap, Map<String, String> headers) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (paramMap != null && paramMap.size() > 0) {
            for (String key : paramMap.keySet()) {
                urlBuilder
                        .append(key)
                        .append("=")
                        .append(URLEncoder.encode(paramMap.get(key), "utf-8"))
                        .append("&");
            }
        }

        Request.Builder builder = new Request.Builder();
        if (headers != null) {
            headers.putAll(defaultHeaders);
        } else {
            headers = new HashMap<String, String>();
            headers.putAll(defaultHeaders);
        }


        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }
        return builder
                .url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .get()
                .build();
    }

    /**
     * okHttp post返回request对象
     *
     * @param url
     * @param body
     * @return
     */
    private static Request getRequest(String url, RequestBody body, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        if (headers != null) {
            headers.putAll(defaultHeaders);
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }
        return builder
                .url(url)
                .post(body)
                .build();
    }

    /**
     * okHttp get同步请求
     *
     * @param url
     * @return
     * @throws java.io.IOException
     */
    public static String poolHttpGet(String url, Map<String, String> paramMap, Map<String, String> headers) throws IOException {
        Response response = CLIENT.newCall(getRequest(url, paramMap, headers)).execute();

        return response.body().string();
    }

    /**
     * okHttp get异步请求
     *
     * @param url
     * @param paramMap
     * @param callback
     * @return
     * @throws IOException
     */
    public static void poolHttpGet(String url, Map<String, String> paramMap, Map<String, String> headers, Callback callback) throws UnsupportedEncodingException {
        CLIENT.newCall(getRequest(url, paramMap, headers))
                .enqueue(callback);
    }


    /**
     * okHttp post同步请求(json方式提交)
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String poolHttpJsonPost(String url, String json, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Response response = CLIENT.newCall(getRequest(url, body, headers))
                .execute();
        return response.body().string();
    }

    /**
     * okHttp post同步请求(文本提交)
     *
     * @param url
     * @param content
     * @return
     * @throws IOException
     */
    public static String poolHttpTxtPost(String url, String content, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), content);
        Response response = CLIENT.newCall(getRequest(url, body, headers))
                .execute();
        return response.body().string();
    }

    public static String poolHttpBytesPost(String url, byte[] content, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), content);
        Response response = CLIENT.newCall(getRequest(url, body, headers))
                .execute();
        return response.body().string();
    }

    /**
     * okHttp post异步请求(json方式提交)
     *
     * @param url
     * @param json
     * @param callback
     * @return
     * @throws IOException
     */
    public static void poolHttpJsonPost(String url, String json, Map<String, String> headers, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        CLIENT.newCall(getRequest(url, body, headers))
                .enqueue(callback);
    }


    /**
     * okHttp post同步请求(form方式提交)
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */

    public static String poolHttpFormPost(String url, Map<String, String> paramMap, Map<String, String> headers) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        if (paramMap != null && paramMap.size() > 0) {
            for (String key : paramMap.keySet()) {
                builder.add(key, paramMap.get(key));
            }
        }

        Response response = CLIENT.newCall(getRequest(url, builder.build(), headers))
                .execute();
        return response.body().string();
    }

    public static String poolHttpMultipartFormPost(String url, Map<String, Object> paramMap, Map<String, String> headers) throws IOException {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                //一定要设置这句
                .setType(MultipartBody.FORM);
        if (paramMap != null && paramMap.size() > 0) {
            for (String key : paramMap.keySet()) {
                Object value = paramMap.get(key);
                if (value instanceof File) {
                    File file = (File) paramMap.get(key);
                    builder.addFormDataPart(key, file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"), file));
                } else {
                    builder.addFormDataPart(key, paramMap.get(key).toString());
                }

            }
        }
        Response response = CLIENT.newCall(getRequest(url, builder.build(), headers))
                .execute();
        return response.body().string();
    }

    /**
     * okHttp post异步请求(form方式提交)
     *
     * @param url
     * @param paramMap
     * @param callback
     * @return
     * @throws IOException
     */

    public static void poolHttpFormPost(String url, Map<String, String> paramMap, Map<String, String> headers, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramMap.keySet()) {
            builder.add(key, paramMap.get(key));
        }
        CLIENT.newCall(getRequest(url, builder.build(), headers))
                .enqueue(callback);
    }


    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }


    /**
     * okHttp get同步请求
     *
     * @param url
     * @return
     * @throws java.io.IOException
     */
    public static String httpGet(String url, Map<String, String> paramMap, Map<String, String> headers, int timeOutSeconds) throws IOException {
        Response response = getClient(timeOutSeconds).newCall(getRequest(url, paramMap, headers)).execute();

        return response.body().string();
    }

    /**
     * okHttp get异步请求
     *
     * @param url
     * @param paramMap
     * @param callback
     * @return
     * @throws IOException
     */
    public static void httpGet(String url, Map<String, String> paramMap, Map<String, String> headers, int timeOutSeconds, Callback callback) throws UnsupportedEncodingException {
        getClient(timeOutSeconds).newCall(getRequest(url, paramMap, headers))
                .enqueue(callback);
    }


    /**
     * okHttp post同步请求(json方式提交)
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String httpJsonPost(String url, String json, Map<String, String> headers, int timeOutSeconds) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Response response = getClient(timeOutSeconds).newCall(getRequest(url, body, headers))
                .execute();
        return response.body().string();
    }

    /**
     * okHttp post同步请求(文本提交)
     *
     * @param url
     * @param content
     * @return
     * @throws IOException
     */
    public static String httpTxtPost(String url, String content, Map<String, String> headers, int timeOutSeconds) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), content);
        Response response = getClient(timeOutSeconds).newCall(getRequest(url, body, headers))
                .execute();
        return response.body().string();
    }

    public static String httpBytesPost(String url, byte[] content, Map<String, String> headers, int timeOutSeconds) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), content);
        Response response = getClient(timeOutSeconds).newCall(getRequest(url, body, headers))
                .execute();
        return response.body().string();
    }

    /**
     * okHttp post异步请求(json方式提交)
     *
     * @param url
     * @param json
     * @param callback
     * @return
     * @throws IOException
     */
    public static void httpJsonPost(String url, String json, Map<String, String> headers, int timeOutSeconds, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        getClient(timeOutSeconds).newCall(getRequest(url, body, headers))
                .enqueue(callback);
    }


    /**
     * okHttp post同步请求(form方式提交)
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */

    public static String httpFormPost(String url, Map<String, String> paramMap, Map<String, String> headers, int timeOutSeconds) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        if (paramMap != null && paramMap.size() > 0) {
            for (String key : paramMap.keySet()) {
                builder.add(key, paramMap.get(key));
            }
        }

        Response response = getClient(timeOutSeconds).newCall(getRequest(url, builder.build(), headers))
                .execute();
        return response.body().string();
    }

    public static String httpMultipartFormPost(String url, Map<String, Object> paramMap, Map<String, String> headers,
                                               int timeOutSeconds) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                //一定要设置这句
                .setType(MultipartBody.FORM);
        if (paramMap != null && paramMap.size() > 0) {
            for (String key : paramMap.keySet()) {
                Object value = paramMap.get(key);
                if (value instanceof File) {
                    File file = (File) paramMap.get(key);
                    builder.addFormDataPart(key, file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"), file));
                } else {
                    builder.addFormDataPart(key, paramMap.get(key).toString());
                }

            }
        }
        Response response = getClient(timeOutSeconds).newCall(getRequest(url, builder.build(), headers))
                .execute();
        return response.body().string();
    }

    /**
     * okHttp post异步请求(form方式提交)
     *
     * @param url
     * @param paramMap
     * @param callback
     * @return
     * @throws IOException
     */

    public static void httpFormPost(String url, Map<String, String> paramMap, Map<String, String> headers,
                                    int timeOutSeconds, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramMap.keySet()) {
            builder.add(key, paramMap.get(key));
        }
        getClient(timeOutSeconds).newCall(getRequest(url, builder.build(), headers))
                .enqueue(callback);
    }

    private static OkHttpClient getClient(int timeOutSeconds) {
        OkHttpClient CLIENT = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectTimeout(timeOutSeconds, TimeUnit.SECONDS)
                .readTimeout(timeOutSeconds, TimeUnit.SECONDS)
                .writeTimeout(timeOutSeconds, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .followRedirects(true)
                .addNetworkInterceptor(logInterceptor)
                .followSslRedirects(true)
                .build();
        return CLIENT;
    }


    public static class RedirectInterceptor implements Interceptor {

        public static RedirectInterceptor instance = new RedirectInterceptor();

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl beforeUrl = request.url();
            Response response = chain.proceed(request);
            HttpUrl afterUrl = response.request().url();
            //1.根据url判断是否是重定向
            if (!beforeUrl.equals(afterUrl)) {
                //处理两种情况 1、跨协议 2、原先不是GET请求。
                if (!beforeUrl.scheme().equals(afterUrl.scheme()) || !request.method().equals("GET")) {
                    //重新请求
                    Request newRequest = request.newBuilder().url(response.request().url()).build();
                    response = chain.proceed(newRequest);
                }
            }
            return response;
        }
    }


    /**
     * 日志拦截
     */

    public static class LogInterceptor implements Interceptor {

        public static LogInterceptor instance = new LogInterceptor();

        public LogInterceptor() {

        }

        private static final String MILLIS_PATTERN = "yyyy-MM-dd HH:mm:ss";

        private LogLevel logLevel = LogLevel.NONE;
        private ColorLevel colorLevel = ColorLevel.DEBUG;

        private String toDateTimeStr(Long millis, String pattern) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            return simpleDateFormat.format(millis);
        }

        public void setLogLevel(LogLevel logLevel) {
            this.logLevel = logLevel;
        }

        public void setColorLevel(ColorLevel colorLevel) {
            this.colorLevel = colorLevel;
        }

        public enum LogLevel {
            NONE,
            BASIC,
            HEADERS,
            BODY
        }
        public enum ColorLevel {
            DEBUG,
            INFO,
            WARN,
            ERROR
        }

        private void logResponse(Response response) {
            StringBuffer sb = new StringBuffer();

            if (logLevel == LogLevel.NONE) {

            } else if (logLevel == LogLevel.BASIC) {
                logBasicRsp(sb, response);
            } else if (logLevel == LogLevel.HEADERS) {
                logHeadersRsp(sb, response);
            } else if (logLevel == LogLevel.BODY) {
                logHeadersRsp(sb, response);
                ResponseBody peekBody;
                try {
                    peekBody = response.peekBody(1024 * 1024);
                    sb.append("response body:\n" + new String(peekBody.bytes(), Charset.defaultCharset()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            logIt(sb.toString());
        }

        private void logRequest(Request request, Connection connection) {
            StringBuffer sb = new StringBuffer();

            if (logLevel == LogLevel.NONE) {

            } else if (logLevel == LogLevel.BASIC) {
                logBasicReq(sb, request, connection);
            } else if (logLevel == LogLevel.HEADERS) {
                logHeaderReq(sb, request, connection);
            } else if (logLevel == LogLevel.BODY) {
                logBodyReq(sb, request, connection);
                sb.append("\n");
            }

        }


        private void logBasicRsp(StringBuffer sb, Response response) {
            String s = toDateTimeStr(response.sentRequestAtMillis(), MILLIS_PATTERN);
            sb.append("response protocol: ");
            sb.append(response.protocol());
            sb.append("\n");
            sb.append("response code: ").append(response.code());
            sb.append("\n");
            sb.append("response message: ").append(response.message());
            sb.append("\n");
            sb.append("response request Url: ").append(decodeUrlString(response.request().url().toString()));
            sb.append("\n");
            sb.append("response sentRequestTime:").append(s);
            sb.append("\n");

        }

        private void logHeadersRsp(StringBuffer sb, Response response) {
            logBasicRsp(sb, response);
            Headers headers = response.headers();
            for (int i = 0; i < headers.size(); i++) {
                sb.append("response Header:").append(headers.name(i)).append(" = ").append(headers.value(i));
                sb.append("\n");
            }
        }


        private String decodeUrlString(String url) {
            try {
                return URLDecoder.decode(url, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }

        private void logIt(String sb) {

            switch (colorLevel) {
                case INFO:
                    log.info(sb);
                    break;
                case DEBUG:
                    log.debug(sb);
                    break;
                case ERROR:
                    log.error(sb);
                    break;
                case WARN:
                    log.warn(sb);
                    break;
            }

        }


        private void logBodyReq(StringBuffer sb, Request request, Connection connection) {
            logHeaderReq(sb, request, connection);
            sb.append("RequestBody: ").append(Objects.requireNonNull(bodyToString(request)));
        }

        private void logHeaderReq(StringBuffer sb, Request request, Connection connection) {
            logBasicReq(sb, request, connection);
            Headers headers = request.headers();
            for (int i = 0; i < headers.size(); i++) {
                String name = headers.name(i);
                String value = headers.value(i);
                String headersStr = "request Header: " + name + "=" + value + "\n";
                sb.append(headersStr);
            }
        }

        private String bodyToString(Request request) {

            try {
                final Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                RequestBody body = copy.body();
                if (body != null) {
                    body.writeTo(buffer);
                }
                return buffer.readUtf8();
            } catch (final IOException e) {
                return "error";
            }
        }

        private void logBasicReq(StringBuffer sb, Request request, Connection connection) {
            sb.append("method: ");
            sb.append(request.method());
            sb.append("\n");
            sb.append("url: ");
            sb.append(decodeUrlString(request.url().toString()));
            sb.append("\n");
            sb.append("tag: ");
            sb.append(request.tag());
            sb.append("\n");
            sb.append("protocol:  ");
            if (connection != null) {
                sb.append(connection.protocol());
            } else {
                sb.append(okhttp3.Protocol.HTTP_1_1);
            }
            sb.append("\n");
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response proceed = chain.proceed(request);
            Connection connection = chain.connection();
            logRequest(request, connection);
            logResponse(proceed);
            return proceed;
        }
    }

    public static void main(String[] args) throws Exception {
        String s = HttpUtilsOk.httpGet("http://www.163.com", null, null, 3000);
        System.out.println(s);
        //System.out.println(HttpUtils.get("https://www.163.com","utf-8"));

    }
}
