package com.xwkj.ygjces.model;

import java.util.Date;

public class TaskInfoNotice {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_task_notice.id
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_task_notice.content
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    private String content;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_task_notice.task_id
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    private Long taskId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_task_notice.creat_time
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    private Date creatTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_task_notice.send_time
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    private Date sendTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_task_notice.is_render
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    private Integer isRender;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_task_notice.render_time
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    private Date renderTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_task_notice.user_id
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_gb_task_notice.is_overtime
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    private Integer isOvertime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_task_notice.id
     *
     * @return the value of tbl_gb_task_notice.id
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_task_notice.id
     *
     * @param id the value for tbl_gb_task_notice.id
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_task_notice.content
     *
     * @return the value of tbl_gb_task_notice.content
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_task_notice.content
     *
     * @param content the value for tbl_gb_task_notice.content
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_task_notice.task_id
     *
     * @return the value of tbl_gb_task_notice.task_id
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public Long getTaskId() {
        return taskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_task_notice.task_id
     *
     * @param taskId the value for tbl_gb_task_notice.task_id
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_task_notice.creat_time
     *
     * @return the value of tbl_gb_task_notice.creat_time
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public Date getCreatTime() {
        return creatTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_task_notice.creat_time
     *
     * @param creatTime the value for tbl_gb_task_notice.creat_time
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_task_notice.send_time
     *
     * @return the value of tbl_gb_task_notice.send_time
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_task_notice.send_time
     *
     * @param sendTime the value for tbl_gb_task_notice.send_time
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_task_notice.is_render
     *
     * @return the value of tbl_gb_task_notice.is_render
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public Integer getIsRender() {
        return isRender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_task_notice.is_render
     *
     * @param isRender the value for tbl_gb_task_notice.is_render
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public void setIsRender(Integer isRender) {
        this.isRender = isRender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_task_notice.render_time
     *
     * @return the value of tbl_gb_task_notice.render_time
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public Date getRenderTime() {
        return renderTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_task_notice.render_time
     *
     * @param renderTime the value for tbl_gb_task_notice.render_time
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public void setRenderTime(Date renderTime) {
        this.renderTime = renderTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_task_notice.user_id
     *
     * @return the value of tbl_gb_task_notice.user_id
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_task_notice.user_id
     *
     * @param userId the value for tbl_gb_task_notice.user_id
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_gb_task_notice.is_overtime
     *
     * @return the value of tbl_gb_task_notice.is_overtime
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public Integer getIsOvertime() {
        return isOvertime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_gb_task_notice.is_overtime
     *
     * @param isOvertime the value for tbl_gb_task_notice.is_overtime
     *
     * @mbggenerated Sat Jan 11 17:21:55 CST 2020
     */
    public void setIsOvertime(Integer isOvertime) {
        this.isOvertime = isOvertime;
    }
}