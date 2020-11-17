package com.xwkj.ygjces.service;

import com.xwkj.ygjces.model.OrganizationInfo;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

public interface OrganizationInfoService {
    /**
     * 获取所有组织机构信息
     * @return
     * @author zyh
     */
    public List<OrganizationInfo> getOrganizationInfoList();

    /**
     * 通过ID获取组织机构
     * @param id
     * @return
     * @author zyh
     */
    public OrganizationInfo getOrganizationInfoById(Long id);

    /**
     * 根据ID删除组织机构信息
     * @param ids
     * @author zyh
     */
    public boolean deleteOrganizationInfoById(String[] ids) throws WxErrorException;

    /**
     * 插入组织结构信息并返回插入信息的主键
     * @param organizationInfo
     * @author zyh
     */
    public Long insertOrganizationInfo(OrganizationInfo organizationInfo) throws WxErrorException;

    /**
     * 修改组织机构信息
     * @param organizationInfo
     * @author zyh
     */
    public void updateOrganizationInfo(OrganizationInfo organizationInfo) throws WxErrorException;

    /**
     * 根据用户id获取组织结构信息
     * @param id
     * @return
     * @author zyh
     */
    public List<OrganizationInfo> getOrganizationInfoByUid(Long id);

    /**
     * 给组织机构添加领导
     * @param uId
     * @param oId
     * @author zyh
     */
    public void addLeaderToOrganization(Long uId, Long oId) throws WxErrorException;

    /**
     * 给组织机构移除领导
     * @param uId
     * @param oId
     * @author zyh
     */
    public void removeLeaderFromOrganization(Long uId, Long oId) throws WxErrorException;

    /**
     * 根据领导编号获取其领导的部门编号集合
     * @param leaderId
     * @return
     * @author zyh
     */
    List<Long> getOrgIdsByLeaderId(Long leaderId);
}
