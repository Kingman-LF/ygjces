package com.xwkj.ygjces.model;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource(value = {"classpath:/config/wxConfig.properties"})
@Component
public class UrlData {
    @Value("${wx.corpId}")
    String corpId;
    @Value("${wx.msgCorpSecret}")
    String msgCorpSecret;
    String getTokenUrl;
    String sendMessageUrl;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getMsgCorpSecret() {
        return msgCorpSecret;
    }

    public void setMsgCorpSecret(String msgCorpSecret) {
        this.msgCorpSecret = msgCorpSecret;
    }

    public void setGetTokenUrl() {
        this.getTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpId + "&corpsecret=" + msgCorpSecret;
    }

    public String getGetTokenUrl() {
        getTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpId + "&corpsecret=" + msgCorpSecret;
        return getTokenUrl;
    }

    public String getSendMessageUrl() {
        sendMessageUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";
        return sendMessageUrl;
    }
}