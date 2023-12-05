package com.glasssix.dubbo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ph
 **/
@Slf4j
public class HttpUtil {

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    /**
     * 表单提交
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String postRequest(String url, Map<String, String> headers, Map<String, Object> params) {
        log.info("请求第三方URL:{}", url);
        String result = null;
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(url);
        if (null != headers && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                httpPost.addHeader(new BasicHeader(key, value));
            }
        }
        if (null != params && params.size() > 0) {
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(defaultEncoding)));
        }

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, Charset.forName(defaultEncoding));
                }
            } finally {
                response.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("响应结果:{}", result);
        return result;
    }

    public static String doPostInfo(String url, Map<String, Object> param) {
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
/*            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(2000)
                    .setSocketTimeout(1000).setConnectTimeout(2000).build();*/
            HttpPost httpPost = new HttpPost(url);
            //httpPost.setConfig(requestConfig);
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
            httpPost.setHeader("charset", HTTP.UTF_8);
            // 创建参数列表
            ObjectMapper json = new ObjectMapper();
            String params = json.writeValueAsString(param);
            StringEntity entity = new StringEntity(params, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            System.out.println(entity);
            System.out.println("请求地址：" + url);
            response = httpclient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println("返回结果：" + resultString);
        } catch (Exception e) {
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException ignored) {

            }
        }
        return resultString;
    }


    public static String doPostInfo(String url, List param) {
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
/*            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(2000)
                    .setSocketTimeout(1000).setConnectTimeout(2000).build();*/
            HttpPost httpPost = new HttpPost(url);
            //httpPost.setConfig(requestConfig);
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
            httpPost.setHeader("charset", HTTP.UTF_8);
            // 创建参数列表
            ObjectMapper json = new ObjectMapper();
            String params = json.writeValueAsString(param);
            StringEntity entity = new StringEntity(params, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            // System.out.println(entity);
//            log.info("请求地址：" + url);
            response = httpclient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
//            log.info("返回结果：{}", resultString);
        } catch (Exception e) {
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException ignored) {

            }
        }
        return resultString;
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
//    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
//        log.info("发送请求HTTPS", requestUrl);
//        JSONObject jsonObject = null;
//        StringBuffer buffer = new StringBuffer();
//        try {
//            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
////            TrustManager[] tm = {(TrustManager) new MyX509TrustManager()};
////            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
////            sslContext.init(null, tm, new java.security.SecureRandom());
////            // 从上述SSLContext对象中得到SSLSocketFactory对象
////            SSLSocketFactory ssf = sslContext.getSocketFactory();
//            URL url = new URL(null, requestUrl, new Handler());
////            URL url = new URL(requestUrl);
//            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
////            httpUrlConn.setSSLSocketFactory(ssf);
//            httpUrlConn.setDoOutput(true);
//            httpUrlConn.setDoInput(true);
//            httpUrlConn.setUseCaches(false);
//            // 设置请求方式（GET/POST）
//            httpUrlConn.setRequestMethod(requestMethod);
//
//            if ("GET".equalsIgnoreCase(requestMethod)) {
//                httpUrlConn.connect();
//            }
//
//            // 当有数据需要提交时
//            if (!ObjectUtils.isNotEmpty(outputStr)) {
//                OutputStream outputStream = httpUrlConn.getOutputStream();
//                // 注意编码格式，防止中文乱码
//                outputStream.write(outputStr.getBytes("UTF-8"));
//                outputStream.close();
//            }
//
//            // 将返回的输入流转换成字符串
//            InputStream inputStream = httpUrlConn.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            String str = null;
//            while ((str = bufferedReader.readLine()) != null) {
//                buffer.append(str);
//            }
//            System.err.println(str);
//            bufferedReader.close();
//            inputStreamReader.close();
//            // 释放资源
//            inputStream.close();
//            inputStream = null;
//            httpUrlConn.disconnect();
//            if (ObjectUtils.isNotEmpty(buffer)) {
//                System.out.println(buffer);
//                jsonObject = JSONObject.parseObject(buffer.toString());
//            }
//        } catch (ConnectException e) {
//            System.out.println("kkkd21");
//            e.printStackTrace();
//        } catch (Exception e) {
//            System.out.println("kkkd");
//            e.printStackTrace();
//        }
//        return jsonObject;
//
//    }

    public static String defaultEncoding = "utf-8";

    public static CloseableHttpClient buildHttpClient() {
        try {
            RegistryBuilder<ConnectionSocketFactory> builder = RegistryBuilder
                    .create();
            ConnectionSocketFactory factory = new PlainConnectionSocketFactory();
            builder.register("http", factory);
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            SSLContext context = SSLContexts.custom().useTLS()
                    .loadTrustMaterial(trustStore, new TrustStrategy() {
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) throws CertificateException {
                            return true;
                        }
                    }).build();
            LayeredConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(
                    context,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            builder.register("https", sslFactory);
            Registry<ConnectionSocketFactory> registry = builder.build();
            PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(
                    registry);
            ConnectionConfig connConfig = ConnectionConfig.custom()
                    .setCharset(Charset.forName(defaultEncoding)).build();
            SocketConfig socketConfig = SocketConfig.custom()
                    .setSoTimeout(100000).build();
            manager.setDefaultConnectionConfig(connConfig);
            manager.setDefaultSocketConfig(socketConfig);
            return HttpClientBuilder.create().setConnectionManager(manager)
                    .build();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 发送http get请求
     *
     * @param url 请求url
     * @return url返回内容
     */
    public static String getRequest(String url) {
        return getRequest(url, null);
    }


    /**
     * 发送http get请求
     *
     * @param url    请求的url
     * @param params 请求的参数
     * @return
     */
    public static String getRequest(String url, Map<String, Object> params) {
        return getRequest(url, null, params);
    }

    /**
     * 发送http get请求
     *
     * @param url        请求的url
     * @param headersMap 请求头
     * @param params     请求的参数
     * @return
     */
    public static String getRequest(String url, Map<String, String> headersMap, Map<String, Object> params) {
        log.info("请求连接:{},请求头：{}，请求提：{}", url, headersMap, params);
        String result = null;
        CloseableHttpClient httpClient = buildHttpClient();
        try {
            String apiUrl = url;
            if (null != params && params.size() > 0) {
                StringBuffer param = new StringBuffer();
                int i = 0;
                for (String key : params.keySet()) {
                    if (i == 0) {
                        param.append("?");
                    } else {
                        param.append("&");
                    }
                    param.append(key).append("=").append(params.get(key));
                    i++;
                }
                apiUrl += param;
            }

            HttpGet httpGet = new HttpGet(apiUrl);
            if (null != headersMap && headersMap.size() > 0) {
                for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    httpGet.addHeader(new BasicHeader(key, value));
                }
            }
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    result = EntityUtils.toString(entity, defaultEncoding);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.error("返回结果：{}", result);
        return result;
    }
}

