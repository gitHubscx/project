package com.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.params.CoreConnectionPNames;

public class HttpClientUtils {

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static final Log logger = LogFactory.getLog(HttpClientUtils.class);

    private static HttpClient client;

    static {
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        client = new HttpClient(connectionManager);
        client.getParams().setConnectionManagerTimeout(15000);
    }

    /**
     * 使用默认字符集执行HTTP GET请求
     * 
     * @param url
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        return doGet(url, DEFAULT_CHARSET);
    }

    /**
     * 使用指定字符集执行HTTP GET请求
     * 
     * @param url
     * @param charset
     * @return
     * @throws Exception
     */
    public static String doGet(String url, String charset) throws Exception {
        GetMethod method = null;
        String result = null;
        try {
            method = new GetMethod(url);
            // 请求超时 5分钟
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 300000);
            // 数据传输时间 5分钟
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 300000);
            client.executeMethod(method);
            int status = method.getStatusCode();
            if (status == HttpStatus.SC_OK) {
                // result = charset == null ? method.getResponseBodyAsString() : new String(method.getResponseBody(), charset);

                // 替换方式
                InputStream inputStream = method.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                result = stringBuffer.toString();
            }
        } catch (HttpException e) {
            logger.error(e, e);
            throw e;
        } catch (IOException e) {
            logger.error(e, e);
            throw e;
        } finally {
            // 释放连接
            if (method != null) {
                method.releaseConnection();
            }
        }
        return result;
    }

    /**
     * 使用默认字符集执行HTTP POST请求
     * 
     * @param url
     * @param paramMap
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> paramMap) throws Exception {
        Header header = new Header();
        header.setName("Content-Type");
        header.setValue("application/x-www-form-urlencoded;charset=utf-8");
        return doPost(url, header, paramMap, "UTF-8");
    }

    /**
     * 使用指定字符集执行HTTP POST请求
     * 
     * @param url
     * @param paramMap
     * @param charset
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Header header, Map<String, Object> paramMap, String charset) throws Exception {
        PostMethod method = null;
        String result = null;
        try {
            method = new PostMethod(url);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
            method.setRequestHeader(header);
            if (paramMap != null && !paramMap.isEmpty()) {
                String key = null;
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    key = entry.getKey();
                    if (key != null && !"".equals(key.trim())) {
                        method.setParameter(key, entry.getValue().toString());
                    }
                }
            }
            // 请求超时 5分钟
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 300000);
            // 数据传输时间 5分钟
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 300000);

            client.executeMethod(method);
            int status = method.getStatusCode();
            if (status == HttpStatus.SC_OK) {
                // 超过长度会溢出
                // result = charset == null ? method.getResponseBodyAsString() : new String(method.getResponseBody(), charset);

                // 替换方式
                InputStream inputStream = method.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                result = stringBuffer.toString();
            }
        } catch (HttpException e) {
            logger.error(e, e);
            throw e;
        } catch (IOException e) {
            logger.error(e, e);
            throw e;
        } finally {
            // 释放连接
            if (method != null) {
                method.releaseConnection();
            }
        }
        return result;
    }

    /**
     * 以传入参数jsondata作为请求的整个body发送到指定的url
     * 
     * @param url
     * @param jsondata
     * @param charset
     * @return json数据类型的返回结果
     */
    public static String doPost(String url, String jsondata, String charset) throws Exception {
        PostMethod method = null;
        String result = null;
        ByteArrayOutputStream requestBodyStream = null;
        ByteArrayOutputStream responseBodyStream = null;
        try {
            // setting headers
            ArrayList<Header> headers = new ArrayList<Header>();
            headers.add(new Header("Accept-Language", "en-us,zh-cn,zh-tw,en-gb,en;q=0.7,*;q=0.3"));
            headers.add(new Header("Accept-Charset", "big5,gb2312,gbk,utf-8,ISO-8859-1;q=0.7,*;q=0.7"));
            headers.add(new Header("Accept", "text/html,application/xml;q=0.9,application/xhtml+xml,text/xml;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5"));
            headers.add(new Header("Accept-Encoding", "x-gzip,gzip"));
            headers.add(new Header("Connection", "close"));
            // 请求超时 5分钟
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 300000);
            // 数据传输时间 5分钟
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 300000);
            client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);

            method = new PostMethod(url);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, DEFAULT_CHARSET);

            requestBodyStream = new ByteArrayOutputStream();
            requestBodyStream.write((jsondata == null ? "" : jsondata).getBytes(charset));
            requestBodyStream.flush();
            RequestEntity entity = new ByteArrayRequestEntity(requestBodyStream.toByteArray());
            method.setRequestEntity(entity);

            client.executeMethod(method);
            int status = method.getStatusCode();
            if (status == HttpStatus.SC_OK) {
                InputStream is = null;
                responseBodyStream = new ByteArrayOutputStream();
                ;
                is = method.getResponseBodyAsStream();
                int nRead = 0;
                byte[] buf = new byte[10240];
                while ((nRead = is.read(buf)) > 0) {
                    responseBodyStream.write(buf, 0, nRead);
                    responseBodyStream.flush();
                }
                result = charset == null ? responseBodyStream.toString() : responseBodyStream.toString(charset);
                if (is != null) {
                    is.close();
                }
            }
        } catch (HttpException e) {
            logger.error(e, e);
            throw e;
        } catch (IOException e) {
            logger.error(e, e);
            throw e;
        } finally { // 释放资源
            if (method != null) {
                method.releaseConnection();
            }
            if (requestBodyStream != null) {
                requestBodyStream.close();
            }
            if (responseBodyStream != null) {
                responseBodyStream.close();
            }
        }
        return result;
    }

}
