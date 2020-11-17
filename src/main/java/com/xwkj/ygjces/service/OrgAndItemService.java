package com.xwkj.ygjces.service;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.model.Achievements;
import com.xwkj.ygjces.model.OrgItemIntermediate;
import com.xwkj.ygjces.model.SampleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrgAndItemService {

    /**
     * 给检测项目绑定组织机构
     * @param orgItemIntermediate
     * @author zyh
     */
    public void bindOrgAndItems(OrgItemIntermediate orgItemIntermediate);

    /**
     * 解除组织机构与检测项目之间的绑定
     * @param orgItemIntermediate
     * @author zyh
     */
    public void unboundOrgAndItems(OrgItemIntermediate orgItemIntermediate);

    /**
     * 根据组织机构id获取该组织机构绑定的项目列表和该组织机构没有绑定的项目列表
     * @param orgId
     * @return
     * @author zyh
     */
    public List<Object> getItemsByOrgId(Long orgId);

    /**
     * 根据组织机构名称获取绑定了检测项目的组织机构的绩效信息
     * @param startTime
     * @param endTime
     * @param orgName
     * @param pageNum
     * @param pageSize
     * @return
     * @author zyh
     */
    public PageInfo<Achievements> getAchievementsInfoList(Date startTime, Date endTime, String orgName, Integer pageNum, Integer pageSize);

    /**
     * 根据项目id列表获取检测样品信息
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param orgId
     * @return
     * @author zyh
     */
    public PageInfo<SampleInfo> getSampleInfoByOrgId(Integer pageNum, Integer pageSize,Date startTime, Date endTime,Long orgId);

    /**
     * 根据部门id获取及时的检测样品和不及时的检测样品的数量
     * @param startTime
     * @param endTime
     * @param orgId
     * @return
     * @author zyh
     */
    public List<Object> getOnTimeAndOvertimeSampleCountByOrgId(Date startTime, Date endTime,Long orgId);
}
