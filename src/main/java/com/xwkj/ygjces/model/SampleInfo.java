package com.xwkj.ygjces.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 样品实体类
 * @author zyh
 */
public class SampleInfo {

    private String itemId;
    private String itemName;
    private String principal; //委托方（委托公司）
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;
    private Integer overtime;
    private Integer useTime;
    private String sampleNum;
    private String wtNum;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date wtDate;

    public Date getWtDate() {
        return wtDate;
    }

    public void setWtDate(Date wtDate) {
        this.wtDate = wtDate;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getOvertime() {
        return overtime;
    }

    public void setOvertime(Integer overtime) {
        this.overtime = overtime;
    }

    public Integer getUseTime() {
        return useTime;
    }

    public void setUseTime(Integer useTime) {
        this.useTime = useTime;
    }

    public String getSampleNum() {
        return sampleNum;
    }

    public void setSampleNum(String sampleNum) {
        this.sampleNum = sampleNum;
    }

    public String getWtNum() {
        return wtNum;
    }

    public void setWtNum(String wtNum) {
        this.wtNum = wtNum;
    }

}
