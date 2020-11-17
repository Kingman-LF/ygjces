package com.xwkj.ygjces.service.impl;

import com.xwkj.ygjces.Mapper.OrganizationInfoMapperManual;
import com.xwkj.ygjces.Mapper.UserInfoMapper;
import com.xwkj.ygjces.Mapper.auto.OrganizationInfoMapper;
import com.xwkj.ygjces.model.MyWxCpUser;
import com.xwkj.ygjces.model.OrganizationInfo;
import com.xwkj.ygjces.model.UserInfo;
import com.xwkj.ygjces.model.UserOrgIntermediate;
import com.xwkj.ygjces.service.OrganizationInfoService;
import com.xwkj.ygjces.service.WxService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrganizationInfoServiceImpl implements OrganizationInfoService{

    @Autowired
    private OrganizationInfoMapperManual organizationInfoMapperManual;

    @Autowired
    private OrganizationInfoMapper organizationInfoMapper;

    @Autowired
    private WxService wxService;

    @Autowired
    private UserInfoMapper userInfoMapper;


    /**
     * 获取所有组织机构信息
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrganizationInfo> getOrganizationInfoList() {
        return organizationInfoMapperManual.getOrganizationInfoList();
    }

    /**
     * 通过ID获取组织机构
     * @param id
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrganizationInfo getOrganizationInfoById(Long id) {
        return organizationInfoMapperManual.getOrganizationInfoById(id);
    }

    /**
     * 根据ID删除组织机构信息
     * @param ids 选中的组织机构及其所有子节点组织机构的id组成的数组
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrganizationInfoById(String[] ids) throws WxErrorException {
        //遍历id为ids中的值的组织机构信息，只要其中一条记录与中间表有关联，那么说明有人员属于这个组织机构，那么这个组织机构无法删除
        for (int i = 0; i < ids.length ; i++) {
            List<OrganizationInfo> info = organizationInfoMapperManual.getInfoFromUserAndOrganizationRelatedByID(Long.parseLong(ids[i]));
            System.out.println("->>>>"+organizationInfoMapperManual.getInfoFromUserAndOrganizationRelatedByID(Long.parseLong(ids[i])));
            //当前组织机构内有人员
            if (info.size()!=0){
                //删除失败
                return false;
            }
        }
        //执行完上一个循环说明：选中的组织机构及其所有子节点组织机构内都不存在人员。
        //删除选中的组织机构及其所有子节点组织机构
        for (int i = ids.length-1; i >= 0; i--) {
            OrganizationInfo organizationInfo=new OrganizationInfo();
            organizationInfo.setId(Long.parseLong(ids[i]));
            organizationInfo.setIsenablez(0);
            organizationInfoMapper.updateByPrimaryKeySelective(organizationInfo);
            //为防止因为假删除数据造成后台与微信端的数据冲突进行的修改主键以及子节点父id的操作
            Long maxPrimaryKey = organizationInfoMapperManual.getMaxPrimaryKey();
            organizationInfoMapperManual.updatePrimaryKeyByKey(Long.parseLong(ids[i]),maxPrimaryKey+1);
            organizationInfoMapperManual.updatePidByPrimaryKey(Long.parseLong(ids[i]),maxPrimaryKey+1);

            wxService.getDepartmentService().delete(Long.parseLong(ids[i]));
        }
        //删除成功
        return true;
    }

    /**
     * 插入组织结构信息并返回插入信息的主键
     * @param organizationInfo
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = WxErrorException.class)
    public Long insertOrganizationInfo(OrganizationInfo organizationInfo) throws WxErrorException {
        Long id = organizationInfoMapperManual.selectBigestPrimaryKeyLessThanBoundary();
        Long newId = id+1;
        organizationInfo.setId(newId);
        organizationInfoMapper.insertSelective(organizationInfo);



        WxCpDepart wxCpDepart = new WxCpDepart();
        wxCpDepart.setId(newId);
        wxCpDepart.setName(organizationInfo.getName());
        wxCpDepart.setParentId(organizationInfo.getpId());
        wxService.getDepartmentService().create(wxCpDepart);

        return newId;
    }

    /**
     * 修改组织机构信息
     * @param organizationInfo
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrganizationInfo(OrganizationInfo organizationInfo) throws WxErrorException {
        organizationInfoMapper.updateByPrimaryKeySelective(organizationInfo);
        WxCpDepart wxCpDepart = new WxCpDepart();
        wxCpDepart.setId(organizationInfo.getId());
        wxCpDepart.setName(organizationInfo.getName());
        wxService.getDepartmentService().update(wxCpDepart);
    }

    /**
     * 根据用户id获取组织结构信息
     * @param id
     * @return
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrganizationInfo> getOrganizationInfoByUid(Long id) {
        return organizationInfoMapperManual.getOrganizationInfoByUid(id);
    }

    /**
     * 给组织机构添加领导
     * @param uId
     * @param oId
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addLeaderToOrganization(Long uId, Long oId) throws WxErrorException {
        organizationInfoMapperManual.addLeaderToOrganization(uId,oId);



        //给微信的部门同步添加领导
        UserInfo userInfo = userInfoMapper.getUserInfoById(uId);

        List<Long> orgIdsListByUserId = organizationInfoMapperManual.getOrgIdsListByUserId(uId);
        List<Long> orgIdsListByLeaderId = organizationInfoMapperManual.getOrgIdsListByLeaderId(uId);
        Integer[] isLeaderInDept = new Integer[orgIdsListByUserId.size()];
        for (int i = 0; i < orgIdsListByUserId.size() ; i++) {
            if (orgIdsListByLeaderId.contains(orgIdsListByUserId.get(i))){
                isLeaderInDept[i] = 1;
            }else {
                isLeaderInDept[i] = 0;
            }
        }
        Long[] depts = new Long[orgIdsListByUserId.size()];
        for (int j = 0; j < orgIdsListByUserId.size() ; j++) {
            depts[j] = orgIdsListByUserId.get(j);
        }

        WxCpUser wxCpUser = new WxCpUser();
        wxCpUser.setUserId(userInfo.getAccount());
        wxCpUser.setDepartIds(depts);
        wxCpUser.setIsLeaderInDept(isLeaderInDept);

        wxService.getUserService().update(wxCpUser);
    }

    /**
     * 给组织机构移除领导
     * @param uId
     * @param oId
     * @author zyh
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeLeaderFromOrganization(Long uId, Long oId) throws WxErrorException {
        organizationInfoMapperManual.removeLeaderFromOrganization(uId,oId);



        //给微信的部门同步添加领导
        UserInfo userInfo = userInfoMapper.getUserInfoById(uId);
        List<Long> orgIdsListByUserId = organizationInfoMapperManual.getOrgIdsListByUserId(uId);
        List<Long> orgIdsListByLeaderId = organizationInfoMapperManual.getOrgIdsListByLeaderId(uId);
        Integer[] isLeaderInDept = new Integer[orgIdsListByUserId.size()];
        for (int i = 0; i < orgIdsListByUserId.size() ; i++) {
            if (orgIdsListByLeaderId.contains(orgIdsListByUserId.get(i))){
                isLeaderInDept[i] = 1;
            }else {
                isLeaderInDept[i] = 0;
            }
        }
        Long[] depts = new Long[orgIdsListByUserId.size()];
        for (int j = 0; j < orgIdsListByUserId.size() ; j++) {
            depts[j] = orgIdsListByUserId.get(j);
        }

        WxCpUser wxCpUser = new WxCpUser();
        wxCpUser.setUserId(userInfo.getAccount());
        wxCpUser.setDepartIds(depts);
        wxCpUser.setIsLeaderInDept(isLeaderInDept);

        wxService.getUserService().update(wxCpUser);
    }

    /**
     * 根据领导编号获取其领导的部门编号集合
     * @param leaderId
     * @return
     * @author zyh
     */
    @Override
    public List<Long> getOrgIdsByLeaderId(Long leaderId) {
        return organizationInfoMapperManual.getOrgIdsListByLeaderId(leaderId);
    }
}
