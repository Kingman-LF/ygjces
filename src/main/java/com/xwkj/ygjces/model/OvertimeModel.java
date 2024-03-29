package com.xwkj.ygjces.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class OvertimeModel {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_xm_overtime.pid
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    private Long pid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_xm_overtime.sy_num
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    private String syNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_xm_overtime.wt_num
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    private String wtNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_xm_overtime.wt_unit
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    private String wtUnit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_xm_overtime.xm_id
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    private String xmId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_xm_overtime.xm_name
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    private String xmName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_xm_overtime.stage
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    private Integer stage;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_xm_overtime.create_date
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_xm_overtime.update_date
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_xm_overtime.wt_date
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date wtDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_xm_overtime.pid
     *
     * @return the value of tbl_gb_xm_overtime.pid
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public Long getPid() {
        return pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_xm_overtime.pid
     *
     * @param pid the value for tbl_gb_xm_overtime.pid
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_xm_overtime.sy_num
     *
     * @return the value of tbl_gb_xm_overtime.sy_num
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public String getSyNum() {
        return syNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_xm_overtime.sy_num
     *
     * @param syNum the value for tbl_gb_xm_overtime.sy_num
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public void setSyNum(String syNum) {
        this.syNum = syNum == null ? null : syNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_xm_overtime.wt_num
     *
     * @return the value of tbl_gb_xm_overtime.wt_num
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public String getWtNum() {
        return wtNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_xm_overtime.wt_num
     *
     * @param wtNum the value for tbl_gb_xm_overtime.wt_num
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public void setWtNum(String wtNum) {
        this.wtNum = wtNum == null ? null : wtNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_xm_overtime.wt_unit
     *
     * @return the value of tbl_gb_xm_overtime.wt_unit
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public String getWtUnit() {
        return wtUnit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_xm_overtime.wt_unit
     *
     * @param wtUnit the value for tbl_gb_xm_overtime.wt_unit
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public void setWtUnit(String wtUnit) {
        this.wtUnit = wtUnit == null ? null : wtUnit.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_xm_overtime.xm_id
     *
     * @return the value of tbl_gb_xm_overtime.xm_id
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public String getXmId() {
        return xmId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_xm_overtime.xm_id
     *
     * @param xmId the value for tbl_gb_xm_overtime.xm_id
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public void setXmId(String xmId) {
        this.xmId = xmId == null ? null : xmId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_xm_overtime.xm_name
     *
     * @return the value of tbl_gb_xm_overtime.xm_name
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public String getXmName() {
        return xmName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_xm_overtime.xm_name
     *
     * @param xmName the value for tbl_gb_xm_overtime.xm_name
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public void setXmName(String xmName) {
        this.xmName = xmName == null ? null : xmName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_xm_overtime.stage
     *
     * @return the value of tbl_gb_xm_overtime.stage
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public Integer getStage() {
        return stage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_xm_overtime.stage
     *
     * @param stage the value for tbl_gb_xm_overtime.stage
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public void setStage(Integer stage) {
        this.stage = stage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_xm_overtime.create_date
     *
     * @return the value of tbl_gb_xm_overtime.create_date
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_xm_overtime.create_date
     *
     * @param createDate the value for tbl_gb_xm_overtime.create_date
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_xm_overtime.update_date
     *
     * @return the value of tbl_gb_xm_overtime.update_date
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_xm_overtime.update_date
     *
     * @param updateDate the value for tbl_gb_xm_overtime.update_date
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_xm_overtime.wt_date
     *
     * @return the value of tbl_gb_xm_overtime.wt_date
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public Date getWtDate() {
        return wtDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_xm_overtime.wt_date
     *
     * @param wtDate the value for tbl_gb_xm_overtime.wt_date
     *
     * @mbggenerated Tue Aug 27 10:21:37 CST 2019
     */
    public void setWtDate(Date wtDate) {
        this.wtDate = wtDate;
    }
}