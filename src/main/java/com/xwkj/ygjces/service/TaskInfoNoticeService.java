package com.xwkj.ygjces.service;

import com.xwkj.ygjces.model.TaskInfoNotice;

public interface TaskInfoNoticeService {

    /**
     * 根据主键获取任务消息信息
     * @param id
     * @return
     * @author zyh
     */
    TaskInfoNotice selectByPrimaryKey(Long id);

    /**
     * 有选择的修改任务消息信息
     * @param taskInfoNotice
     * @author zyh
     */
    void updateByPrimaryKeySelective(TaskInfoNotice taskInfoNotice);

}
