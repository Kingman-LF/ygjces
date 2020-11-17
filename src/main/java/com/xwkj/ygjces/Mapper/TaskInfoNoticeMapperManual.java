package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.model.TaskInfoNotice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskInfoNoticeMapperManual {

    /**
     * 获取当天需要发送的提醒或超时消息
     * @return
     * @author zyh
     */
    List<TaskInfoNotice> getTodayNoticeInfo();

    /**
     * 根据发送用户和发送时间获取信息
     * @param taskInfoNotice
     * @return zyh
     */
    TaskInfoNotice getTaskInfoNoticeByUserIdAndSendTime(TaskInfoNotice taskInfoNotice);

}