package com.zyh.chuxin.app.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 异步HTTP工具类
 * Created by zhyh on 2015/1/22.
 */
public class HttpUtil {

    /**
     * @param url HTTP请求URL
     */
    public static String sendHttpRequest(String url) {
        return _doGet(url);
    }

    private static String _doGet(String url) {
        HttpURLConnection connection = null;
        try {
            connection = _openPostConnection(url);
            return _doFetchResponse(connection);
        } catch (IOException e) {
            return null;
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    private static HttpURLConnection _openPostConnection(String purl) throws IOException {
        URL url = new URL(purl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setDoOutput(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Android Client Agent");

        return connection;
    }

    private static String _doFetchResponse(HttpURLConnection connection) throws IOException {
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("服务器返回状态非正常响应状态.");
        }
        return new String(streamToByteArray(connection.getInputStream()));
    }

    /**
     * 输入流转换成字节数组
     *
     * @param stream 输入流参数
     * @return 输入流转换后的字节数组
     * @throws java.io.IOException
     */
    public static byte[] streamToByteArray(InputStream stream) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            int count;
            while ((count = stream.read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
            }
            return bytes.toByteArray();
        } finally {
            stream.close();
            bytes.close();
        }
    }

    public static void main(String[] args) {
        String s = sendHttpRequest("http://www.weather.com.cn/data/list3/city.xml");
        System.out.println(s);
    }
}
