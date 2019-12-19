package com.yu.tools.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MyHttpClientUtil {
    @Value("${yu.tools.proxy.ip}")
    private String ip;
    @Value("${yu.tools.proxy.port}")
    private int port;
    private static Logger logger = Logger.getLogger(MyHttpClientUtil.class);
    public String getDataFromURL(String url,String encode,boolean useProxy){
        String result = null;
        CloseableHttpClient httpclient = null;
        if(useProxy && ip != null && ip.length()>0 && port>0){
            HttpClientBuilder build = HttpClients.custom();
            HttpHost proxy = new HttpHost(ip, port);
            httpclient = build.setProxy(proxy).build();
        }else{
            httpclient = HttpClients.createDefault();
        }
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        HttpPost post = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();//设置请求和传输超时时间
        post.setConfig(requestConfig);
        for(int i = 0;i < 3;i++){
            try {
                //post.setEntity((HttpEntity) new UrlEncodedFormEntity(nvps, "UTF-8"));
                HttpEntity entity = new UrlEncodedFormEntity(nvps, "UTF-8");
                post.setEntity(entity);
                System.out.println(EntityUtils.toString(entity));
                HttpResponse response = httpclient.execute(post);
                System.out.println(response.getStatusLine().getStatusCode());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String res = EntityUtils.toString(resEntity, encode);
                    result = res;
                }
                break;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(),e);
            }
        }
        return result;
    }

    public String getDataFromURL_GET(String url,String encode,boolean useProxy){
        String result = null;
        CloseableHttpClient httpclient = null;
        if(useProxy && ip != null && ip.length()>0 && port>0){
            HttpClientBuilder build = HttpClients.custom();
            HttpHost proxy = new HttpHost(ip, port);
            httpclient = build.setProxy(proxy).build();
        }else{
            httpclient = HttpClients.createDefault();
        }
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        HttpGet post = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();//设置请求和传输超时时间
        post.setConfig(requestConfig);
        for(int i=0;i<3;i++){
            try {
                HttpResponse response = httpclient.execute(post);
                System.out.println(response.getStatusLine().getStatusCode());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String res = EntityUtils.toString(resEntity, encode);
                    result = res;
                }
                break;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(),e);
            }
        }
        return result;
    }

    public long diffDate(Date endDate,Date nowDate){
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        long hour = diff / nh;
        return hour;
    }
}
