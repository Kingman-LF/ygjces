package com.xwkj.ygjces.service.impl;

import com.xwkj.ygjces.Mapper.auto.TaskInfoNoticeMapper;
import com.xwkj.ygjces.model.TaskInfoNotice;
import com.xwkj.ygjces.service.TaskInfoNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskInfoNoticeServiceImpl implements TaskInfoNoticeService {

    @Autowired
    TaskInfoNoticeMapper taskInfoNoticeMapper;

    /**
     * 根据主键获取任务消息信息
     * @param id
     * @return
     * @author zyh
     */
    @Override
    public TaskInfoNotice selectByPrimaryKey(Long id) {
        return taskInfoNoticeMapper.selectByPrimaryKey(id);
    }

    /**
     * 有选择的修改任务消息信息
     * @param taskInfoNotice
     * @author zyh
     */
    @Override
    public void updateByPrimaryKeySelective(TaskInfoNotice taskInfoNotice) {
        taskInfoNoticeMapper.updateByPrimaryKeySelective(taskInfoNotice);
    }
}
