package com.xwkj.ygjces.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwkj.ygjces.model.UrlData;
import com.xwkj.ygjces.model.WeChatData;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
public class SendMsgService {

    private static final String CONTENT_TYPE = "application/json" ;
    private static final String charset = "utf-8";
    private Gson gson = new Gson();
    @Autowired
    UrlData urlData;

    public String getToken() throws IOException {
        String resp = toAuth(urlData.getGetTokenUrl());//就是按照GET方式拼接了字符串得到url
        Map<String, Object> map = gson.fromJson(resp, new TypeToken<Map<String, Object>>() {
        }.getType());
        System.out.println(map);
        return map.get("access_token").toString();
    }

    protected String toAuth(String Get_Token_Url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(Get_Token_Url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        System.out.println(response.toString());
        String resp;
        try {
            HttpEntity entity = response.getEntity();
            System.out.println(response.getAllHeaders());
            resp = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return resp;
    }

    /**
     * 创建POST BODY
     */
    public String createPostData(WeChatData weChatData) {
        return gson.toJson(weChatData);
    }

    /**
     * POST请求
     */
    public String post(WeChatData weChatData) throws IOException {
        String token = getToken();
        String data = createPostData(weChatData);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(urlData.getSendMessageUrl() + token);
        httpPost.setHeader("Content-Type", CONTENT_TYPE);
        httpPost.setEntity(new StringEntity(data, charset));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        String resp;
        try {
            HttpEntity entity = response.getEntity();
            resp = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return resp;
    }
}
