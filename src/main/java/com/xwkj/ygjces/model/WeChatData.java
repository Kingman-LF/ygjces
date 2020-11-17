package com.xwkj.ygjces.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource(value = {"classpath:/config/wxConfig.properties"})
@Component
public class WeChatData {
    String touser;
    String msgtype;
    int agentid;
    Object text;

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public int getAgentid() {
        return agentid;
    }

    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }
}