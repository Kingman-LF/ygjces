package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.model.NoticeModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeModelMapperManual {

    /**
     * 批量插入消息信息
     * @param list
     * @author zyh
     */
    public void insertNoticeModelBatch(List<NoticeModel> list);


    /**
     * 获取当天的消息信息列表
     * @return
     * @author zyh
     */
    List<NoticeModel> getTodayNoticeModelList();
}